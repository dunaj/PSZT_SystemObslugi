package com.flow.shop.pszt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * In loaded file rows correspond to tasks and columns to machines
 * 
 * Created by krris on 04.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class TasksLoader {

    private ArrayList<Task> tasks = new ArrayList<>();

    private static int tasksNo;    // Number of all tasks
    private static int machinesNo; // Number of all machines

    private String filePath;

    public ArrayList<Task> getTasks() {
        if (tasks.isEmpty()) {
            this.tasks = loadTasksAndSetMachineAndTasksNo();
        }
        return this.tasks;
    }

    private ArrayList<Task> loadTasksAndSetMachineAndTasksNo() {
        List<List<String>> records = readRecords(filePath);
        ArrayList<Task> tasks = new ArrayList<>();
        int taskId = 0;
        int machineId = 0;

        for (List<String> line : records) {
            HashMap<Integer, Double> computationTimeForMachine = new HashMap<>();
            for (int i = 0; i < line.size(); i += 2) {
                machineId = Integer.parseInt(line.get(i));
                double computationTime = Double.parseDouble(line.get(i+1));
                computationTimeForMachine.put(machineId, computationTime);
            }
            tasks.add(new Task(taskId, computationTimeForMachine));
            taskId++;
        }

        // we start counting from 0
        tasksNo = taskId - 1;
        machinesNo = machineId;

        return tasks;
    }

    private List<List<String>> readRecords(String file) {
        Path path = Paths.get(file);
        try(Stream<String> lines = Files.lines(path)){
            return lines.map(line -> Arrays.asList(line.trim().split("\\s+")))
                        .filter(listWithNonEmptyElements())
                        .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Predicate<List<String>> listWithNonEmptyElements() {
        return strings -> {
            for (String string : strings) {
                if (string.isEmpty())
                    return false;
            }
            return true;
        };
    }

    public static int getMachinesNo() {
        return machinesNo;
    }

    public static int getTasksNo() {
        return tasksNo;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
