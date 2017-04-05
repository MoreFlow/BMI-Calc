package com.rudnicki.lab1;

public class CountBmiImperial implements ICountBmi {

    public static float MIN_WEIGHT = 20f;
    public static float MAX_WEIGHT = 500f;
    public static float MIN_HEIGHT = 36f;
    public static float MAX_HEIGHT = 96f;

    @Override
    public boolean isWeightValid(float weight) {
        return weight >= MIN_WEIGHT && weight <= MAX_WEIGHT;
    }

    @Override
    public boolean isHeightValid(float height) {
        return height >= MIN_HEIGHT && height <= MAX_HEIGHT;
    }

    @Override
    public float countBmi(float weight, float height) throws IllegalArgumentException {
        if(!isHeightValid(height) || !isWeightValid(weight)) {
            throw new IllegalArgumentException("Data is not valid.");
        }
        return (weight / (height * height)) * 703;
    }
}
