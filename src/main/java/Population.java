import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by krris on 02.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Population {

    private double mutationRate;           // Mutation rate
    private DNA[] population;             // Array to hold the current population
    private List<DNA> matingPool;         // ArrayList which we will use for our "mating pool"
    private String target;                // Target phrase
    private int generations;              // Number of generations
    private boolean finished;             // Are we finished evolving?
    private int perfectScore;

    private Random random = new Random();

    public Population(String p, double m, int num) {
        target = p;
        mutationRate = m;
        population = new DNA[num];
        for (int i = 0; i < population.length; i++) {
            population[i] = new DNA(target.length());
        }
        calcFitness();
        matingPool = new ArrayList<DNA>();
        finished = false;
        generations = 0;

        perfectScore = 1;
    }

    // Fill our fitness array with a value for every member of the population
    void calcFitness() {
        for (int i = 0; i < population.length; i++) {
            population[i].calculateFitness(target);
        }
    }

    // Generate a mating pool
    void naturalSelection() {
        // Clear the ArrayList
        matingPool.clear();

        float maxFitness = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].getFitness() > maxFitness) {
                maxFitness = population[i].getFitness();
            }
        }

        float minFitness = Float.MAX_VALUE;
        for (int i = 0; i < population.length; i++) {
            if (population[i].getFitness() < minFitness) {
                minFitness = population[i].getFitness();
            }
        }

        // Based on fitness, each member will get added to the mating pool a certain number of times
        // a higher fitness = more entries to mating pool = more likely to be picked as a parent
        // a lower fitness = fewer entries to mating pool = less likely to be picked as a parent
        for (int i = 0; i < population.length; i++) {

            // Normalization: brings all values into the range [0,1]
            float fitness = (population[i].getFitness() - minFitness) / (maxFitness - minFitness);
            int n = (int) (fitness * 100);  // Arbitrary multiplier, we can also use monte carlo method
            for (int j = 0; j < n; j++) {              // and pick two random numbers
                matingPool.add(population[i]);
            }
        }
    }

    // Create a new generation
    void generate() {
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
    String getBest() {
        float worldrecord = 0;
        int index = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].getFitness() > worldrecord) {
                index = i;
                worldrecord = population[i].getFitness();
            }
        }

        if (worldrecord == perfectScore ) finished = true;
        return population[index].getPhrase();
    }

    boolean finished() {
        return finished;
    }

    int getGenerations() {
        return generations;
    }

    // Compute average fitness for the population
    float getAverageFitness() {
        float total = 0;
        for (int i = 0; i < population.length; i++) {
            total += population[i].getFitness();
        }
        return total / (population.length);
    }
}
