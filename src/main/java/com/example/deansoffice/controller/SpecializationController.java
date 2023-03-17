package com.example.deansoffice.controller;

import com.example.deansoffice.service.SpecializationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specializations")
public class SpecializationController {
    private SpecializationService specializationService;
    public SpecializationController(SpecializationService theSpecializationService) {
        specializationService = theSpecializationService;
    }
}
