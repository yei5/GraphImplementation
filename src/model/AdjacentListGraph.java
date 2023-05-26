package model;

import exception.*;

import java.awt.*;
import java.util.Queue;
import java.util.*;
public class AdjacentListGraph<V> implements IGraph<V> {
    private ArrayList<AdjacentListVertex<V>> vertex;
    private LinkedHashMap<V, Integer> index;
    private boolean isDirected;
    private boolean loops;
    private boolean multipleEdges;
    private int time;

    public AdjacentListGraph(boolean directed, boolean loops, boolean multipleEdges) {
        isDirected = directed;
        this.loops = loops;
        this.multipleEdges = multipleEdges;
        vertex = new ArrayList<>();
        index = new LinkedHashMap<>();
    }

    public int getIndex(V value) {
        return index.getOrDefault(value, -1);
    }

    @Override
    public void addVertex(V value) throws ExistenceVertexException {
        if (getIndex(value) != -1) throw new ExistenceVertexException("Vertex already exists");
        vertex.add(new AdjacentListVertex<>(value));
        index.put(value, vertex.size() - 1);
    }

    @Override
    public void deleteVertex(V value) throws VertexNotFoundException {
        int index = getIndex(value);
        if (index == -1) {
            throw new VertexNotFoundException("Vertex not found");
        }
        for (AdjacentListVertex<V> v : vertex) {
            v.delete(value);
        }
        vertex.remove(index);
        this.index.remove(value);
    }

    @Override
    public void addEdge(V source, V destination, int weight) throws VertexNotFoundException, MultipleEdgesNotAllowedException, LoopNotAllowedException {
        int sourceIndex = getIndex(source);
        int destinationIndex = getIndex(destination);
        if (sourceIndex == -1 || destinationIndex == -1) {
            throw new VertexNotFoundException("Vertex not found");
        }
        AdjacentListVertex<V> sourceVertex = vertex.get(sourceIndex);
        AdjacentListVertex<V> destinationVertex = vertex.get(destinationIndex);
        if (sourceVertex == destinationVertex && !loops) {
            throw new LoopNotAllowedException("Loops not allowed");
        }
        if (sourceVertex.searchVertex(destination) != null && !multipleEdges) {
            throw new MultipleEdgesNotAllowedException("Multiple edges not allowed");
        }
        sourceVertex.addEdge(destinationVertex, weight);
        if (!isDirected) {
            destinationVertex.addEdge(sourceVertex, weight);
        }
    }

    @Override
    public void removeEdge(V source, V destination, int weight) throws VertexNotFoundException, EdgeNotFoundException {
        int sourceIndex = getIndex(source);
        int destinationIndex = getIndex(destination);
        if (sourceIndex == -1 || destinationIndex == -1) {
            throw new VertexNotFoundException("Vertex not found");
        }
        AdjacentListVertex<V> sourceVertex = vertex.get(sourceIndex);
        AdjacentListVertex<V> destinationVertex = vertex.get(destinationIndex);
        if (sourceVertex.searchVertex(destination) == null) {
            throw new EdgeNotFoundException("Edge not found");
        }
        sourceVertex.deleteEdge(destination, weight);
        if (!isDirected) {
            destinationVertex.deleteEdge(source, weight);
        }
    }

    @Override
    public void BFS(V value) throws VertexNotFoundException {
        int startIndex = getIndex(value);
        if (startIndex == -1) {
            throw new VertexNotFoundException("Vertex not found");
        }

        for (AdjacentListVertex<V> v : vertex) {
            if (getIndex(v.getValue()) != startIndex) {
                v.setColor(Color.WHITE);
                v.setDistance(Integer.MAX_VALUE);
                v.setParent(null);
            }
        }

        AdjacentListVertex<V> startVertex = vertex.get(startIndex);
        startVertex.setColor(Color.GRAY);
        startVertex.setDistance(0);
        startVertex.setParent(null);

        Queue<AdjacentListVertex<V>> queue = new LinkedList<>();
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            AdjacentListVertex<V> u = queue.poll();
            for (Pair<AdjacentListVertex<V>, Integer> p : u.getAdjacentList()) {
                AdjacentListVertex<V> v = p.getValue1();
                if (v.getColor() == Color.WHITE) {
                    v.setColor(Color.GRAY);
                    v.setDistance(u.getDistance() + 1);
                    v.setParent(u);
                    queue.offer(v);
                }
            }
            u.setColor(Color.BLACK);
        }
    }

    @Override
    public void DFS() {
        for (AdjacentListVertex<V> u : vertex) {
            u.setColor(Color.WHITE);
            u.setParent(null);
        }
        time = 0;
        for (AdjacentListVertex<V> u : vertex) {
            if (u.getColor() == Color.WHITE) {
                DfsVisit(u);
            }
        }
    }

    @Override
    public void DfsVisit(Vertex<V> u) {
        time += 1;
        AdjacentListVertex<V> u1 = (AdjacentListVertex<V>) u;
        u1.setDistance(time);
        u1.setColor(Color.GRAY);
        for (Pair<AdjacentListVertex<V>, Integer> p : u1.getAdjacentList()) {
            AdjacentListVertex<V> v = p.getValue1();
            if (v.getColor() == Color.WHITE) {
                v.setParent(u1);
                DfsVisit(v);
            }
        }
        u1.setColor(Color.BLACK);
        time += 1;
        u1.setTime(time);
    }

    @Override
    public Pair<ArrayList<Vertex<V>>, ArrayList<Integer>> dijkstra(V source) throws VertexNotFoundException {
        int index = getIndex(source);
        AdjacentListVertex<V> s = vertex.get(index);
        if (s == null) {
            throw new VertexNotFoundException("Vertex not found");
        }

        ArrayList<Vertex<V>> previous = new ArrayList<>(Collections.nCopies(vertex.size(), null));
        ArrayList<Integer> distances = new ArrayList<>(Collections.nCopies(vertex.size(), Integer.MAX_VALUE));
        distances.set(getIndex(source), 0);

        PriorityQueue<AdjacentListVertex<V>> queue = new PriorityQueue<>(Comparator.comparingInt(v -> distances.get(getIndex(v.getValue()))));
        queue.addAll(vertex);

        while (!queue.isEmpty()) {
            AdjacentListVertex<V> u = queue.poll();
            for (Pair<AdjacentListVertex<V>, Integer> p : u.getAdjacentList()) {
                AdjacentListVertex<V> v = p.getValue1();
                int uIndex = getIndex(u.getValue());
                int vIndex = getIndex(v.getValue());
                int vDistance = distances.get(vIndex);
                int distance = distances.get(uIndex) + p.getValue2();
                if (distance < vDistance) {
                    distances.set(vIndex, distance);
                    previous.set(vIndex, u);
                    queue.offer(v);
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

        for (AdjacentListVertex<V> u : vertex) {
            int uIndex = vertex.indexOf(u);
            for (Pair<AdjacentListVertex<V>, Integer> p : u.getAdjacentList()) {
                AdjacentListVertex<V> v = p.getValue1();
                int vIndex = vertex.indexOf(v);
                distances[uIndex][vIndex] = p.getValue2();
                parents.set(uIndex, vIndex, u.getValue());
            }
        }

        for (int k = 0; k < vertex.size(); k++) {
            for (int i = 0; i < vertex.size(); i++) {
                for (int j = 0; j < vertex.size(); j++) {
                    int distance = (distances[i][k]==Integer.MAX_VALUE || distances[k][j]==Integer.MAX_VALUE)? Integer.MAX_VALUE : distances[i][k] + distances[k][j];
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
        for (AdjacentListVertex<V> v : vertex) {
            v.setColor(Color.WHITE);
        }
        ArrayList<Vertex<V>> parents = new ArrayList<>(Collections.nCopies(vertex.size(), null));
        ArrayList<Integer> distances = new ArrayList<>(Collections.nCopies(vertex.size(), Integer.MAX_VALUE));
        AdjacentListVertex<V> r = vertex.get(0);
        distances.set(0, 0);
        parents.set(0, null);
        PriorityQueue<AdjacentListVertex<V>> queue = new PriorityQueue<>(Comparator.comparingInt(v -> distances.get(getIndex(v.getValue()))));
        queue.addAll(vertex);
        while (!queue.isEmpty()) {
            AdjacentListVertex<V> u = queue.poll();
            for (Pair<AdjacentListVertex<V>, Integer> p : u.getAdjacentList()) {
                AdjacentListVertex<V> v = p.getValue1();
                int uIndex = getIndex(u.getValue());
                int vIndex = getIndex(v.getValue());
                int vDistance = distances.get(vIndex);
                int distance = p.getValue2();
                if (v.getColor() == Color.WHITE && distance < vDistance) {
                    distances.set(vIndex, distance);
                    parents.set(vIndex, u);
                    queue.offer(v);
                }
            }
            u.setColor(Color.BLACK);
        }
        return new Pair<>(parents, distances);
    }

    @Override
    public ArrayList<Pair<Pair<Vertex<V>, Vertex<V>>, Integer>> kruskal() {
        UnionFind unionFind = new UnionFind(vertex.size());
        ArrayList<Pair<Pair<Vertex<V>, Vertex<V>>, Integer>> minimumSpanningTree = new ArrayList<>();
        ArrayList<Pair<Pair<Vertex<V>, Vertex<V>>, Integer>> edges = new ArrayList<>();
        for (AdjacentListVertex<V> u : vertex) {
            for (Pair<AdjacentListVertex<V>, Integer> p : u.getAdjacentList()) {
                AdjacentListVertex<V> v = p.getValue1();
                int weight = p.getValue2();
                edges.add(new Pair<>(new Pair<>(u, v), weight));
            }
        }
        edges.sort(Comparator.comparingInt(Pair::getValue2));
        for (Pair<Pair<Vertex<V>, Vertex<V>>, Integer> edge : edges) {
            Vertex<V> u = edge.getValue1().getValue1();
            Vertex<V> v = edge.getValue1().getValue2();
            int uIndex = getIndex(u.getValue());
            int vIndex = getIndex(v.getValue());
            if (unionFind.find(uIndex) != unionFind.find(vIndex)) {
                minimumSpanningTree.add(edge);
                unionFind.union(uIndex, vIndex);
            }
        }
        return minimumSpanningTree;
    }

    public ArrayList<AdjacentListVertex<V>> getVertex() {
        return vertex;
    }
}