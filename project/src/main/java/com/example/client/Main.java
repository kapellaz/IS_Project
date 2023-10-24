package com.example.client;

import com.example.client.entidades.Owner;
import com.example.client.entidades.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class Main {
    private static String URL = "http://localhost:8080";
    static WebClient.Builder webClientBuilder = WebClient.builder();
    static WebClient webClient = webClientBuilder.build();


    public static void main(String[] args) throws InterruptedException {
        getOwnersNameAndPhone().subscribe(System.out::println);
        getTotalNumberOfPets().subscribe(System.out::println);
        getTotalNumberOfDogs().subscribe(System.out::println);
        getAnimalsWithWeightGreaterThan(10).subscribe(System.out::println);
        //getNameOfEldestPet().subscribe(System.out::println);
        sleep(2000);
    }


    //ex1
    public static Flux<String> getOwnersNameAndPhone() {
        return webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve()
                .bodyToFlux(Owner.class)
                .retry(3)
                .map(o -> o.getName() + " " + o.getTelephone_number());
    }

    //ex2

    public static Mono<Long> getTotalNumberOfPets() {
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .count();
    }


    //ex3

    public static Mono<Long> getTotalNumberOfDogs(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .filter(p -> p.getSpecies().equals("Dog"))
                .count();
    }


    //ex4

    public static Flux<Pet> getAnimalsWithWeightGreaterThan(Integer weight){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .filter(p -> p.getWeight() > weight)
                .sort((p1, p2) -> p1.getWeight() - p2.getWeight());
    }

    //ex5



    //ex6
    public static Mono<String> getNameOfEldestPet(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .sort((p1, p2) -> p1.getBirthday().compareTo(p2.getBirthday()))
                .last()
                .map(Pet::getName);
    }

    //ex7

}