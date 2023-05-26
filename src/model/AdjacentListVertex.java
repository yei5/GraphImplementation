package model;

import java.awt.*;
import java.util.*;

public class AdjacentListVertex <V> extends Vertex<V>{

    private ArrayList<Pair<AdjacentListVertex<V>,Integer>> adjacentList;

    public AdjacentListVertex(V value){
        super(value);
        adjacentList = new ArrayList<>();

    }

    public AdjacentListVertex<V> searchVertex(V value){
        for (Pair<AdjacentListVertex<V>,Integer> p : adjacentList){
            if (p.getValue1().getValue().equals(value)){
                return p.getValue1();
            }
        }
        return null;
    }

    public void deleteEdge(V value, int weight){
        adjacentList.removeIf(p -> p.getValue1().getValue().equals(value) && p.getValue2().equals(weight));
    }

    public void delete (V value){
        adjacentList.removeIf(p -> p.getValue1().getValue().equals(value));
    }

    public void addEdge(AdjacentListVertex<V> dest, Integer weight){
        V value = dest.getValue();
        Pair<AdjacentListVertex<V>,Integer> p = new Pair<>(dest,weight);
        adjacentList.add(p);
    }

    public ArrayList< Pair<AdjacentListVertex<V>, Integer>> getAdjacentList() {
        return adjacentList;
    }

    public void setAdjacentList(ArrayList<Pair<AdjacentListVertex<V>, Integer>> adjacentList) {
        this.adjacentList = adjacentList;
    }
}
