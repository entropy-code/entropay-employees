package com.entropyteam.entropay.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Static holder for Spring ApplicationContext to allow non-Spring managed components
 * (e.g., Jackson serializers) to obtain Spring beans when needed.
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContext.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
        LOGGER.debug("ApplicationContext initialized in SpringContext holder: {}", applicationContext.getDisplayName());
    }

    public static <T> T getBean(Class<T> clazz) {
        if (context == null) {
            LOGGER.warn("Spring ApplicationContext is not yet initialized when requesting bean: {}", clazz.getName());
            return null;
        }
        try {
            return context.getBean(clazz);
        } catch (BeansException ex) {
            LOGGER.warn("Failed to get bean of type {} from ApplicationContext: {}", clazz.getName(), ex.getMessage());
            return null;
        }
    }
}
