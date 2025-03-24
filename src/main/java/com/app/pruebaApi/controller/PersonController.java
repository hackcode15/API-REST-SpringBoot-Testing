package com.app.pruebaApi.controller;

//import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
//import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.pruebaApi.exception.PersonNotFoundException;
import com.app.pruebaApi.model.ApiResponse;
import com.app.pruebaApi.model.Person;
import com.app.pruebaApi.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    
    @Autowired
    private PersonService service;

    @GetMapping
    @ResponseBody
    public ResponseEntity<ApiResponse<List<Person>>> getAllPersons() {

        List<Person> persons = service.findAllPersons();

        /* ApiResponse<List<Person>> response = new ApiResponse<>(
            "success, status: " + HttpStatus.OK.value(),
            "Persons retrived successfully",
            persons
        ); */

        ApiResponse<List<Person>> response = new ApiResponse<>(
            "Success",
            "Persons retrived successfully",
            persons
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    
    }

    @GetMapping("/{dni}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> getPersonaByDni(@PathVariable("dni") BigInteger dni) {
        
        Person person = service.findPersonByDni(dni);

        ApiResponse<Person> response = new ApiResponse<>(
            "Success",
            "Person successfully recovered",
            person
        );

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> saveNewPerson(@RequestBody Person person) {
    
        Person newPerson = service.savePerson(person);

        ApiResponse<Person> response = new ApiResponse<>(
            "Success",
            "Create person successfully",
            newPerson
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
        
    }

    /* @PutMapping("/edit")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> editPerson(@RequestBody Person person) {
        
        Person updatePerson = service.updatedPerson(person);

        ApiResponse<Person> response = new ApiResponse<>(
            "Success",
            "Update person successfully",
            updatePerson
        );

        return new ResponseEntity<>(response, HttpStatus.OK);

    } */

    @PutMapping("/edit/{dni}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> updatePersonByDni(@PathVariable("dni") BigInteger dni, @RequestBody Person personDetails) {
    
        Person updatePerson = service.updatedPersonByDni(dni, personDetails);

        ApiResponse<Person> response = new ApiResponse<>(
            "Success",
            "Updated person successfully",
            updatePerson
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    
    }

    // forma sin dependencias
    /* @PatchMapping("/edit/{dni}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> editPersonPartial(@PathVariable("dni") BigInteger dni, @RequestBody Map<String, Object> updates) {
        
        Person updatedPerson = service.updatePersonPartial(dni, updates);

        ApiResponse<Person> response = new ApiResponse<>(
            "Success",
            "Updated person successfully",
            updatedPerson
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    
    } */

    // usando la dependencia jsonPatch con estándar JSON Patch (RFC 6902)
    // patch forma-2
    /*@PatchMapping("/edit/{dni}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> editPersonPartial(@PathVariable("dni") BigInteger dni, @RequestBody JsonPatch patch) {
        
        try {
            
            Person updatePerson = service.updatePersonPartial(dni, patch);

            ApiResponse<Person> response = new ApiResponse<>(
                "Success",
                "Updated person successfully",
                updatePerson
            );

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (JsonPatchException | IOException e) {
            
            ApiResponse<Person> errorResponse = new ApiResponse<>(
                "Error",
                "Failed to apply patch: " + e.getMessage(),
                null
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

    }*/

    // usando la dependencia jsonPatch con estándar JSON Patch (RFC 6902)
    // patch forma-3 - implementacion de Baeldung
    // funciona agregando en el postman el content-type application/json-patch+json
    // mejor practica
    @PatchMapping(path = "/edit/{dni}", consumes = "application/json-patch+json")
    @ResponseBody
    public ResponseEntity<ApiResponse<Person>> editPersonPartial(@PathVariable("dni") BigInteger dni, @RequestBody JsonPatch patch) {
    
        try {

            // Buscar la persona por su DNI
            Person existingPerson = service.findPersonByDni(dni);

            // Aplicar el parche a la persona
            Person patchedPerson = service.updatePersonPartial(patch, existingPerson);

            // Guardar la persona actualizada
            Person updatedPerson = service.savePerson(patchedPerson);

            // Crear la respuesta exitosa
            ApiResponse<Person> response = new ApiResponse<>(
                "Success",
                "Updated person successfully",
                updatedPerson
            );

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (JsonPatchException | JsonProcessingException e) {
            
            // Manejar errores relacionados con el parche
            ApiResponse<Person> errorResponse = new ApiResponse<>(
                "Error",
                "Failed to apply patch: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        } catch (PersonNotFoundException e) {
        
            // Manejar errores si la persona no existe
            ApiResponse<Person> errorResponse = new ApiResponse<>(
                "Error",
                "Person not found: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            // Manejar cualquier otro error inesperado
            ApiResponse<Person> errorResponse = new ApiResponse<>(
                "Error",
                "An unexpected error occurred: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
    }

    @DeleteMapping("/delete/{dni}")
    public ResponseEntity<ApiResponse<String>> deletePerson(@PathVariable("dni") BigInteger dni) {
        
        service.deletePersonByDni(dni);

        ApiResponse<String> response = new ApiResponse<>(
            "Succes",
            "Deleted person seccessfully",
            null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
