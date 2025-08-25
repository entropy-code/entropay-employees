package com.entropyteam.entropay.common.sensitiveInformation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Annotation used to mark fields containing sensitive information that should be masked during serialization
 * based on the user's access rights.
 *
 * <p>This annotation can be applied to fields, parameters, and record components. When applied,
 * the annotated field will be serialized using {@link SensitiveInformationSerializer}.</p>
 *
 * <p><strong>Important:</strong> To use this annotation, the containing class must implement
 * the {@link EmployeeIdAware} interface to provide the employee ID context needed for
 * masking decisions.</p>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInformationSerializer.class)
public @interface SensitiveInformation {

}
