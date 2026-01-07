package io.zipcoder.crudapp.controller;

import io.zipcoder.crudapp.model.Person;
import io.zipcoder.crudapp.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    // POST /people  → 201 Created
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person saved = repository.save(person);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // GET /people → 200 OK
    @GetMapping
    public Iterable<Person> getAllPeople() {
        return repository.findAll();
    }

    // GET /people/{id} → 200 OK or 404
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Integer id) {
        Optional<Person> person = repository.findById(id);
        return person
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /people/{id} → 200 OK or 201 Created
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(
            @PathVariable Integer id,
            @RequestBody Person person) {

        boolean exists = repository.existsById(id);
        person.setId(id);
        Person saved = repository.save(person);

        if (exists) {
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }
    }

    // DELETE /people/{id} → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Integer id) {
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
