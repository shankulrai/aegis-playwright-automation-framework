plugins {
    id("java-module-conventions")
}

dependencies {
    api(project(":core"))
    implementation(project(":utilities"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
