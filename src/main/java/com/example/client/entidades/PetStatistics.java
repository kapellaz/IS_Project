package com.example.client.entidades;

public class PetStatistics {
    public double totalWeight;
    public int count;
    public double sumOfSquares;

    public PetStatistics(double totalWeight, int count, double sumOfSquares) {
        this.totalWeight = totalWeight;
        this.count = count;
        this.sumOfSquares = sumOfSquares;
    }

    public double averageWeight() {
        return totalWeight / count;
    }

    public double standardDeviation() {
        return Math.sqrt(sumOfSquares / count);
    }

    public String result() {
        return "Média de pesos: " + averageWeight() + ", Desvio Padrão: " + standardDeviation();
    }
}