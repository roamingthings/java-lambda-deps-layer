package de.roamingthings.workbench;

import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicronautFunction2Handler extends MicronautRequestHandler<SampleRequest, SampleResponse> {
    private static final Logger log = LoggerFactory.getLogger(MicronautFunction2Handler.class);

    public SampleResponse execute(SampleRequest requestEvent) {
        log.info("Hello from Micronaut Function 2");
        return new SampleResponse();
    }
}
