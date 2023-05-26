package model;

import exception.*;

import java.awt.*;
import java.util.Queue;
import java.util.*;

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
        int index = getIndex(value);
        if (index == -1) throw new VertexNotFoundException("Vertex not found");
        for (Vertex<V> vVertex : vertex) {
            vVertex.setColor(Color.WHITE);
            vVertex.setDistance(Integer.MAX_VALUE);
            vVertex.setParent(null);
        }

        Vertex<V> s = vertex.get(index);
        s.setColor(Color.GRAY);
        s.setDistance(0);
        s.setParent(null);
        Queue<Vertex<V>> queue = new LinkedList<>();
        queue.offer(s);
        while (!queue.isEmpty()) {
            Vertex<V> u = queue.poll();
            int uIndex = getIndex(u.getValue());
            for (int i = 0; i < vertex.size(); i++) {
                if (adjacentMatrix.get(uIndex, i) != null) {
                    Vertex<V> v = vertex.get(i);
                    if (v.getColor() == Color.WHITE) {
                        v.setColor(Color.GRAY);
                        v.setDistance(u.getDistance() + 1);
                        v.setParent(u);
                        queue.offer(v);
                    }
                }
            }
            u.setColor(Color.BLACK);
        }
    }

    @Override
    public void DFS() {
        for (Vertex<V> u : vertex) {
            u.setColor(Color.WHITE);
            u.setParent(null);
        }
        time = 0;
        for (Vertex<V> u : vertex) {
            if (u.getColor() == Color.WHITE) {
                DfsVisit(u);
            }
        }
    }

    @Override
    public void DfsVisit(Vertex<V> u) {
        time += 1;
        u.setDistance(time);
        u.setColor(Color.GRAY);
        int uIndex = getIndex(u.getValue());
        for (int i = 0; i < vertex.size(); i++) {
            if (adjacentMatrix.get(uIndex, i) != null) {
                Vertex<V> v = vertex.get(i);
                if (v.getColor() == Color.WHITE) {
                    v.setParent(u);
                    DfsVisit(v);
                }
            }
        }
        u.setColor(Color.BLACK);
        time += 1;
        u.setTime(time);
    }

    @Override
    public Pair<ArrayList<Vertex<V>>, ArrayList<Integer>> dijkstra(V source) throws VertexNotFoundException {
        int sourceIndex = getIndex(source);
        if (sourceIndex == -1) throw new VertexNotFoundException("Vertex not found");
        ArrayList<Vertex<V>> previous = new ArrayList<>(Collections.nCopies(vertex.size(), null));
        ArrayList<Integer> distances = new ArrayList<>(Collections.nCopies(vertex.size(), Integer.MAX_VALUE));
        distances.set(sourceIndex, 0);
        AdjacentListVertex<V> sourceVertex = (AdjacentListVertex<V>) vertex.get(sourceIndex);
        PriorityQueue<Vertex<V>> queue = new PriorityQueue<>(Comparator.comparingInt(v -> distances.get(getIndex(v.getValue()))));
        queue.addAll(vertex);
        while (!queue.isEmpty()) {
            Vertex<V> u = queue.poll();
            int uIndex = getIndex(u.getValue());
            for (int i = 0; i < vertex.size(); i++) {
                if (adjacentMatrix.get(uIndex, i) != null) {
                    Vertex<V> v = vertex.get(i);
                    int weight = weightMatrix.get(uIndex, i);
                    if (distances.get(uIndex) + weight < distances.get(i)) {
                        distances.set(i, distances.get(uIndex) + weight);
                        previous.set(i, u);
                        queue.add(v);
                    }
                }
            }
        }
        return new Pair<>(previous, distances);
    }

    @Override
    public Pair<int[][], GenericMatrix<V>> floydWarshall() {
        int[][] distances = new int[vertex.size()][vertex.size()];
        GenericMatrix<V> parents = new GenericMatrix<>(vertex.size(), vertex.size());

        for (int i = 0; i < vertex.size(); i++) {
            for (int j = 0; j < vertex.size(); j++) {
                distances[i][j] = Integer.MAX_VALUE;
                parents.set(i, j, null);
            }
            distances[i][i] = 0;
            parents.set(i, i, vertex.get(i).getValue());
        }

        for (int i = 0; i < vertex.size(); i++) {
            for (int j = 0; j < vertex.size(); j++) {
                if (adjacentMatrix.get(i, j) != null) {
                    distances[i][j] = weightMatrix.get(i, j);
                    parents.set(i, j, vertex.get(i).getValue());
                }
            }
        }
        for (int k = 0; k < vertex.size(); k++) {
            for (int i = 0; i < vertex.size(); i++) {
                for (int j = 0; j < vertex.size(); j++) {
                    int distance = (distances[i][k] == Integer.MAX_VALUE || distances[k][j] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : distances[i][k] + distances[k][j];
                    if (distances[i][j] > distance) {
                        distances[i][j] = distance;
                        parents.set(i, j, parents.get(k, j));
                    }
                }
            }
        }

        return new Pair<>(distances, parents);
    }

    @Override
    public Pair<ArrayList<Vertex<V>>, ArrayList<Integer>> prim() {
        return null;
    }

    @Override
    public ArrayList<Pair<AdjacentListVertex<V>, Integer>> kruskal() {
        return null;
    }

    public GenericMatrix<Integer> getAdjacentMatrix() {
        return adjacentMatrix;
    }

    public GenericMatrix<Integer> getWeightMatrix() {
        return weightMatrix;
    }

    public ArrayList<Vertex<V>> getVertex() {
        return vertex;
    }
}
