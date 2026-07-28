package structures.graphs.implementations;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

import java.util.LinkedHashSet;
import java.util.Set;
public class DFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        long startTime = System.nanoTime();

        Node<T> startNode = new Node<>(start);
        Node<T> endNode = new Node<>(end);

        if (!graph.contains(start)) {
            return PathResult.empty();
        }

        Set<Node<T>> visitados = new LinkedHashSet<>();
        Set<Node<T>> path = new LinkedHashSet<>();

        boolean found = dfs(graph, startNode, endNode, visitados, path);

        Set<T> visitadosData = new LinkedHashSet<>();
        for (Node<T> node : visitados) {
            visitadosData.add(node.getData());
        }

        Set<T> pathData = new LinkedHashSet<>();
        if (found) {
            for (Node<T> node : path) {
                pathData.add(node.getData());
            }
        }

        long elapsed = System.nanoTime() - startTime;

        return new PathResult<>(visitadosData, pathData, found, elapsed);
    }

    private boolean dfs(Graph<T> graph, Node<T> current, Node<T> end,
                         Set<Node<T>> visitados, Set<Node<T>> path) {

        visitados.add(current);
        path.add(current);

        if (current.equals(end)) {
            return true;
        }

        Set<Node<T>> vecinos = graph.getGraph().getOrDefault(current, new LinkedHashSet<>());
        for (Node<T> vecino : vecinos) {
            if (!visitados.contains(vecino)) {
                if (dfs(graph, vecino, end, visitados, path)) {
                    return true;
                }
            }
        }
        path.remove(current);
        return false;
    }
}