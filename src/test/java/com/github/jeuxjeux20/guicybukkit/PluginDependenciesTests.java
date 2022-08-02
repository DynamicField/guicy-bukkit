package com.github.jeuxjeux20.guicybukkit;

import com.github.jeuxjeux20.guicybukkit.command.CommandConfigurator;
import com.github.jeuxjeux20.guicybukkit.command.CommandNotFoundException;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.RegisteredListener;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PluginDependenciesTests extends PluginTestBase {

    @Test
    void registerCommandsRunsConfigurators() {
        final String commandName = "some command";
        PluginCommand command = PluginCommandUtils.createPluginCommand(commandName, plugin);
        final PluginCommand[] configuredCommand = {null};
        CommandConfigurator commandConfigurator = new CommandConfigurator() {
            @Override
            public String getCommandName() {
                return commandName;
            }

            @Override
            public void configureCommand(@Nullable PluginCommand command) {
                configuredCommand[0] = command;
            }
        };
        PluginDependencies pluginDependencies = new PluginDependencies(Collections.emptySet(),
                Collections.singleton(commandConfigurator));

        pluginDependencies.registerCommands(name -> name.equals(commandName) ? command : null);

        assertSame(command, configuredCommand[0]);
    }

    @Test
    void registerCommandsConfiguresNullCommandName() {
        CommandConfigurator commandConfigurator = mock(CommandConfigurator.class);
        Set<CommandConfigurator> commandConfigurators = Collections.singleton(commandConfigurator);
        PluginDependencies pluginDependencies = new PluginDependencies(Collections.emptySet(), commandConfigurators);

        pluginDependencies.registerCommands(plugin);
        verify(commandConfigurator).configureCommand(null);
    }

    @Test
    void registerCommandsThrowsOnUnknownCommand() {
        CommandConfigurator commandConfigurator = new CommandConfigurator() {
            @Override
            public String getCommandName() {
                return "aaaaaaaaaaaa";
            }

            @Override
            public void configureCommand(@Nullable PluginCommand command) {
            }
        };

        Set<CommandConfigurator> commandConfigurators = Collections.singleton(commandConfigurator);
        PluginDependencies pluginDependencies = new PluginDependencies(Collections.emptySet(), commandConfigurators);

        assertThrows(CommandNotFoundException.class, () -> pluginDependencies.registerCommands(plugin));
    }

    @Test
    void registerListenersAddsListeners() {
        Listener listener = new Listener() {
            @EventHandler
            public void onWhatever(PlayerDeathEvent event) {
            }
        };
        Set<Listener> listeners = Collections.singleton(listener);
        PluginDependencies pluginDependencies = new PluginDependencies(listeners, Collections.emptySet());

        pluginDependencies.registerListeners(plugin);

        List<Listener> registeredListeners = Arrays.stream(PlayerDeathEvent.getHandlerList().getRegisteredListeners())
                .map(RegisteredListener::getListener)
                .collect(Collectors.toList());
        assertThat(registeredListeners, contains(listener));
    }
}
