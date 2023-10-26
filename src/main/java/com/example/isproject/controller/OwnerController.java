package com.example.isproject.controller;


import com.example.isproject.entity.Owner;
import com.example.isproject.service.OwnerService;
import lombok.Builder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/owner")
@Builder
public class OwnerController {

    static final Logger logger = LoggerFactory.getLogger(OwnerController.class);
    private final OwnerService owner_service;
    @GetMapping("/getAllOwners")
    public Flux<Owner> getAllOwners(){
        logger.info("Getting all owners");
        return owner_service.getAllOwners();
    }

    @GetMapping("/getOwner/{id}")
    public Mono<Owner> getSpecificOwner(@PathVariable Integer id){
        logger.info("Getting owner with id: " + id);
        return owner_service.getOwnerById(id);
    }

    @PostMapping("/createOwner")
    public Mono<Owner> saveEmployee(@RequestBody Owner owner){
        logger.info("Creating owner");
        return owner_service.createOwner(owner);
    }

    @DeleteMapping("/deleteOwner/{id}")
    public Mono<Void> deleteOwner(@PathVariable Integer id){
        logger.info("Deleting owner with id: " + id);
        return owner_service.deleteOwner(id);
    }

}
