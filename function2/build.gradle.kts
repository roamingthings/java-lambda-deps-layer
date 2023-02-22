plugins {
    java
    id("de.roamingthings.lambda-distribution")
}

dependencies {
    implementation(project(":common-deps"))
    implementation(project(":shared-lib"))
}
