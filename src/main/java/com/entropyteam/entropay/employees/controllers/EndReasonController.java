package com.entropyteam.entropay.employees.controllers;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.EndReasonDto;
import com.entropyteam.entropay.employees.services.EndReasonService;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_DEVELOPMENT})
@RequestMapping(value = "/end-reasons", produces = MediaType.APPLICATION_JSON_VALUE)
public class EndReasonController extends BaseController<EndReasonDto, UUID> {

    public EndReasonController(EndReasonService endReasonService) { super(endReasonService); }

}
