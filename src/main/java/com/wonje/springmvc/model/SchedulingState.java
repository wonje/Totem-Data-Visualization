package com.wonje.springmvc.model;

/**
 * Created by wonje on 5/7/17.
 */
public enum SchedulingState {
    FIVE_MIN,
    FIFTHTEEN_MIN,
    THIRTY_MIN;

    public static SchedulingState currentState = FIVE_MIN;
}
