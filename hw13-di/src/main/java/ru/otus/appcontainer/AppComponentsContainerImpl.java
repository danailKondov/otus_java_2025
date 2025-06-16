package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exceptions.BeanAlreadyExistsException;
import ru.otus.exceptions.BeanCreationException;
import ru.otus.exceptions.BeanDuplicateException;
import ru.otus.exceptions.BeanNotFoundException;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        Arrays.stream(initialConfigClass)
                .filter(cl -> cl.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparing(method -> method.getDeclaredAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);

    }

    public AppComponentsContainerImpl(String path) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(path));
        reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).stream()
                .sorted(Comparator.comparing(method -> method.getDeclaredAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object instance = getInstance(configClass);
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getDeclaredAnnotation(AppComponent.class).order()))
                .forEach(method -> createBean(method, instance));
    }

    private Object getInstance(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Error creating new instance", e);
            throw new BeanCreationException("Error creating new instance", e);
        }
    }

    private void createBean(Method method, Object instance) {
        String name = method.getDeclaredAnnotation(AppComponent.class).name();
        if (appComponentsByName.containsKey(name)) {
            throw new BeanAlreadyExistsException("Bean already exist: " + name);
        }
        Object[] params = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray();
        Object bean = createBean(method, instance, params);
        appComponents.add(bean);
        appComponentsByName.put(name, bean);
    }

    private Object createBean(Method method, Object config, Object[] objects) {
        try {
            return method.invoke(config, objects);
        } catch (Exception e) {
            logger.error("Error invoke method", e);
            throw new BeanCreationException("Error invoke method", e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> components = appComponents.stream()
                .filter(componentClass::isInstance)
                .map(obj -> (C) obj)
                .toList();

        if (components.isEmpty()) {
            throw new BeanNotFoundException("Bean not found: " + componentClass);
        } else if (components.size() > 1) {
            throw new BeanDuplicateException("Bean duplicate found: " + componentClass);
        } else {
            return components.getFirst();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        Object bean = appComponentsByName.get(componentName);
        if (bean == null) {
            throw new BeanNotFoundException("Bean not found: " + componentName);
        }
        return (C) bean;
    }
}
