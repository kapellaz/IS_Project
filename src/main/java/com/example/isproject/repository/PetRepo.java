package com.example.isproject.repository;

import com.example.isproject.entity.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepo extends ReactiveCrudRepository<Pet,Integer>{
}
