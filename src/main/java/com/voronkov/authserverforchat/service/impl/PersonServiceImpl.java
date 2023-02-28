package com.voronkov.authserverforchat.service.impl;

import com.voronkov.authserverforchat.model.Person;
import com.voronkov.authserverforchat.model.Role;
import com.voronkov.authserverforchat.repository.PersonRepository;
import com.voronkov.authserverforchat.repository.RoleRepository;
import com.voronkov.authserverforchat.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Person savePerson(Person person) {
        Role role = roleRepository.findByName("ROLE_USER");
        person.setRoles(List.of(role));
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Person findByLoginAndPassword(String login, String password) {
        Person person = personRepository.findByLogin(login);
        if (person != null) {
            if (passwordEncoder.matches(password, person.getPassword()))
                return person;
        }
        return null;
    }
}
