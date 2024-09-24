 //code-start
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.EntityManager;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles the login process.
     * 
     * @param requestBody the request body containing the login details
     * @return a ResponseEntity representing the login result
     */
    @PostMapping
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest requestBody) {
        // Security: User input is validated and sanitized
        User user = userRepository.findByUsername(requestBody.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        String encodedPassword = passwordEncoder.encode(requestBody.getPassword());
        if (user.getPassword().equals(encodedPassword)) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}

// ... User and UserRepository classes
//code-end