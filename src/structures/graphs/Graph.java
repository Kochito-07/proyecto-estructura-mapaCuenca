package structures.graphs;

import structures.node.Node;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
public class Graph<T> {

    private final Map<Node<T>, Set<Node<T>>> graph;

    public Graph() {
        this.graph = new LinkedHashMap<>();
    }
    public void add(T data) {
        Node<T> node = new Node<>(data);
        graph.putIfAbsent(node, new LinkedHashSet<>());
    }

    public void addEdge(T v1, T v2) {
        add(v1);
        add(v2);
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);
        graph.get(n1).add(n2);
        graph.get(n2).add(n1);
    }
    public void addEdgeUni(T v1, T v2) {
        add(v1);
        add(v2);
        Node<T> n1 = new Node<>(v1);
        graph.get(n1).add(new Node<>(v2));
    }
    public void remove(T data) {
        Node<T> target = new Node<>(data);
        graph.remove(target);
        for (Set<Node<T>> neighbors : graph.values()) {
            neighbors.remove(target);
        }
    } 
    public void removeEdge(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);
        if (graph.containsKey(n1)) graph.get(n1).remove(n2);
        if (graph.containsKey(n2)) graph.get(n2).remove(n1);
    }
    public void removeEdgeUni(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);
        if (graph.containsKey(n1)) graph.get(n1).remove(n2);
    }
    public Set<Node<T>> getNodes() {
        return graph.keySet();
    }
    public Map<Node<T>, Set<Node<T>>> getGraph() {
        return graph;
    }
    public Set<Node<T>> getNeighbors(T data) {
        Node<T> node = new Node<>(data);
        return graph.getOrDefault(node, new LinkedHashSet<>());
    }
    public boolean contains(T data) {
        return graph.containsKey(new Node<>(data));
    }
    
}