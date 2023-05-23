package model;

import exception.*;
import org.junit.Test;
import java.awt.Color;


import static org.junit.Assert.*;

public class AdjacentMatrixGraphTest {
    private AdjacentMatrixGraph<City> graph;

    public void setUp1(){graph = new AdjacentMatrixGraph<>(false,false,false);}

    @Test
    public void insertVertex(){
        setUp1();
        City city = new City("Bogota");
        try {
            graph.addVertex(city);
            assertEquals(city,graph.getVertex().get(0).getValue());
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void testDeleteVertex(){
        setUp1();
        City city = new City("Bogota");
        City city2 = new City("Medellin");

        try{
            graph.addVertex(city);
            graph.addVertex(city2);
            graph.deleteVertex(city);
            assertEquals(city2,graph.getVertex().get(0).getValue());
            assertEquals(-1,graph.getIndex(city));
            assertNull(graph.getAdjacentMatrix().get(0,0));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAddEdge(){
        setUp1();
        City city = new City("Bogota");
        City city2 = new City("Medellin");
        City city3 = new City("Cali");
        try{
            graph.addVertex(city);
            graph.addVertex(city2);
            graph.addVertex(city3);
            graph.addEdge(city,city2,10);
            assertEquals(10,(int)graph.getWeightMatrix().get(0,1));
            assertEquals(10,(int)graph.getWeightMatrix().get(1,0));
            assertEquals(1,(int)graph.getAdjacentMatrix().get(0,1));
            assertEquals(1,(int)graph.getAdjacentMatrix().get(1,0));
            graph.removeEdge(city,city2,10);
            assertNull(graph.getAdjacentMatrix().get(0,1));
            assertNull(graph.getAdjacentMatrix().get(1,0));
            assertNull(graph.getWeightMatrix().get(0,1));
            assertNull(graph.getWeightMatrix().get(1,0));
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
