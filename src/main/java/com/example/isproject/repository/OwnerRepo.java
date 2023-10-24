package com.example.isproject.repository;

import com.example.isproject.entity.Owner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepo extends ReactiveCrudRepository<Owner,Integer> {
}
