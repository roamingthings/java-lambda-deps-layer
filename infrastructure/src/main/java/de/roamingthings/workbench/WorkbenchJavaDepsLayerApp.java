package de.roamingthings.workbench;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;
import java.util.Map;

public class WorkbenchJavaDepsLayerApp {

    private static final String ACCOUNT = "12345678";

    public static void main(final String[] args) {
        App app = new App();

        new WorkbenchJavaDepsLayerStack(app, "WorkbenchJavaDepsLayerStack", StackProps.builder()
// Optional define a deployment environment explicitly
//                .env(Environment.builder()
//                        .account(ACCOUNT)
//                        .region("eu-central-1")
//                        .build())
                .tags(Map.of(
                        "scope", "experiment"
                        )
                )
                .build());

        app.synth();
    }
}
