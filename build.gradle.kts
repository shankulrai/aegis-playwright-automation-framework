plugins {
    base
}

group = "com.enterprise.framework"
version = providers.gradleProperty("frameworkVersion").orElse("1.0.0").get()

subprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.withId("java") {
        extensions.configure<JavaPluginExtension>("java") {
            withSourcesJar()
            withJavadocJar()
        }

        tasks.withType<JavaCompile>().configureEach {
            options.release.set(21)
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
            val forwardedProperties = listOf(
                "env",
                "headless",
                "browser",
                "remote",
                "remoteEndpoint",
                "slowMoMs",
                "timeoutMs",
                "baseUrl",
                "apiBaseUrl"
            )
            forwardedProperties.forEach { key ->
                System.getProperty(key)?.let { value ->
                    systemProperty(key, value)
                }
            }
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}
