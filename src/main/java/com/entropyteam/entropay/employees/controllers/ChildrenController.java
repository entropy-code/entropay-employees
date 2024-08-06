package com.entropyteam.entropay.employees.controllers;

import com.entropyteam.entropay.common.BaseController;
import com.entropyteam.entropay.employees.dtos.ChildrenDto;
import com.entropyteam.entropay.employees.services.ChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/childrens", produces = MediaType.APPLICATION_JSON_VALUE)

public class ChildrenController extends BaseController<ChildrenDto, UUID> {
    @Autowired
    public ChildrenController(ChildrenService childrenService){ super(childrenService); }
}