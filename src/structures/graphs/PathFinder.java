package structures.graphs;
public interface PathFinder<T> {

    /**
     * Busca una ruta entre start y end dentro del grafo dado.
     *
     * @param graph grafo sobre el que se realiza la búsqueda
     * @param start nodo de inicio
     * @param end   nodo de destino
     * @return PathResult<T> con los nodos visitados (en orden) y la ruta encontrada
     */
    PathResult<T> find(Graph<T> graph, T start, T end);
}