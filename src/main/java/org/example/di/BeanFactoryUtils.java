package org.example.di;

import java.lang.reflect.Constructor;
import java.util.Set;
import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

public class BeanFactoryUtils {

    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        final Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz,
            ReflectionUtils.withAnnotation(Inject.class));

        if (injectedConstructors.isEmpty()) {
            return null;
        }

        return injectedConstructors.iterator().next();
    }
}
