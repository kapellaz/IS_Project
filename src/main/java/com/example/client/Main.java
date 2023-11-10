package com.example.client;

import com.example.client.entidades.Owner;
import com.example.client.entidades.Pet;
import com.example.client.entidades.PetStatistics;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

public class Main {
    private static String URL = "http://localhost:8080";
    static WebClient.Builder webClientBuilder = WebClient.builder();
    static WebClient webClient = webClientBuilder.build();


    static Scheduler s = Schedulers.newParallel("parallel-scheduler", 9);
    static CountDownLatch latch = new CountDownLatch(9);

    public static int contador = 0;
    public static void main(String[] args) throws InterruptedException {
        LimpezaFicheiros();
        getOwnersNameAndPhoneRetry().subscribe(s-> {enviaFile(s, 0);});

        sleep(5000); // para dar tempo a que o retry acabe
        getOwnersNameAndPhone().doOnComplete(latch::countDown).subscribeOn(s).subscribe(s-> enviaFile(s, 1));
        getTotalNumberOfPets().subscribeOn(s).subscribe(s-> {enviaFile(s, 2);latch.countDown();});
        getTotalNumberOfDogs().subscribeOn(s).subscribe(s-> {enviaFile(s, 3);latch.countDown();});
        getAnimalsWithWeightGreaterThan(10).subscribeOn(s).doOnComplete(latch::countDown).subscribe(s-> enviaFile(s, 4));
        getAverageStandardDeviationsOfAnimalWeights().subscribeOn(s).subscribe(s-> {enviaFile(s, 5);latch.countDown();});
        getNameOfEldestPet().subscribeOn(s).subscribe(s-> {enviaFile(s, 6);latch.countDown();});
        calculateAverageOfPetsWithOwnersHavingMoreThanOnePet().subscribeOn(s).subscribe(s-> {enviaFile(s, 7);latch.countDown();});
        NameOfOwnerandNumberOfRespectivePets().subscribeOn(s).doOnComplete(latch::countDown).subscribe(s-> enviaFile(s,8 ));
        NameOfOwnerandNameOfRespectivePets().subscribeOn(s).doOnComplete(latch::countDown).subscribe(s-> enviaFile(s,9));

        latch.await();
        s.dispose();

    }

    public static Flux<String> getOwnersNameAndPhoneRetry() {
        return webClient.get()
                .uri(URL + "/owner/getAllOwnersRetry")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Owner.class)
                .retry(3)
                .map(o -> o.getName() + " " + o.getTelephone_number())
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
    }

    //ex1
    public static Flux<String> getOwnersNameAndPhone() {
        return webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Owner.class)
                .retry(3)
                .map(o -> o.getName() + " " + o.getTelephone_number())
                .doOnNext(p -> System.out.println("getOwnersNameAndPhone " + Thread.currentThread().getName()))
                .log()
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
    }
    //ex2




    public static Mono<Long> getTotalNumberOfPets() {
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .count()
                .doOnNext(p -> System.out.println("getTotalNumberOfPets " + Thread.currentThread().getName()))
                .log()
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
    }


    //ex3

    public static Mono<Long> getTotalNumberOfDogs(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .filter(p -> p.getSpecies().equals("Dog"))
                .count()
                .doOnNext(p -> System.out.println("getTotalNumberOfDogs " + Thread.currentThread().getName()))
                .log()
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
    }


    //ex4
    public static Flux<Pet> getAnimalsWithWeightGreaterThan(Integer weight){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .filter(p -> p.getWeight() > weight)
                .sort((p1, p2) -> p1.getWeight() - p2.getWeight())
                .doOnNext(p -> System.out.println("getAnimalsWithWeightGreaterThan " + Thread.currentThread().getName()))
                .log()
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
    }

    //ex5

    public static Mono<String> getAverageStandardDeviationsOfAnimalWeights() {
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .collect(() -> new PetStatistics(0, 0, 0), (petStatistics, pet) -> {
                    double weight = pet.getWeight();
                    petStatistics.total += weight;
                    petStatistics.count++;
                    petStatistics.sumOfSquares += Math.pow(weight - petStatistics.average(), 2);
                })
                .map(PetStatistics::result)
                .doOnNext(s -> System.out.println("getAverageStandardDeviationsOfAnimalWeights " + Thread.currentThread().getName()))
                .log()
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});

    }



    //ex6
    public static Mono<String> getNameOfEldestPet(){
        return webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .sort((p1, p2) -> p2.getBirthdate().compareTo(p1.getBirthdate()))
                .last()
                .map(Pet::getName)
                .doOnNext(p -> System.out.println("getNameOfEldestPet " + Thread.currentThread().getName()))
                .log()
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
    }



    //ex7

    public static Mono<Double> calculateAverageOfPetsWithOwnersHavingMoreThanOnePet() {
        Flux<Long> pets = webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();})
                .groupBy(Pet::getOwner_id)
                .flatMap(ownerGroup -> ownerGroup
                        .count()
                        .filter(count -> count > 1)
                );

        Mono<PetStatistics> statistics = pets
                .reduce(new PetStatistics(0.0, 0, 0.0), (accumulator, count) -> {
                    double newTotal = accumulator.total + count;
                    int newCount = accumulator.count + 1;
                    return new PetStatistics(newTotal, newCount, 0.0);
                });
        System.out.println("calculateAverageOfPetsWithOwnersHavingMoreThanOnePet " + Thread.currentThread().getName());


        return statistics.map(PetStatistics::average);



    }


    //ex8

    public static Flux<String> NameOfOwnerandNumberOfRespectivePets() {
        //todos os pets
        Flux<Pet> pets = webClient.get()
                .uri(URL + "/pet/getAllPets")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
        //todos os owners
        Flux<Owner> owners = webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Owner.class)
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});


        Flux<Tuple2<String, Long>> ownerAndPetIdsFlux = owners
                .flatMap(owner -> {
                    Flux<Long> petIds = pets
                            .filter(pet -> Objects.equals(pet.getOwner_id(), owner.getId()))
                            .map(Pet::getId)
                            .sort((id1, id2) -> id2.compareTo(id1)); // Ordenar em ordem decrescente
                    return petIds
                            .map(petIdList -> Tuples.of(owner.getName(), petIdList));
                })
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});
        System.out.println("NameOfOwnerandNumberOfRespectivePets " + Thread.currentThread().getName());
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
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Pet.class)
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});

        Flux<Owner> owners = webClient.get()
                .uri(URL + "/owner/getAllOwners")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToFlux(Owner.class)
                .doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});

        Flux<Tuple2<String, String>> ownerAndPetNameFlux = owners.flatMap(owner ->
                pets.filter(pet -> Objects.equals(pet.getOwner_id(), owner.getId()))
                        .map(Pet::getName)
                        .map(petName -> Tuples.of(owner.getName(), petName))
                        .sort((id1, id2) -> id2.getT2().compareTo(id1.getT2()))
        ).doOnError(e -> {System.out.println("Erro: " + e.getMessage()); latch.countDown();});

        System.out.println("NameOfOwnerandNameOfRespectivePets " + Thread.currentThread().getName());
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
        for (int i = 0; i<=9; i++)
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


