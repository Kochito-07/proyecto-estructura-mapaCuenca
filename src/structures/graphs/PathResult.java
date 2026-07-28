package structures.graphs;

import java.util.LinkedHashSet;
import java.util.Set;

public class PathResult<T> {

    private final Set<T> visitados;
    private final Set<T> path;
    private final boolean found;
    private final long executionTimeNanos;

    public PathResult(Set<T> visitados, Set<T> path, boolean found, long executionTimeNanos) {
        this.visitados = visitados;
        this.path = path;
        this.found = found;
        this.executionTimeNanos = executionTimeNanos;
    }

    public PathResult(Set<T> visitados, Set<T> path, boolean found) {
        this(visitados, path, found, 0L);
    }

    public static <T> PathResult<T> empty() {
        return new PathResult<>(new LinkedHashSet<>(), new LinkedHashSet<>(), false, 0L);
    }

    public Set<T> getVisitados() {
        return visitados;
    }

    public Set<T> getPath() {
        return path;
    }

    public boolean isFound() {
        return found;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }

    @Override
    public String toString() {
        return "PathResult{" +
                "found=" + found +
                ", visitados=" + visitados.size() +
                ", path=" + path +
                ", tiempo=" + getExecutionTimeMillis() + "ms" +
                '}';
    }
}