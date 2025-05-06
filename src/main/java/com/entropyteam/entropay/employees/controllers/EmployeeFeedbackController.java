package com.entropyteam.entropay.employees.controllers;

import java.util.UUID;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.FeedbackDto;
import com.entropyteam.entropay.employees.services.EmployeeFeedbackService;

@RestController
@CrossOrigin
@RequestMapping("/feedback/employee")
public class EmployeeFeedbackController extends BaseController<FeedbackDto, UUID> {

    public EmployeeFeedbackController(EmployeeFeedbackService employeeFeedbackService) {
        super(employeeFeedbackService);
    }
}