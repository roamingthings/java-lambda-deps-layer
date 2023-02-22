plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}


gradlePlugin {
    plugins {
        create("lambdaDistributionPlugin") {
            id = "de.roamingthings.lambda-distribution"
            implementationClass = "de.roamingthings.gradle.plugin.lambda.LambdaDistributionPlugin"
        }
    }
}

//gradlePlugin {
//    plugins {
//        create<LambdaDistributionPlugin> {
//            id = "lambda-distribution"
//            implementationClass = "de.roamingthings.gradle.plugin.lambda.LambdaDistributionPlugin"
//        }
//    }
//}
