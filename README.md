# Workbench: AWS Java Lambda with Dependencies in a Layer

This project demonstrates how to deploy an application to AWS with a Lambda written in Java using a layer for all
dependencies.

Serverless applications that contain multiple Lambda functions will inevitably share a lot of dependencies between those
lambda functions. This would usually mean that you are deploying these dependencies over and over again with every
lambda function. This will lead to big deployment assets (usually >20MB) and can lead to long deployment times and
further problems e.g. when deploying using a CDK CodePipeline that currently limits the total deployment size to 256MB.

AWS Lambda provides a mechanism called [Layers](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-concepts.html#gettingstarted-concepts-layer)
that allow you to share common code, dependencies or other resources between multiple Lambda functions.

This project demonstrates how to set up a Java project using Gradle and CDK to deploy an application in such a way.

It contains the following modules:

* `common-deps` - A module containing the sum of all dependencies that are required by all Lambda functions.
* `shared-lib` - An example module containing implementation such as shared business logic, configuration, tools that is shared
  between multiple function implementations.
* `shared-model` - An example module containing a model that is shared between multiple functions.
* `deps-layer` - This module assembles the asset that is used to deploy the dependencies layer when the application is
  deployed.
* `function1`... - The functions of the application.
* `infrastructure` - CDK code to build the topology of the application and deploy it to AWS.

The most important modules are the `common-deps` and `deps-layer` module. The `common-deps` module the sum of all
dependencies that are used by all functions. From the point of view of one function that will mean that the
dependencies-layer will contain unused dependencies.

All archives are generated so that the hash of the archive only changes when the content changes. This means that the
dependencies-layer will only be deployed, when one of the dependency changes.

## Adding Dependencies local to a Function

In order to use dependencies that are not included in the dependencies layer a new Gradle configuration
`localImplementation` is created. When the distribution Zip for a Lambda is built, it will only contain the classes
and dependencies from the `localImplementation` configuration.

This functionality is provided by the plugin [LambdaDistributionPlugin](buildSrc/src/main/java/de/roamingthings/gradle/plugin/lambda/LambdaDistributionPlugin.java)
that is included with this project and can be import by adding the following to the `build.gradle.kts` of the function:

```kotlin
plugins {
    id("de.roamingthings.lambda-distribution")
}
```

See [function1/build.gradle.kts](function1/build.gradle.kts) for an example.
