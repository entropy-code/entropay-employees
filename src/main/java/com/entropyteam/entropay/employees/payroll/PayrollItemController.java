package com.entropyteam.entropay.employees.payroll;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.common.ReactAdminParams;

/**
 * REST surface for individual payroll items. Inherited list/get/update from {@link BaseController};
 * the service rejects create/delete and gates update behind run-status DRAFT.
 *
 * <p>{@code getList}/{@code getOne} are overridden to re-declare {@code @Secured} without
 * {@code ROLE_ANALYST}/{@code ROLE_DEVELOPMENT}: payroll items expose company-wide salary data, so
 * the inherited method-level annotations (which would otherwise override this class-level
 * restriction) must not be allowed to widen access.
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
}
