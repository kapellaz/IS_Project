package com.example.isproject.service;

import com.example.isproject.entity.Owner;
import com.example.isproject.repository.OwnerRepo;
import com.example.isproject.repository.PetRepo;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Service
@Builder

public class OwnerService {
    @Autowired
    private final OwnerRepo ownerRepo;

    private final PetRepo petRepo;

    static final Logger logger = LoggerFactory.getLogger(OwnerService.class);

    public Flux<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }

    public Mono<Owner> getOwnerById(Integer id){
        return ownerRepo.findById(id);
    }

    public Mono<Owner> createOwner(Owner owner) {
        AtomicReference<Boolean> Error = new AtomicReference<>(false);

        return ownerRepo.save(owner).onErrorResume(throwable -> {
                    Error.set(true);

                    return Mono.empty();
                })
                .doOnTerminate(() -> {
                    if (Error.get()) {
                        logger.error("Owner with id " + owner.getId() +  " cannot be created!");
                    } else {
                        logger.info("Owner with id " + owner.getId() +  " created!");
                    }
                });
    }

    public Mono<Void> deleteOwner(Integer id) {
        AtomicReference<Boolean> Error = new AtomicReference<>(false);
        return ownerRepo.deleteById(id)
                .onErrorResume(throwable -> {
                    Error.set(true);

                    return Mono.empty();
                })
                .doOnTerminate(() -> {
                    if (Error.get()) {
                        logger.error("Owner with id " + id +  " cannot be deleted!");
                    } else {
                        logger.info("Owner with id " + id + " deleted! (or not exists)");
                    }
                });
       
    }

    public Mono<Owner> updateOwner(Integer id, Owner owner) {
        AtomicReference<Boolean> Error = new AtomicReference<>(false);
        return ownerRepo.findById(id)
                .flatMap(existingOwner -> {
                    existingOwner.setName(owner.getName());
                    existingOwner.setTelephone_number(owner.getTelephone_number());
                    return ownerRepo.save(existingOwner);
                }) .onErrorResume(throwable -> {
                    Error.set(true);

                    return Mono.empty();
                })
                .doOnTerminate(() -> {
                    if (Error.get()) {
                        logger.error("Owner with id " + id +  " cannot be updated!");
                    } else {
                        logger.info("Owner with id " + id +  " updated! (or not exists)");
                    }
                });
    }
}
