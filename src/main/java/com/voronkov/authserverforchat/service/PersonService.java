package com.voronkov.authserverforchat.service;

import com.voronkov.authserverforchat.model.Person;

import java.util.List;

public interface PersonService {

    Person savePerson(Person person);

    List<Person> getAllPersons();

    Person findByLogin(String login);

    Person findById(Long id);

    void delete(Long id);

    Person findByLoginAndPassword(String login, String password);
}
