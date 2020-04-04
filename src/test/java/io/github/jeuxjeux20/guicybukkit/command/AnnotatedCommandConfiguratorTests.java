package io.github.jeuxjeux20.guicybukkit.command;

import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotatedCommandConfiguratorTests {

    private static final String COMMAND_NAME = "meow";

    @Test
    void getCommandNameReturnsCommandNameAnnotationValue() {
        CommandConfigurator commandConfigurator = new WithAnnotationConfigurator();

        String actualCommandName = commandConfigurator.getCommandName();

        assertEquals(COMMAND_NAME, actualCommandName);
    }
    @Test
    void getCommandNameWithoutCommandNameAnnotationThrows() {
       CommandConfigurator commandConfigurator = new WithoutAnnotationConfigurator();

       assertThrows(UnsupportedOperationException.class, commandConfigurator::getCommandName);
    }

    @CommandName(COMMAND_NAME)
    private static class WithAnnotationConfigurator implements AnnotatedCommandConfigurator {
        @Override
        public void configureCommand(PluginCommand command) {
        }
    }

    private static class WithoutAnnotationConfigurator implements AnnotatedCommandConfigurator {
        @Override
        public void configureCommand(PluginCommand command) {
        }
    }
}
