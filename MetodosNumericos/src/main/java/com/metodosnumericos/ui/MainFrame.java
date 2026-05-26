package com.metodosnumericos.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.metodosnumericos.ui.panels.*;
import com.metodosnumericos.utils.Constantes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Ventana principal de la aplicación.
 * Contiene el sidebar de navegación y el panel de contenido con CardLayout.
 */
public class MainFrame extends JFrame {

    // Constantes de navegación
    public static final String PANEL_INICIO        = "INICIO";
    public static final String PANEL_TRAPECIO      = "TRAPECIO";
    public static final String PANEL_BOOLE         = "BOOLE";
    public static final String PANEL_SIMPSON13     = "SIMPSON13";
    public static final String PANEL_SIMPSON38     = "SIMPSON38";
    public static final String PANEL_SIMP_ABIERTO  = "SIMPSON_ABIERTO";
    public static final String PANEL_INTEGRANTES   = "INTEGRANTES";
    public static final String PANEL_INFO          = "INFO";

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private boolean modoOscuro = false;

    // Botones del sidebar para resaltar el activo
    private JToggleButton[] sidebarBotones;
    private ButtonGroup grupoNav;

    public MainFrame() {
        configurarVentana();
        construirUI();
        navegarA(PANEL_INICIO);
    }

    private void configurarVentana() {
        setTitle("Métodos Numéricos - Integración Definida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void construirUI() {
        // Header superior
        add(crearHeader(), BorderLayout.NORTH);

        // Contenedor central: sidebar + contenido
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(crearSidebar(), BorderLayout.WEST);

        // Panel de contenido con CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Constantes.COLOR_FONDO);

        // Registrar todos los paneles
        contentPanel.add(new InicioPanel(this), PANEL_INICIO);
        contentPanel.add(new MetodoPanel(this, PANEL_TRAPECIO),      PANEL_TRAPECIO);
        contentPanel.add(new MetodoPanel(this, PANEL_BOOLE),         PANEL_BOOLE);
        contentPanel.add(new MetodoPanel(this, PANEL_SIMPSON13),     PANEL_SIMPSON13);
        contentPanel.add(new MetodoPanel(this, PANEL_SIMPSON38),     PANEL_SIMPSON38);
        contentPanel.add(new MetodoPanel(this, PANEL_SIMP_ABIERTO),  PANEL_SIMP_ABIERTO);
        contentPanel.add(new IntegrantesPanel(),                     PANEL_INTEGRANTES);
        contentPanel.add(new InfoPanel(),                            PANEL_INFO);

        centro.add(contentPanel, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    // ─── Header ──────────────────────────────────────────────────────────────

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Constantes.COLOR_PRIMARIO);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Logo + título
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izq.setOpaque(false);

        JLabel icono = new JLabel("∫");
        icono.setFont(new Font("Serif", Font.BOLD, 36));
        icono.setForeground(Color.WHITE);

        JLabel titulo = new JLabel("Métodos Numéricos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("  |  Integración Definida");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(180, 210, 240));

        izq.add(icono);
        izq.add(titulo);
        izq.add(subtitulo);
        header.add(izq, BorderLayout.WEST);

        // Controles derecha
        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        der.setOpaque(false);

        JToggleButton btnOscuro = crearToggleModo();
        der.add(btnOscuro);

        JLabel univ = new JLabel("Tecnológico Comfenalco");
        univ.setFont(new Font("SansSerif", Font.ITALIC, 12));
        univ.setForeground(new Color(180, 210, 240));
        der.add(univ);

        header.add(der, BorderLayout.EAST);
        return header;
    }

    private JToggleButton crearToggleModo() {
        JToggleButton btn = new JToggleButton("🌙 Modo Oscuro");
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 255, 255, 40));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> toggleModoOscuro(btn));
        return btn;
    }

    // ─── Sidebar ─────────────────────────────────────────────────────────────

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Constantes.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(new EmptyBorder(10, 0, 10, 0));

        grupoNav = new ButtonGroup();

        String[][] items = {
            {"🏠", "Inicio",         PANEL_INICIO},
            {"∫",  "Trapecio",       PANEL_TRAPECIO},
            {"∫",  "Jorge Boole",    PANEL_BOOLE},
            {"∫",  "Simpson 1/3",    PANEL_SIMPSON13},
            {"∫",  "Simpson 3/8",    PANEL_SIMPSON38},
            {"∫",  "Simpson Abierto",PANEL_SIMP_ABIERTO},
            {"👥", "Integrantes",    PANEL_INTEGRANTES},
            {"ℹ",  "Información",   PANEL_INFO},
        };

        sidebarBotones = new JToggleButton[items.length];

        // Separador visual antes de métodos
        sidebar.add(Box.createVerticalStrut(5));

        for (int i = 0; i < items.length; i++) {
            if (i == 1) {
                sidebar.add(crearSeparadorSidebar("MÉTODOS"));
            } else if (i == 6) {
                sidebar.add(crearSeparadorSidebar("INFO"));
            }

            JToggleButton btn = crearBotonSidebar(items[i][0], items[i][1], items[i][2]);
            sidebarBotones[i] = btn;
            grupoNav.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JLabel crearSeparadorSidebar(String texto) {
        JLabel lbl = new JLabel("  " + texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(new Color(150, 170, 190));
        lbl.setBorder(new EmptyBorder(8, 0, 4, 0));
        lbl.setMaximumSize(new Dimension(200, 25));
        return lbl;
    }

    private JToggleButton crearBotonSidebar(String icono, String texto, String panelId) {
        JToggleButton btn = new JToggleButton("  " + icono + "  " + texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setForeground(Constantes.COLOR_TEXTO_SIDEBAR);
        btn.setBackground(Constantes.COLOR_SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setPreferredSize(new Dimension(200, 40));

        // Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!btn.isSelected()) btn.setBackground(Constantes.COLOR_SIDEBAR_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!btn.isSelected()) btn.setBackground(Constantes.COLOR_SIDEBAR);
            }
        });

        btn.addActionListener(e -> navegarA(panelId));
        return btn;
    }

    // ─── Navegación ──────────────────────────────────────────────────────────

    public void navegarA(String panelId) {
        cardLayout.show(contentPanel, panelId);
        // Marcar botón activo
        String[][] items = {
            {"", "", PANEL_INICIO},
            {"", "", PANEL_TRAPECIO},
            {"", "", PANEL_BOOLE},
            {"", "", PANEL_SIMPSON13},
            {"", "", PANEL_SIMPSON38},
            {"", "", PANEL_SIMP_ABIERTO},
            {"", "", PANEL_INTEGRANTES},
            {"", "", PANEL_INFO},
        };
        for (int i = 0; i < items.length; i++) {
            if (items[i][2].equals(panelId) && sidebarBotones != null) {
                sidebarBotones[i].setSelected(true);
                sidebarBotones[i].setBackground(Constantes.COLOR_PRIMARIO);
                sidebarBotones[i].setForeground(Color.WHITE);
            } else if (sidebarBotones != null) {
                sidebarBotones[i].setBackground(Constantes.COLOR_SIDEBAR);
                sidebarBotones[i].setForeground(Constantes.COLOR_TEXTO_SIDEBAR);
            }
        }
    }

    // ─── Modo Oscuro ─────────────────────────────────────────────────────────

    private void toggleModoOscuro(JToggleButton btn) {
        modoOscuro = btn.isSelected();
        try {
            if (modoOscuro) {
                FlatDarkLaf.setup();
                btn.setText("☀️ Modo Claro");
                Constantes.aplicarModoOscuro();
            } else {
                FlatLightLaf.setup();
                btn.setText("🌙 Modo Oscuro");
                Constantes.aplicarModoClaro();
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
