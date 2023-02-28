package com.voronkov.authserverforchat.service.impl;

import com.voronkov.authserverforchat.dto.AuthRefreshRequest;
import com.voronkov.authserverforchat.dto.AuthRequest;
import com.voronkov.authserverforchat.dto.AuthResponse;
import com.voronkov.authserverforchat.dto.RegistrationRequest;
import com.voronkov.authserverforchat.model.Person;
import com.voronkov.authserverforchat.model.RefreshToken;
import com.voronkov.authserverforchat.repository.RefreshTokenRepository;
import com.voronkov.authserverforchat.security.JwtProvider;
import com.voronkov.authserverforchat.service.AuthService;
import com.voronkov.authserverforchat.service.PersonService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonService personService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Person registerNewUser(RegistrationRequest request) {
        if (personService.findByLoginAndPassword(request.getLogin(), request.getPassword()) == null) {
            Person person = new Person();
            person.setLogin(request.getLogin());
            person.setPassword(request.getPassword());

            return personService.savePerson(person);
        } else {
            throw new RuntimeException("Person with login " + request.getLogin() + "already exists");
        }
    }

    @Override
    public AuthResponse auth(AuthRequest request) {
        Person person = personService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String accessToken = jwtProvider.generateAccessToken(person.getLogin());
        String refreshToken = jwtProvider.generateRefreshToken(person.getLogin());
        refreshTokenRepository.save(new RefreshToken(request.getLogin(), refreshToken));
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(AuthRefreshRequest request) {
        if(jwtProvider.validateRefreshToken(request.getToken())) {
            String login = jwtProvider.getLoginFromRefreshToken(request.getToken());
            RefreshToken optionalRefreshToken = refreshTokenRepository.findByToken(request.getToken()).orElseThrow();
            String saveRefreshToken = optionalRefreshToken.getToken();
            if(saveRefreshToken != null && saveRefreshToken.equals(request.getToken())) {
                Person person = personService.findByLogin(login);
                String accessToken = jwtProvider.generateAccessToken(login);
                String refreshToken = jwtProvider.generateRefreshToken(login);
                refreshTokenRepository.deleteByLogin(login);
                refreshTokenRepository.save(new RefreshToken(person.getLogin(),refreshToken));
                return new AuthResponse(accessToken,refreshToken);
            }
        }
        throw new JwtException("Invalid token");
    }

    @Override
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }
}
