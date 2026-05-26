package com.metodosnumericos.ui.panels;

import com.metodosnumericos.ui.MainFrame;
import com.metodosnumericos.utils.Constantes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla de inicio con información del proyecto.
 */
public class InicioPanel extends JPanel {

    private final MainFrame frame;

    public InicioPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Constantes.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        // Banner superior
        add(crearBanner(), BorderLayout.NORTH);

        // Contenido central
        JScrollPane scroll = new JScrollPane(crearContenidoCentral());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ─── Banner ───────────────────────────────────────────────────────────────

    private JPanel crearBanner() {
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente azul
                GradientPaint gp = new GradientPaint(0, 0, new Color(13, 71, 161),
                    getWidth(), getHeight(), new Color(0, 150, 200));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Patrón decorativo (líneas matemáticas)
                g2.setColor(new Color(255, 255, 255, 15));
                g2.setFont(new Font("Serif", Font.PLAIN, 14));
                String[] formulas = {"∫f(x)dx", "Δ=(b-a)/n", "Simpson", "Trapecio", "∑", "∫", "dx"};
                for (int i = 0; i < 20; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    g2.drawString(formulas[i % formulas.length], x, y);
                }
            }
        };
        banner.setPreferredSize(new Dimension(0, 200));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblIntegral = new JLabel("∫");
        lblIntegral.setFont(new Font("Serif", Font.PLAIN, 80));
        lblIntegral.setForeground(new Color(255, 255, 255, 80));
        lblIntegral.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("MÉTODOS NUMÉRICOS");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Integración Numérica Definida");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSub.setForeground(new Color(180, 220, 255));
        lblSub.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblUniv = new JLabel("Fundación Universitaria Tecnológico Comfenalco");
        lblUniv.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblUniv.setForeground(new Color(150, 200, 240));
        lblUniv.setAlignmentX(CENTER_ALIGNMENT);

        textPanel.add(lblTitulo);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(lblSub);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(lblUniv);

        banner.add(textPanel, BorderLayout.CENTER);

        // Ícono lateral
        JLabel lblIcon = new JLabel("∫", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Serif", Font.PLAIN, 100));
        lblIcon.setForeground(new Color(255, 255, 255, 60));
        lblIcon.setBorder(new EmptyBorder(0, 0, 0, 40));
        banner.add(lblIcon, BorderLayout.EAST);

        return banner;
    }

    // ─── Contenido central ────────────────────────────────────────────────────

    private JPanel crearContenidoCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Constantes.COLOR_FONDO);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Info del proyecto
        panel.add(crearSeccionInfo());
        panel.add(Box.createVerticalStrut(20));

        // Tarjetas de métodos
        panel.add(crearEtiquetaSeccion("Métodos Disponibles"));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearGridMetodos());
        panel.add(Box.createVerticalStrut(20));

        // Instrucciones
        panel.add(crearEtiquetaSeccion("Cómo usar la aplicación"));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearInstrucciones());

        return panel;
    }

    private JPanel crearSeccionInfo() {
        JPanel card = new JPanel(new GridLayout(2, 3, 16, 8));
        card.setBackground(Constantes.COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_BORDE, 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));

        agregarInfoItem(card, "📚 Materia",    "Métodos Numéricos");
        agregarInfoItem(card, "👨‍🏫 Profesor",   "Por definir");
        agregarInfoItem(card, "🏛 Universidad", "Tecnológico Comfenalco");
        agregarInfoItem(card, "📅 Año",        "2025");
        agregarInfoItem(card, "💻 Lenguaje",   "Java 17 + Swing");
        agregarInfoItem(card, "🔢 Métodos",    "5 métodos de integración");

        return card;
    }

    private void agregarInfoItem(JPanel panel, String etiqueta, String valor) {
        JPanel item = new JPanel(new BorderLayout(0, 2));
        item.setOpaque(false);

        JLabel lbl1 = new JLabel(etiqueta);
        lbl1.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl1.setForeground(Constantes.COLOR_TEXTO_SEC);

        JLabel lbl2 = new JLabel(valor);
        lbl2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl2.setForeground(Constantes.COLOR_TEXTO);

        item.add(lbl1, BorderLayout.NORTH);
        item.add(lbl2, BorderLayout.CENTER);
        panel.add(item);
    }

    private JPanel crearGridMetodos() {
        JPanel grid = new JPanel(new GridLayout(1, 5, 12, 0));
        grid.setOpaque(false);

        String[][] metodos = {
            {"∫", "Trapecio",       "Aproxima con\ntrapezoides",        MainFrame.PANEL_TRAPECIO},
            {"∫", "Jorge Boole",    "5 puntos,\nalta precisión",        MainFrame.PANEL_BOOLE},
            {"∫", "Simpson 1/3",    "3 puntos,\nparábolicas",           MainFrame.PANEL_SIMPSON13},
            {"∫", "Simpson 3/8",    "4 puntos,\ncúbica",                MainFrame.PANEL_SIMPSON38},
            {"∫", "Simp. Abierto", "n par,\npuntos internos",           MainFrame.PANEL_SIMP_ABIERTO},
        };

        Color[] colores = {
            new Color(13, 71, 161),
            new Color(0, 120, 100),
            new Color(120, 0, 150),
            new Color(160, 60, 0),
            new Color(0, 100, 160),
        };

        for (int i = 0; i < metodos.length; i++) {
            final String panelId = metodos[i][3];
            final Color color = colores[i];
            JPanel card = crearTarjetaMetodo(metodos[i][0], metodos[i][1], metodos[i][2], color, panelId);
            grid.add(card);
        }

        return grid;
    }

    private JPanel crearTarjetaMetodo(String icono, String nombre, String desc, Color color, String panelId) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        card.setOpaque(false);
        card.setBackground(color);
        card.setBorder(new EmptyBorder(16, 12, 16, 12));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblIco = new JLabel(icono, SwingConstants.CENTER);
        lblIco.setFont(new Font("Serif", Font.PLAIN, 40));
        lblIco.setForeground(new Color(255, 255, 255, 150));

        JLabel lblNom = new JLabel("<html><center>" + nombre + "</center></html>", SwingConstants.CENTER);
        lblNom.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNom.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel("<html><center><small>" + desc.replace("\n", "<br>") + "</small></center></html>", SwingConstants.CENTER);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblDesc.setForeground(new Color(200, 230, 255));

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);
        lblNom.setAlignmentX(CENTER_ALIGNMENT);
        lblDesc.setAlignmentX(CENTER_ALIGNMENT);
        centro.add(lblNom);
        centro.add(Box.createVerticalStrut(4));
        centro.add(lblDesc);

        card.add(lblIco, BorderLayout.NORTH);
        card.add(centro, BorderLayout.CENTER);

        // Hover
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { frame.navegarA(panelId); }
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.WHITE, 2, true), new EmptyBorder(14, 10, 14, 10)));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(16, 12, 16, 12));
            }
        });

        return card;
    }

    private JPanel crearInstrucciones() {
        JPanel p = new JPanel(new GridLayout(1, 3, 16, 0));
        p.setOpaque(false);

        agregarPaso(p, "1", "Seleccionar método",
            "Elige un método del menú lateral izquierdo: Trapecio, Simpson, etc.");
        agregarPaso(p, "2", "Ingresar datos",
            "Escribe la función f(x), los límites a y b, y el Δ o n de particiones.");
        agregarPaso(p, "3", "Calcular y graficar",
            "Presiona CALCULAR para ver la tabla de procedimiento y el resultado, y GRAFICAR para ver la gráfica.");

        return p;
    }

    private void agregarPaso(JPanel panel, String num, String titulo, String desc) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Constantes.COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_BORDE, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel numLbl = new JLabel(num);
        numLbl.setFont(new Font("SansSerif", Font.BOLD, 32));
        numLbl.setForeground(Constantes.COLOR_PRIMARIO);

        JLabel titLbl = new JLabel(titulo);
        titLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        titLbl.setForeground(Constantes.COLOR_TEXTO);

        JLabel descLbl = new JLabel("<html>" + desc + "</html>");
        descLbl.setFont(Constantes.FUENTE_NORMAL);
        descLbl.setForeground(Constantes.COLOR_TEXTO_SEC);

        JPanel textos = new JPanel(new BorderLayout(0, 4));
        textos.setOpaque(false);
        textos.add(titLbl, BorderLayout.NORTH);
        textos.add(descLbl, BorderLayout.CENTER);

        card.add(numLbl, BorderLayout.WEST);
        card.add(textos, BorderLayout.CENTER);
        panel.add(card);
    }

    private JLabel crearEtiquetaSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(Constantes.COLOR_PRIMARIO);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Constantes.COLOR_PRIMARIO));
        return lbl;
    }
}
