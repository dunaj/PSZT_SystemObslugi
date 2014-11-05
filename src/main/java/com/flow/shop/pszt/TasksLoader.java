package com.flow.shop.pszt;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by krris on 04.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class TasksLoader {

    private ArrayList<Task> tasks = new ArrayList<>();

    private static int tasksNo;
    private static int machinesNo;

    public TasksLoader(String csvPath) {
        this.tasks = loadTasksAndSetMachineAndTasksNo(csvPath);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    private ArrayList<Task> loadTasksAndSetMachineAndTasksNo(String csvPath) {
        CSVParser parser = getCsvParser(csvPath);
        ArrayList<Task> tasks = new ArrayList<>();
        int taskId = 0;
        int machineId = 0;

        for (CSVRecord record : parser) {
            HashMap<Integer, Double> computationTimeForMachine = new HashMap<>();

            machineId = 0;
            for (String field : record) {
                computationTimeForMachine.put(machineId, Double.parseDouble(field));
                machineId++;
            }
            tasks.add(new Task(taskId, computationTimeForMachine));
            taskId++;
        }

        // we start counting from 0
        tasksNo = taskId - 1;
        machinesNo = machineId - 1;

        return tasks;
    }

    private CSVParser getCsvParser(String csvPath) {
        CSVParser parser = null;
        try {
            parser = new CSVParser(new FileReader(csvPath), CSVFormat.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parser;
    }

    public static int getMachinesNo() {
        return machinesNo;
    }

    public static int getTasksNo() {

        return tasksNo;
    }
}
