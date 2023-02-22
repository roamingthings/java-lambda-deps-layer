package de.roamingthings.workbench;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.lambda.powertools.logging.Logging;
import software.amazon.lambda.powertools.metrics.Metrics;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.Map;
import java.util.UUID;

public class Function1Handler implements RequestHandler<Map<String, String>, String> {

    private final S3Client s3Client;
    private final HelloTool helloTool;
    private final ObjectMapper objectMapper;

    public Function1Handler() {
        s3Client = S3Client.builder().region(Region.EU_CENTRAL_1).build();
        helloTool = new HelloTool();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        preHeatS3Client();
        preHeatObjectMapper();
    }

    private void preHeatS3Client() {
        try {
            s3Client.getObject(GetObjectRequest.builder()
                    .bucket("pre-heat")
                    .key("not-existing")
                    .build());
        } catch (Exception e) {
            // Swallow exception for pre-heat
        }
    }

    private void preHeatObjectMapper() {
        try {
            objectMapper.writeValueAsBytes(Person.builder().build());
        } catch (Exception e) {
            // Swallow exception for pre-heat
        }
    }

    @Tracing
    @Logging(clearState = true)
    @Metrics(captureColdStart = true)
    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("This is function handler 1");
        logger.log("EVENT: " + event);

        // Use the shared model
        var person = Person.builder()
                .firstName("Toni")
                .lastName("Tester")
                .build();
        try {
            // Use the local dependency ObjectMapper
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(System.getenv("BUCKET_NAME"))
                            .key(UUID.randomUUID().toString())
                            .contentType("application/json")
                            .build(),
                    RequestBody.fromString(objectMapper.writeValueAsString(person))
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not marshall Person: ", e);
        }

        // Use the shared library
        return helloTool.fetchMessage(person);
    }
}
