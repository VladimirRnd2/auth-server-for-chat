package com.voronkov.authserverforchat.rest;

import com.voronkov.authserverforchat.dto.AuthRefreshRequest;
import com.voronkov.authserverforchat.dto.AuthRequest;
import com.voronkov.authserverforchat.dto.AuthResponse;
import com.voronkov.authserverforchat.dto.RegistrationRequest;
import com.voronkov.authserverforchat.model.Person;
import com.voronkov.authserverforchat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/public")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public Person registerNewPerson(@RequestBody RegistrationRequest request) {
        return authService.registerNewUser(request);
    }

    @PostMapping("/auth")
    public AuthResponse getAuthTokens(@RequestBody AuthRequest request) {
        return authService.auth(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshTokens(@RequestBody AuthRefreshRequest request) {
        return authService.refresh(request);
    }

    @GetMapping("/user/all")
    public List<Person> getAllPersons() {
        return authService.getAllPersons();
    }
}
