package com.app.pruebaApi.service.imp;

//import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
//import java.util.Map;

import com.app.pruebaApi.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

public interface IPersonService {
    
    public List<Person> findAllPersons();
    public Person savePerson(Person person);
    public Person findPersonByDni(BigInteger dni);

    //public Person updatedPerson(Person person);
    public Person updatedPersonByDni(BigInteger dni, Person person);

    // ************************
    // patch forma-1
    //public Person updatePersonPartial(BigInteger dni, Map<String, Object> updates);
    
    // patch forma-2 uso de dependencia jsonPatch
    //public Person updatePersonPartial(BigInteger dni, JsonPatch patch) throws JsonPatchException, IOException;
    
    // patch forma-3 uso de dependencia jsonPatch - implementacion de Baeldung
    public Person updatePersonPartial(JsonPatch patch, Person targetPerson) throws JsonPatchException, JsonProcessingException;
    // ************************

    public void deletePersonByDni(BigInteger dni);

}
