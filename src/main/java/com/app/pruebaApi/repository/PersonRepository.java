package com.app.pruebaApi.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.pruebaApi.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, BigInteger> {
    
    // metodos personalizados

}
