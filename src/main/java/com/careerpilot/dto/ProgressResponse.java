package com.careerpilot.dto;

public class ProgressResponse {

    private long totalInterviews;
    private double averageScore;
    private int highestScore;
    private int lowestScore;

    public ProgressResponse() {
    }

    public long getTotalInterviews() {
        return totalInterviews;
    }

    public void setTotalInterviews(long totalInterviews) {
        this.totalInterviews = totalInterviews;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(int lowestScore) {
        this.lowestScore = lowestScore;
    }
}