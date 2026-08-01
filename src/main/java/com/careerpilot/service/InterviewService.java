package com.careerpilot.service;

import com.careerpilot.dto.InterviewRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    public List<String> generateQuestions(InterviewRequest request){

        if(request.getTechnology().equalsIgnoreCase("Java")){

            return List.of(
                    "What is JVM?",
                    "Explain OOP.",
                    "Difference between JDK and JRE?",
                    "What is Polymorphism?",
                    "What is Spring Boot?"
            );

        }

        if(request.getTechnology().equalsIgnoreCase("Python")){

            return List.of(
                    "What is Python?",
                    "Explain List and Tuple.",
                    "What is PIP?",
                    "What is a Dictionary?",
                    "Explain Lambda Function."
            );

        }

        return List.of(
                "Tell me about yourself."
        );

    }

}