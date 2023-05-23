package model;

import exception.*;
import org.junit.Test;
import java.awt.Color;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdjacentListGraphTest {
    private AdjacentListGraph<City> cities;

    public void setUp1() {
        this.cities = new AdjacentListGraph<>(false, false, false);
    }

    public void setUp3() {
        cities = new AdjacentListGraph<>(false, true, true);
    }

    public void setUp4() {
        cities = new AdjacentListGraph<>(true, true, false);
    }

    @Test
    public void testInsertVertex() {
        setUp1();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        City c3 = new City("Medellin");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        City c6 = new City("Bucaramanga");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addVertex(c6);
            assertEquals(0, cities.getIndex(c1));
            assertEquals(1, cities.getIndex(c2));
            assertEquals(2, cities.getIndex(c3));
            assertEquals(3, cities.getIndex(c4));
            assertEquals(4, cities.getIndex(c5));
            assertEquals(5, cities.getIndex(c6));
            assertEquals(6, cities.getVertex().size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddRepeatedVertex() {
        setUp1();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        City c3 = new City("Medellin");
        City c4 = new City("Riohacha");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
        } catch (Exception e) {
            fail();
        }
        assertThrows(ExistenceVertexException.class, () -> cities.addVertex(c1));
        assertThrows(ExistenceVertexException.class, () -> cities.addVertex(c2));
        assertThrows(ExistenceVertexException.class, () -> cities.addVertex(c3));
        assertThrows(ExistenceVertexException.class, () -> cities.addVertex(c4));
    }

    @Test
    public void testAddingMultipleVertex() {
        setUp1();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        City c3 = new City("Medellin");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
        } catch (Exception e) {
            fail();
        }
        assertEquals(5, cities.getVertex().size());
    }

    @Test
    public void testAddEdgeInSimpleGraph() {
        setUp1();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addEdge(c1, c2, 0);
            assertThrows(MultipleEdgesNotAllowedException.class, () -> cities.addEdge(c2, c1, 0));
            assertThrows(LoopNotAllowedException.class, () -> cities.addEdge(c1, c1, 0));
            assertThrows(VertexNotFoundException.class, () -> cities.addEdge(new City("Medellin"), c1, 0));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddEdgeInPseudoGraph() {
        setUp3();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");

        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c2, c1, 2);
            cities.addEdge(c1, c1, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(c1, cities.getVertex().get(cities.getIndex(c1)).searchVertex(c1).getValue());
        assertNull(cities.getVertex().get(cities.getIndex(c2)).searchVertex(c2));
    }

    @Test
    public void testAddEdgeInDirectedGraph() {
        setUp4();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        City c3 = new City("Medellin");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        City c6 = new City("Bucaramanga");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addVertex(c6);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c4, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c1, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c3, c6, 0);
            cities.addEdge(c4, c5, 0);
        } catch (Exception e) {
            fail();
        }
        assertNull(cities.getVertex().get(cities.getIndex(c2)).searchVertex(c1));
        assertEquals(c1, cities.getVertex().get(cities.getIndex(c3)).searchVertex(c1).getValue());//3--1
        assertEquals(c2, cities.getVertex().get(cities.getIndex(c1)).searchVertex(c2).getValue());//1--2
        assertNull(cities.getVertex().get(cities.getIndex(c1)).searchVertex(c3));//1--3
        assertEquals(c3, cities.getVertex().get(cities.getIndex(c2)).searchVertex(c3).getValue());//2--3
        assertEquals(c4, cities.getVertex().get(cities.getIndex(c1)).searchVertex(c4).getValue());//1--4
        assertNull(cities.getVertex().get(cities.getIndex(c2)).searchVertex(c1));//1--5
        assertEquals(c6, cities.getVertex().get(cities.getIndex(c3)).searchVertex(c6).getValue());//3--6
        assertNull(cities.getVertex().get(cities.getIndex(c2)).searchVertex(c6));//2--6
    }

    @Test
    public void testDeleteVertexInDirectedGraph() {
        setUp4();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        City c3 = new City("Medellin");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c4, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c1, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 0);
            cities.deleteVertex(c1);
            cities.deleteVertex(c3);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(3, cities.getVertex().size());
        assertEquals(-1,(cities.getIndex(c1)));
        assertEquals(-1,(cities.getIndex(c3)));
    }

    @Test
    public void testDeleteVertexInPseudoGraph() {
        setUp3();
        City c1 = new City("Bogota");
        City c2 = new City("Cartagena");
        City c3 = new City("Santa Marta");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c4, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 0);
            cities.deleteVertex(c4);
            cities.deleteVertex(c2);
        } catch (Exception e) {
            fail();
        }
        assertThrows(VertexNotFoundException.class, () -> cities.deleteVertex(c4));
        assertEquals(3, cities.getVertex().size());
        assertEquals(-1,(cities.getIndex(c4)));
        assertEquals(-1,(cities.getIndex(c2)));
    }

    @Test
    public void testDeleteVertexInSimpleGraph() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.deleteVertex(c1);
        } catch (Exception e) {
            fail();
        }

        assertEquals(2, cities.getVertex().size());
        assertEquals(-1,(cities.getIndex(c1)));
        assertNull(cities.getVertex().get(cities.getIndex(c2)).searchVertex(c1));
    }

    @Test
    public void testDeleteEdgeInSimpleGraph() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.removeEdge(c1, c2, 0);
            cities.removeEdge(c1, c3, 0);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, cities.getVertex().size());
        assertEquals(0, cities.getVertex().get(cities.getIndex(c1)).getAdjacentList().size());//c1
        assertEquals(2, cities.getVertex().get(cities.getIndex(c2)).getAdjacentList().size());//c2
        assertNull(cities.getVertex().get(cities.getIndex(c1)).searchVertex(c2));//c1--c2
    }

    @Test
    public void testDeleteEdgeExceptions() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
        } catch (Exception e) {
            fail();
        }
        assertThrows(EdgeNotFoundException.class, () -> cities.removeEdge(c1, c4, 0));
        assertThrows(EdgeNotFoundException.class, () -> cities.removeEdge(c3, c4, 0));
        assertThrows(VertexNotFoundException.class, () -> cities.removeEdge(new City("Cali"), c2, 1));
    }

    @Test
    public void testDeleteEdgeInPseudoGraph() {
        setUp3();
        City c1 = new City("Bogota");
        City c2 = new City("Cartagena");
        City c3 = new City("Santa Marta");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c4, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 2);
            cities.removeEdge(c1, c2, 0);
            cities.removeEdge(c1, c4, 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(5, cities.getVertex().size());
        assertEquals(1, cities.getVertex().get(cities.getIndex(c1)).getAdjacentList().size());//c1
        assertEquals(2, cities.getVertex().get(cities.getIndex(c2)).getAdjacentList().size());//c2
        assertNull(cities.getVertex().get(cities.getIndex(c1)).searchVertex(c2));//c1--c2
        assertEquals(c1, cities.getVertex().get(cities.getIndex(c4)).searchVertex(c1).getValue());//c4--c1
    }

    @Test
    public void testBFSColor() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.BFS(c1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, cities.getVertex().size());
        assertEquals(Color.BLACK, cities.getVertex().get(cities.getIndex(c1)).getColor());//c1
        assertEquals(Color.BLACK, cities.getVertex().get(cities.getIndex(c2)).getColor());//c2
        assertEquals(Color.BLACK, cities.getVertex().get(cities.getIndex(c3)).getColor());//c3
        assertEquals(Color.BLACK, cities.getVertex().get(cities.getIndex(c4)).getColor());//c4
    }

    @Test
    public void testBFSParents() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.BFS(c1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, cities.getVertex().size());
        assertNull(cities.getVertex().get(cities.getIndex(c1)).getParent());//c1
        assertEquals(c1, cities.getVertex().get(cities.getIndex(c2)).getParent().getValue());//c2
        assertEquals(c1, cities.getVertex().get(cities.getIndex(c3)).getParent().getValue());//c3
        assertEquals(c2, cities.getVertex().get(cities.getIndex(c4)).getParent().getValue());//c4
    }

    @Test
    public void testBFSDistance() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Santa Marta");
        City c4 = new City("Riohacha");
        City c5 = new City("Valledupar");
        City c6 = new City("Bucaramanga");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addVertex(c6);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 2);
            cities.addEdge(c4, c6, 0);
            cities.addEdge(c5, c6, 0);
            cities.BFS(c1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(6, cities.getVertex().size());
        assertEquals(0, cities.getVertex().get(cities.getIndex(c1)).getDistance());//c1
        assertEquals(1, cities.getVertex().get(cities.getIndex(c2)).getDistance());//c2
        assertEquals(1, cities.getVertex().get(cities.getIndex(c3)).getDistance());//c3
        assertEquals(1, cities.getVertex().get(cities.getIndex(c4)).getDistance());//c4
        assertEquals(2, cities.getVertex().get(cities.getIndex(c5)).getDistance());//c5
        assertEquals(2, cities.getVertex().get(cities.getIndex(c6)).getDistance());//c6
    }

    @Test
    public void testDFSTime() {
        setUp1();
        City c1 = new City("New York");
        City c2 = new City("Los Angeles");
        City c3 = new City("Chicago");
        City c4 = new City("Houston");
        City c5 = new City("Phoenix");
        City c6 = new City("Philadelphia");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addVertex(c6);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 2);
            cities.addEdge(c4, c6, 0);
            cities.addEdge(c5, c6, 0);
            cities.DFS();
        } catch (Exception e) {
            fail();
        }
        assertEquals(6, cities.getVertex().size());
        assertEquals(12, cities.getVertex().get(cities.getIndex(c1)).getTime());
        assertEquals(11, cities.getVertex().get(cities.getIndex(c2)).getTime());
        assertEquals(10, cities.getVertex().get(cities.getIndex(c3)).getTime());
        assertEquals(9, cities.getVertex().get(cities.getIndex(c4)).getTime());
        assertEquals(7, cities.getVertex().get(cities.getIndex(c5)).getTime());
        assertEquals(8, cities.getVertex().get(cities.getIndex(c6)).getTime());
    }

    @Test
    public void testDFSDistance() {
        setUp1();
        City c1 = new City("Tokyo");
        City c2 = new City("Shanghai");
        City c3 = new City("Seoul");
        City c4 = new City("Beijing");
        City c5 = new City("Hong Kong");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 2);
            cities.addEdge(c4, c5, 0);
            cities.DFS();
        } catch (Exception e) {
            fail();
        }
        assertEquals(5, cities.getVertex().size());
        assertEquals(1, cities.getVertex().get(cities.getIndex(c1)).getDistance());//c1---c5
        assertEquals(2, cities.getVertex().get(cities.getIndex(c2)).getDistance());
        assertEquals(3, cities.getVertex().get(cities.getIndex(c3)).getDistance());
        assertEquals(4, cities.getVertex().get(cities.getIndex(c4)).getDistance());
        assertEquals(5, cities.getVertex().get(cities.getIndex(c5)).getDistance());
    }

    @Test
    public void testDFSParents() {
        setUp1();
        //ciudades de europa
        City c1 = new City("London");
        City c2 = new City("Paris");
        City c3 = new City("Rome");
        City c4 = new City("Berlin");
        City c5 = new City("Madrid");
        City c6 = new City("Moscow");
        City c7 = new City("Athens");
        try {
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addVertex(c5);
            cities.addVertex(c6);
            cities.addVertex(c7);
            cities.addEdge(c1, c2, 0);
            cities.addEdge(c1, c3, 0);
            cities.addEdge(c2, c3, 0);
            cities.addEdge(c2, c4, 0);
            cities.addEdge(c3, c4, 0);
            cities.addEdge(c3, c5, 0);
            cities.addEdge(c4, c1, 2);
            cities.addEdge(c4, c6, 0);
            cities.addEdge(c5, c6, 0);
            cities.addEdge(c6, c7, 0);
            cities.DFS();
        } catch (Exception e) {
            fail();
        }
        assertEquals(7, cities.getVertex().size());
        assertNull(cities.getVertex().get(cities.getIndex(c1)).getParent());
        assertEquals(cities.getVertex().get(cities.getIndex(c1)), cities.getVertex().get(cities.getIndex(c2)).getParent());//1---2
        assertEquals(cities.getVertex().get(cities.getIndex(c2)), cities.getVertex().get(cities.getIndex(c3)).getParent());//2---3
        assertEquals(cities.getVertex().get(cities.getIndex(c3)), cities.getVertex().get(cities.getIndex(c4)).getParent());//3---4
        assertEquals(cities.getVertex().get(cities.getIndex(c6)), cities.getVertex().get(cities.getIndex(c5)).getParent());//6---5
        assertEquals(cities.getVertex().get(cities.getIndex(c4)), cities.getVertex().get(cities.getIndex(c6)).getParent());//4---6
        assertEquals(cities.getVertex().get(cities.getIndex(c6)), cities.getVertex().get(cities.getIndex(c7)).getParent());//6---7
    }

    @Test
    public void testFloydWarshall(){
        setUp1();
        City c1 = new City("A");
        City c2 = new City("B");
        City c3 = new City("C");
        City c4 = new City("D");

        try{
            cities.addVertex(c1);
            cities.addVertex(c2);
            cities.addVertex(c3);
            cities.addVertex(c4);
            cities.addEdge(c1, c2, 1);
            cities.addEdge(c1, c3, 5);
            cities.addEdge(c2, c3, 2);
            cities.addEdge(c2, c4, 2);
            cities.addEdge(c3, c4, 1);
        }catch(Exception e){
            fail();
        }

        Pair<int[][], GenericMatrix<City>> floyd = cities.floydWarshall();
        int[][] matrix = floyd.getValue1();
        GenericMatrix<City> path = floyd.getValue2();
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
        for(ArrayList<City> a : path.getMatrix()){
            for(City c : a){
                System.out.print(c.getName() + " ");
            }
            System.out.println();
        }


        assertEquals(4, cities.getVertex().size());
    }
}