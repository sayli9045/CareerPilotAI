package com.careerpilot.repository;

import com.careerpilot.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("SELECT AVG(i.score) FROM Interview i")
    Double getAverageScore();

    @Query("SELECT MAX(i.score) FROM Interview i")
    Integer getHighestScore();

    @Query("SELECT MIN(i.score) FROM Interview i")
    Integer getLowestScore();

}