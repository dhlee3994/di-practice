package org.example.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BeanFactory {

    private final Set<Class<?>> preInstantiatedClazz;
    private Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(final Set<Class<?>> preInstantiatedClazz) {
        this.preInstantiatedClazz = preInstantiatedClazz;
        init();
    }

    private void init() {
        for (Class<?> clazz : preInstantiatedClazz) {
            Object instance = createInstance(clazz);
            beans.put(clazz, instance);
        }
    }

    private Object createInstance(final Class<?> clazz) {
        final Constructor<?> constructor = findConstructor(clazz);

        List<Object> parameters = new ArrayList<>();
        for (Class<?> typeClass : constructor.getParameterTypes()) {
            parameters.add(getParameterByClass(typeClass));
        }

        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (Objects.nonNull(constructor)) {
            return constructor;
        }

        return clazz.getConstructors()[0];
    }

    private Object getParameterByClass(final Class<?> typeClass) {
        final Object instanceBean = getBean(typeClass);

        if (Objects.nonNull(instanceBean)) {
            return instanceBean;
        }

        return createInstance(typeClass);
    }

    public <T> T getBean(final Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }
}
