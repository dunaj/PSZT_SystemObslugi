package com.flow.shop.pszt;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by krris on 04.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class TasksLoaderTest {

    private ApplicationContext context;
    private TasksLoader tasksLoader;

    @Before
    public  void setUp() {
        this.context = new ClassPathXmlApplicationContext("com/flow/shop/pszt/bean.xml");
        this.tasksLoader = (TasksLoader) context.getBean("tasksLoader");
    }

    @Test
    public void testLoadingCsvFile() {
        ArrayList<Task> tasks = tasksLoader.getTasks();

        int expectedTasksNo = 4;
        int actualTasksNo = tasks.size() - 1;
        assertEquals(expectedTasksNo, actualTasksNo);

        int expectedMachinesNo = 4;
        Task task = tasks.get(0);
        int actualMachinesNo = task.getComputationTimeForMachines().size() - 1;
        assertEquals(expectedMachinesNo, actualMachinesNo);
    }

    @Test
    public void machinesAndTasksNo() {
        assertEquals(4, TasksLoader.getMachinesNo());
        assertEquals(4, TasksLoader.getTasksNo());
    }
}
