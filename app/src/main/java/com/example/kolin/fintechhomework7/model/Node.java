package com.example.kolin.fintechhomework7.model;

import java.util.List;

/**
 * Created by kolin on 12.11.2017.
 */

public class Node {
    private int value;
    private List<Node> nodes;

    public Node(int value, List<Node> nodes) {
        this.value = value;
        this.nodes = nodes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", nodes=" + nodes +
                '}';
    }
}
