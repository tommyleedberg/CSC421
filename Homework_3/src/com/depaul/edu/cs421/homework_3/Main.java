/**
 * file: Main.java
 * Ddte: February 20, 2019
 * ownder: Tommy Leedberg
 */
package com.depaul.edu.cs421.homework_3;

import com.depaul.edu.cs421.homework_3.implementations.ClosetPair;
import com.depaul.edu.cs421.homework_3.implementations.CoordinatePair;
import com.depaul.edu.cs421.homework_3.interfaces.IClosetPair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    private static boolean DEBUG_MODE = false;

    public static void main(String[] args)
    {
        if (args.length <= 0)
        {
            PrintError("Must supply valid arguments");
            return;
        }

        IClosetPair closetPair = new ClosetPair(ReadInputFile(args[0]));
        try
        {
            closetPair.GetClosetPair();
        }
        catch( Exception ex)
        {
            PrintError( ex.getMessage(), ex);
        }

        Print(args[0].substring(args[0].lastIndexOf("\\") + 1, args[0].indexOf(".")) + " test file:");
        closetPair.Print();
    }

    /**
     * Read in the input file generate a coordinate list from it
     *
     * @param fileName  the file path to read in
     * @return a HashMap of the coordinates in the file
     */
    public static List<CoordinatePair> ReadInputFile(String fileName)
    {
        List<CoordinatePair> coordinates = new ArrayList<>();

        // read through the input file setting each of the BlockRecord fields
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String inputLine;
            while ((inputLine = br.readLine()) != null)
            {
               String[] inputCoordinates = inputLine.split(" ");
               coordinates.add( new CoordinatePair( Integer.parseInt(inputCoordinates[0]),
                                                    Integer.parseInt(inputCoordinates[1])));
            }
        }
        catch (Exception ex)
        {
            PrintError("Error while reading input text file", ex);
        }

        if(DEBUG_MODE)
        {
            // Print out the coordinate pairs from the file
            for (CoordinatePair pair : coordinates)
            {
               Print( pair.ToString());
            }
            Print("");
        }
        return coordinates;
    }

    /**
     * Summary:
     *      Print the given text to the console
     * @param textToPrint
     *      The text to print
     */
    private static void Print(String textToPrint)
    {
        System.out.println(textToPrint);
    }

    /**
     * Summary:
     *      Print the given warning
     * @param warningStr
     *      The warning to print
     */
    private static void PrintWarning(String warningStr)
    {
        warningStr =  "*********************WARNING********************\n" + warningStr + "/ n";
        warningStr += "*********************WARNING********************";
        System.out.println(ConsoleColors.YELLOW + warningStr + ConsoleColors.RESET + " NORMAL");
    }

    /**
     * Summary:
     *      Print the given error
     * @param errorStr
     *      The error to print
     * @param ex
     *      The exception to go with the error
     */
    private static void PrintError(String errorStr, Exception ex)
    {
        errorStr = errorStr + "*********************START STACK TRACE********************\n";
        errorStr += ex.getStackTrace() + "\n*********************END STACK TRACE********************\n";

        PrintError(errorStr);
    }

    /**
     * Summary:
     *      Print the given error
     * @param errorStr
     *      The error message
     */
    private static void PrintError(String errorStr)
    {
        errorStr = "*********************ERROR********************\n" + errorStr + "\n";
        errorStr += "*********************ERROR********************";
        System.out.println(ConsoleColors.RED + errorStr + ConsoleColors.RESET);
    }
}
