package com.rapidmart.controllers;

import com.rapidmart.models.User;
import com.rapidmart.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @PostMapping("/assign-coadmin")
    public ResponseEntity<String> assignCoAdmin(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(User.Role.CO_ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok("User promoted to CO_ADMIN");
    }
}
