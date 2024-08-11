package com.colorpl.global.redis.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.colorpl.global.redis.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RedisTemplate<String, Person> redisTemplate;

    private Person person;

    @BeforeEach
    void setUp() {
        person = Person.builder()
            .id("1")
            .firstname("John")
            .lastname("Doe")
            .build();
    }

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    void testSave() {
        Person savedPerson = personRepository.save(person);
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isEqualTo(person.getId());
    }

    @Test
    void testFindById() {
        personRepository.save(person);
        Person foundPerson = personRepository.findById(person.getId()).orElse(null);
        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getFirstname()).isEqualTo(person.getFirstname());
    }

    @Test
    void testFindAll() {
        personRepository.save(person);
        Iterable<Person> people = personRepository.findAll();
        assertThat(people).isNotEmpty();
    }

    @Test
    void testDeleteById() {
        personRepository.save(person);
        personRepository.deleteById(person.getId());
        Person deletedPerson = personRepository.findById(person.getId()).orElse(null);
        assertThat(deletedPerson).isNull();
    }

    @Test
    void testCount() {
        personRepository.save(person);
        long count = personRepository.count();
        assertThat(count).isEqualTo(1);
    }
}
