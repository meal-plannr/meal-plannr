package com.mealplanner.meal;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HandlersTest {

    @Test
    public void all_handlers_have_zero_argument_constructor() {
        final Reflections reflections = new Reflections("com.mealplanner");

        @SuppressWarnings("rawtypes")
        final Set<Class<? extends RequestHandler>> subTypes = reflections.getSubTypesOf(RequestHandler.class);
        subTypes.stream().forEach(clazz -> assertClassHasOnlyZeroArgumentConstructor(clazz));
    }

    private void assertClassHasOnlyZeroArgumentConstructor(final Class<?> clazz) {
        final boolean hasZeroArgConstructor = Stream.of(clazz.getConstructors())
                .anyMatch(c -> c.getParameterCount() == 0);

        if (!hasZeroArgConstructor) {
            Assertions.fail(clazz.getName() + " does not have a zero argument constructor. This will prevent it being created in AWS");
        }
    }
}