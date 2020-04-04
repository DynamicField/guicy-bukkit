package fr.jeuxjeux20.guicybukkit;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import fr.jeuxjeux20.guicybukkit.command.CommandConfigurator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * An extension for {@link AbstractModule} that provides support for binding spigot plugins components
 * such as listeners and commands.
 * <p>
 * Commands and listeners can later be easily registered by getting an instance of {@link PluginDependencies}
 * with the injector.
 * <p>
 * <b>Notes for inheritors</b>
 * <p>
 * Additional bindings can be configured by overriding the {@link #configureBindings()} method.
 * <p>
 * Here is an example of using a {@link PluginModule} with {@link Listener}s and {@link CommandConfigurator}s:
 * <pre>
 * public class MyNicePluginModule extends PluginModule {
 *     // We need a MyNicePlugin instance to create this module, and inject it.
 *     public AwesomePluginModule(MyNicePlugin plugin) {
 *         super(plugin);
 *     }
 *
 *     &#64;Override
 *     protected void configureListeners(Multibinder&lt;Listener&gt; binder) {
 *         binder.addBinding().to(UsefulListener.class);
 *     }
 *
 *     &#64;Override
 *     protected void configureCommands(Multibinder&lt;CommandConfigurator&gt; binder) {
 *         binder.addBinding().to(HelloCommand.class);
 *     }
 * }
 * </pre>
 *
 * @see PluginDependencies
 * @see PluginDependencies#fromInjector(Injector)
 */
public abstract class PluginModule extends AbstractModule {

    /**
     * The plugin this module contains.
     */
    protected final Plugin plugin;

    /**
     * Creates a new {@link PluginModule} with the specified {@code plugin}.
     *
     * @param plugin the plugin (that will be bound as a singleton)
     */
    public PluginModule(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     *
     * @apiNote <b>For inheritors</b>: override {@link #configureBindings()} to configure additional bindings.
     * @implSpec This implementation binds the plugin as a singleton,
     * adds every listener using {@link #configureListeners(Multibinder)}
     * and adds every command using {@link #configureCommands(Multibinder)},
     * and finally calls {@link #configureBindings()} to configure any additional bindings.
     */
    @Override
    protected final void configure() {
        configurePlugin();
        configureListeners(Multibinder.newSetBinder(binder(), Listener.class));
        configureCommands(Multibinder.newSetBinder(binder(), CommandConfigurator.class));

        configureBindings();
    }

    /**
     * Configures any additional bindings.
     * <p>
     * This method is called after listeners and commands bindings in {@link #configure()}.
     */
    protected void configureBindings() {
    }

    /**
     * Configures the plugin binding, so it can be later injected.
     * <p>
     * This method is called in {@link #configure()}.
     * @implSpec The default implementation binds the {@link PluginModule#plugin} to its own class.
     * <p>
     * For example, if the {@link PluginModule#plugin} is a {@code MyPlugin}, the {@link PluginModule#plugin}
     * can be retrieved by requesting a {@code MyPlugin}.
     */
    @SuppressWarnings("unchecked")
    protected void configurePlugin() {
        bind((Class<Plugin>) plugin.getClass()).toInstance(plugin);
    }

    /**
     * Configures the listeners to be used in the plugin.
     * <p>
     * Listeners can be registered using the provided {@code binder}, e.g.
     * <pre>binder.addBinding().to(MyListener.class);</pre>
     * <p>
     * This method is called in {@link #configure()}.
     *
     * @param binder the multi binder, used to bind listeners
     * @see Listener
     * @see Multibinder
     */
    protected void configureListeners(Multibinder<Listener> binder) {
    }

    /**
     * Configures the commands to be used in the plugin.<br>
     * Commands are represented by {@link CommandConfigurator}s, which are used to configure
     * commands by their name.
     * <p>
     * Commands can be registered using the provided {@code binder}, e.g.
     * <pre>binder.addBinding().to(MyCommand.class);</pre>
     * <p>
     * This method is called in {@link #configure()}.
     *
     * @param binder the multi binder, used to bind commands
     * @see CommandConfigurator
     * @see Multibinder
     */
    protected void configureCommands(Multibinder<CommandConfigurator> binder) {
    }
}
