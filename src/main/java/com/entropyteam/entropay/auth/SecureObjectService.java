package com.entropyteam.entropay.auth;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SecureObjectService {

    private static final Logger LOGGER = LogManager.getLogger();

    public Object secureObjectByRole(Object object, Set<String> userRoles) {
        try {
            Class<?> clazz = object.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(SecureField.class)) {
                    SecureField annotation = field.getAnnotation(SecureField.class);
                    String[] roles = annotation.roles();
                    boolean contains = Arrays.stream(roles).anyMatch(userRoles::contains);
                    if (!contains) {
                        field.set(object, null);
                    }
                }
            }
            return object;
        } catch (IllegalAccessException e) {
            LOGGER.error("Error securing fields", e);
            throw new RuntimeException("Error securing fields", e);
        }
    }
}
