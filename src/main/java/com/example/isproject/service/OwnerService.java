package com.example.isproject.service;

import com.example.isproject.entity.Owner;
import com.example.isproject.entity.Pet;
import com.example.isproject.repository.OwnerRepo;
import com.example.isproject.repository.PetRepo;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Builder

public class OwnerService {
    @Autowired
    private final OwnerRepo ownerRepo;

    private final PetRepo petRepo;

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
        return petRepo.findAll()
                .filter(pet -> pet.getOwner_id().equals(id))
                .count()
                .flatMap(petCount -> {
                    if (petCount == 0) {
                        // Não há pets associados, exclua o proprietário
                        return ownerRepo.findById(id)
                                .flatMap(existingOwner -> ownerRepo.deleteById(id));

                    } else {
                        // Existem pets associados, retorne um ResponseEntity de erro
                        return Mono.just(ResponseEntity.badRequest().body("O proprietário possui pets associados e não pode ser excluído."));
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .then();



       
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
