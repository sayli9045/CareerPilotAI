package com.careerpilot.dto;

public class InterviewRequest {

    private String technology;
    private String difficulty;

    public InterviewRequest() {
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
}