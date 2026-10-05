package com.careerpilot.repository;

import com.careerpilot.entity.Resume;
import com.careerpilot.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUser(User user);

    Optional<Resume> findTopByUserOrderByUploadDateDesc(User user);

    List<Resume> findByUserId(Long userId);

    Optional<Resume> findByIdAndUser(Long id, User user);

}