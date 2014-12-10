package com.flow.shop.pszt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by krris on 02.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Main {

    private static final int DEFAULT_POPULATION_MAX = 500;
    private static final double DEFAULT_MUTATION_RATE = 0.3;
    private static final int DEFAULT_MAX_GENERATIONS = 3000;
    private static final String DEFAULT_DATA_PATH = "src/main/resources/test/0520.problem";
//    private static final String DEFAULT_DATA_PATH = "src/main/resources/2020rand/problem.1";
//    private static final String DEFAULT_DATA_PATH = "src/main/resources/0203rand/problem.1";

    private Population population;
    private ApplicationContext context;

    private int populationMax;
    private double mutationRate;
    private int maxGenerations;
    private String dataPath = "";

    private boolean stopAlgorithm = false;

    public static void main(String[] args) {
        Instant start = Instant.now();

        Main main = new Main();
        if (args.length==0) { // if we don't have any command line arguments
            main.populationMax = DEFAULT_POPULATION_MAX;
            main.mutationRate = DEFAULT_MUTATION_RATE;
            main.maxGenerations = DEFAULT_MAX_GENERATIONS;
            main.dataPath = DEFAULT_DATA_PATH;
        } else {
            main.populationMax = Integer.parseInt(args[0]);
            main.mutationRate = Double.parseDouble(args[1]);
            main.maxGenerations = Integer.parseInt(args[2]);
            main.dataPath = args[3];
        }
        main.setup(main.populationMax, main.mutationRate);
//        main.startKeyInputThread();

        for (int i = 0; i < main.maxGenerations && !main.shouldBeStopped(); i++){
            main.oneGeneration(i);
        }

        Instant stop = Instant.now();
        long executionTime = Duration.between(start, stop).getSeconds();
        System.out.println("Execution time: " + executionTime + " seconds");
        main.displayInfo();
    }

    public void setup(int populationMax, double mutationRate) {
        // Load csv file and get all tasks
        this.context = new ClassPathXmlApplicationContext("com/flow/shop/pszt/bean.xml");
        TasksLoader tasksLoader = (TasksLoader) this.context.getBean("tasksLoader");
        tasksLoader.setFilePath(dataPath);
        ArrayList<Task> loadedTasks = tasksLoader.getTasks();

        // Create a population with a target phrase, mutation rate, and population max
        population = new Population(mutationRate, populationMax, loadedTasks);
    }
    
    public void oneGeneration(int generationNo) {
        // Generate mating pool
        population.naturalSelection();
        //Create next generation
        population.generate();
        // Calculate fitness
        population.calcFitness();
        population.evaluate(generationNo);
    }

    public void displayInfo() {
    	// Display the best population ever and its fitness
        System.out.println("Best fitness: "+ population.getBestFitnessEver());
        // Display current status of population
        DNA bestMemberInCurrentPopulation = population.getBestMemberDNA();
        System.out.println("Best member's fitness in current population: " + bestMemberInCurrentPopulation.getFitness());
        System.out.println("Best generation number: " + population.getBestGenerationNo());
        System.out.println("Best member ever: " + population.getBestMemberEver());
        System.out.println("Best member in current population: " + bestMemberInCurrentPopulation.getOrder());
        System.out.println("Total generations: " + population.getGenerations());
        System.out.println("Average fitness: " + population.getAverageFitness());
        System.out.println("Total population: " + populationMax);
        System.out.println("Mutation rate: " + (mutationRate * 100) + "%");
        System.out.println("===========================");
    }

    private Thread inputThread = new Thread(() -> {
        Scanner scan = new Scanner(System.in);
        String input;
        while (true) {
            System.out.println("Type 'c' to stop this program: ");
            System.out.println("===========================");
            input = scan.next().trim();
            if (input.equals("c")) {
                setStopAlgorithm(true);
                break;
            }
        }
    });

    public void startKeyInputThread() {
        this.inputThread.start();
    }

    public void setStopAlgorithm(boolean value) {
        this.stopAlgorithm = value;
    }

    public boolean shouldBeStopped() {
        return this.stopAlgorithm;
    }
}
