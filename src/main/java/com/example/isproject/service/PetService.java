package com.example.isproject.service;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.isproject.entity.Pet;
import com.example.isproject.repository.PetRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@Builder
public class PetService {
    @Autowired
    private final PetRepo petRepo;

    public Flux<Pet> getAllPets() {
        return petRepo.findAll();
    }

    public Mono<Pet> getPetById(Long id){
        return petRepo.findById(id);
    }

}
