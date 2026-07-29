package views;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    
    public MapPanel mapPanel;
    public JButton btnBFS;
    public JButton btnDFS;
    public JButton btnLimpiar;
    public JComboBox<String> comboInicio;
    public JComboBox<String> comboDestino;
    public JComboBox<String> comboModo;

    public MainFrame() {
        setTitle("Proyecto Final - Búsqueda de Rutas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(250, 600));
        controlPanel.setBackground(new Color(240, 244, 248));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        controlPanel.setLayout(new GridLayout(12, 1, 10, 10));

        controlPanel.add(new JLabel("Modo de Visualización:"));
        comboModo = new JComboBox<>(new String[]{"Modo Ruta Final", "Modo Exploración"});
        controlPanel.add(comboModo);

        controlPanel.add(new JLabel("Nodo de Inicio:"));
        comboInicio = new JComboBox<>();
        controlPanel.add(comboInicio);

        controlPanel.add(new JLabel("Nodo de Destino:"));
        comboDestino = new JComboBox<>();
        controlPanel.add(comboDestino);

        controlPanel.add(new JLabel(""));

        btnBFS = new RoundedButton("Ejecutar BFS", new Color(46, 204, 113), new Color(39, 174, 96), 20);
        controlPanel.add(btnBFS);
        
        btnDFS = new RoundedButton("Ejecutar DFS", new Color(155, 89, 182), new Color(142, 68, 173), 20);
        controlPanel.add(btnDFS);

        controlPanel.add(new JLabel(""));

        btnLimpiar = new RoundedButton("Limpiar Mapa", new Color(231, 76, 60), new Color(192, 57, 43), 20);
        controlPanel.add(btnLimpiar);

        add(controlPanel, BorderLayout.WEST);

        mapPanel = new MapPanel();
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(mapPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class RoundedButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private Color pressedColor;
        private int radius;

        public RoundedButton(String text, Color nColor, Color hColor, int r) {
            super(text);
            this.normalColor = nColor;
            this.hoverColor = hColor;
            this.pressedColor = nColor.darker();
            this.radius = r;
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setBackground(normalColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { setBackground(hoverColor); }
                @Override
                public void mouseExited(MouseEvent e) { setBackground(normalColor); }
                @Override
                public void mousePressed(MouseEvent e) { setBackground(pressedColor); }
                @Override
                public void mouseReleased(MouseEvent e) { setBackground(hoverColor); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}