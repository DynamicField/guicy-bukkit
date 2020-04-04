package fr.jeuxjeux20.guicybukkit;

import be.seeseemelk.mockbukkit.plugin.ListenerEntry;
import fr.jeuxjeux20.guicybukkit.command.CommandConfigurator;
import fr.jeuxjeux20.guicybukkit.command.CommandNotFoundException;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandUtils;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

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
            public void configureCommand(PluginCommand command) {
                configuredCommand[0] = command;
            }
        };
        PluginDependencies pluginDependencies = new PluginDependencies(Collections.emptySet(),
                Collections.singleton(commandConfigurator));

        pluginDependencies.registerCommands(name -> name.equals(commandName) ? command : null);

        assertSame(command, configuredCommand[0]);
    }

    @Test
    void registerCommandsThrowsOnCommandNull() {
        CommandConfigurator commandConfigurator = mock(CommandConfigurator.class);
        Set<CommandConfigurator> commandConfigurators = Collections.singleton(commandConfigurator);
        PluginDependencies pluginDependencies = new PluginDependencies(Collections.emptySet(), commandConfigurators);

        assertThrows(CommandNotFoundException.class,
                () -> pluginDependencies.registerCommands(name -> null));
    }

    @Test
    void registerListenersAddsListeners() throws NoSuchFieldException, IllegalAccessException {
        Listener listener = new Listener() {
            @EventHandler
            public void onWhatever(Event event) {
            }
        };
        Set<Listener> listeners = Collections.singleton(listener);
        PluginDependencies pluginDependencies = new PluginDependencies(listeners, Collections.emptySet());

        pluginDependencies.registerListeners(plugin);

        List<Listener> registeredListeners = getAllListeners(plugin);
        assertThat(registeredListeners, contains(listener));
    }

    private static List<Listener> getAllListeners(Plugin plugin) throws NoSuchFieldException, IllegalAccessException {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        Field eventListenersMapField = pluginManager.getClass().getDeclaredField("eventListeners");
        eventListenersMapField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Plugin, List<ListenerEntry>> eventListenersMap =
                (Map<Plugin, List<ListenerEntry>>) eventListenersMapField.get(pluginManager);

        List<ListenerEntry> listenerEntries = eventListenersMap.get(plugin);

        return listenerEntries.stream().map(ListenerEntry::getListener).distinct().collect(Collectors.toList());
    }
}
