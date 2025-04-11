package ru.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IoC {

    private static final Logger logger = LoggerFactory.getLogger(IoC.class);

    @SuppressWarnings("unchecked")
    static <T> T createProxyByClass(Class<T> tClass) {
        try {
            Constructor<T> constructor = tClass.getConstructor();
            InvocationHandler handler = new DemoInvocationHandler<>(constructor.newInstance());
            return (T) Proxy.newProxyInstance(IoC.class.getClassLoader(), tClass.getInterfaces(), handler);
        } catch (Exception e) {
            logger.error("Ошибка при создании прокси", e);
            throw new RuntimeException(e);
        }
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T instance;
        private final Set<Method> methodsToLog;

        DemoInvocationHandler(T instance) {
            this.instance = instance;
            this.methodsToLog = Arrays.stream(this.instance.getClass().getMethods())
                    .filter(method -> method.getDeclaredAnnotation(Log.class) != null)
                    .flatMap(method ->
                            Arrays.stream(this.instance.getClass().getInterfaces())
                                    .flatMap(iface -> Arrays.stream(iface.getMethods()))
                                    .filter(interfaceMethod -> method.getName().equals(interfaceMethod.getName())
                                            && Arrays.equals(method.getParameterTypes(), interfaceMethod.getParameterTypes())))
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodsToLog.contains(method)) {
                logger.info("executed method: {}, param: {}",
                        method.getName(),
                        Arrays.stream(args)
                                .map(Object::toString)
                                .collect(Collectors.joining(",")));
            }
            return method.invoke(instance, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + instance + '}';
        }
    }
}
