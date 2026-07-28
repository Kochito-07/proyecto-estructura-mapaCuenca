package controllers;

import models.MapPoint;
import persistence.FileGraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathResult;
import structures.node.Node;
import views.MainFrame;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder; 

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
    }

    private void loadData() {
        graph = repository.load();
        view.mapPanel.setGraph(graph);

        for (Node<MapPoint> node : graph.getNodes()) {
            view.comboInicio.addItem(node.getData().getId());
            view.comboDestino.addItem(node.getData().getId());
        }
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
            System.out.println("BFS: " + resultado.toString());
            view.mapPanel.setRuta(resultado.getPath());
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
            System.out.println("DFS: " + resultado.toString());
            view.mapPanel.setRuta(resultado.getPath());
        }
    }
}