package model;

import exception.*;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class AdjacentMatrixGraphTest {
    private AdjacentMatrixGraph<City> graph;

    public void setUp1() {
        this.graph = new AdjacentMatrixGraph<>(false, false, false);
    }

    public void setUp3() {
        graph = new AdjacentMatrixGraph<>(false, true, true);
    }

    public void setUp4() {
        graph = new AdjacentMatrixGraph<>(true, true, false);
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addVertex(c6);
            assertEquals(0, graph.getIndex(c1));
            assertEquals(1, graph.getIndex(c2));
            assertEquals(2, graph.getIndex(c3));
            assertEquals(3, graph.getIndex(c4));
            assertEquals(4, graph.getIndex(c5));
            assertEquals(5, graph.getIndex(c6));
            assertEquals(6, graph.getVertex().size());
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
        } catch (Exception e) {
            fail();
        }
        assertThrows(ExistenceVertexException.class, () -> graph.addVertex(c1));
        assertThrows(ExistenceVertexException.class, () -> graph.addVertex(c2));
        assertThrows(ExistenceVertexException.class, () -> graph.addVertex(c3));
        assertThrows(ExistenceVertexException.class, () -> graph.addVertex(c4));
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
        } catch (Exception e) {
            fail();
        }
        assertEquals(5, graph.getVertex().size());
    }

    @Test
    public void testAddEdgeInSimpleGraph() {
        setUp1();
        City c1 = new City("Bogota");
        City c2 = new City("Cali");
        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addEdge(c1, c2, 0);
            assertThrows(MultipleEdgesNotAllowedException.class, () -> graph.addEdge(c2, c1, 0));
            assertThrows(LoopNotAllowedException.class, () -> graph.addEdge(c1, c1, 0));
            assertThrows(VertexNotFoundException.class, () -> graph.addEdge(new City("Medellin"), c1, 0));
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c2, c1, 2);
            graph.addEdge(c1, c1, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, (int) graph.getAdjacentMatrix().get(0, 1));
        assertNull(graph.getAdjacentMatrix().get(1, 1));
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addVertex(c6);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c4, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c1, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c3, c6, 0);
            graph.addEdge(c4, c5, 0);
        } catch (Exception e) {
            fail();
        }
        assertNull(graph.getAdjacentMatrix().get(1, 0));//c2--c1
        assertEquals(1, (int) graph.getAdjacentMatrix().get(2, 0));//3--1
        assertEquals(1, (int) graph.getAdjacentMatrix().get(0, 1));//1--2
        assertNull(graph.getAdjacentMatrix().get(0, 2));//1--3
        assertEquals(1, (int) graph.getAdjacentMatrix().get(1, 2));//2--3
        assertEquals(1, (int) graph.getAdjacentMatrix().get(0, 3));//1--4
        assertNull(graph.getAdjacentMatrix().get(0, 4));//1--5
        assertEquals(1, (int) graph.getAdjacentMatrix().get(2, 5));//3--6
        assertNull(graph.getAdjacentMatrix().get(1, 5));//2--6
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c4, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c1, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c4, c1, 0);
            graph.deleteVertex(c1);
            graph.deleteVertex(c3);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(3, graph.getVertex().size());
        assertEquals(-1, (graph.getIndex(c1)));
        assertEquals(-1, (graph.getIndex(c3)));
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c4, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c4, c1, 0);
            graph.deleteVertex(c4);
            graph.deleteVertex(c2);
        } catch (Exception e) {
            fail();
        }
        assertThrows(VertexNotFoundException.class, () -> graph.deleteVertex(c4));
        assertEquals(3, graph.getVertex().size());
        assertEquals(-1, (graph.getIndex(c4)));
        assertEquals(-1, (graph.getIndex(c2)));
    }

    @Test
    public void testDeleteVertexInSimpleGraph() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.deleteVertex(c1);
        } catch (Exception e) {
            fail();
        }

        assertEquals(2, graph.getVertex().size());
        assertEquals(-1, (graph.getIndex(c1)));
        assertNull(graph.getAdjacentMatrix().get(1, 0));//c2--c1
    }

    @Test
    public void testDeleteEdgeInSimpleGraph() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.removeEdge(c1, c2, 0);
            graph.removeEdge(c1, c3, 0);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, graph.getVertex().size());
        assertNull(graph.getAdjacentMatrix().get(0, 1));//c1
        assertNull(graph.getAdjacentMatrix().get(0, 2));//c2
        assertNull(graph.getAdjacentMatrix().get(0, 1));//c1--c2
    }

    @Test
    public void testDeleteEdgeExceptions() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
        } catch (Exception e) {
            fail();
        }
        assertThrows(EdgeNotFoundException.class, () -> graph.removeEdge(c1, c4, 0));
        assertThrows(EdgeNotFoundException.class, () -> graph.removeEdge(c3, c4, 0));
        assertThrows(VertexNotFoundException.class, () -> graph.removeEdge(new City("Cali"), c2, 1));
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c4, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.removeEdge(c1, c2, 0);
            graph.removeEdge(c1, c4, 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(5, graph.getVertex().size());
        assertEquals(1, (int) graph.getAdjacentMatrix().get(3, 1));//c1
        assertNull(graph.getAdjacentMatrix().get(0, 3));//c2
        assertNull(graph.getAdjacentMatrix().get(0, 1));//c1--c2
        assertNull(graph.getAdjacentMatrix().get(3, 0));//c4--c1
    }

    @Test
    public void testBFSColor() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.BFS(c1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, graph.getVertex().size());
        assertEquals(Color.BLACK, graph.getVertex().get(graph.getIndex(c1)).getColor());//c1
        assertEquals(Color.BLACK, graph.getVertex().get(graph.getIndex(c2)).getColor());//c2
        assertEquals(Color.BLACK, graph.getVertex().get(graph.getIndex(c3)).getColor());//c3
        assertEquals(Color.BLACK, graph.getVertex().get(graph.getIndex(c4)).getColor());//c4
    }

    @Test
    public void testBFSParents() {
        setUp1();
        City c1 = new City("Barranquilla");
        City c2 = new City("Cartagena");
        City c3 = new City("Bogota");
        City c4 = new City("Santa Marta");
        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.BFS(c1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(4, graph.getVertex().size());
        assertNull(graph.getVertex().get(graph.getIndex(c1)).getParent());//c1
        assertEquals(c1, graph.getVertex().get(graph.getIndex(c2)).getParent().getValue());//c2
        assertEquals(c1, graph.getVertex().get(graph.getIndex(c3)).getParent().getValue());//c3
        assertEquals(c2, graph.getVertex().get(graph.getIndex(c4)).getParent().getValue());//c4
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addVertex(c6);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c4, c1, 2);
            graph.addEdge(c4, c6, 0);
            graph.addEdge(c5, c6, 0);
            graph.BFS(c1);
        } catch (Exception e) {
            fail();
        }
        assertEquals(6, graph.getVertex().size());
        assertEquals(0, graph.getVertex().get(graph.getIndex(c1)).getDistance());//c1
        assertEquals(1, graph.getVertex().get(graph.getIndex(c2)).getDistance());//c2
        assertEquals(1, graph.getVertex().get(graph.getIndex(c3)).getDistance());//c3
        assertEquals(1, graph.getVertex().get(graph.getIndex(c4)).getDistance());//c4
        assertEquals(2, graph.getVertex().get(graph.getIndex(c5)).getDistance());//c5
        assertEquals(2, graph.getVertex().get(graph.getIndex(c6)).getDistance());//c6
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addVertex(c6);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c4, c1, 2);
            graph.addEdge(c4, c6, 0);
            graph.addEdge(c5, c6, 0);
            graph.DFS();
        } catch (Exception e) {
            fail();
        }
        assertEquals(6, graph.getVertex().size());
        assertEquals(12, graph.getVertex().get(graph.getIndex(c1)).getTime());
        assertEquals(11, graph.getVertex().get(graph.getIndex(c2)).getTime());
        assertEquals(10, graph.getVertex().get(graph.getIndex(c3)).getTime());
        assertEquals(9, graph.getVertex().get(graph.getIndex(c4)).getTime());
        assertEquals(7, graph.getVertex().get(graph.getIndex(c5)).getTime());
        assertEquals(8, graph.getVertex().get(graph.getIndex(c6)).getTime());
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c4, c1, 2);
            graph.addEdge(c4, c5, 0);
            graph.DFS();
        } catch (Exception e) {
            fail();
        }
        assertEquals(5, graph.getVertex().size());
        assertEquals(1, graph.getVertex().get(graph.getIndex(c1)).getDistance());//c1---c5
        assertEquals(2, graph.getVertex().get(graph.getIndex(c2)).getDistance());
        assertEquals(3, graph.getVertex().get(graph.getIndex(c3)).getDistance());
        assertEquals(4, graph.getVertex().get(graph.getIndex(c4)).getDistance());
        assertEquals(5, graph.getVertex().get(graph.getIndex(c5)).getDistance());
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
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addVertex(c5);
            graph.addVertex(c6);
            graph.addVertex(c7);
            graph.addEdge(c1, c2, 0);
            graph.addEdge(c1, c3, 0);
            graph.addEdge(c2, c3, 0);
            graph.addEdge(c2, c4, 0);
            graph.addEdge(c3, c4, 0);
            graph.addEdge(c3, c5, 0);
            graph.addEdge(c4, c1, 2);
            graph.addEdge(c4, c6, 0);
            graph.addEdge(c5, c6, 0);
            graph.addEdge(c6, c7, 0);
            graph.DFS();
        } catch (Exception e) {
            fail();
        }
        assertEquals(7, graph.getVertex().size());
        assertNull(graph.getVertex().get(graph.getIndex(c1)).getParent());
        assertEquals(graph.getVertex().get(graph.getIndex(c1)), graph.getVertex().get(graph.getIndex(c2)).getParent());//1---2
        assertEquals(graph.getVertex().get(graph.getIndex(c2)), graph.getVertex().get(graph.getIndex(c3)).getParent());//2---3
        assertEquals(graph.getVertex().get(graph.getIndex(c3)), graph.getVertex().get(graph.getIndex(c4)).getParent());//3---4
        assertEquals(graph.getVertex().get(graph.getIndex(c6)), graph.getVertex().get(graph.getIndex(c5)).getParent());//6---5
        assertEquals(graph.getVertex().get(graph.getIndex(c4)), graph.getVertex().get(graph.getIndex(c6)).getParent());//4---6
        assertEquals(graph.getVertex().get(graph.getIndex(c6)), graph.getVertex().get(graph.getIndex(c7)).getParent());//6---7
    }

    @Test
    public void testFloydWarshall() {
        setUp1();
        City c1 = new City("A");
        City c2 = new City("B");
        City c3 = new City("C");
        City c4 = new City("D");

        try {
            graph.addVertex(c1);
            graph.addVertex(c2);
            graph.addVertex(c3);
            graph.addVertex(c4);
            graph.addEdge(c1, c2, 1);
            graph.addEdge(c1, c3, 5);
            graph.addEdge(c2, c3, 2);
            graph.addEdge(c2, c4, 2);
            graph.addEdge(c3, c4, 1);
        } catch (Exception e) {
            fail();
        }

        Pair<int[][], GenericMatrix<City>> floyd = graph.floydWarshall();
        int[][] distances = floyd.getValue1();
        GenericMatrix<City> path = floyd.getValue2();
        assertEquals(0,distances[0][0]);
        assertEquals(1,distances[0][1]);
        assertEquals(3,distances[0][2]);
        assertEquals(3,distances[0][3]);
        assertEquals(1,distances[1][0]);
        assertEquals(0,distances[1][1]);
        assertEquals(2,distances[1][2]);
        assertEquals(2,distances[1][3]);
        assertEquals(3,distances[2][0]);
        assertEquals(2,distances[2][1]);
        assertEquals(0,distances[2][2]);
        assertEquals(1,distances[2][3]);
        assertEquals(3,distances[3][0]);
        assertEquals(2,distances[3][1]);
        assertEquals(1,distances[3][2]);
        assertEquals(0,distances[3][3]);
        assertEquals(c1,path.get(0,0));
        assertEquals(c1,path.get(0,1));
        assertEquals(c2,path.get(0,2));
        assertEquals(c2,path.get(0,3));
        assertEquals(c2,path.get(1,0));
        assertEquals(c2,path.get(1,1));
        assertEquals(c2,path.get(1,2));
        assertEquals(c2,path.get(1,3));
        assertEquals(c2,path.get(2,0));
        assertEquals(c3,path.get(2,1));
        assertEquals(c3,path.get(2,2));
        assertEquals(c3,path.get(2,3));
        assertEquals(c2,path.get(3,0));
        assertEquals(c4,path.get(3,1));
        assertEquals(c4,path.get(3,2));
        assertEquals(c4,path.get(3,3));
    }

}
