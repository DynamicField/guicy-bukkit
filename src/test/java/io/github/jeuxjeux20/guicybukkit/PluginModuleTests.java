package io.github.jeuxjeux20.guicybukkit;

import be.seeseemelk.mockbukkit.MockPlugin;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import io.github.jeuxjeux20.guicybukkit.command.CommandConfigurator;
import org.bukkit.event.Listener;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PluginModuleTests extends PluginTestBase {

    @Test
    void bindsPluginInstance() {
        Injector injector = Guice.createInjector(new PluginModule(plugin) {
        });

        MockPlugin injectedPlugin = injector.getInstance(MockPlugin.class);

        assertSame(plugin, injectedPlugin);
    }

    @Test
    void bindsListeners() {
        Listener testListener = Mockito.mock(Listener.class);

        Injector injector = Guice.createInjector(new PluginModule(plugin) {
            @Override
            protected void configureListeners(Multibinder<Listener> binder) {
                binder.addBinding().to(testListener.getClass());
            }
        });

        Set<Listener> listeners = injector.getInstance(PluginDependencies.class).getListeners();

        Set<Class<? extends Listener>> listenerClasses
                = listeners.stream().map(Listener::getClass).collect(Collectors.toSet());
        MatcherAssert.assertThat(listenerClasses, containsInAnyOrder(testListener.getClass()));
    }
    @Test
    void bindsCommands() {
        CommandConfigurator commandConfigurator = Mockito.mock(CommandConfigurator.class);

        Injector injector = Guice.createInjector(new PluginModule(plugin) {
            @Override
            protected void configureCommands(Multibinder<CommandConfigurator> binder) {
                binder.addBinding().to(commandConfigurator.getClass());
            }
        });

        Set<CommandConfigurator> commandConfigurators = injector.getInstance(PluginDependencies.class).getCommandsConfigurators();

        Set<Class<? extends CommandConfigurator>> commandConfiguratorClasses
                = commandConfigurators.stream().map(CommandConfigurator::getClass).collect(Collectors.toSet());
        MatcherAssert.assertThat(commandConfiguratorClasses, containsInAnyOrder(commandConfigurator.getClass()));
    }

}
