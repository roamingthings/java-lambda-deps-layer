package de.roamingthings.gradle.plugin.lambda;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Zip;
import org.jetbrains.annotations.NotNull;

public class LambdaDistributionPlugin implements Plugin<Project> {

    public static final String DISTRIBUTION_ARCHIVE_FILENAME = "lambda-dist.zip";
    public static final String CREATE_DISTRIBUTION_TASK_NAME = "functionDistZip";

    @Override
    public void apply(Project project) {
        var localImplementationConfiguration = createConfiguration(project);
        updateClasspaths(project, localImplementationConfiguration);
        var functionDistZipTask = createDistZipTask(project, localImplementationConfiguration);
        project.getTasks().getByName("build").dependsOn(functionDistZipTask);
    }

    @NotNull
    private static TaskProvider<Zip> createDistZipTask(Project project, Configuration localImplementationConfiguration) {
        var functionDistZipTask = project.getTasks().register(CREATE_DISTRIBUTION_TASK_NAME, Zip.class);
        functionDistZipTask.configure(zip -> {
            zip.setDescription("Build the zip that can be distributed as Lambda code artifact");
            zip.setGroup("Distribution");
            zip.from(project.getTasks().getByName("compileJava"));
            zip.from(project.getTasks().getByName("processResources"));
            zip.from(localImplementationConfiguration.getIncoming().getArtifacts().getArtifactFiles(), copySpec -> {
                copySpec.from(localImplementationConfiguration.getIncoming().getArtifacts().getArtifactFiles());
                copySpec.into("lib");
            });
            zip.setProperty("archiveFileName", DISTRIBUTION_ARCHIVE_FILENAME);
            zip.setProperty("archiveVersion", "");
            zip.setProperty("archiveClassifier", "");
            zip.setProperty("preserveFileTimestamps", false);
            zip.setProperty("reproducibleFileOrder", true);

        });
        return functionDistZipTask;
    }

    private static void updateClasspaths(Project project, Configuration localImplementation) {
        project.getConfigurations().findByName("compileClasspath").extendsFrom(localImplementation);
        project.getConfigurations().findByName("runtimeClasspath").extendsFrom(localImplementation);
    }

    private static Configuration createConfiguration(Project project) {
        var localImplementation = project.getConfigurations().create("localImplementation");
        localImplementation.setDescription("Dependencies only used by this function");
        localImplementation.setVisible(false);
        return localImplementation;
    }
}
