plugins {
    `java-library`
    id("io.micronaut.application")
}

// This is a super-set of dependencies of all Lambda functions. It will be compiled into the dependency layer
dependencies {
    api("com.amazonaws:aws-lambda-java-core:1.2.2")

    api(platform("software.amazon.awssdk:bom:2.19.12"))
    api("software.amazon.awssdk:s3")
    api("software.amazon.awssdk:url-connection-client")

//    api("org.slf4j:slf4j-simple:2.0.6")
    // This brings in/requires Log4J2 as logging system
    api("software.amazon.lambda:powertools-idempotency:1.14.0")
    api("software.amazon.lambda:powertools-logging:1.14.0")

    api("io.micronaut:micronaut-inject")
    api("io.micronaut:micronaut-validation")
    api("io.micronaut.aws:micronaut-function-aws")
//    api("ch.qos.logback:logback-classic")

    api(platform("com.amazonaws:aws-xray-recorder-sdk-bom:2.11.0"))
    api("com.amazonaws:aws-xray-recorder-sdk-core")
    api("com.amazonaws:aws-xray-recorder-sdk-apache-http")
    api("com.amazonaws:aws-xray-recorder-sdk-aws-sdk")
    api("com.amazonaws:aws-xray-recorder-sdk-aws-sdk-instrumentor")
    api("com.amazonaws:aws-xray-recorder-sdk-aws-sdk-v2")
}

configurations.all {
    exclude(group = "software.amazon.awssdk", module = "netty-nio-client")
    exclude(group = "software.amazon.awssdk", module = "apache-client")
    exclude(group = "com.amazonaws", module = "aws-xray-recorder-sdk-apache-http")
    exclude(group = "ch.qos.logback", module = "logback-classic")
}
