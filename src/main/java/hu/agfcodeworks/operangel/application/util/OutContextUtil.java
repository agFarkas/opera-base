package hu.agfcodeworks.operangel.application.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class OutContextUtil {

    private final Map<Class<?>, Object> components = new HashMap<>();

    public void setUpComponent(@NonNull Class<? extends Object> clazz) {
        try {
            var constructor = clazz.getConstructors()[0];
            var params = new LinkedList<>();

            for (var paramClass : constructor.getParameterTypes()) {
                var param = obtainParamComponent(paramClass);
                params.add(param);
            }

            var component = constructor.newInstance(params.toArray());
            components.put(clazz, component);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Object obtainParamComponent(Class<?> paramClass) {
        var componentOpt = getOptionalComponent(paramClass);


        if (componentOpt.isPresent()) {
            return componentOpt.get();
        }

        setUpComponent(paramClass);
        return components.get(paramClass);
    }

    public <T> T getComponent(Class<T> clazz) {
        var componentOpt = getOptionalComponent(clazz);

        if(componentOpt.isPresent()) {
            return componentOpt.get();
        }

        setUpComponent(clazz);
        return (T) components.get(clazz);
    }


    private <T> Optional<T> getOptionalComponent(Class<T> clazz) {
        return Optional.ofNullable((T) components.get(clazz));
    }
}
