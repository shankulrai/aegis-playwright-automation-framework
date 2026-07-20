plugins {
    id("java-module-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":utilities"))
    api("software.amazon.awssdk:s3:${libs.versions.aws.get()}")
    api("software.amazon.awssdk:sns:${libs.versions.aws.get()}")
    api("software.amazon.awssdk:sqs:${libs.versions.aws.get()}")
    api("software.amazon.awssdk:sts:${libs.versions.aws.get()}")
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.csv)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}
