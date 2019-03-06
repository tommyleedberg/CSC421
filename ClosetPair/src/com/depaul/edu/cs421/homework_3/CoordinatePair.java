package com.depaul.edu.cs421.homework_3;

public class CoordinatePair implements  Comparable<CoordinatePair>
{
    private int xCoordinate;
    private int yCoordinate;

    /**
     * Summary:
     *      Creates a coordinate pair
     * @param x
     *      The x-coordinate
     * @param y
     *      The y-coordinate
     */
    public CoordinatePair(int x, int y)
    {
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    /**
     * Summary:
     *      Gets the x-coordinate
     * @return
     *      The x-coordinate
     */
    public int GetXCoordinate()
    {
        return this.xCoordinate;
    }

    /**
     * Summary:
     *      Gets the y-coordinate
     * @return
     *      The y-coordinate
     */
    public int GetYCoordinate()
    {
        return this.yCoordinate;
    }

    public String ToString()
    {
       return "( " + this.xCoordinate + ", " + this.yCoordinate + " )";
    }

    public int CompareToYCoordinate(CoordinatePair pair)
    {
        return Integer.compare(this.yCoordinate, pair.GetYCoordinate());
    }

    @Override
    public int compareTo(CoordinatePair pair)
    {
        return Integer.compare(this.xCoordinate, pair.GetXCoordinate());
    }
}
