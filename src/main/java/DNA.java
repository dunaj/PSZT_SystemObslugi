import java.util.Random;

/**
 * Created by krris on 29.10.14.
 * Copyright (c) 2014 krris. All rights reserved.
 */
public class DNA implements Operators {
    
    private char[] genes;
    private float fitness;

    private Random random = new Random();
    
    public DNA(int num) {
        this.genes = new char[num];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = (char) Util.randInt(32, 128); // Pick from range of chars
        }
    }

    public DNA crossover(DNA partner) {
        // A new child
        DNA child = new DNA(genes.length);

        int midpoint = new Random().nextInt(genes.length); // Pick a midpoint

        // Half from one, half from the other
        for (int i = 0; i < genes.length; i++) {
            if (i > midpoint) child.genes[i] = genes[i];
            else              child.genes[i] = partner.genes[i];
        }
        return child;
    }

    // Based on a mutation probability, picks a new random character
    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if (random.nextFloat() % 1 < mutationRate) {
                genes[i] = (char) Util.randInt(32,128);
            }
        }
    }

    public String getPhrase() {
        return new String(this.genes);
    }

    // Fitness function (set floating point % of "correct" characters)
    public void calculateFitness (String target) {
        int score = 0;
        for (int i = 0; i < this.genes.length; i++) {
            if (this.genes[i] == target.charAt(i)) {
                score++;
            }
        }

        this.fitness = (float)score / (float)target.length();
    }

    public float getFitness() {
        return fitness;
    }
}
