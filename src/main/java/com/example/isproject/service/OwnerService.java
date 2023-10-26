package com.example.isproject.service;

import com.example.isproject.entity.Owner;
import com.example.isproject.repository.OwnerRepo;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Builder

public class OwnerService {
    @Autowired
    private final OwnerRepo ownerRepo;

    public Flux<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }

    public Mono<Owner> getOwnerById(Integer id){
        return ownerRepo.findById(id);
    }

    public Mono<Owner> createOwner(Owner owner) {
        return ownerRepo.save(owner);
    }

    public Mono<Void> deleteOwner(Integer id) {
        return ownerRepo.deleteById(id);
    }

    public Mono<Owner> updateOwner(Integer id, Owner owner) {
        return ownerRepo.findById(id)
                .flatMap(existingOwner -> {
                    existingOwner.setName(owner.getName());
                    existingOwner.setTelephone_number(owner.getTelephone_number());
                    return ownerRepo.save(existingOwner);
                });
    }
}
