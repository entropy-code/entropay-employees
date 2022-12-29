package com.entropyteam.entropay.auth;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class SecureObjectService {

    private static final Logger LOGGER = LogManager.getLogger();

    public Object secureObjectByRole(Object object, AppRole userRole) {
        try {
            Class<?> clazz = object.getClass();
            Constructor<?> ctor = clazz.getConstructor();
            Object objectCopy = ctor.newInstance();
            Field[] parentFields = clazz.getSuperclass().getDeclaredFields();
            Field[] classFields = clazz.getDeclaredFields();
            Field[] allFields = ArrayUtils.addAll(parentFields, classFields);
            for (Field field : allFields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(SecureField.class)) {
                    SecureField annotation = field.getAnnotation(SecureField.class);
                    String[] roles = annotation.roles();
                    boolean contains = Arrays.stream(roles).anyMatch(r -> StringUtils.equalsIgnoreCase(r, userRole.value));
                    if (!contains) {
                        field.set(objectCopy, null);
                        continue;
                    }
                }
                field.set(objectCopy, field.get(object));
            }
            return objectCopy;
        } catch (Exception e) {
            LOGGER.error("Error securing fields", e);
            throw new RuntimeException("Error securing fields", e);
        }
    }
}
