package com.voronkov.authserverforchat.repository;

import com.voronkov.authserverforchat.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByLogin(String login);
}
