package com.careerpilot.service;
import com.careerpilot.dto.LoginRequest;
import java.util.Optional;

import com.careerpilot.dto.RegisterRequest;
import com.careerpilot.entity.User;
import com.careerpilot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));        user.setRole("STUDENT");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "Registration Successful";
    }

public String login(LoginRequest request) {

    Optional<User> optionalUser =
            userRepository.findByEmail(request.getEmail());

    if(optionalUser.isEmpty()){

        return "User not found";

    }

    User user = optionalUser.get();

    if(passwordEncoder.matches(request.getPassword(),
            user.getPassword())){

        return "Login Successful";

    }

    return "Invalid Password";

}
}