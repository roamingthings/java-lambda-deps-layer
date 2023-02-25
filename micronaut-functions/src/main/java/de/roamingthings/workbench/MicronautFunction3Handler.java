package de.roamingthings.workbench;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicronautFunction3Handler extends MicronautRequestHandler<SampleRequest, SampleResponse> {
    private static final Logger log = LoggerFactory.getLogger(MicronautFunction3Handler.class);

    @Inject
    HelloTool helloTool;

    public SampleResponse execute(SampleRequest requestEvent) {
        log.info("Hello from Micronaut Function 3");
        var sampleResponse = new SampleResponse();
        sampleResponse.setMessage(helloTool.fetchMessage(new Person("Toni", "Tester")));
        return sampleResponse;
    }
}
