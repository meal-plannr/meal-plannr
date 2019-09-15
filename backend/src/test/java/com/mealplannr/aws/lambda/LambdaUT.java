package com.mealplannr.aws.lambda;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import com.amazonaws.services.lambda.runtime.RequestHandler;

class LambdaUT {

    @Test
    void all_handlers_have_zero_argument_constructor() {
        final Reflections reflections = new Reflections("com.mealplannr");

        final Set<Class<? extends RequestHandler>> subTypes = reflections.getSubTypesOf(RequestHandler.class);
        subTypes.forEach(this::assertClassHasOnlyZeroArgumentConstructor);
    }

    private void assertClassHasOnlyZeroArgumentConstructor(final Class<?> clazz) {
        final boolean hasZeroArgConstructor = Stream.of(clazz.getConstructors())
                .anyMatch(c -> c.getParameterCount() == 0);

        if (!hasZeroArgConstructor) {
            Assertions.fail(clazz.getName() + " does not have a zero argument constructor. This will prevent it being instantiated in AWS");
        }
    }
}