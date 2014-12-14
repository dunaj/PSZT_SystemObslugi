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

    private static final int DEFAULT_POPULATION_MAX = 100;
    private static final double DEFAULT_MUTATION_RATE = 0.2;
    private static final int DEFAULT_MAX_GENERATIONS = 5000;
    private static final double DEFAULT_ALPHA = 3.0;
    private static final String DEFAULT_DATA_PATH = "src/main/resources/05-20/2.problem";

    private static final double LOG_EVERY_NTH_GENERATION = 100;

    private Population population;
    private ApplicationContext context;

    private int populationMax;
    private double mutationRate;
    private int maxGenerations;
    private double alpha;
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
            main.alpha = DEFAULT_ALPHA;
        } else {
            main.populationMax = Integer.parseInt(args[0]);
            main.alpha = Double.parseDouble(args[1]);
            main.mutationRate = Double.parseDouble(args[2]);
            main.maxGenerations = Integer.parseInt(args[3]);
            main.dataPath = args[4];
        }
        main.setup(main.populationMax, main.mutationRate, main.alpha);
        main.startKeyInputThread();

        for (int i = 0; i < main.maxGenerations && !main.shouldBeStopped(); i++){
            main.oneGeneration(i);
            if (i % LOG_EVERY_NTH_GENERATION == 0) {
                main.displayInfo();
            }
        }

        Instant stop = Instant.now();
        long executionTime = Duration.between(start, stop).getSeconds();
        System.out.println("Execution time: " + executionTime + " seconds");
        main.displayInfo();
    }

    public void setup(int populationMax, double mutationRate, double alpha) {
        // Load csv file and get all tasks
        this.context = new ClassPathXmlApplicationContext("com/flow/shop/pszt/bean.xml");
        TasksLoader tasksLoader = (TasksLoader) this.context.getBean("tasksLoader");
        tasksLoader.setFilePath(dataPath);
        ArrayList<Task> loadedTasks = tasksLoader.getTasks();

        // Create a population with a target phrase, mutation rate, and population max
        population = new Population(mutationRate, alpha, populationMax, loadedTasks);
    }
    
    public void oneGeneration(int generationNo) {
        population.oneGeneration();
        population.evaluate(generationNo);
    }

    public void displayInfo() {
    	// Display the best population ever and its fitness
        System.out.println("Best fitness: "+ population.getBestFitnessEver());
        // Display current status of population
        DNA bestMemberInCurrentPopulation = population.getBestMemberDNA();
        System.out.println("Best member's fitness in current population: " + bestMemberInCurrentPopulation.getFitness());
        System.out.println("Best generation number: " + population.getBestGenerationNo());
//        System.out.println("Best member ever: " + population.getBestMemberEver());
//        System.out.println("Best member in current population: " + bestMemberInCurrentPopulation.getOrder());
        System.out.println("Total generations: " + population.getGenerations());
//        System.out.println("Average fitness: " + population.getAverageFitness());
//        System.out.println("Total population: " + populationMax);
//        System.out.println("Mutation rate: " + (mutationRate * 100) + "%");
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
