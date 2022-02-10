package com.example.firstcommit.controller;


import com.example.firstcommit.entities.User;
import com.example.firstcommit.repository.UserRepository;
import com.example.firstcommit.security.jwt.JwtTokenUtil;
import com.example.firstcommit.security.payload.JwtResponse;
import com.example.firstcommit.security.payload.LoginRequest;
import com.example.firstcommit.security.payload.MessageResponse;
import com.example.firstcommit.security.payload.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controlador para llevar a cabo la autenticación utilizando JWT
 * <p>
 * Se utiliza AuthenticationManager para autenticar las credenciales que son el
 * usuario y password que llegan por POST en el cuerpo de la petición
 * <p>
 * Si las credenciales son válidas se genera un token JWT como respuesta
 */
@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authManager,
                          UserRepository userRepository,
                          PasswordEncoder encoder,
                          JwtTokenUtil jwtTokenUtil) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

        if (isEmail(loginRequest.getUsername())) {
            String email = loginRequest.getUsername();
            Optional<User>  userOptional = userRepository.findByEmail(email);
            userOptional.ifPresent(user -> loginRequest.setUsername(user.getUsername()));
        }

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userDB = userRepository.findByUsername(userDetails.getUsername()).get();

        return ResponseEntity.ok(new JwtResponse(jwt, userDB));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest signUpRequest) {

        // Check 1: username
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check 2: email
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/auth/check")
    public ResponseEntity<Void> checkToken() {
        return ResponseEntity.ok().build();
    }

    private static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("[\\w\\d.%+-]+@\\b[\\w\\d.-]+.[\\w]{2,4}");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
