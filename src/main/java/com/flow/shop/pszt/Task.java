package com.flow.shop.pszt;

import java.util.Map;

/**
 * Created by krris on 03.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class Task {
    private int id;
    // Computation time for given machine id.
    private Map<Integer, Double> computationTimeForMachine;

    public Task(int id, Map<Integer, Double> computationTimeForMachine) {
        this.id = id;
        this.computationTimeForMachine = computationTimeForMachine;
    }

    public Map<Integer, Double> getComputationTimeForMachines() {
        return computationTimeForMachine;
    }

    public double getComputationTimeForMachine(int machineId) {
        return this.computationTimeForMachine.get(machineId);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return Integer.toString(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
