package de.roamingthings.workbench;

import java.io.IOException;

import static java.lang.String.format;

public class HelloTool {
    public String fetchMessage(Person person) {
        try (var messageStream = HelloTool.class.getResourceAsStream("/message.txt")) {
            return createMessage(person, new String(messageStream.readAllBytes()));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot find message resource", e);
        }
    }

    private static String createMessage(Person person, String message) throws IOException {
        return format("%s - %s %s", message, person.getFirstName(), person.getLastName());
    }
}
