package com.flow.shop.pszt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * Created by krris on 02.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Main {

    private static final int DEFAULT_POPULATION_MAX = 5;
    private static final double DEFAULT_MUTATION_RATE = 0.03;
    private static final int DEFAULT_MAX_GENERATIONS = 1000;

    private Population population;
    private ApplicationContext context;

    private int populationMax;
    private double mutationRate;
    private int maxGenerations;

    public static void main(String[] args) {
        Main main = new Main();
        if (args.length==0) { // if we don't have any command line arguments
            main.populationMax = DEFAULT_POPULATION_MAX;
            main.mutationRate = DEFAULT_MUTATION_RATE;
            main.maxGenerations = DEFAULT_MAX_GENERATIONS;
        } else {
            main.populationMax = Integer.parseInt(args[0]);
            main.mutationRate = Double.parseDouble(args[1]);
            main.maxGenerations = Integer.parseInt(args[2]);
        }
        main.setup(main.populationMax, main.mutationRate);

        for (int i = 0; i < main.maxGenerations; i++){
            main.oneGeneration();
        }
    }

    public void setup(int populationMax, double mutationRate) {
        // Load csv file and get all tasks
        this.context = new ClassPathXmlApplicationContext("com/flow/shop/pszt/bean.xml");
        TasksLoader tasksLoader = (TasksLoader) this.context.getBean("tasksLoader");
        ArrayList<Task> loadedTasks = tasksLoader.getTasks();

        // Create a population with a target phrase, mutation rate, and population max
        population = new Population(mutationRate, populationMax, loadedTasks);
    }
    
    public void oneGeneration() {
        // Generate mating pool
        population.naturalSelection();
        //Create next generation
        population.generate();
        // Calculate fitness
        population.calcFitness();
        population.evaluate();
        displayInfo();
    }

    public void displayInfo() {
    	// Display the best population ever and its fitness
        System.out.println("Best fitness: "+ population.getBEST_FITNESS_EVER());
        System.out.println("Best member ever: " + population.getBEST_MEMBER_EVER());
        // Display current status of population
        DNA bestMemberInCurrentPopulation = population.getBestMemberDNA();
        System.out.println("Best member's fitness in current population: " + bestMemberInCurrentPopulation.getFitness());
        System.out.println("Best member in current population: " + bestMemberInCurrentPopulation.getOrder());
        System.out.println("Total generations: " + population.getGenerations());
        System.out.println("Average fitness: " + population.getAverageFitness());
        System.out.println("Total population: " + populationMax);
        System.out.println("Mutation rate: " + (mutationRate * 100) + "%");
        System.out.println("===========================");
    }
}
