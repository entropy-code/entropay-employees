package com.entropyteam.entropay.employees.controllers;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_DEVELOPMENT;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_HR_DIRECTOR;
import static com.entropyteam.entropay.auth.AuthConstants.ROLE_MANAGER_HR;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.HolidayDto;
import com.entropyteam.entropay.employees.services.HolidayService;

@RestController
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_HR_DIRECTOR, ROLE_MANAGER_HR, ROLE_DEVELOPMENT})
@RequestMapping(value = "/holidays", produces = MediaType.APPLICATION_JSON_VALUE)
public class HolidayController extends BaseController<HolidayDto, UUID> {

    private final HolidayService holidayService;

    @Autowired
    public HolidayController(HolidayService holidayService) {
        super(holidayService);
        this.holidayService = holidayService;
    }

    @GetMapping("/years")
    public ResponseEntity<List<Map<String, Integer>>> getHolidayYears() {
        List<Map<String, Integer>> holidayYears = holidayService.getHolidayYears();
        return ResponseEntity.ok().header(BaseController.X_TOTAL_COUNT, String.valueOf(holidayYears.size()))
                .body(holidayYears);
    }
}
