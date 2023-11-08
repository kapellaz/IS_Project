package com.example.client.entidades;

public class PetStatistics {
    public double total;
    public int count;
    public double sumOfSquares;

    public PetStatistics(double total, int count, double sumOfSquares) {
        this.total = total;
        this.count = count;
        this.sumOfSquares = sumOfSquares;
    }

    public double averageWeight() {
        return total / count;
    }

    public double standardDeviation() {
        return Math.sqrt(sumOfSquares / count);
    }

    public String result() {
        return "Média de pesos: " + averageWeight() + ", Desvio Padrão: " + standardDeviation();
    }
}
