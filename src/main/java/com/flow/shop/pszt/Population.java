package com.flow.shop.pszt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by krris on 02.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Population {

    private double mutationRate;          // Mutation rate
    private DNA[] population;             // Array to hold the current population
    private List<DNA> matingPool;         // ArrayList which we will use for our "mating pool"
    private int generations;              // Number of generations
    private boolean finished;             // Are we finished evolving?
    private int perfectScore;

    private Random random = new Random();

    public Population( double mutationRate, int populationMax, ArrayList<Task> tasks) {
        this.mutationRate = mutationRate;
        this.population = new DNA[populationMax];
        for (int i = 0; i < population.length; i++) {
            population[i] = new DNA(tasks);
        }
        calcFitness();
        this.matingPool = new ArrayList<>();
        this.finished = false;
        this.generations = 0;

        this.perfectScore = 1;
    }

    // Fill our fitness array with a value for every member of the population
    void calcFitness() {
        for (int i = 0; i < population.length; i++) {
            population[i].calculateFitness();
        }
    }

    // Generate a mating pool
    public void naturalSelection() {
        // Clear the ArrayList
        matingPool.clear();

        double totalFitness = 0;
        for (int i = 0; i < population.length; i++) {
            totalFitness += population[i].getFitness();
        }

        // Based on fitness, each member will get added to the mating pool a certain number of times
        // a lower fitness = more entries to mating pool = more likely to be picked as a parent
        // a higher fitness = fewer entries to mating pool = less likely to be picked as a parent
        for (int i = 0; i < population.length; i++) {

            // switch value of fitness: lower value = more likely to be picked
            double fitness = totalFitness - population[i].getFitness();

            // Normalization: brings all values into the range [0,1]. Sum of all normalized fitness is 1.
            double normalizedFitness = fitness / totalFitness;
            int n = (int) (normalizedFitness * 100);         // Arbitrary multiplier, we can also use monte carlo method
            for (int j = 0; j < n; j++) {                    // and pick a random numbers
                matingPool.add(population[i]);
            }
        }
    }

    // Create a new generation
    public void generate() {
        // Refill the population with children from the mating pool
        for (int i = 0; i < population.length; i++) {
            int a = random.nextInt(matingPool.size());
            int b = random.nextInt(matingPool.size());
            DNA partnerA = matingPool.get(a);
            DNA partnerB = matingPool.get(b);
            DNA child = partnerA.crossover(partnerB);
            child.mutate(mutationRate);
            population[i] = child;
        }
        generations++;
    }

    // Compute the current "most fit" member of the population
    public String getBest() {
        double worldrecord = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].getFitness() < worldrecord) {
                index = i;
                worldrecord = population[i].getFitness();
            }
        }

        if (worldrecord == perfectScore ) finished = true;
        return population[index].getOrder();
    }

    public boolean finished() {
        return finished;
    }

    public int getGenerations() {
        return generations;
    }

    // Compute average fitness for the population
    public float getAverageFitness() {
        float total = 0;
        for (int i = 0; i < population.length; i++) {
            total += population[i].getFitness();
        }
        return total / (population.length);
    }
}
