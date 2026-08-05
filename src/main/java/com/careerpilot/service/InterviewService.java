package com.careerpilot.service;

import com.careerpilot.dto.InterviewAnswerRequest;
import com.careerpilot.dto.InterviewRequest;
import com.careerpilot.entity.Interview;
import com.careerpilot.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public List<String> generateQuestions(InterviewRequest request) {

        if (request.getTechnology().equalsIgnoreCase("Java")) {

            return List.of(
                    "What is JVM?",
                    "Explain OOP concepts.",
                    "Difference between JDK and JRE?",
                    "What is Polymorphism?",
                    "What is Spring Boot?"
            );
        }

        if (request.getTechnology().equalsIgnoreCase("Python")) {

            return List.of(
                    "What is Python?",
                    "Difference between List and Tuple?",
                    "What is PIP?",
                    "Explain Dictionary.",
                    "What is Lambda Function?"
            );
        }

        if (request.getTechnology().equalsIgnoreCase("DevOps")) {

            return List.of(
                    "What is Docker?",
                    "What is Kubernetes?",
                    "Explain CI/CD.",
                    "What is Jenkins?",
                    "What is Git?"
            );
        }

        return List.of("Tell me about yourself.");
    }

    public int submitInterview(InterviewAnswerRequest request) {

        int score = 0;

        for (String answer : request.getAnswers()) {

            if (answer != null && answer.trim().length() > 20) {
                score += 2;
            }

        }

        Interview interview = new Interview();

        interview.setTechnology(request.getTechnology());
        interview.setDifficulty(request.getDifficulty());
        interview.setTotalQuestions(request.getAnswers().size());
        interview.setScore(score);
        interview.setStatus("COMPLETED");
        interview.setInterviewDate(LocalDateTime.now());

        interviewRepository.save(interview);

        return score;
    }
}