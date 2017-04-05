package com.rudnicki.lab1;

public interface ICountBmi {
    boolean isWeightValid(float weight);
    boolean isHeightValid(float height);
    float countBmi(float weight, float height) throws IllegalArgumentException;
}
