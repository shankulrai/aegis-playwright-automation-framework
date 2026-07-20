plugins {
    `java-library`
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
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
