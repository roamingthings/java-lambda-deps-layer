plugins {
    id("io.freefair.aspectj.post-compile-weaving") version "6.6.1" apply (false)
    id("io.micronaut.application") version "3.7.2" apply (false)
}

subprojects {
    group = "de.roamingthings"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }

    plugins.withType(org.gradle.api.plugins.JavaPlugin::class) {
        extensions.configure(JavaPluginExtension::class.java) {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showCauses = true
            showStackTraces = true
        }
    }
}

allprojects {
    afterEvaluate {
        tasks.withType<Zip> {
            archiveVersion.set("")
            archiveClassifier.set("")
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }
        tasks.withType<Jar> {
            archiveVersion.set("")
            archiveClassifier.set("")
        }
    }
}

tasks.register("synth") {
    description = "Synthesize all CDK stacks"
    group = "cdk"
    dependsOn(subprojects.flatMap { project: Project -> project.tasks }.filter { task -> task.name == "synth" })
}

tasks.register("deploy") {
    description = "Deploy all CDK stacks"
    group = "cdk"
    dependsOn(subprojects.flatMap { project: Project -> project.tasks }.filter { task -> task.name == "deploy" })
}

tasks.register("clean") {
    dependsOn(subprojects.flatMap { project: Project -> project.tasks }.filter { task -> task.name == "clean" })
}

tasks.register("destroy") {
    description = "Destroy all CDK stacks"
    group = "cdk"
    dependsOn(subprojects.flatMap { project: Project -> project.tasks }.filter { task -> task.name == "destroy" })
}
