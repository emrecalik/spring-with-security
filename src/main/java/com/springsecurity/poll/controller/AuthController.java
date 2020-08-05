package com.springsecurity.poll.controller;

import com.springsecurity.poll.domain.User;
import com.springsecurity.poll.model.request.LoginRequestDTO;
import com.springsecurity.poll.model.request.SignUpRequestDTO;
import com.springsecurity.poll.model.response.LoginResponseDTO;
import com.springsecurity.poll.model.response.ApiResponseDTO;
import com.springsecurity.poll.repository.UserRepository;
import com.springsecurity.poll.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;

    JwtTokenProvider jwtTokenProvider;

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO signIn(@RequestBody LoginRequestDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUserNameOrEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return new LoginResponseDTO(jwtToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpDTO) {

        if (userRepository.existsByUserName(signUpDTO.getUserName())) {
            return new ResponseEntity<>(new ApiResponseDTO(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponseDTO(false, "Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        User newUser = User.builder()
                .name(signUpDTO.getName())
                .userName(signUpDTO.getUserName())
                .email(signUpDTO.getEmail()).build();

        newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

        User savedUser = userRepository.save(newUser);

        UriComponents savedUserUri = UriComponentsBuilder.fromPath("/api/users/{user}")
                .buildAndExpand(savedUser.getName());

        return ResponseEntity.created(savedUserUri.toUri())
                .body(new ApiResponseDTO(true, "User registered successfully"));
    }
}
