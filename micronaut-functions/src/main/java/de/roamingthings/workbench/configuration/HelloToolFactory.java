package de.roamingthings.workbench.configuration;

import de.roamingthings.workbench.HelloTool;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Factory
public class HelloToolFactory {
    private static final Logger log = LoggerFactory.getLogger(HelloToolFactory.class);

    @Bean
    @Singleton
    public HelloTool helloTool() {
        log.info("Creating HelloTool");
        return new HelloTool();
    }
}
