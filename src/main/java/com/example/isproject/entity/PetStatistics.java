package com.example.isproject.entity;

class PetStatistics {
    double total;
    int count;
    double sumOfSquares;

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
