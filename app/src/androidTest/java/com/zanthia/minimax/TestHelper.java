package com.zanthia.minimax;

import junit.framework.TestCase;

/**
 * Created by Zanthia on 16/02/2015.
 */
public class TestHelper extends TestCase {

    public static void assertArrayEquals(FieldState[][] expected, FieldState[][] fieldStates){
        assertEquals(expected.length, fieldStates.length);
        for (int i = 0; i < expected.length; i++){
            assertEquals(expected[i].length, fieldStates[i].length);
            for(int j = 0; j < expected[i].length; j++){
                assertEquals(expected[i][j], fieldStates[i][j]);
            }
        }
    }
}
