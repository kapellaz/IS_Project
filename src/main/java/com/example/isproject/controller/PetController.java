package com.example.isproject.controller;


import lombok.Builder;
import com.example.isproject.entity.Pet;
import com.example.isproject.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/pet")
@Builder
public class PetController {

    private final PetService petService;
    static final Logger logger = LoggerFactory.getLogger(PetController.class);
    @GetMapping("/getAllPets")
    public Flux<Pet> getAllPets(){
        logger.info("Getting all pets");
        return petService.getAllPets();
    }

    @GetMapping("/getPet/{id}")
    public Mono<Pet> getSpecificPet(@PathVariable Long id){
        logger.info("Getting pet with id: " + id);
        return petService.getPetById(id);
    }


}

