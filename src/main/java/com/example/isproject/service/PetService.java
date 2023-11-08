package com.example.isproject.service;

import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.isproject.entity.Pet;
import com.example.isproject.repository.PetRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;


@Service
@Builder
public class PetService {
    @Autowired
    private final PetRepo petRepo;

    static final Logger logger = LoggerFactory.getLogger(PetService.class);

    public Flux<Pet> getAllPets() {
        return petRepo.findAll();
    }

    public Mono<Pet> getPetById(Long id) {
        return petRepo.findById(id);
    }


    // PROBLEMA NO BIRTHDATE
    public Mono<Pet> createPet(Pet pet) {

        AtomicReference<Boolean> Error = new AtomicReference<>(false);
        return petRepo.save(pet).onErrorResume(throwable -> {
                    Error.set(true);

                    return Mono.empty();
                })
                .doOnTerminate(() -> {
                    if (Error.get()) {
                        logger.error("Pet with id " + pet.getId() + " cannot be created!");
                    } else {
                        logger.info("Pet with id " + pet.getId() + " created!");
                    }
                });


    }

    public Mono<Void> deletePet(Long id) {
        AtomicReference<Boolean> Error = new AtomicReference<>(false);
        return petRepo.deleteById(id)
                .onErrorResume(throwable -> {
                    Error.set(true);

                    return Mono.empty();
                })
                .doOnTerminate(() -> {
                    if (Error.get()) {
                        logger.error("Pet with id " + id + " cannot be deleted!");
                    } else {
                        logger.info("Pet with id " + id + " deleted!");
                    }
                });
    }

    // PROBLEMA NO BIRTHDATE
    public Mono<Pet> updatePet(Long id, Pet pet) {
        AtomicReference<Boolean> Error = new AtomicReference<>(false);
        return petRepo.findById(id)
                .flatMap(existingPet -> {
                    existingPet.setName(pet.getName());
                    existingPet.setBirthdate(pet.getBirthdate());
                    existingPet.setOwner_id(pet.getOwner_id());
                    return petRepo.save(existingPet);
                }).onErrorResume(throwable -> {
                    Error.set(true);

                    return Mono.empty();
                })
                .doOnTerminate(() -> {
                    if (Error.get()) {
                        logger.error("Pet with id " + id + " cannot be updated!");
                    } else {
                        logger.info("Pet with id " + id + " updated!");
                    }
                });

    }
}
