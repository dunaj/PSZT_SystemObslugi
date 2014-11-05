package com.flow.shop.pszt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by krris on 05.11.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class DNATest {
    private ApplicationContext context;
    private TasksLoader tasksLoader;

    @Before
    public  void setUp() {
        this.context = new ClassPathXmlApplicationContext("com/flow/shop/pszt/bean.xml");
        this.tasksLoader = (TasksLoader) context.getBean("tasksLoader");
    }

    @Test
    public void dnaConstructionTest() {
        ArrayList<Task> tasks = tasksLoader.getTasks();
        DNA dna1 = new DNA(tasks);
        Assert.assertNotEquals(tasks, dna1.getGenes());
    }

    @Test
    public void crossoverTest() {
        ArrayList<Task> tasks = tasksLoader.getTasks();

        int loopNo = 1000;
        for (int i = 0; i < loopNo; i++) {
            DNA dna = new DNA(tasks);
            DNA partner = new DNA(tasks);
            DNA afterCrossover = dna.crossover(partner);

            for (Task task : dna.getGenes()) {
                assertTrue(afterCrossover.getGenes().contains(task));
            }

            assertEquals(partner.getGenes().size(), afterCrossover.getGenes().size());
            assertEquals(dna.getGenes().size(), afterCrossover.getGenes().size());

//            System.out.println("DNA 1: " + dna.getGenes());
//            System.out.println("DNA 2: " + partner.getGenes());
//            System.out.println("After crossover: " + afterCrossover.getGenes());
        }
    }
}
