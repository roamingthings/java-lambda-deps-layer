plugins {
    java
    id("io.freefair.aspectj.post-compile-weaving")
    id("de.roamingthings.lambda-distribution")
}

dependencies {
    implementation(project(":common-deps"))
    implementation(project(":shared-lib"))
    implementation(project(":shared-model"))

    aspect("software.amazon.lambda:powertools-logging:1.14.0")
    aspect("software.amazon.lambda:powertools-metrics:1.14.0")
    aspect("software.amazon.lambda:powertools-tracing:1.14.0")

    localImplementation(platform("com.fasterxml.jackson:jackson-bom:2.14.1"))
    localImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    localImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}
repositories {
    mavenCentral()
}
