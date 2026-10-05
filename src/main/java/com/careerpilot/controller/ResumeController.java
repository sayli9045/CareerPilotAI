package com.careerpilot.controller;

import com.careerpilot.entity.Resume;
import com.careerpilot.entity.User;
import com.careerpilot.repository.UserRepository;
import com.careerpilot.service.ResumeService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin("*")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;

    public ResumeController(
            ResumeService resumeService,
            UserRepository userRepository) {

        this.resumeService = resumeService;
        this.userRepository = userRepository;
    }


    // =====================================================
    // UPLOAD RESUME
    // =====================================================

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();

            Resume resume =
                    resumeService.uploadResume(
                            file,
                            email
                    );

            return ResponseEntity.ok(resume);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =====================================================
    // GET LOGGED-IN USER'S LATEST RESUME
    // =====================================================

    @GetMapping("/my-resume")
    public ResponseEntity<?> getMyResume(
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();

            User user =
                    userRepository.findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );

            Resume resume =
                    resumeService.getLatestResume(user);

            if (resume == null) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            return ResponseEntity.ok(resume);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =====================================================
    // VIEW RESUME
    // =====================================================

    @GetMapping("/view/{id}")
    public ResponseEntity<?> viewResume(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();

            User user =
                    userRepository.findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );

            Resume resume =
                    resumeService.getResumeByIdAndUser(
                            id,
                            user
                    );

            if (resume == null) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            Path filePath =
                    Paths.get(resume.getFilePath());

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

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadResume(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();

            User user =
                    userRepository.findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );

            Resume resume =
                    resumeService.getResumeByIdAndUser(
                            id,
                            user
                    );

            if (resume == null) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            Path filePath =
                    Paths.get(resume.getFilePath());

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
}