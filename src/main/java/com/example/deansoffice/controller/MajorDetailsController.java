package com.example.deansoffice.controller;

import com.example.deansoffice.dto.MajorDetailsDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.service.MajorDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/major-details")
public class MajorDetailsController {

    private MajorDetailsService majorDetailsService;

    public MajorDetailsController(MajorDetailsService theMajorDetailsService) {
        majorDetailsService = theMajorDetailsService;
    }

    @GetMapping("")
    private ResponseEntity<Map<String,List<MajorDetailsDTO>>> getAllMajors(@RequestParam(name="year", required = false) String year) {
        Map<String,List<MajorDetailsDTO>> result = new HashMap<>(1);

        if(year != null)
            result.put("majors",majorDetailsService.getMajorsByYear(Integer.parseInt(year)));
        else
            result.put("majors",majorDetailsService.getAllMajors());

        return ResponseEntity.ok().body(result);
    }
}
