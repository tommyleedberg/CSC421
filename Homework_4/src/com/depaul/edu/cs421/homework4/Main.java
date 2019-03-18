package com.depaul.edu.cs421.homework4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

enum STATUS_CODE
{
    SUCCESS,
    FAIL
}

public class Main
{
    private static boolean DEBUG_MODE = false;

    private static Map<Vertex, List<Vertex>> adjList = new HashMap<>();
    private static Map<Vertex, Integer> distanceArray = new HashMap<>();
    private static PriorityQueue<Vertex> heap;

    public static void main(String[] args)
    {
        try
        {
            if( args.length < 1)
            {
                Print("You must include the filepath for the adjacency list");
            }

            STATUS_CODE result = Initialize(args[0]);
            if( result == STATUS_CODE.FAIL)
            {
                PrintError("Failed to generate adjacency list from file");
            }

            Vertex target = DijkstrasAlgorithm();
            Print("Total Weight:" + distanceArray.get(target));
            PrintShortestPath(target);
        }
        catch (Exception ex)
        {
            PrintError( ex.getMessage(), ex);
        }

    }

    private static Vertex DijkstrasAlgorithm()
    {
        Vertex target = new Vertex();

        while( heap.size() != 0)
        {
            Vertex vertex = heap.poll();

            if( vertex.name.compareToIgnoreCase("B") == 0)
            {
                target = vertex;
            }

            List<Vertex> neighbors = adjList.get(vertex);

            if( neighbors != null)
            {
                for (Vertex destination : neighbors)
                {
                    try
                    {
                        int newWeight = distanceArray.get(vertex) + destination.weight;
                        if (distanceArray.get(destination) > newWeight)
                        {
                            heap.remove(destination);
                            destination.weight = newWeight;
                            destination.parent = vertex;
                            distanceArray.put(destination, newWeight);
                            heap.add(destination);
                        }
                    }
                    catch (Exception ex)
                    {
                        PrintError("Error while checking weights", ex);
                    }
                }
            }
        }

        return target;
    }

    private static STATUS_CODE Initialize(String fileName) throws Exception
    {
        try
        {
            GenerateAdjacencyList(fileName);
            heap = new PriorityQueue<>(adjList.size(),new Vertex());
            for( Map.Entry<Vertex, List<Vertex>> kvPair : adjList.entrySet())
            {
                heap.add(kvPair.getKey());
            }

        }
        catch (Exception ex)
        {
            throw ex;
        }

        return STATUS_CODE.SUCCESS;
    }

    /**
     * Generate the Adjacency List from the input file
     * @param fileName The path to the input file
     * @throws Exception
     */
    private static void GenerateAdjacencyList(String fileName) throws Exception
    {
        ArrayList<String> lines;
        Map<Vertex, List<Vertex>> adjacencyList = new HashMap<>();

        try
        {
            lines = ReadInputFile(fileName);
        }
        catch( Exception ex)
        {
            throw ex;
        }

        for( int i = 1; i < lines.size(); ++i)
        {
            String[] path = lines.get(i).split(" ");
            Vertex destination = new Vertex(path[1], Integer.parseInt(path[2]));
            Vertex source;
            if( path[0].compareToIgnoreCase("A") == 0)
            {
                source = new Vertex(path[0], 0);
            }
            else
            {
                source = new Vertex(path[0], Integer.MAX_VALUE);
            }

            if( !adjacencyList.containsKey(source))
            {
                ArrayList<Vertex> edgeList = new ArrayList<>();
                edgeList.add(destination);
                adjacencyList.put(source, edgeList);
            }
            else
            {
                adjacencyList.get(source).add(destination);
            }

            if( !distanceArray.containsKey(source))
            {
                distanceArray.put(source, source.weight);
            }

            if( !distanceArray.containsKey(destination))
            {
                distanceArray.put(destination, Integer.MAX_VALUE);
            }

        }

        if(DEBUG_MODE)
        {
            Print("ADJACENCY LIST");
            for( Map.Entry<Vertex, List<Vertex>> kvPair : adjacencyList.entrySet())
            {
                Print("Source Node Name: " + kvPair.getKey().name + " --> ");
                for (Vertex destination : kvPair.getValue())
                {
                    Print( "(" + destination.name + ", " + destination.weight + ")" );
                }
            }
        }

        adjList = adjacencyList;
    }

    private static void PrintShortestPath(Vertex vertex)
    {
        if( vertex.parent != null)
        {
            PrintShortestPath(vertex.parent);
        }
        System.out.print( vertex.name + " ");
        return;
    }

    /**
     * Read in the input file generate an adjacency list from it
     *
     * @param fileName The file path to read in
     * @return An ArrayList containing all the lines in the file
     * * @throws Exception
     */
    public static ArrayList<String> ReadInputFile(String fileName) throws Exception
    {
        ArrayList<String> lines = new ArrayList<>();
        // read through the input file setting each of the BlockRecord fields
        try
        {
            Scanner scanner = new Scanner(new File(fileName));

            while (scanner.hasNextLine())
            {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        }
        catch (FileNotFoundException ex)
        {
            throw new Exception( "Could not find input file.", ex);
        }
        catch (Exception ex)
        {
            throw new Exception( "Exception happened while reading input file.", ex);
        }

        return lines;
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
        System.out.println(warningStr);
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
        System.out.println(errorStr);
    }
}
