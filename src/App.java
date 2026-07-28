import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearVentana());
    }

    private static void crearVentana() {
        JFrame frame = new JFrame("Sistema de Mapa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        frame.add(layeredPane, BorderLayout.CENTER);

        PanelMapaImagen panelMapa = new PanelMapaImagen("mapa_cuenca.png");

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        BotonRedondoFlotante botonUbicacion = new BotonRedondoFlotante("📍");
        BotonRedondoFlotante botonBuscar = new BotonRedondoFlotante("🔍");
        BotonRedondoFlotante botonAjustes = new BotonRedondoFlotante("⚙️");

        JPopupMenu menuUbicaciones = new JPopupMenu();
        menuUbicaciones.setOpaque(false);
        menuUbicaciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuUbicaciones.setBackground(new Color(0, 0, 0, 0));

        ItemLlamativo loc1 = new ItemLlamativo("Ubicación 1: Parque Central");
        ItemLlamativo loc2 = new ItemLlamativo("Ubicación 2: Estación Sur");
        ItemLlamativo loc3 = new ItemLlamativo("Ubicación 3: Aeropuerto");

        loc1.addActionListener(e -> System.out.println("Navegando a Parque Central..."));
        loc2.addActionListener(e -> System.out.println("Navegando a Estación Sur..."));
        loc3.addActionListener(e -> System.out.println("Navegando al Aeropuerto..."));

        menuUbicaciones.add(loc1);
        menuUbicaciones.add(Box.createVerticalStrut(8));
        menuUbicaciones.add(loc2);
        menuUbicaciones.add(Box.createVerticalStrut(8));
        menuUbicaciones.add(loc3);

        botonUbicacion.addActionListener(e -> {
            int x = (botonUbicacion.getWidth() - menuUbicaciones.getPreferredSize().width) / 2;
            int y = -menuUbicaciones.getPreferredSize().height - 5;
            menuUbicaciones.show(botonUbicacion, x, y);
        });

        panelBotones.add(botonUbicacion);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonAjustes);

        layeredPane.add(panelMapa, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(panelBotones, JLayeredPane.PALETTE_LAYER);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelMapa.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                panelBotones.setBounds(0, frame.getHeight() - 120, frame.getWidth(), 80);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class PanelMapaImagen extends JPanel {
    private Image imagenFondo;

    public PanelMapaImagen(String rutaImagen) {
        java.net.URL imgUrl = getClass().getResource(rutaImagen);
        
        if (imgUrl != null) {
            ImageIcon icono = new ImageIcon(imgUrl);
            imagenFondo = icono.getImage();
        } else {
            System.err.println("¡Ojo! No se encontró la imagen: " + rutaImagen);
        }
        
        setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class ItemLlamativo extends JMenuItem {
    private Color colorNormal = new Color(255, 87, 34); 
    private Color colorHover = new Color(230, 74, 25);

    public ItemLlamativo(String texto) {
        super(texto);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 25, 25);

        g2.setColor(getModel().isArmed() ? colorHover : colorNormal);
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 6, 25, 25);

        g2.dispose();
        super.paintComponent(g);
    }
}

class BotonRedondoFlotante extends JButton {
    private Color colorNormal = new Color(44, 62, 80);
    private Color colorHover = new Color(52, 73, 94);
    private boolean isHovered = false;
    private int elevacionY = 0; 
    private Timer timerAnimacion;

    public BotonRedondoFlotante(String icono) {
        super(icono);
        setPreferredSize(new Dimension(60, 60)); 
        setContentAreaFilled(false); 
        setFocusPainted(false); 
        setBorderPainted(false); 
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        timerAnimacion = new Timer(15, e -> {
            int objetivoY = isHovered ? -6 : 0;
            int velocidad = 2;

            if (elevacionY < objetivoY) {
                elevacionY += velocidad;
                if (elevacionY > objetivoY) elevacionY = objetivoY;
            } else if (elevacionY > objetivoY) {
                elevacionY -= velocidad;
                if (elevacionY < objetivoY) elevacionY = objetivoY;
            }
            
            repaint();

            if (elevacionY == objetivoY) {
                timerAnimacion.stop();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                timerAnimacion.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                timerAnimacion.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int diametro = getWidth() - 16; 
        int xCenter = (getWidth() - diametro) / 2;
        int yCenter = (getHeight() - diametro) / 2;

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(xCenter, yCenter + 4, diametro, diametro);

        g2.setColor(isHovered ? colorHover : colorNormal);
        g2.fillOval(xCenter, yCenter + elevacionY, diametro, diametro);

        g2.dispose();

        Graphics gOffset = g.create(0, elevacionY, getWidth(), getHeight());
        super.paintComponent(gOffset);
        gOffset.dispose();
    }
}
