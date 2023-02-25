plugins {
    java
    id("io.micronaut.application")
    id("de.roamingthings.lambda-distribution")
}

dependencies {
    implementation(project(":micronaut-deps"))
    implementation(project(":shared-lib")) {
        isTransitive = false
    }
    implementation(project(":shared-model"))

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.micronaut.aws:micronaut-function-aws-api-proxy-test")
}

micronaut {
    runtime("lambda_java")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("de.roamingthings.*")
    }
}

configurations.all {
    exclude(group = "ch.qos.logback", module = "logback-classic")
}
