package com.example.deansoffice.controller;

import com.example.deansoffice.dto.MajorDetailsDTO;
import com.example.deansoffice.service.MajorDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/major-details")
public class MajorDetailsController {

    private final MajorDetailsService majorDetailsService;

    public MajorDetailsController(@Qualifier("majorDetailsServiceImpl") MajorDetailsService theMajorDetailsService) {
        majorDetailsService = theMajorDetailsService;
    }

    @GetMapping("")
    private ResponseEntity<Map<String,List<MajorDetailsDTO>>> getAllMajors(@RequestParam(name="year", required = false) String year) {
        return majorDetailsService.getAllMajors(Integer.parseInt(year));
    }
}
