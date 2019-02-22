package com.depaul.edu.cs421.homework_3.interfaces;

import com.depaul.edu.cs421.homework_3.implementations.CoordinatePair;

public interface ICoordinatePair
{
    /**
     * Summary:
     *      Gets the X-coordinate
     * @return
     *      The X-coordinate
     */
    int GetXCoordinate();

    /**
     * Summary:
     *      Gets the Y-coordinate
     * @return
     *      The Y-coordinate
     */
    int GetYCoordinate();

    /**
     * Summary:
     *      Gets a string representation of the coordinate pair
     */
    String ToString();

    /**
     * Compare the y-coordinates
     * @param coordinatePair The coordinate pair to compare
     * @return a value > 0 the the given pair's y-coordinate is greater, equal to 0 if it is equal, and < 0 if it is less
     */
    int CompareToYCoordinate(CoordinatePair coordinatePair);

    /**
     * Summary:
     *      Implementation for comparable. Compares x-coordinates only
     */
    int compareTo(CoordinatePair coordinatePair);
}
