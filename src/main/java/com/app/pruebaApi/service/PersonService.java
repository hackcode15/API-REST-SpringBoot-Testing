package com.app.pruebaApi.service;

//import java.io.IOException;
import java.math.BigInteger;
//import java.time.LocalDate;
import java.util.List;
//import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.pruebaApi.exception.PersonNotFoundException;
import com.app.pruebaApi.model.Person;
import com.app.pruebaApi.repository.PersonRepository;
import com.app.pruebaApi.service.imp.IPersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Service
public class PersonService implements IPersonService {

    @Autowired
    private PersonRepository repository;

    @Override
    public List<Person> findAllPersons() {
        return repository.findAll();
    }

    @Override
    public Person savePerson(Person person) {
        return repository.save(person);
    }

    @Override
    public Person findPersonByDni(BigInteger dni) {
        return repository.findById(dni)
                .orElseThrow(() -> new PersonNotFoundException(dni));
    }

    /* @Override
    public Person updatedPerson(Person person) {

        Person existingPerson = this.findPersonByDni(person.getDni());

        return repository.save(existingPerson);

    } */

    @Override
    public Person updatedPersonByDni(BigInteger dni, Person personDetails) {
        
        Person person = this.findPersonByDni(dni);

        /* if (person == null){
            throw new PersonNotFoundException("Error la persona a actualizar no existe con dni: " + dni);
        } */

        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setBirthdate(personDetails.getBirthdate());
        person.setPhone(personDetails.getPhone());
        person.setAddress(personDetails.getAddress());

        return this.savePerson(person);

    }

    // patch forma-1
    /* @Override
    public Person updatePersonPartial(BigInteger dni, Map<String, Object> updates) {

        Person existingPerson = this.findPersonByDni(dni);

        updates.forEach((key, value) -> {

            switch (key) {
                case "firstName":
                    existingPerson.setFirstName((String) value);
                    break;
                case "lastName":
                    existingPerson.setLastName((String) value);
                    break;
                case "birthdate":
                    existingPerson.setBirthdate(LocalDate.parse((String) value));
                    break;
                case "phone":
                    existingPerson.setPhone((String) value);
                    break;
                case "address":
                    existingPerson.setAddress((String) value);
                    break;
                default:
                    break;
            }

        });

        return repository.save(existingPerson);

    } */

    // con el uso de la dependencia jsonPatch
    // patch forma-2
    /* @Override
    public Person updatePersonPartial(BigInteger dni, JsonPatch patch) throws JsonPatchException, IOException {
        // Obtener la persona existente
        Person existingPerson = this.findPersonByDni(dni);

        // Convertir la persona a un formato JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Para manejar LocalDate
        JsonNode personNode = objectMapper.valueToTree(existingPerson);

        // Aplicar el parche al JSON
        JsonNode patchedNode = patch.apply(personNode);

        // Convertir el JSON modificado de vuelta a un objeto Person
        Person patchedPerson = objectMapper.treeToValue(patchedNode, Person.class);

        // Guardar la persona actualizada en la base de datos
        return repository.save(patchedPerson);
    } */

    // con el uso de la dependencia jsonPatch
    // patch forma-3 - implementacion de Baeldung
    @Override
    public Person updatePersonPartial(JsonPatch patch, Person targetPerson) throws JsonPatchException, JsonProcessingException {
        
        // Convertir la persona a un JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Para manejar LocalDate
        JsonNode personNode = objectMapper.valueToTree(targetPerson);

        // Aplicar el parche al JsonNode
        JsonNode patchedNode = patch.apply(personNode);

        // Convertir el JsonNode modificado de vuelta a un objeto Person
        return objectMapper.treeToValue(patchedNode, Person.class);

    }

    @Override
    public void deletePersonByDni(BigInteger dni) {
        Person person = this.findPersonByDni(dni);
        repository.deleteById(person.getDni());
    }

}
