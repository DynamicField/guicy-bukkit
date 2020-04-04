package io.github.jeuxjeux20.guicybukkit.command;

import io.github.jeuxjeux20.guicybukkit.PluginDependencies;
import io.github.jeuxjeux20.guicybukkit.PluginModule;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.Nullable;

/**
 * Configures a command found by the {@linkplain #getCommandName() name} this interface provides.
 * <p>
 * This interface can be used with Guice using a {@link PluginModule}, and registered in plugins with
 * {@link PluginDependencies}.
 *
 * @see PluginModule
 * @see PluginDependencies
 * @see AnnotatedCommandConfigurator
 */
public interface CommandConfigurator {

    /**
     * Gets the name of the command to configure.
     * @return the name of the command to configure
     */
    String getCommandName();

    /**
     * Configures the command whose name is the {@linkplain #getCommandName() name} this interface requests.
     * @param command the command to configure
     */
    void configureCommand(PluginCommand command);

    /**
     * An interface for finding a command using a given name.
     */
    @FunctionalInterface
    interface CommandFinder {
        /**
         * Finds a command by its name. If no command has been found, this returns {@code null}.
         * @param commandName the command name
         * @return a command of the same name as the specified {@code commandName}.
         */
        @Nullable PluginCommand find(String commandName);
    }
}
