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
}
