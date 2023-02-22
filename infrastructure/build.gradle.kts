plugins {
    java
    application
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.59.0")
    implementation("software.constructs:constructs:10.1.214")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.register<Exec>("synth") {
    description = "Synthesize the CDK stack"
    group = "cdk"
    commandLine("npx", "cdk", "synth", "--require-approval", "never")
}

tasks.register<Exec>("deploy") {
    description = "Deploy the CDK stack"
    group = "cdk"
    commandLine("npx", "cdk", "deploy", "--require-approval", "never")
}

tasks.register<Exec>("destroy") {
    description = "Destroy the CDK stack"
    group = "cdk"
    commandLine("npx", "cdk", "destroy", "--force")
}

application {
    mainClass.set("de.roamingthings.workbench.WorkbenchJavaDepsLayerApp")
}

tasks.clean {
    delete.add("cdk.out")
}

tasks.withType<JavaCompile> {
    dependsOn(":function1:compileJava", ":function2:compileJava")
}

tasks.withType<JavaExec> {
    dependsOn(":function1:build", ":function2:build", ":deps-layer:layerDistZip")
}
