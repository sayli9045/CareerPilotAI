package com.careerpilot.dto;

import java.util.List;

public class InterviewAnswerRequest {

    private String technology;
    private String difficulty;
    private List<String> answers;

    public InterviewAnswerRequest() {
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}