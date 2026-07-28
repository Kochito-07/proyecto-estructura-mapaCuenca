package structures.graphs.implementations;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        long startTime = System.nanoTime(); 

        Queue<Node<T>> queue = new LinkedList<>();
        Map<Node<T>, Node<T>> predecessors = new HashMap<>();
        
        Set<T> visitados = new LinkedHashSet<>();
        Set<T> path = new LinkedHashSet<>();
        
        Set<Node<T>> visitedNodes = new LinkedHashSet<>();

        Node<T> startNode = new Node<>(start);
        Node<T> endNode = new Node<>(end);

        queue.add(startNode);
        visitedNodes.add(startNode);

        boolean found = false;

        while (!queue.isEmpty()) {
            Node<T> current = queue.poll();
            visitados.add(current.getData()); 

            if (current.equals(endNode)) {
                found = true;
                break;
            }

            Set<Node<T>> neighbors = graph.getGraph().get(current);
            if (neighbors != null) {
                for (Node<T> neighbor : neighbors) {
                    if (!visitedNodes.contains(neighbor)) {
                        visitedNodes.add(neighbor);
                        predecessors.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }

        if (found) {
            List<T> tempPath = new ArrayList<>();
            Node<T> step = endNode;
            
            while (step != null) {
                tempPath.add(step.getData());
                step = predecessors.get(step);
            }
            
            Collections.reverse(tempPath);
            path.addAll(tempPath);
        }

        long endTime = System.nanoTime();
        long executionTimeNanos = endTime - startTime;

        return new PathResult<>(visitados, path, found, executionTimeNanos);
    }
}