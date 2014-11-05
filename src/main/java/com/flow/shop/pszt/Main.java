package com.flow.shop.pszt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * Created by krris on 02.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Main {

    private static final int POPULATION_MAX = 150;
    private static final double MUTATION_RATE = 0.03;
    private static final int MAX_GENERATIONS = 2000;

    private Population population;
    private ApplicationContext context;

    public static void main(String[] args) {
        Main main = new Main();
        main.setup();

        for (int i = 0; i < MAX_GENERATIONS; i++){
            main.oneGeneration();
        }
    }

    public void setup() {
        // Load csv file and get all tasks
        this.context = new ClassPathXmlApplicationContext("com/flow/shop/pszt/bean.xml");
        TasksLoader tasksLoader = (TasksLoader) this.context.getBean("tasksLoader");
        ArrayList<Task> loadedTasks = tasksLoader.getTasks();

        // Create a population with a target phrase, mutation rate, and population max
        population = new Population(MUTATION_RATE, POPULATION_MAX, loadedTasks);
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

    public void displayInfo() {
        // Display current status of population
        String answer = population.getBest();
        System.out.println("Answer: " + answer);
        System.out.println("Total generations: " + population.getGenerations());
        System.out.println("Average fitness: " + population.getAverageFitness());
        System.out.println("Total population: " + POPULATION_MAX);
        System.out.println("Mutation rate: " + (MUTATION_RATE * 100) + "%");
        System.out.println("===========================");
    }
}
