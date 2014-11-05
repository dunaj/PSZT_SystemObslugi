package com.flow.shop.pszt;

/**
 * com.flow.shop.pszt.Operators for the evolutionary algorithms;
 *
 * Created by krris on 29.10.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public interface Operators {
    /**
     * Crossover operation between two parents.
     */
    DNA crossover(DNA partner);

    /**
     * Mutation operator.
     */
    void mutate(double mutationRate);
}
