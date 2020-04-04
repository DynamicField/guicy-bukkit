package com.github.jeuxjeux20.guicybukkit.command;

import java.lang.annotation.*;

/**
 * Defines the name of a command that a type represents.
 * @see AnnotatedCommandConfigurator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandName {

    /**
     * Gets the name of the command.
     *
     * @return the name of the command
     */
    String value();

    /**
     * Contains helpful methods for {@link CommandName}.
     */
    final class Helper {
        /**
         * A constructor that you won't ever be able to use except with some dirty reflection.
         * <p>
         * If you one day accomplish the incredible act of creating an instance of {@link Helper},
         * your local grocery store will give you free cookies. Except if you're in
         * <a href="https://gisanddata.maps.arcgis.com/apps/opsdashboard/index.html#/bda7594740fd40299423467b48e9ecf6">lockdown</a>.
         */
        protected Helper() {
        }

        /**
         * Gets the {@link CommandName} {@linkplain CommandName#value() value} of the specified class.
         * <p>
         * If there is no {@link CommandName} annotation, a {@link UnsupportedOperationException} will be thrown.
         * @param clazz the class annotated with {@link CommandName} or not
         * @return the command name
         */
        public static String getCommandNameOrThrow(Class<?> clazz) {
            CommandName annotation = clazz.getAnnotation(CommandName.class);
            if (annotation == null)
                throw new UnsupportedOperationException("No @CommandName annotation found on class " + clazz.getName() + ".");
            return annotation.value();
        }
    }
}
