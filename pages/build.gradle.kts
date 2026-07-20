plugins {
    id("java-library")
}

dependencies {
    api(project(":core"))
    implementation(project(":utilities"))
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
