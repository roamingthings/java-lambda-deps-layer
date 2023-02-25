package de.roamingthings.workbench;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Singleton
public class MicronautFunction1Service {

    private static final Logger log = LoggerFactory.getLogger(MicronautFunction1Service.class);

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final HelloTool helloTool;


    public MicronautFunction1Service(S3Client s3Client, ObjectMapper objectMapper, HelloTool helloTool) {
        log.info("Initializing MicronautFunctionService1");

        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
        this.helloTool = helloTool;
        preHeatObjectMapper();
        preHeatS3Client();
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

    public String doService() {
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
