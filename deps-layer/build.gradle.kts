plugins {
    `java-library`
}

dependencies {
    api(project(":common-deps"))
    api(project(":shared-lib"))
    api(project(":shared-model"))
}

var layerDistZipTask = tasks.register<Zip>("layerDistZip") {
    into("java/lib") {
        from(configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") })
    }
    archiveFileName.set("deps-layer-dist.zip")
}

tasks.getByName("build").dependsOn(layerDistZipTask)
