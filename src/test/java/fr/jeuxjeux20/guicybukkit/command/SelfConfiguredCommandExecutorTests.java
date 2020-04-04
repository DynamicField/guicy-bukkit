package fr.jeuxjeux20.guicybukkit.command;

import fr.jeuxjeux20.guicybukkit.PluginTestBase;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SelfConfiguredCommandExecutorTests extends PluginTestBase {

    @Test
    void configureCommandSetsExecutorToItself() {
        SelfConfiguredCommandExecutor commandConfigurator = mock(SelfConfiguredCommandExecutor.class);
        PluginCommand command = PluginCommandUtils.createPluginCommand("cats_are_good", plugin);

        commandConfigurator.configureCommand(command);

        assertSame(commandConfigurator, command.getExecutor());
    }

    @Test
    void configureCommandRunsAlterCommand() {
        SelfConfiguredCommandExecutor commandConfigurator = mock(SelfConfiguredCommandExecutor.class);

        PluginCommand command = PluginCommandUtils.createPluginCommand("cats_are_really_good", plugin);

        commandConfigurator.configureCommand(command);

        verify(commandConfigurator).alterCommand(command);
    }
}
