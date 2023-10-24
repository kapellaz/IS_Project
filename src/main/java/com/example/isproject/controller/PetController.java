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

@RestController
@RequestMapping("/pet")
@Builder
public class PetController {

    private final PetService petService;

    @GetMapping("/getAllPets")
    public Flux<Pet> getAllPets(){
        return petService.getAllPets();
    }

    @GetMapping("/getPet/{id}")
    public Mono<Pet> getSpecificPet(@PathVariable Integer id){
        return petService.getPetById(id);
    }


}

