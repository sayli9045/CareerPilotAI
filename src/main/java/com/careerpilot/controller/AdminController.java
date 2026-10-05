package com.careerpilot.controller;

import com.careerpilot.dto.AdminUserResponse;
import com.careerpilot.entity.Interview;
import com.careerpilot.entity.Resume;
import com.careerpilot.repository.InterviewRepository;
import com.careerpilot.repository.ResumeRepository;
import com.careerpilot.repository.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final ResumeRepository resumeRepository;


    public AdminController(
            UserRepository userRepository,
            InterviewRepository interviewRepository,
            ResumeRepository resumeRepository) {

        this.userRepository = userRepository;
        this.interviewRepository = interviewRepository;
        this.resumeRepository = resumeRepository;
    }


    // =====================================================
    // ADMIN DASHBOARD
    // =====================================================

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> data = new HashMap<>();

        data.put(
                "totalUsers",
                userRepository.count()
        );

        data.put(
                "totalInterviews",
                interviewRepository.count()
        );

        data.put(
                "totalResumes",
                resumeRepository.count()
        );

        return data;
    }


    // =====================================================
    // ALL USERS
    // =====================================================

    @GetMapping("/users")
    public List<AdminUserResponse> getUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))
                .toList();
    }


    // =====================================================
    // GET USER BY ID
    // =====================================================

    @GetMapping("/users/{id}")
    public AdminUserResponse getUserById(
            @PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }


    // =====================================================
    // ALL INTERVIEWS
    // =====================================================

    @GetMapping("/interviews")
    public List<Interview> getInterviews() {

        return interviewRepository.findAll();
    }


    // =====================================================
    // ALL RESUMES
    // =====================================================

    @GetMapping("/resumes")
    public List<Resume> getResumes() {

        return resumeRepository.findAll();
    }


    // =====================================================
    // VIEW RESUME
    // =====================================================

    @GetMapping("/resumes/view/{id}")
    public ResponseEntity<?> viewResume(
            @PathVariable Long id) {

        try {

            Resume resume =
                    resumeRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Resume not found"
                                    )
                            );

            Path filePath =
                    Paths.get(
                            resume.getFilePath()
                    );

            if (!Files.exists(filePath)) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            byte[] fileBytes =
                    Files.readAllBytes(filePath);


            MediaType mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;


            if (resume.getFileType() != null) {

                try {

                    mediaType =
                            MediaType.parseMediaType(
                                    resume.getFileType()
                            );

                } catch (Exception ignored) {

                }
            }


            return ResponseEntity.ok()

                    .contentType(mediaType)

                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                                    resume.getFileName() +
                                    "\""
                    )

                    .body(fileBytes);


        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =====================================================
    // DOWNLOAD RESUME
    // =====================================================

    @GetMapping("/resumes/download/{id}")
    public ResponseEntity<?> downloadResume(
            @PathVariable Long id) {

        try {

            Resume resume =
                    resumeRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Resume not found"
                                    )
                            );


            Path filePath =
                    Paths.get(
                            resume.getFilePath()
                    );


            if (!Files.exists(filePath)) {

                return ResponseEntity
                        .notFound()
                        .build();
            }


            byte[] fileBytes =
                    Files.readAllBytes(filePath);


            MediaType mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;


            if (resume.getFileType() != null) {

                try {

                    mediaType =
                            MediaType.parseMediaType(
                                    resume.getFileType()
                            );

                } catch (Exception ignored) {

                }
            }


            return ResponseEntity.ok()

                    .contentType(mediaType)

                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    resume.getFileName() +
                                    "\""
                    )

                    .body(fileBytes);


        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =====================================================
    // DELETE RESUME
    // =====================================================

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<?> deleteResume(
            @PathVariable Long id) {

        try {

            Resume resume =
                    resumeRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Resume not found"
                                    )
                            );


            // Delete physical file
            if (resume.getFilePath() != null) {

                Path filePath =
                        Paths.get(
                                resume.getFilePath()
                        );

                Files.deleteIfExists(filePath);
            }


            // Delete database record
            resumeRepository.delete(resume);


            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Resume deleted successfully"
                    )
            );


        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}