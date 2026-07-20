plugins {
    id("java-library")
}

dependencies {
    api(project(":core"))
    api(project(":utilities"))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.dataformat.csv)
    implementation(libs.apache.poi)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
