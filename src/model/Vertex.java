package model;

import java.awt.*;

public class Vertex <V>{
    private V value;
    private Color color;
    private int distance;
    private int time;

    private Vertex<V> parent;

    public Vertex (V value){
        this.value = value;
        color = Color.WHITE;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Vertex<V> getParent() {
        return parent;
    }

    public void setParent(Vertex<V> parent) {
        this.parent = parent;
    }
}
