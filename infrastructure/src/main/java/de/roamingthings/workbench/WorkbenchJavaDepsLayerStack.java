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

public class WorkbenchJavaDepsLayerStack extends Stack {

    public WorkbenchJavaDepsLayerStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public WorkbenchJavaDepsLayerStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var bucket = Bucket.Builder.create(this, "Bucket")
                .removalPolicy(RemovalPolicy.DESTROY)
                .bucketName("workbench-java-deps-layer-test-bucket-" + getAccount())
                .build();

        var depsLayer = LayerVersion.Builder.create(this, "DepsLayer")
                .removalPolicy(RemovalPolicy.DESTROY)
                .code(Code.fromAsset("../deps-layer/build/distributions/deps-layer-dist.zip"))
                .compatibleArchitectures(List.of(Architecture.X86_64, Architecture.ARM_64))
                .build();

        var environment = Map.of("BUCKET_NAME", bucket.getBucketName());

        var function1 = Function.Builder.create(this, "Function1")
                .runtime(Runtime.JAVA_11)
                .memorySize(512)
                .architecture(Architecture.ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.Function1Handler::handleRequest")
                .code(Code.fromAsset("../function1/build/distributions/lambda-dist.zip"))
                .layers(List.of(depsLayer))
                .tracing(Tracing.ACTIVE)
                .environment(environment)
                .build();
        bucket.grantWrite(function1);

        Function.Builder.create(this, "Function2")
                .runtime(Runtime.JAVA_11)
                .memorySize(512)
                .architecture(Architecture.ARM_64)
                .timeout(Duration.seconds(30))
                .handler("de.roamingthings.workbench.Function2Handler::handleRequest")
                .code(Code.fromAsset("../function2/build/distributions/lambda-dist.zip"))
                .layers(List.of(depsLayer))
                .tracing(Tracing.ACTIVE)
                .environment(environment)
                .build();
    }
}
