package com.buffboosterapp.buffbooster;

public class RepElement {
    /* Number of reps */
    public int reps;

    /* If false, no weight is being used */
    public boolean usesWeight;

    /* Weight - ex: 35 */
    int weight;

    /* Units for weight - ex: lbs */
    String weightUnits;

    public RepElement(int reps, boolean usesWeight, int weight, String weightUnits) {
        this.reps = reps;
        this.usesWeight = usesWeight;
        this.weight = weight;
        this.weightUnits = weightUnits;
    }
}
