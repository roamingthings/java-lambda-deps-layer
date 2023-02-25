package de.roamingthings.workbench;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static software.amazon.awscdk.RemovalPolicy.DESTROY;
import static software.amazon.awscdk.services.lambda.Architecture.ARM_64;
import static software.amazon.awscdk.services.lambda.Architecture.X86_64;
import static software.amazon.awscdk.services.lambda.Tracing.ACTIVE;

public class WorkbenchJavaDepsLayerStack extends Stack {

    public WorkbenchJavaDepsLayerStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public WorkbenchJavaDepsLayerStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var bucket = Bucket.Builder.create(this, "Bucket")
                .removalPolicy(DESTROY)
                .bucketName("workbench-java-deps-layer-test-bucket-" + getAccount())
                .build();

        var depsLayer = LayerVersion.Builder.create(this, "DepsLayer")
                .removalPolicy(DESTROY)
                .code(Code.fromAsset("../deps-layer/build/distributions/deps-layer-dist.zip"))
                .compatibleArchitectures(List.of(X86_64, ARM_64))
                .build();

        var environment = Map.of("BUCKET_NAME", bucket.getBucketName());

        var function1 = Function.Builder.create(this, "Function1")
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .architecture(ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.Function1Handler::handleRequest")
                .code(Code.fromAsset("../function1/build/distributions/lambda-dist.zip"))
                .layers(List.of(depsLayer))
                .tracing(ACTIVE)
                .environment(environment)
                .build();
        bucket.grantWrite(function1);

        Function.Builder.create(this, "Function2")
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .architecture(ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.Function2Handler::handleRequest")
                .code(Code.fromAsset("../function2/build/distributions/lambda-dist.zip"))
                .layers(List.of(depsLayer))
                .tracing(ACTIVE)
                .environment(environment)
                .build();

        var micronautDepsLayer = LayerVersion.Builder.create(this, "MicronautDepsLayer")
                .removalPolicy(DESTROY)
                .code(Code.fromAsset("../micronaut-deps-layer/build/distributions/deps-layer-dist.zip"))
                .compatibleArchitectures(List.of(X86_64, ARM_64))
                .build();

        var micronautFunction1 = Function.Builder.create(this, "MicronautFunction1")
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .architecture(ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.MicronautFunction1Handler")
                .code(Code.fromAsset("../micronaut-functions/build/distributions/lambda-dist.zip"))
                .layers(List.of(micronautDepsLayer))
                .tracing(ACTIVE)
                .environment(environment)
                .build();
        bucket.grantWrite(micronautFunction1);

        Function.Builder.create(this, "MicronautFunction2")
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .architecture(ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.MicronautFunction2Handler")
                .code(Code.fromAsset("../micronaut-functions/build/distributions/lambda-dist.zip"))
                .layers(List.of(micronautDepsLayer))
                .tracing(ACTIVE)
                .environment(environment)
                .build();

        Function.Builder.create(this, "MicronautFunction3")
                .runtime(Runtime.JAVA_11)
                .memorySize(1024)
                .architecture(ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.MicronautFunction3Handler")
                .code(Code.fromAsset("../micronaut-functions/build/distributions/lambda-dist.zip"))
                .layers(List.of(micronautDepsLayer))
                .tracing(ACTIVE)
                .environment(environment)
                .build();
    }
}
