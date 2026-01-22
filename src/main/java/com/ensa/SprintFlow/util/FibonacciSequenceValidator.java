package com.ensa.SprintFlow.util;

import java.util.Set;

public class FibonacciSequenceValidator {
    private static final Set<Integer> validSequence = Set.of(1,2,3,5,8,13,20);

    public static boolean isValid(Integer... numbers){
        for( Integer number : numbers){
            if( !validSequence.contains(number)){
                return false;
            }
        }
        return true;
    }
}
