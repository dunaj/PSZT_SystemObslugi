/**
 * Created by krris on 02.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Main {
    // Genetic Algorithm, Evolving Shakespeare

    // Demonstration of using a genetic algorithm to perform a search

    // setup()
    //  # Step 1: The Population
    //    # Create an empty population (an array or ArrayList)
    //    # Fill it with DNA encoded objects (pick random values to start)

    // draw()
    //  # Step 1: Selection
    //    # Create an empty mating pool (an empty ArrayList)
    //    # For every member of the population, evaluate its fitness based on some criteria / function,
    //      and add it to the mating pool in a manner consistant with its fitness, i.e. the more fit it
    //      is the more times it appears in the mating pool, in order to be more likely picked for reproduction.

    //  # Step 2: Reproduction Create a new empty population
    //    # Fill the new population by executing the following steps:
    //       1. Pick two "parent" objects from the mating pool.
    //       2. Crossover -- create a "child" object by mating these two parents.
    //       3. Mutation -- mutate the child's DNA based on a given probability.
    //       4. Add the child object to the new population.
    //    # Replace the old population with the new population
    //
    //   # Rinse and repeat

    private static final String target = "To be or not to be.";
    private static final int populationMax = 150;
    private static final double mutationRate = 0.01;
    private static final int maxGenerations = 500;

    private Population population;

    private static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        Main main = new Main();
        main.setup();

        for (int i = 0; i < maxGenerations; i++){
            main.oneGeneration();
            if (main.isFinished()) {
                System.out.println(("Time: " + (System.currentTimeMillis() - startTime) / 1000.0) + "s");
                break;
            }
        }
    }

    public void setup() {
        // Create a population with a target phrase, mutation rate, and population max
        population = new Population(target, mutationRate, populationMax);
    }

    public void oneGeneration() {
        // Generate mating pool
        population.naturalSelection();
        //Create next generation
        population.generate();
        // Calculate fitness
        population.calcFitness();
        displayInfo();
    }

    public boolean isFinished() {
        // If we found the target phrase, stop
        return population.finished();
    }

    public void displayInfo() {
        // Display current status of population
        String answer = population.getBest();
        System.out.println("Answer: " + answer);
        System.out.println("Total generations: " + population.getGenerations());
        System.out.println("Average fitness: " + population.getAverageFitness());
        System.out.println("Total population: " + populationMax);
        System.out.println("Mutation rate: " + (mutationRate * 100) + "%");
        System.out.println("===========================");
    }
}
