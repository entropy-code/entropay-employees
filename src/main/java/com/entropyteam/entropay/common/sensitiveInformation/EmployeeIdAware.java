package com.entropyteam.entropay.common.sensitiveInformation;

import java.util.UUID;

/**
 * Defines a contract for types that expose an associated employee identifier.
 * <p>
 * Classes that declare fields annotated with {@link SensitiveInformation}
 * should implement this interface so infrastructure (e.g., serializers)
 * can access the owning employee ID for masking, auditing, or validation.
 * </p>
 */
public interface EmployeeIdAware {

    UUID getEmployeeId();
}
