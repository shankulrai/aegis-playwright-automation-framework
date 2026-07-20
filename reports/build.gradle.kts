plugins {
    id("java-module-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":utilities"))
    implementation(libs.extent)
    implementation(libs.allure.java.commons)
    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
