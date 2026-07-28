package persistence;

import models.MapPoint;
import structures.graphs.Graph;
import structures.node.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileGraphRepository implements GraphRepository<MapPoint> {

    private final String filePath;

    public FileGraphRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Graph<MapPoint> graph) {
        try (FileWriter writer = new FileWriter(filePath)) {

            for (Node<MapPoint> node : graph.getNodes()) {
                MapPoint point = node.getData();
                writer.write(String.format("NODE,%s,%d,%d%n",
                        point.getId(), point.getX(), point.getY()));
            }

            Set<String> aristasEscritas = new HashSet<>();
            Map<Node<MapPoint>, Set<Node<MapPoint>>> adjacency = graph.getGraph();

            for (Map.Entry<Node<MapPoint>, Set<Node<MapPoint>>> entry : adjacency.entrySet()) {
                MapPoint from = entry.getKey().getData();

                for (Node<MapPoint> vecino : entry.getValue()) {
                    MapPoint to = vecino.getData();

                    boolean esBidireccional = adjacency
                            .getOrDefault(vecino, new HashSet<>())
                            .contains(entry.getKey());

                    String claveDirecta = from.getId() + "->" + to.getId();
                    String claveInversa = to.getId() + "->" + from.getId();

                    if (esBidireccional) {
                        if (aristasEscritas.contains(claveDirecta) || aristasEscritas.contains(claveInversa)) {
                            continue;
                        }
                        writer.write(String.format("EDGE,%s,%s,true%n", from.getId(), to.getId()));
                        aristasEscritas.add(claveDirecta);
                        aristasEscritas.add(claveInversa);
                    } else {
                        if (aristasEscritas.contains(claveDirecta)) {
                            continue;
                        }
                        writer.write(String.format("EDGE,%s,%s,false%n", from.getId(), to.getId()));
                        aristasEscritas.add(claveDirecta);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el grafo en " + filePath, e);
        }
    }

    @Override
    public Graph<MapPoint> load() {
        Graph<MapPoint> graph = new Graph<>();
        Set<String> idsRegistrados = new HashSet<>();

        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) {
            return graph;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int numeroLinea = 0;

            while ((line = reader.readLine()) != null) {
                numeroLinea++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] partes = line.split(",");

                if (partes[0].equals("NODE")) {
                    if (!validarNode(partes, numeroLinea)) continue;

                    String id = partes[1].trim();
                    if (idsRegistrados.contains(id)) {
                        System.out.println("Aviso: id de nodo duplicado '" + id + "' en línea " + numeroLinea + ", se ignora.");
                        continue;
                    }

                    int x = Integer.parseInt(partes[2].trim());
                    int y = Integer.parseInt(partes[3].trim());
                    graph.add(new MapPoint(id, x, y));
                    idsRegistrados.add(id);

                } else if (partes[0].equals("EDGE")) {
                    if (!validarEdge(partes, numeroLinea)) continue;

                    String fromId = partes[1].trim();
                    String toId = partes[2].trim();
                    boolean bidireccional = Boolean.parseBoolean(partes[3].trim());

                    if (!idsRegistrados.contains(fromId) || !idsRegistrados.contains(toId)) {
                        System.out.println("Aviso: arista en línea " + numeroLinea +
                                " referencia un nodo inexistente ('" + fromId + "' o '" + toId + "'), se ignora.");
                        continue;
                    }

                    MapPoint from = new MapPoint(fromId, 0, 0);
                    MapPoint to = new MapPoint(toId, 0, 0);

                    if (bidireccional) {
                        graph.addEdge(from, to);
                    } else {
                        graph.addEdgeUni(from, to);
                    }
                } else {
                    System.out.println("Aviso: línea " + numeroLinea + " con tipo de registro desconocido, se ignora.");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el grafo desde " + filePath, e);
        }

        return graph;
    }

    private boolean validarNode(String[] partes, int numeroLinea) {
        if (partes.length < 4) {
            System.out.println("Aviso: NODE incompleto en línea " + numeroLinea + ", se ignora.");
            return false;
        }
        if (partes[1].trim().isEmpty()) {
            System.out.println("Aviso: NODE sin id en línea " + numeroLinea + ", se ignora.");
            return false;
        }
        try {
            Integer.parseInt(partes[2].trim());
            Integer.parseInt(partes[3].trim());
        } catch (NumberFormatException e) {
            System.out.println("Aviso: NODE con coordenadas inválidas en línea " + numeroLinea + ", se ignora.");
            return false;
        }
        return true;
    }

    private boolean validarEdge(String[] partes, int numeroLinea) {
        if (partes.length < 4) {
            System.out.println("Aviso: EDGE incompleto en línea " + numeroLinea + ", se ignora.");
            return false;
        }
        if (partes[1].trim().isEmpty() || partes[2].trim().isEmpty()) {
            System.out.println("Aviso: EDGE con id vacío en línea " + numeroLinea + ", se ignora.");
            return false;
        }
        return true;
    }
}