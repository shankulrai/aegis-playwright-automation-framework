pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "aegis-playwright-automation-framework"

include(
    "core",
    "pages",
    "utilities",
    "aws-services",
    "api-tests",
    "ui-tests",
    "data-provider",
    "reports",
    "integration"
)