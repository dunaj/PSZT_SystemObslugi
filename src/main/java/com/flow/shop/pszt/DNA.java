package com.flow.shop.pszt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by krris on 29.10.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class DNA implements Operators {

    private ArrayList<Task> genes;
    private double fitness;

    private Random random = new Random();

    public DNA(ArrayList<Task> tasks) {
        this.genes = new ArrayList<>(tasks);

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

    public void calculateFitness () {
    	Double[][] computedValues = new Double[TasksLoader.getTasksNo() + 1][TasksLoader.getMachinesNo() + 1];
    	this.fitness = c(TasksLoader.getTasksNo(), TasksLoader.getMachinesNo(), computedValues);
    }

    /**
     * Function which implements counting of fitness by recurrence
     *
     * @param i - actual position in rows (tasks) in recurrence
     * @param j - actual position in columns (machines) in recurrence
     * @param computedValues - temporary table for avoiding stack overflow
     * @return - fitness count
     */
    private double c(int i, int j, Double[][] computedValues) {
        if (computedValues[i][j] != null) {
            return computedValues[i][j];
        }

        if (i == 0 && j == 0) {
            double returnValue = genes.get(i).getComputationTimeForMachine(j);
            computedValues[i][j] = returnValue;
            return returnValue;
        }

        if (i == 0 && j > 0) {
            double returnValue =  c(i, j-1, computedValues) + genes.get(i).getComputationTimeForMachine(j);
            computedValues[i][j] = returnValue;
            return returnValue;
        }

        if (i > 0 && j == 0) {
            double returnValue =  c(i-1, j, computedValues) + genes.get(i).getComputationTimeForMachine(j);
            computedValues[i][j] = returnValue;
            return returnValue;
        }

        double returnValue = Math.max(c(i - 1, j, computedValues), c(i, j - 1, computedValues)) + genes.get(i).getComputationTimeForMachine(j);
        computedValues[i][j] = returnValue;
        return returnValue;
    }

    public double getFitness() {
        return fitness;
    }
}
