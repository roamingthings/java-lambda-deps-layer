package de.roamingthings.workbench;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicronautFunction1Handler extends MicronautRequestHandler<SampleRequest, SampleResponse> {
    private static final Logger log = LoggerFactory.getLogger(MicronautFunction1Handler.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MicronautFunction1Service function1Service;

    public SampleResponse execute(SampleRequest requestEvent) {
        log.info("Hello from Micronaut Function 1");

        var serviceResponse = function1Service.doService();

        var sampleResponse = new SampleResponse();
        sampleResponse.setMessage(serviceResponse);
        return sampleResponse;
    }
}
