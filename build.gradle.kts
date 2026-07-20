plugins {
    base
}

group = "com.enterprise.framework"
version = providers.gradleProperty("frameworkVersion").orElse("1.0.0").get()
