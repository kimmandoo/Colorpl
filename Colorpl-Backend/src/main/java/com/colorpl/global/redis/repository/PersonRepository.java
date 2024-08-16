package com.colorpl.global.redis.repository;

import com.colorpl.global.redis.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {

}
