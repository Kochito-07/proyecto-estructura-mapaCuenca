package views;

import models.MapPoint;
import structures.graphs.Graph;
import structures.node.Node;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapPanel extends JPanel {
    private Image backgroundImage;
    private Graph<MapPoint> graph;
    
    private Set<MapPoint> rutaActual;
    private Color colorRuta = new Color(46, 204, 113);

    private List<MapPoint> nodosExplorados;
    private int indiceAnimacion = 0;
    private Timer timer;

    private MapPoint nodoInicioSeleccionado;
    private MapPoint nodoDestinoSeleccionado;

    public MapPanel() {
        File imgFile = new File("resources/mapa_cuenca.png");
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
            backgroundImage = icon.getImage();
            
            setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        } else {
            System.out.println("No se encontró la imagen en: " + imgFile.getAbsolutePath());
        }

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("NODE,Nuevo_Punto," + evt.getX() + "," + evt.getY());
            }
        });
    }

    public void setGraph(Graph<MapPoint> graph) {
        this.graph = graph;
        repaint();
    }

    public void setNodosSeleccionados(MapPoint inicio, MapPoint destino) {
        this.nodoInicioSeleccionado = inicio;
        this.nodoDestinoSeleccionado = destino;
        repaint();
    }

    public void limpiar() {
        if (timer != null && timer.isRunning()) timer.stop();
        rutaActual = null;
        nodosExplorados = null;
        indiceAnimacion = 0;
        repaint();
    }

    public void setRutaInmediata(Set<MapPoint> ruta, Color color) {
        limpiar();
        this.rutaActual = ruta;
        this.colorRuta = color;
        repaint();
    }

    public void animarExploracion(Set<MapPoint> explorados, Set<MapPoint> rutaFinal, Color color) {
        limpiar();
        this.colorRuta = color;
        this.nodosExplorados = new ArrayList<>(explorados);
        
        timer = new Timer(300, e -> {
            indiceAnimacion++;
            if (indiceAnimacion >= nodosExplorados.size()) {
                ((Timer)e.getSource()).stop();
                rutaActual = rutaFinal;
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, this);
        }

        if (graph == null) return;

        g2.setColor(new Color(50, 50, 50, 150));
        g2.setStroke(new BasicStroke(3));
        for (Node<MapPoint> node : graph.getNodes()) {
            MapPoint from = node.getData();
            Set<Node<MapPoint>> neighbors = graph.getGraph().get(node);
            if (neighbors != null) {
                for (Node<MapPoint> neighbor : neighbors) {
                    MapPoint to = neighbor.getData();
                    g2.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
                }
            }
        }

        if (nodosExplorados != null && indiceAnimacion > 0) {
            g2.setColor(new Color(241, 196, 15, 200)); 
            for (int i = 0; i < Math.min(indiceAnimacion, nodosExplorados.size()); i++) {
                MapPoint p = nodosExplorados.get(i);
                g2.fillOval(p.getX() - 15, p.getY() - 15, 30, 30);
            }
        }

        if (rutaActual != null && !rutaActual.isEmpty()) {
            g2.setColor(colorRuta);
            g2.setStroke(new BasicStroke(5));
            MapPoint prev = null;
            for (MapPoint p : rutaActual) {
                if (prev != null) {
                    g2.drawLine(prev.getX(), prev.getY(), p.getX(), p.getY());
                }
                prev = p;
            }
        }

        g2.setColor(new Color(41, 128, 185));
        for (Node<MapPoint> node : graph.getNodes()) {
            MapPoint p = node.getData();
            g2.fillOval(p.getX() - 10, p.getY() - 10, 20, 20);
            g2.setColor(Color.WHITE);
            g2.drawString(p.getId(), p.getX() - 5, p.getY() + 5);
            g2.setColor(new Color(41, 128, 185));
        }
        if (nodoInicioSeleccionado != null) {
            g2.setColor(new Color(46, 204, 113, 100));
            g2.fillOval(nodoInicioSeleccionado.getX() - 18, nodoInicioSeleccionado.getY() - 18, 36, 36);
            g2.setColor(new Color(46, 204, 113));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(nodoInicioSeleccionado.getX() - 18, nodoInicioSeleccionado.getY() - 18, 36, 36);
        }

        if (nodoDestinoSeleccionado != null) {
            g2.setColor(new Color(231, 76, 60, 100)); 
            g2.fillOval(nodoDestinoSeleccionado.getX() - 18, nodoDestinoSeleccionado.getY() - 18, 36, 36);
            g2.setColor(new Color(231, 76, 60));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(nodoDestinoSeleccionado.getX() - 18, nodoDestinoSeleccionado.getY() - 18, 36, 36);
        }
    }
}