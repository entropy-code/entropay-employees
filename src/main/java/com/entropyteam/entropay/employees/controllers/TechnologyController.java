package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;

import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.TechnologyDto;
import com.entropyteam.entropay.employees.models.TechnologyType;
import com.entropyteam.entropay.employees.services.TechnologyService;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_DEVELOPMENT})
@RequestMapping(value = "/technologies", produces = MediaType.APPLICATION_JSON_VALUE)
public class TechnologyController extends BaseController<TechnologyDto, UUID> {

    private static final TechnologyType[] TECHNOLOGY_TYPES = Arrays.stream(TechnologyType.values())
            .sorted(Comparator.comparing(TechnologyType::getValue))
            .toArray(TechnologyType[]::new);

    public TechnologyController(TechnologyService technologyService) {
        super(technologyService);
    }

    @GetMapping("/types")
    public ResponseEntity<TechnologyType[]> getTechnologyTypes() {
        return ResponseEntity.ok()
                .header(BaseController.X_TOTAL_COUNT, String.valueOf(TECHNOLOGY_TYPES.length))
                .body(TECHNOLOGY_TYPES);

    }
}