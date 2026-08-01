package com.careerpilot.controller;

import com.careerpilot.dto.InterviewRequest;
import com.careerpilot.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin("*")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/start")
    public List<String> startInterview(
            @RequestBody InterviewRequest request){

        return interviewService.generateQuestions(request);

    }

}