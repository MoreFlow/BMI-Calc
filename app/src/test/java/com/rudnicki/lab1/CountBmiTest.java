package com.rudnicki.lab1;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CountBmiTest {
    @Test
    public void weightUnderZero_isInvalid() {
        //GIVEN
        float testWeight = -1.0f;
        //WHEN
        ICountBmi countBmi = new CountBmiMetrics();
        boolean isWeightValid = countBmi.isWeightValid(testWeight);
        //THEN
        assertFalse(isWeightValid);
    }

    @Test
    public void myBmi_isValid() throws Exception {
        //GIVEN
        float myWeight = 50;
        float myHeight = 1.25f;
        //WHEN
        ICountBmi bmiCounter = new CountBmiMetrics();
        boolean isMyHeightValid = bmiCounter.isHeightValid(myHeight);
        boolean isMyWeightValid = bmiCounter.isWeightValid(myWeight);
        float bmi = bmiCounter.countBmi(myWeight, myHeight);
        //THEN
        assertTrue(isMyHeightValid);
        assertTrue(isMyWeightValid);
        assertEquals(32.0, bmi, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongWeight_willThrowException() throws IllegalArgumentException {
        //GIVEN
        float lessThanMinimalWeight = 3;
        float goodHeight = 140;
        //WHEN
        ICountBmi bmiCounter = new CountBmiMetrics();
        bmiCounter.countBmi(lessThanMinimalWeight, goodHeight);
    }
}
