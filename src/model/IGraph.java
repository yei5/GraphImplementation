package model;

import exception.EdgeNotFoundException;
import exception.ExistenceVertexException;
import exception.VertexNotFoundException;

import java.util.ArrayList;

public interface IGraph<V> {
    void addVertex(V value) throws ExistenceVertexException;

    void deleteVertex(V value) throws VertexNotFoundException;

    void addEdge(V source, V destination, int weight) throws Exception;

    void removeEdge(V source, V destination, int weight) throws VertexNotFoundException, EdgeNotFoundException;

    void BFS(V value) throws VertexNotFoundException;

    void DFS();

    void DfsVisit(Vertex<V> u);

    Pair<ArrayList<Vertex<V>>, ArrayList<Integer>> dijkstra(V source) throws VertexNotFoundException;

    Pair<int[][], GenericMatrix<V>> floydWarshall();

    Pair<ArrayList<Vertex<V>>, ArrayList<Integer>> prim();

    ArrayList<Pair<Pair<AdjacentListVertex<V>,AdjacentListVertex<V>>,Integer>> kruskal();

}
