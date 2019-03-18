package com.depaul.edu.cs421.homework4;

import java.util.Comparator;

public class Vertex implements Comparator<Vertex>
{
    int weight;
    String name;
    Vertex parent;

    public Vertex(){}

    public Vertex( String name)
    {
        this.name = name;
    }

    public Vertex( String name, int weight)
    {
        this.name = name;
        this.weight = weight;
    }

    public void SetParent(Vertex parent)
    {
        this.parent = parent;
    }

    public Vertex GetParent()
    {
        return this.parent;
    }

    @Override
    public int compare (Vertex v1, Vertex v2)
    {
        if( v1.weight < v2.weight)
        {
            return -1;
        }
        if( v1.weight > v2.weight)
        {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if( o == null )
        {
            return false ;
        }

        if( o == this )
        {
            return true;
        }
        Vertex v2 = (Vertex)o;
        return this.name.compareToIgnoreCase(v2.name) == 0;
    }

    @Override
    public int hashCode()
    {
        return Character.getNumericValue(this.name.charAt(0));
    }
}
