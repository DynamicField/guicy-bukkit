package com.github.jeuxjeux20.guicybukkit.command;

import org.bukkit.command.PluginCommand;

import javax.annotation.Nonnull;

/**
 * A {@link CommandConfigurator} that uses the {@link CommandName} annotation to get
 * its {@linkplain #getCommandName() command name}.
 * <p>
 * <b>IMPORTANT:</b> Implementors of this interface should have a {@link CommandName} annotation, or a
 * {@link UnsupportedOperationException} will be thrown when calling {@link #getCommandName()}.
 *
 * @see CommandName
 */
public interface AnnotatedCommandConfigurator extends CommandConfigurator {

    /**
     * {@inheritDoc}
     * <p>
     * If a {@link CommandName} annotation is not present on this object's class, this will throw a
     * {@link UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException when no {@link CommandName} annotation on this type has been found.
     * @implSpec The default implementation gets
     * the {@link CommandName} value using {@link CommandName.Helper#getCommandNameOrThrow(Class)}.
     */
    @Override
    default String getCommandName() {
        return CommandName.Helper.getCommandNameOrThrow(this.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems") // We're overriding the @Nullable
    @Override
    void configureCommand(@Nonnull PluginCommand command);
}
