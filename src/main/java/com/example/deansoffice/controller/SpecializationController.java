package com.example.deansoffice.controller;

import com.example.deansoffice.service.SpecializationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specializations")
public class SpecializationController {
    private SpecializationService specializationService;
    public SpecializationController(@Qualifier("specializationServiceImpl") SpecializationService theSpecializationService) {
        specializationService = theSpecializationService;
    }
}
