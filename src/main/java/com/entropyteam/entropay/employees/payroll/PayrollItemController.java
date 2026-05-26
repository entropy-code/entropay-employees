package com.entropyteam.entropay.employees.payroll;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.ReactAdminParams;

/**
 * REST surface for individual payroll items. Inherited from {@link BaseController}; the service
 * rejects create/delete and gates update behind run-status DRAFT.
 *
 * <p>Every CRUD method is re-declared with {@code @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR,
 * ROLE_MANAGER_HR})}. The class-level {@code @Secured} alone is not enough: Spring's method-level
 * {@code @Secured} on the inherited methods (e.g. {@code BaseController.update}, which permits
 * {@code ROLE_DEVELOPMENT}) takes precedence and would otherwise widen access to company-wide
 * salary data.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/payroll-items", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
class PayrollItemController extends BaseController<PayrollItemDto, UUID> {

    public PayrollItemController(PayrollItemService payrollItemService) {
        super(payrollItemService);
    }

    @Override
    @GetMapping
    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
    public ResponseEntity<List<PayrollItemDto>> getList(ReactAdminParams params) {
        return super.getList(params);
    }

    @Override
    @GetMapping("/{id}")
    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
    public ResponseEntity<PayrollItemDto> getOne(@PathVariable(value = "id") UUID id) {
        return super.getOne(id);
    }

    @Override
    @PutMapping("/{id}")
    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
    public ResponseEntity<PayrollItemDto> update(@PathVariable(value = "id") UUID id,
            @Valid @RequestBody PayrollItemDto entity) {
        return super.update(id, entity);
    }

    @Override
    @PostMapping
    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
    public ResponseEntity<PayrollItemDto> create(@Valid @RequestBody PayrollItemDto entity) {
        return super.create(entity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR})
    public ResponseEntity<PayrollItemDto> delete(@PathVariable(value = "id") UUID id) {
        return super.delete(id);
    }
}
