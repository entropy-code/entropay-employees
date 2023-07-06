package com.entropyteam.entropay.employees.controllers;

import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.HolidayDto;
import com.entropyteam.entropay.employees.services.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.entropyteam.entropay.auth.AuthConstants.ROLE_ADMIN;

@RestController
@CrossOrigin
@Secured(ROLE_ADMIN)
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
