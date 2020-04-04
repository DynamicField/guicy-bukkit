package com.github.jeuxjeux20.guicybukkit.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

/**
 * A simple command executor that also provides compatibility with {@link CommandConfigurator}, by
 * setting the {@linkplain PluginCommand#setExecutor(CommandExecutor) command's executor}.
 * <p>
 * <b>The command name can be defined using the {@link CommandName} attribute.</b><br>
 * To use it, simply put {@code @CommandName("mycommand")} on top of your class.
 * <p>
 * Here is an example on using this class:
 * <pre>
 * &#064;CommandName("hello")
 * public class HelloCommandAnnotated extends SelfConfiguredCommandExecutor
 *     &#064;Override
 *     public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
 *         sender.sendMessage("Hello, " + sender.getName() + "!");
 *         return true;
 *     }
 * }</pre>
 * This describes a command "{@code /hello}" that sends a greet message to the sender.
 * @see CommandName
 * @see CommandExecutor
 * @see CommandConfigurator
 */
public abstract class SelfConfiguredCommandExecutor implements CommandExecutor, AnnotatedCommandConfigurator {

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation sets the {@linkplain PluginCommand#setExecutor(CommandExecutor)
     * command's executor} to {@code this}.
     */
    @Override
    public final void configureCommand(PluginCommand command) {
        command.setExecutor(this);
        alterCommand(command);
    }

    /**
     * Applies additional configuration to the specified {@code command}.
     * <p>
     * This method is called at the end of {@link #configureCommand(PluginCommand)}.
     *
     * @param command the command to apply additional configuration on
     */
    protected void alterCommand(PluginCommand command) {
    }
}






