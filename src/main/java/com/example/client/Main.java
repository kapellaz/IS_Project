package com.example.client;

import com.example.client.entidades.Owner;
import com.example.client.entidades.Pet;
import com.example.client.entidades.PetStatistics;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static java.lang.Thread.sleep;

public class Main {
    private static String URL = "http://localhost:8080";
    static WebClient.Builder webClientBuilder = WebClient.builder();
    static WebClient webClient = webClientBuilder.build();



    static CountDownLatch latch = new CountDownLatch(9);

    public static int contador = 0;
    public static void main(String[] args) throws InterruptedException {
        LimpezaFicheiros();

        getOwnersNameAndPhone().doOnComplete(latch::countDown).subscribe(s-> enviaFile(s, 1));
        getTotalNumberOfPets().subscribe(s-> {enviaFile(s, 2);latch.countDown();});
        getTotalNumberOfDogs().subscribe(s-> {enviaFile(s, 3);latch.countDown();});
        getAnimalsWithWeightGreaterThan(10).doOnComplete(latch::countDown).subscribe(s-> enviaFile(s, 4));
        getAverageStandardDeviationsOfAnimalWeights().subscribe(s-> {enviaFile(s, 5);latch.countDown();});
        getNameOfEldestPet().subscribe(s-> {enviaFile(s, 6);latch.countDown();});
        calculateAverageWeightOfPetsWithOwnersHavingMoreThanOnePet().doOnComplete(latch::countDown).subscribe(s-> enviaFile(s, 7));
        NameOfOwnerandNumberOfRespectivePets().doOnComplete(latch::countDown).subscribe(s-> enviaFile(s,8 ));
        NameOfOwnerandNameOfRespectivePets().doOnComplete(latch::countDown).subscribe(s-> enviaFile(s,9));


        latch.await();


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
                .log()
                .count();
    }


    //ex3

    public static Mono<Long> getTotalNumberOfDogs(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .filter(p -> p.getSpecies().equals("Dog"))
                .log()
                .count();
    }


    //ex4
    public static Flux<Pet> getAnimalsWithWeightGreaterThan(Integer weight){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .filter(p -> p.getWeight() > weight)

                .log()
                .sort((p1, p2) -> p1.getWeight() - p2.getWeight());
    }

    //ex5

    public static Mono<String> getAverageStandardDeviationsOfAnimalWeights() {
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)

                .collect(() -> new PetStatistics(0, 0, 0), (petStatistics, pet) -> {
                    double weight = pet.getWeight();
                    petStatistics.totalWeight += weight;
                    petStatistics.count++;
                    petStatistics.sumOfSquares += Math.pow(weight - petStatistics.averageWeight(), 2);
                })
                .map(PetStatistics::result)
                .log();
    }



    //ex6
    public static Mono<String> getNameOfEldestPet(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)

                .sort((p1, p2) -> p2.getBirthdate().compareTo(p1.getBirthdate()))
                .last()
                .log()
                .map(Pet::getName);
    }



    //ex7

    public static Flux<String> calculateAverageWeightOfPetsWithOwnersHavingMoreThanOnePet() {



        Flux<Pet> pets = webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve().bodyToFlux(Pet.class);

        // Agrupar os animais pelo ID do proprietário
       Flux<Tuple2<Integer, Long>> petCountsByOwner = pets
                .groupBy(Pet::getOwner_id)
                .flatMap(ownerGroup -> ownerGroup
                        .count()
                        .map(count -> Tuples.of(ownerGroup.key(), count))
                );


        // apenas owners com mais de um pet
        Flux<Tuple2<Integer, Long>> ownersWithMoreThanOnePet = petCountsByOwner
                .filter(tuple -> tuple.getT2() > 1);


        Flux<Tuple3<Integer, Double, Double>> statisticsByOwner = ownersWithMoreThanOnePet
                .flatMap(ownerTuple -> {
                    Integer ownerId = ownerTuple.getT1();
                    Flux<Integer> petWeights = pets
                            .filter(pet -> pet.getOwner_id().equals(ownerId))
                            .map(Pet::getWeight);

                    return petWeights
                            .reduce(
                                    new double[]{0.0, 0.0, 0},
                                    (stats, weight) -> {
                                        stats[0] += weight;
                                        stats[1] += weight * weight;
                                        stats[2]++;
                                        return stats;
                                    }
                            )
                            .map(stats -> {
                                double mean = stats[0] / stats[2];
                                double variance = (stats[1] / stats[2]) - (mean * mean);
                                double stdDev = Math.sqrt(variance);
                                return Tuples.of(ownerId, mean, stdDev);
                            });   });

        return statisticsByOwner.map(tuple -> {
            Integer ownerId = tuple.getT1();
            Double mean = tuple.getT2();
            Double standardDeviation = tuple.getT3();
            return "Proprietário ID: " + ownerId + ", Média de Peso: " + mean + ", Desvio Padrão de Peso: " + standardDeviation;
        });
    }


    //ex8

    public static Flux<String> NameOfOwnerandNumberOfRespectivePets() {
        //todos os pets
        Flux<Pet> pets = webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve().bodyToFlux(Pet.class);
        //todos os owners
        Flux<Owner> owners = webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve().bodyToFlux(Owner.class);


        Flux<Tuple2<String, Long>> ownerAndPetIdsFlux = owners
                .flatMap(owner -> {
                    Flux<Long> petIds = pets
                            .filter(pet -> Objects.equals(pet.getOwner_id(), owner.getId()))
                            .map(Pet::getId)
                            .sort((id1, id2) -> id2.compareTo(id1)); // Ordenar em ordem decrescente
                    return petIds
                            .map(petIdList -> Tuples.of(owner.getName(), petIdList));
                });

        //colocar tudo em Flux de strings
        return ownerAndPetIdsFlux.map(tuple -> {
            String nome = tuple.getT1();
            Long petIds = tuple.getT2();
            return "Proprietário " + nome + " ->  Lista de id's PETS " + petIds;
        });

    }


    //ex 9
    public static Flux<String> NameOfOwnerandNameOfRespectivePets() {
        Flux<Pet> pets = webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve().bodyToFlux(Pet.class);

        Flux<Owner> owners = webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve().bodyToFlux(Owner.class);

        Flux<Tuple2<String, String>> ownerAndPetNameFlux = owners.flatMap(owner ->
                pets.filter(pet -> Objects.equals(pet.getOwner_id(), owner.getId()))
                        .map(Pet::getName)
                        .map(petName -> Tuples.of(owner.getName(), petName))
                        .sort((id1, id2) -> id2.getT2().compareTo(id1.getT2()))
        );

        return ownerAndPetNameFlux.map(tuple -> {
            String ownerName = tuple.getT1();
            String petName = tuple.getT2();
            return "Proprietário " + ownerName + " -> Nome do PET: " + petName;
        });
    }


    //FUNCOES AUXILIARES PARA TRATAMENTO DE FICHEIROS

    public static void enviaFile(Object s, int i) {
        contador++;
        escritaFicheiro(s.toString(),  i);

    }
    public static void LimpezaFicheiros() {
        File f = new File("Results");
        if (!f.exists()){
            if(f.mkdirs()){
                System.out.println("Ficheiro criado!!");
            }
        }
        for (int i = 1; i<=9; i++)
        {
            try {
                String s = "Results/resp"+i+".txt";
                FileWriter myWriter = new FileWriter(s);
                myWriter.write("");
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }



    public static void escritaFicheiro(String info, int exerc) {
        try {

            String filename = "Results/resp"+exerc+".txt";
            FileWriter writer = new FileWriter(filename, true);
            synchronized(writer)
            {
                writer.write(info + "\n");
                writer.close();
                System.out.println("Escrita ao Ficheiro " + exerc );
            }
        } catch (IOException e) {
            System.out.println("ERRO");
            e.printStackTrace();
        }
    }


}


