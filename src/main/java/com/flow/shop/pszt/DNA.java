package com.flow.shop.pszt;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by krris on 29.10.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class DNA implements Operators, ApplicationContextAware {

    private ArrayList<Task> loadedTasks;
    private ArrayList<Task> genes;
    private double fitness;

    private Random random = new Random();
    private ApplicationContext context;

    public DNA(ArrayList<Task> loadedTasks) {
        this.genes = new ArrayList<>(loadedTasks);

        // Initialize DNA with random order of genes
        Collections.shuffle(this.genes);
    }

    private DNA() {
        // child with empty genes
        this.genes = new ArrayList<>();
    }

    public DNA crossover(DNA partner) {
        // A new child
        DNA child = this.createChild();

        int midpoint = new Random().nextInt(genes.size()); // Pick a midpoint

        // Half from one, half from the other
        for (int i = 0; i < midpoint; i++) {
            child.genes.add(this.genes.get(i));
        }

        for (Task task: partner.getGenes()) {
            if (!child.genes.contains(task)) {
                child.genes.add(task);
            }
        }
        return child;
    }

    // Based on a mutation probability, changes two tasks with each other
    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.size(); i++) {
            if (random.nextFloat() % 1 < mutationRate) {
                int firstTaskId = random.nextInt(genes.size());
                int secondTaskId = random.nextInt(genes.size());
                while (firstTaskId == secondTaskId) {
                    secondTaskId = random.nextInt(genes.size());
                }

                switchGenes(firstTaskId, secondTaskId);
            }
        }
    }

    private void switchGenes(int firstTaskId, int secondTaskId) {
        Task temp = this.genes.get(firstTaskId);
        this.genes.set(firstTaskId, this.genes.get(secondTaskId));
        this.genes.set(secondTaskId, temp);
    }

    private DNA createChild() {
        return new DNA();
    }

    public String getOrder() {
        return this.genes.toString();
    }

    public ArrayList<Task> getGenes() {
        return genes;
    }

    // Fitness function (set floating point % of "correct" characters)
    public void calculateFitness () {
        this.fitness = c(TasksLoader.getTasksNo(), TasksLoader.getMachinesNo());
    }

    private double c(int i, int j) {
        if (i == 1 && i == 1) {
            return genes.get(i).getComputationTimeForMachine(j);
        }

        if (i == 1 && j > 1) {
            return c(i, j-1) + genes.get(i).getComputationTimeForMachine(j);
        }

        if (i > 1 && j == 1) {
            return c(i-1, j) + genes.get(i).getComputationTimeForMachine(j);
        }

        return Math.max(c(i -1, j), c(i, j-1)) + genes.get(i).getComputationTimeForMachine(j);
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
