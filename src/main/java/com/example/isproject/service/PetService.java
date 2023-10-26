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

    public Mono<Pet> createPet(Pet pet) {
        return petRepo.save(pet);
    }

    public Mono<Void> deletePet(Long id) {
        return petRepo.deleteById(id);
    }

    public Mono<Pet> updatePet(Long id, Pet pet) {
        return petRepo.findById(id)
                .flatMap(existingPet -> {
                    existingPet.setName(pet.getName());
                    existingPet.setBirthdate(pet.getBirthdate());
                    existingPet.setOwner_id(pet.getOwner_id());
                    return petRepo.save(existingPet);
                });
    }
}
