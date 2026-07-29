package controllers;

import models.MapPoint;
import persistence.FileGraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathResult;
import structures.node.Node;
import views.MainFrame;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder;

import java.awt.Color;
import java.awt.event.ActionEvent;

public class MapController {
    
    private MainFrame view;
    private Graph<MapPoint> graph;
    private FileGraphRepository repository;

    public MapController(MainFrame view) {
        this.view = view;
        this.repository = new FileGraphRepository("configuracion.csv");
        
        initController();
        loadData();
    }

    private void initController() {
        view.btnBFS.addActionListener((ActionEvent e) -> ejecutarBFS());
        view.btnDFS.addActionListener((ActionEvent e) -> ejecutarDFS());
        
        view.btnLimpiar.addActionListener((ActionEvent e) -> limpiarTodo());
        
        view.comboInicio.addActionListener((ActionEvent e) -> actualizarNodosResaltados());
        view.comboDestino.addActionListener((ActionEvent e) -> actualizarNodosResaltados());
    }
    private void limpiarTodo() {
        view.mapPanel.limpiar();
        
        view.comboInicio.setSelectedIndex(-1); 
        view.comboDestino.setSelectedIndex(-1); 
        
        view.mapPanel.setNodosSeleccionados(null, null); 
    }

    private void loadData() {
        graph = repository.load();
        view.mapPanel.setGraph(graph);

        for (Node<MapPoint> node : graph.getNodes()) {
            view.comboInicio.addItem(node.getData().getId());
            view.comboDestino.addItem(node.getData().getId());
        }
        
        actualizarNodosResaltados();
    }

    private MapPoint findPointById(String id) {
        for (Node<MapPoint> node : graph.getNodes()) {
            if (node.getData().getId().equals(id)) {
                return node.getData();
            }
        }
        return null;
    }

    private void ejecutarBFS() {
        String idInicio = (String) view.comboInicio.getSelectedItem();
        String idDestino = (String) view.comboDestino.getSelectedItem();
        MapPoint inicio = findPointById(idInicio);
        MapPoint destino = findPointById(idDestino);

        if (inicio != null && destino != null) {
            BFSPathFinder<MapPoint> bfs = new BFSPathFinder<>();
            PathResult<MapPoint> resultado = bfs.find(graph, inicio, destino);
            System.out.println("BFS ejecutado en " + resultado.getExecutionTimeMillis() + " ms");
            
            procesarResultado(resultado, new Color(46, 204, 113));
        }
    }

    private void ejecutarDFS() {
        String idInicio = (String) view.comboInicio.getSelectedItem();
        String idDestino = (String) view.comboDestino.getSelectedItem();
        MapPoint inicio = findPointById(idInicio);
        MapPoint destino = findPointById(idDestino);

        if (inicio != null && destino != null) {
            DFSPathFinder<MapPoint> dfs = new DFSPathFinder<>();
            PathResult<MapPoint> resultado = dfs.find(graph, inicio, destino);
            System.out.println("DFS ejecutado en " + resultado.getExecutionTimeMillis() + " ms");
            
            procesarResultado(resultado, new Color(155, 89, 182));
        }
    }

    private void procesarResultado(PathResult<MapPoint> resultado, Color colorRuta) {
        boolean esExploracion = view.comboModo.getSelectedIndex() == 1;

        if (esExploracion) {
            view.mapPanel.animarExploracion(resultado.getVisitados(), resultado.getPath(), colorRuta);
        } else {
            view.mapPanel.setRutaInmediata(resultado.getPath(), colorRuta);
        }
    }
    private void actualizarNodosResaltados() {
        if (graph == null) {
            return;
        }
        
        MapPoint inicio = null;
        MapPoint destino = null;
        
        if (view.comboInicio.getSelectedIndex() > 0) {
            String idInicio = (String) view.comboInicio.getSelectedItem();
            inicio = findPointById(idInicio);
        }
        
        if (view.comboDestino.getSelectedIndex() > 0) {
            String idDestino = (String) view.comboDestino.getSelectedItem();
            destino = findPointById(idDestino);
        }
        
        view.mapPanel.setNodosSeleccionados(inicio, destino);
    }
}