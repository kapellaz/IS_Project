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

    public double average() {
        return total / count;
    }

    public double standardDeviation() {
        return Math.sqrt(sumOfSquares / count);
    }

    public String result() {
        return "Média: " + average() + ", Desvio Padrão: " + standardDeviation();
    }
}
