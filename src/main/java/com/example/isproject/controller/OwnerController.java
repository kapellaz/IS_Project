package com.example.isproject.controller;


import com.example.isproject.entity.Owner;
import com.example.isproject.service.OwnerService;
import lombok.Builder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/owner")
@Builder
public class OwnerController {

    private final OwnerService owner_service;
    @GetMapping("/getAllOwners")
    public Flux<Owner> getAllOwners(){
        return owner_service.getAllOwners();
    }

    @GetMapping("/getOwner/{id}")
    public Mono<Owner> getSpecificOwner(@PathVariable Integer id){
        return owner_service.getOwnerById(id);
    }


}
