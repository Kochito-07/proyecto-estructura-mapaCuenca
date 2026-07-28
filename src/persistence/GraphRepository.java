package persistence;

import structures.graphs.Graph;

public interface GraphRepository<T> {

    void save(Graph<T> graph);
    Graph<T> load();
}