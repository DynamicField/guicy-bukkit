package com.github.jeuxjeux20.guicybukkit;

import com.github.jeuxjeux20.guicybukkit.command.CommandConfigurator;
import com.github.jeuxjeux20.guicybukkit.command.CommandNotFoundException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * A simple object to gather all injected {@linkplain Listener}s and {@linkplain CommandConfigurator}s,
 * and register them in plugins.
 * <p>
 * Here is an example on how to use {@link PluginDependencies} with a {@link PluginModule} in a {@link JavaPlugin}:
 * <pre>
 * public class MyAwesomePlugin extends JavaPlugin {
 *     &#64;Override
 *     public void onLoad() {
 *         Injector injector = Guice.createInjector(new AwesomePluginModule());
 *         PluginDependencies.fromInjector(injector).registerAll(this);
 *         // The UsefulListener is added, and HelloCommand is registered!
 *     }
 *
 *     private class AwesomePluginModule extends PluginModule {
 *         public AwesomePluginModule() {
 *             super(MyAwesomePlugin.this);
 *         }
 *
 *         &#64;Override
 *         protected void configureListeners(Multibinder&lt;Listener&gt; binder) {
 *             binder.addBinding().to(UsefulListener.class);
 *         }
 *
 *         &#64;Override
 *         protected void configureCommands(Multibinder&lt;CommandConfigurator&gt; binder) {
 *             binder.addBinding().to(HelloCommand.class);
 *         }
 *     }
 * }
 * </pre>
 * @see #fromInjector(Injector)
 */
public class PluginDependencies {

    private final Set<Listener> listeners;
    private final Set<CommandConfigurator> commandsConfigurators;

    /**
     * Creates a new instance of {@link PluginDependencies} with the specified
     * {@code listeners} and {@code commands}.
     * @param listeners the listeners
     * @param commands the commands
     */
    @Inject
    public PluginDependencies(Set<Listener> listeners, Set<CommandConfigurator> commands) {
        this.listeners = listeners;
        this.commandsConfigurators = commands;
    }

    /**
     * Creates a {@link PluginDependencies} instance using the specified {@code injector}.
     *
     * @param injector the injector to use
     * @return an instance of {@link PluginDependencies} from the specified {@code injector}.
     * @implSpec This implementation uses the {@link Injector#getInstance(Class)} method
     * to get an instance of {@link PluginDependencies}.
     */
    public static PluginDependencies fromInjector(Injector injector) {
        return injector.getInstance(PluginDependencies.class);
    }

    /**
     * Gets the injected listeners.
     *
     * @return the listeners
     */
    public final Set<Listener> getListeners() {
        return listeners;
    }

    /**
     * Gets the injected commands.
     *
     * @return the commands
     */
    public final Set<CommandConfigurator> getCommandsConfigurators() {
        return commandsConfigurators;
    }

    /**
     * Registers all listeners in the specified {@code plugin}.
     *
     * @param plugin the plugin to use to register the listeners
     * @implSpec The default implementation uses the server's {@link PluginManager} to register a listener.
     * This is the equivalent of this code:
     * <pre>{@code for (Listener listener : listeners) {
     *     plugin.getServer().getPluginManager().registerEvents(listener, plugin);
     * }}
     * </pre>
     */
    public void registerListeners(Plugin plugin) {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    /**
     * Runs {@link #registerCommands(CommandConfigurator.CommandFinder)} using the specified {@code plugin}.
     *
     * @param plugin the plugin to register the commands to
     * @implSpec The default implementation uses {@link #registerCommands(CommandConfigurator.CommandFinder)}, and gets
     * the command using {@link JavaPlugin#getCommand(String)} on the specified {@code plugin}.
     * <p>
     * This would be equivalent to this code (without the null checks):
     * <pre>{@code for (CommandConfigurator configurator : commandsConfigurators) {
     *     PluginCommand command = plugin.getCommand(configurator.getCommandName());
     *     configurator.configureCommand(command);
     * }}
     * </pre>
     * @see #registerCommands(CommandConfigurator.CommandFinder)
     */
    public final void registerCommands(JavaPlugin plugin) {
        registerCommands(plugin::getCommand);
    }

    /**
     * Executes every {@linkplain CommandConfigurator command configurator} with each command retrieved
     * using the specified {@code commandFinder} with the name
     * the {@linkplain CommandConfigurator command configurator} requests.
     * <p>
     * If the {@code commandFinder} returns {@code null},
     * this method will <b>throw a {@link CommandNotFoundException}</b>.
     *
     * @param commandFinder a command finder used to find the command for each command configurator
     */
    public void registerCommands(CommandConfigurator.CommandFinder commandFinder) {
        for (CommandConfigurator commandConfigurator : commandsConfigurators) {
            String commandName = commandConfigurator.getCommandName();
            PluginCommand pluginCommand = commandFinder.find(commandName);
            if (pluginCommand == null) {
                throw new CommandNotFoundException("Couldn't find the command " + commandName + ".");
            }
            commandConfigurator.configureCommand(pluginCommand);
        }
    }

    /**
     * Registers all listeners and commands on the specified {@code plugin}.
     *
     * @param plugin the plugin to register the listeners and commands to.
     * @implSpec The default implementation applies {@link #registerListeners(Plugin)}
     * and {@link #registerCommands(JavaPlugin)} on the specified {@code plugin}.
     */
    public void registerAll(JavaPlugin plugin) {
        registerListeners(plugin);
        registerCommands(plugin);
    }
}
