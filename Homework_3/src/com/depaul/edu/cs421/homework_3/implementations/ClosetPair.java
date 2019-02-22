/**
 * file: ClosetPair.java
 * Ddte: February 20, 2019
 * ownder: Tommy Leedberg
 */
package com.depaul.edu.cs421.homework_3.implementations;

import com.depaul.edu.cs421.homework_3.interfaces.IClosetPair;
import com.depaul.edu.cs421.homework_3.interfaces.ICoordinatePair;
import com.sun.corba.se.spi.transport.CorbaAcceptor;

import java.util.*;

public class ClosetPair implements IClosetPair
{
    public static Boolean DEBUG_MODE = false ;

    private List<Integer> xCoordinatesList = new ArrayList<>();
    private List<Integer> yCoordinatesList = new ArrayList<>();
    private List<CoordinatePair> coordinates;
    private CoordinatePair[] closetPair = new CoordinatePair[2];
    private double shortestDistance = -1;

    public ClosetPair(List<CoordinatePair> coordinates)
    {
        this.coordinates = coordinates;
        this.GenerateCoordinateLists();
    }

    public void GetClosetPair()
    {
        try
        {
            CoordinatePair[] pairs = this.FindClosetPair(this.coordinates, this.xCoordinatesList, this. yCoordinatesList);
            this.SetClosetPair(pairs[0], pairs[1]);
        }
        catch( Exception ex)
        {
          throw ex;
        }
    }

    public void Print()
    {
        System.out.println("The minimum distance is: " + this.shortestDistance);
        System.out.println( this.closetPair[0].ToString() + "<--->" + this.closetPair[1].ToString() + "\n");
    }

    private void GenerateCoordinateLists()
    {
        for (CoordinatePair pair : this.coordinates)
        {
            this.xCoordinatesList.add(pair.GetXCoordinate());
            this.yCoordinatesList.add(pair.GetYCoordinate());
        }

        // Collection.sort runs in O(nlogn) according to javadoc
        Collections.sort(this.coordinates);
        Collections.sort(this.xCoordinatesList);
        Collections.sort(this.yCoordinatesList);
    }

    private void SetClosetPair(CoordinatePair pair1, CoordinatePair pair2)
    {
        this.closetPair[0] = pair1;
        this.closetPair[1] = pair2;
    }

    private CoordinatePair[] FindClosetPair(List<CoordinatePair> coordinatePairs, List<Integer> xCoordinates, List<Integer> yCoordinates)
    {
        if( coordinatePairs.size() <= 1)
        {
            return null;
        }

        if (coordinatePairs.size() <= 3)
        {
            return this.CalculateShortestDistance(coordinatePairs);
        }

        int median = xCoordinates.get((int)Math.floor(xCoordinates.size() / 2)) ;
        int medianIndex = this.FindMedianIndex(coordinatePairs, median);

        if( medianIndex == -1)
        {
            throw new IllegalArgumentException("failed to find median index");
        }

        // Split the coordinates list in half
        List<CoordinatePair> leftHalfCoordinates = coordinatePairs.subList(0, medianIndex);
        List<CoordinatePair> rightHalfCoordinates = coordinatePairs.subList(medianIndex + 1, coordinatePairs.size());

        // Split the x coordinates in half
        List<Integer> leftHalfXCoordinates = xCoordinates.subList(0, medianIndex);
        List<Integer> rightHalfXCoordinates = xCoordinates.subList(medianIndex +1, coordinatePairs.size());

        // Split the y coordinates in half
        List<Integer> leftHalfYCoordinates = this.FindPairByXCoordinate(coordinatePairs, leftHalfXCoordinates);
        List<Integer> rightHalfYCoordinates = this.FindPairByXCoordinate(coordinatePairs, rightHalfXCoordinates);

        CoordinatePair[] closetLeftPair = this.FindClosetPair(leftHalfCoordinates, leftHalfXCoordinates, leftHalfYCoordinates);
        CoordinatePair[] closetRightPair = this.FindClosetPair(rightHalfCoordinates, rightHalfXCoordinates, rightHalfYCoordinates);

        double leftPairDistance = Double.MAX_VALUE;
        double rightPairDistance = Double.MAX_VALUE;
        double minPair;

        if( closetLeftPair != null)
        {
            leftPairDistance = this.CalculateDistance(closetLeftPair[0], closetLeftPair[1]);
        }

        if( closetRightPair != null)
        {
            rightPairDistance = this.CalculateDistance(closetRightPair[0], closetRightPair[1]);
        }

        minPair = leftPairDistance < rightPairDistance ? leftPairDistance : rightPairDistance;

        List<CoordinatePair> pairRange = FindRangeOfPairs(coordinatePairs, minPair, median);
        pairRange.sort(CoordinatePair::CompareToYCoordinate);
        CoordinatePair[] closetMidPair = new CoordinatePair[2];

        if(pairRange.size() > 7)
        {
            pairRange = pairRange.subList(0, 7);
        }

        closetMidPair = CalculateShortestDistance(pairRange);

        double midPairDistance = Double.MAX_VALUE;
        if( closetMidPair != null)
        {
            midPairDistance = this.CalculateDistance(closetMidPair[0], closetMidPair[1]);
        }

        if( leftPairDistance < rightPairDistance && leftPairDistance < midPairDistance )
        {
            return closetLeftPair;
        }
        else if( rightPairDistance < leftPairDistance && rightPairDistance < midPairDistance )
        {
            return closetRightPair;
        }
        else
        {
            return closetMidPair;
        }
    }

    private int FindMedianIndex(List<CoordinatePair> coordinatePairs, int median)
    {
        for( int i = 0; i < coordinatePairs.size(); ++i)
        {
            if( coordinatePairs.get(i).GetXCoordinate() > median)
            {
                return i;
            }
        }
        return -1;
    }

    private List<CoordinatePair> FindRangeOfPairs(List<CoordinatePair> coordinatePairs, double minPair, int median)
    {
        List<CoordinatePair> pairRange = new ArrayList<>();
        double minX = Math.abs(median - minPair);
        double maxX = Math.abs(median + minPair);

        for( CoordinatePair pair : coordinatePairs )
        {
            if( pair.GetXCoordinate() >= minX && pair.GetXCoordinate() <= maxX )
            {
                pairRange.add(pair);
            }
        }

        return pairRange;
    }

    private List<Integer> FindPairByXCoordinate(List<CoordinatePair> coordinatePairs, List<Integer> xCoordinates)
    {
        List<Integer> yCoordinates = new ArrayList<>();

        for( CoordinatePair pair : coordinatePairs)
        {
            // Contains runs in O(n)
            if( xCoordinates.contains(pair.GetXCoordinate()))
            {
                yCoordinates.add(pair.GetYCoordinate());
            }
        }

        return yCoordinates;
    }

    private CoordinatePair[] CalculateShortestDistance(List<CoordinatePair> coordinatePairs)
    {
        double shortestDistance = Double.MAX_VALUE;
        CoordinatePair[] closetPairs = new CoordinatePair[2];

        if( coordinatePairs.size() == 0)
        {
            return null;
        }

        if( coordinatePairs.size() == 1)
        {
            return null;
        }

        int i = 0;
        int j = i + 1;
        while (i < j && j < coordinatePairs.size())
        {
            try
            {
                if (DEBUG_MODE)
                {
                    System.out.print("Calculating distance between : ");
                    System.out.print(coordinatePairs.get(i).ToString() + " and ");
                    System.out.print(coordinatePairs.get(j).ToString() + "\n");
                }

                if (shortestDistance == -1)
                {
                    shortestDistance = this.CalculateDistance(coordinatePairs.get(i), coordinatePairs.get(j));
                    closetPairs[0] = coordinatePairs.get(i);
                    closetPairs[1] = coordinatePairs.get(j);
                }
                else
                {
                    double tempDistance = this.CalculateDistance(coordinatePairs.get(i), coordinatePairs.get(j));
                    if (shortestDistance > tempDistance)
                    {
                        shortestDistance = tempDistance;
                        closetPairs[0] = coordinatePairs.get(i);
                        closetPairs[1] = coordinatePairs.get(j);
                    }
                }

                if (j + 1 < coordinatePairs.size())
                {
                    j++;
                }
                else
                {
                    i++;
                    j = i + 1;
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if( this.shortestDistance == -1 )
        {
            this.shortestDistance = shortestDistance;
        }
        else
        {
            this.shortestDistance = shortestDistance < this.shortestDistance ? shortestDistance : this.shortestDistance;
        }

        return closetPairs;
    }

    private double CalculateDistance(CoordinatePair pair1, CoordinatePair pair2)
    {
        double xValue = Math.pow((pair1.GetXCoordinate() - pair2.GetXCoordinate()), 2);
        double yValue = Math.pow((pair1.GetYCoordinate() - pair2.GetYCoordinate()), 2);
        double distance = Math.sqrt((xValue + yValue));

        if(DEBUG_MODE)
        {
            System.out.print( "Distance between : " ) ;
            System.out.print( pair1.ToString() + " and " );
            System.out.print( pair2.ToString() + " is: " + distance + "\n\n");
        }

        return distance;
    }
}
