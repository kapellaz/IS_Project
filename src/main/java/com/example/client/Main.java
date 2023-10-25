package com.example.client;

import com.example.client.entidades.Owner;
import com.example.client.entidades.Pet;
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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static java.lang.Thread.sleep;

public class Main {
    private static String URL = "http://localhost:8080";
    static WebClient.Builder webClientBuilder = WebClient.builder();
    static WebClient webClient = webClientBuilder.build();

    static Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

    public static void main(String[] args) throws InterruptedException {
        LimpezaFicheiros();
        getOwnersNameAndPhone().subscribe(s-> enviaFile(s, 1));
        getTotalNumberOfPets().subscribe(s-> enviaFile(s, 2));
        getTotalNumberOfDogs().subscribe(s-> enviaFile(s, 3));
        getAnimalsWithWeightGreaterThan(10).subscribe(s-> enviaFile(s, 4));
        getAverageStandardDeviationsOfAnimalWeights().subscribe(s-> enviaFile(s, 5));
        getNameOfEldestPet().subscribe(s-> enviaFile(s, 6));
        calculateAverageWeightOfPetsWithOwnersHavingMoreThanOnePet().subscribe(s-> enviaFile(s, 7));
        NameOfOwnerandNumberOfRespectivePets().subscribe(s-> enviaFile(s, 8));
        NameOfOwnerandNameOfRespectivePets().subscribe(s-> enviaFile(s,9));
        sleep(5000);
        s.dispose();
    }





    //ex1
    public static Flux<String> getOwnersNameAndPhone() {
        return webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve()
                .bodyToFlux(Owner.class)
                .retry(3)
                .subscribeOn(s)
                .map(o -> o.getName() + " " + o.getTelephone_number());
    }

    //ex2

    public static Mono<Long> getTotalNumberOfPets() {
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .subscribeOn(s)
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
                .subscribeOn(s)
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
                .subscribeOn(s)
                .log()
                .sort((p1, p2) -> p1.getWeight() - p2.getWeight());
    }

    //ex5

    public static Mono<String> getAverageStandardDeviationsOfAnimalWeights(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .bodyToFlux(Pet.class)
                .collectList()
                .flatMap(pets -> {

                    double totalWeight = 0;
                    int count = 0;

                    //soma total dos pesos
                    for (Pet pet : pets) {
                        totalWeight += pet.getWeight();
                        count++;
                    }

                    //media dos pesos
                    double averageWeight = totalWeight / count;

                    // Calcular o desvio padrão dos pesos
                    double sumOfSquares = 0;
                    for (Pet pet : pets) {
                        sumOfSquares += Math.pow(pet.getWeight() - averageWeight, 2);
                    }
                    double standardDeviation = Math.sqrt(sumOfSquares / count);

                    // resultado em string(penso que pode ser assim)
                    String result = "Média de pesos: " + averageWeight + ", Desvio Padrão: " + standardDeviation;

                    return Mono.just(result);
                })
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
                            .collect(DescriptiveStatistics::new, DescriptiveStatistics::addValue)
                            .map(statistics -> Tuples.of(ownerId, statistics.getMean(), statistics.getStandardDeviation()));
                });

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

        // ligação dos owners com os seus pets --> ATENÇÃO VER A QUESTÃO DO CollectList
        Flux<Tuple2<String, List<Long>>> ownerAndPetIdsFlux = owners
                .flatMap(owner -> {
                    Flux<Long> petIds = pets
                            .filter(pet -> Objects.equals(pet.getOwner_id(), owner.getId()))
                            .map(Pet::getId)
                            .sort((id1, id2) -> id2.compareTo(id1)); // Ordenar em ordem decrescente
                    return petIds
                            .collectList()
                            .map(petIdList -> Tuples.of(owner.getName(), petIdList));
                });

        //colocar tudo em Flux de strings
        return ownerAndPetIdsFlux.map(tuple -> {
            String nome = tuple.getT1();
            List<Long> petIds = tuple.getT2();
            return "Proprietário " + nome + " ->  Lista de id's PETS " + petIds;
        });

    }


    //ex 9
    public static Flux<String> NameOfOwnerandNameOfRespectivePets() {
        //todos os pets
        Flux<Pet> pets = webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve().bodyToFlux(Pet.class);
        //todos os owners
        Flux<Owner> owners = webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve().bodyToFlux(Owner.class);

        // ligação dos owners com os seus pets --> ATENÇÃO VER A QUESTÃO DO CollectList
        Flux<Tuple2<String, List<String>>> ownerAndPetIdsFlux = owners
                .flatMap(owner -> {
                    Flux<String> petIds = pets
                            .filter(pet -> Objects.equals(pet.getOwner_id(), owner.getId()))
                            .map(Pet::getName)
                            .sort((id1, id2) -> id2.compareTo(id1)); // Ordenar em ordem decrescente
                    return petIds
                            .collectList()
                            .map(petIdList -> Tuples.of(owner.getName(), petIdList));
                });

        //colocar tudo em Flux de strings
        return ownerAndPetIdsFlux.map(tuple -> {
            String nome = tuple.getT1();
            List<String> petIds = tuple.getT2();
            return "Proprietário " + nome + " ->  Lista de nomes PETS " + petIds;
        });

    }

    //FUNCOES AUXILIARES PARA TRATAMENTO DE FICHEIROS

    public static void enviaFile(Object s, int i) {
        escritaFicheiro(s.toString(),  i);

    }
    public static void LimpezaFicheiros() {
        File f = new File("Results");
        if (!f.exists()){
            if(f.mkdirs()){
                System.out.println("Ficheiro criado!!");
            }
        }
        for (int i = 1; i<9; i++)
        {
            try {
                String s = "Results/resp"+i+".txt";
                FileWriter myWriter = new FileWriter(s);
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


