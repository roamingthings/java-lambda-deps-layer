plugins {
    `java-library`
}

// This is a super-set of dependencies of all Lambda functions. It will be compiled into the dependency layer
dependencies {
    api("com.amazonaws:aws-lambda-java-core:1.2.2")

    api(platform("software.amazon.awssdk:bom:2.19.12"))
    api("software.amazon.awssdk:s3")
    api("software.amazon.awssdk:url-connection-client")

    api("org.slf4j:slf4j-simple:2.0.6")
    api("software.amazon.lambda:powertools-logging:1.13.0")
    api("software.amazon.lambda:powertools-metrics:1.13.0")
    api("software.amazon.lambda:powertools-tracing:1.13.0")
    api("software.amazon.lambda:powertools-idempotency:1.13.0")
}

configurations.all {
    exclude(group = "software.amazon.awssdk", module = "netty-nio-client")
    exclude(group = "software.amazon.awssdk", module = "apache-client")
    exclude(group = "com.amazonaws", module = "aws-xray-recorder-sdk-apache-http")
}
