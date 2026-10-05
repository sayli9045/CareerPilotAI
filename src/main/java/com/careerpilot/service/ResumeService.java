package com.careerpilot.service;

import com.careerpilot.entity.Resume;
import com.careerpilot.entity.User;
import com.careerpilot.repository.ResumeRepository;
import com.careerpilot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    private final Path uploadDirectory =
            Paths.get("uploads/resumes");

    public ResumeService(
            ResumeRepository resumeRepository,
            UserRepository userRepository) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public Resume uploadResume(
            MultipartFile file,
            String email) throws IOException {

        // 1. Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Please select a resume file."
            );
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new RuntimeException(
                    "Invalid file name."
            );
        }

        // 2. Validate file extension
        String lowerFileName =
                fileName.toLowerCase();

        if (!lowerFileName.endsWith(".pdf")
                && !lowerFileName.endsWith(".doc")
                && !lowerFileName.endsWith(".docx")) {

            throw new RuntimeException(
                    "Only PDF, DOC and DOCX files are allowed."
            );
        }

        // 3. Find logged-in user
        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found: " + email
                                )
                        );

        System.out.println(
                "Resume uploaded by: "
                        + user.getEmail()
        );

        System.out.println(
                "User ID: "
                        + user.getId()
        );

        // 4. Create upload directory
        Files.createDirectories(uploadDirectory);

        // 5. Create unique stored file name
        String storedFileName =
                System.currentTimeMillis()
                        + "_"
                        + Paths.get(fileName)
                        .getFileName()
                        .toString();

        // 6. Create physical file path
        Path filePath =
                uploadDirectory.resolve(storedFileName);

        // 7. Save physical file
        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // 8. Create Resume entity
        Resume resume = new Resume();

        // Connect resume with logged-in user
        resume.setUser(user);

        // Original file name
        resume.setFileName(fileName);

        // IMPORTANT:
        // Save the physical file path in database
        resume.setFilePath(
                filePath.toString()
        );

        // File size
        resume.setFileSize(
                file.getSize()
        );

        // File type
        resume.setFileType(
                file.getContentType()
        );

        // Resume status
        resume.setStatus(
                "UPLOADED"
        );

        // Upload date
        resume.setUploadDate(
                LocalDateTime.now()
        );

        // 9. Save resume in database
        return resumeRepository.save(resume);
    }
    // =====================================================
// GET LATEST RESUME
// =====================================================

    public Resume getLatestResume(User user) {

        return resumeRepository
                .findTopByUserOrderByUploadDateDesc(user)
                .orElse(null);
    }


// =====================================================
// GET RESUME BY ID FOR SPECIFIC USER
// =====================================================

    public Resume getResumeByIdAndUser(
            Long id,
            User user) {

        return resumeRepository
                .findByIdAndUser(id, user)
                .orElse(null);
    }
}