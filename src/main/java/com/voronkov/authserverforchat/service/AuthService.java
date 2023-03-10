package com.voronkov.authserverforchat.service;

import com.voronkov.authserverforchat.dto.AuthRefreshRequest;
import com.voronkov.authserverforchat.dto.AuthRequest;
import com.voronkov.authserverforchat.dto.AuthResponse;
import com.voronkov.authserverforchat.dto.RegistrationRequest;
import com.voronkov.authserverforchat.model.Person;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AuthService {

    Person registerNewUser(RegistrationRequest request);

    AuthResponse auth(AuthRequest request);

    AuthResponse refresh(AuthRefreshRequest request);

    List<Person> getAllPersons();

    Person validateToken(String token);
}
