package model;

import exception.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class AdjacentMatrixGraph<V> implements IGraph<V> {


    private GenericMatrix<Integer> adjacentMatrix;
    private GenericMatrix<Integer> weightMatrix;
    private ArrayList<Vertex<V>> vertex;
    private LinkedHashMap<V, Integer> index;
    private boolean isDirected;
    private boolean loops;
    private boolean multipleEdges;
    private int time;

    public AdjacentMatrixGraph(boolean directed, boolean loops, boolean multipleEdges) {
        isDirected = directed;
        this.loops = loops;
        this.multipleEdges = multipleEdges;
        vertex = new ArrayList<>();
        index = new LinkedHashMap<>();
        adjacentMatrix = new GenericMatrix<>();
        weightMatrix = new GenericMatrix<>();
    }

    public int getIndex(V value) {
        return index.getOrDefault(value, -1);
    }


    @Override
    public void addVertex(V value) throws ExistenceVertexException {
        if (getIndex(value) != -1) throw new ExistenceVertexException("Vertex already exists");
        vertex.add(new Vertex<>(value));
        index.put(value, vertex.size() - 1);
        updateMatSize();
    }

    private void updateMatSize() {
        int size = vertex.size();
        adjacentMatrix.setSize(size, size);
        weightMatrix.setSize(size, size);
    }

    public void deleteVertex(V value) throws VertexNotFoundException {
        int index = getIndex(value);
        if (index == -1) {
            throw new VertexNotFoundException("Vertex not found");
        }
        int lastIndex = vertex.size() - 1;
        Vertex<V> lastVertex = vertex.get(lastIndex);
        Collections.swap(vertex, index, lastIndex);
        this.index.put(lastVertex.getValue(), index);
        for (int i = 0; i < vertex.size(); i++) {
            adjacentMatrix.set(index, i, null);
            adjacentMatrix.set(i, index, null);
            weightMatrix.set(index, i, null);
            weightMatrix.set(i, index, null);
        }

        // Eliminar el vértice de la lista y el índice
        vertex.remove(lastIndex);
        this.index.remove(value);
        updateMatSize();
    }


    @Override
    public void addEdge(V source, V destination, int weight) throws Exception {
        int sourceIndex = getIndex(source);
        int destinationIndex = getIndex(destination);
        if (sourceIndex == -1 || destinationIndex == -1) throw new VertexNotFoundException("Vertex not found");
        if (adjacentMatrix.get(sourceIndex, destinationIndex) != null && !multipleEdges)
            throw new MultipleEdgesNotAllowedException("Multiple edges not allowed");
        if (sourceIndex == destinationIndex && !loops) throw new LoopNotAllowedException("Loops not allowed");
        adjacentMatrix.set(sourceIndex, destinationIndex, 1);
        weightMatrix.set(sourceIndex, destinationIndex, weight);
        if (!isDirected) {
            adjacentMatrix.set(destinationIndex, sourceIndex, 1);
            weightMatrix.set(destinationIndex, sourceIndex, weight);
        }
    }

    @Override
    public void removeEdge(V source, V destination, int weight) throws VertexNotFoundException, EdgeNotFoundException {
        int sourceIndex = getIndex(source);
        int destinationIndex = getIndex(destination);
        if (sourceIndex == -1 || destinationIndex == -1) throw new VertexNotFoundException("Vertex not found");
        if (adjacentMatrix.get(sourceIndex, destinationIndex) == null)
            throw new EdgeNotFoundException("Edge not found");
        adjacentMatrix.set(sourceIndex, destinationIndex, null);
        weightMatrix.set(sourceIndex, destinationIndex, null);
        if (!isDirected) {
            adjacentMatrix.set(destinationIndex, sourceIndex, null);
            weightMatrix.set(destinationIndex, sourceIndex, null);
        }
    }

    @Override
    public void BFS(V value) throws VertexNotFoundException {

    }

    @Override
    public void DFS() {

    }

    @Override
    public void visit(Vertex<V> u) {

    }

    @Override
    public Pair<ArrayList<AdjacentListVertex<V>>, ArrayList<Integer>> dijkstra(V source) throws VertexNotFoundException {
        return null;
    }

    @Override
    public Pair<int[][], GenericMatrix<V>> floydWarshall() {
        return null;
    }

    public GenericMatrix<Integer> getAdjacentMatrix() {
        return adjacentMatrix;
    }

    public void setAdjacentMatrix(GenericMatrix<Integer> adjacentMatrix) {
        this.adjacentMatrix = adjacentMatrix;
    }

    public GenericMatrix<Integer> getWeightMatrix() {
        return weightMatrix;
    }

    public void setWeightMatrix(GenericMatrix<Integer> weightMatrix) {
        this.weightMatrix = weightMatrix;
    }

    public ArrayList<Vertex<V>> getVertex() {
        return vertex;
    }

    public void setVertex(ArrayList<Vertex<V>> vertex) {
        this.vertex = vertex;
    }

    public LinkedHashMap<V, Integer> getIndex() {
        return index;
    }

    public void setIndex(LinkedHashMap<V, Integer> index) {
        this.index = index;
    }
}
