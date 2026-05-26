package com.metodosnumericos.ui.panels;

import com.metodosnumericos.utils.Constantes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Panel de integrantes del proyecto.
 * Editar los datos de cada integrante aquí.
 */
public class IntegrantesPanel extends JPanel {

    // ── EDITAR AQUÍ LOS DATOS DEL GRUPO ──────────────────────────────────────
    private static final String[][] INTEGRANTES = {
        {"Integrante 1", "COD-001", "correo1@comfenalco.edu.co", "Desarrollador Principal"},
        {"Integrante 2", "COD-002", "correo2@comfenalco.edu.co", "Diseño UI/UX"},
        {"Integrante 3", "COD-003", "correo3@comfenalco.edu.co", "Lógica Matemática"},
        {"Integrante 4", "COD-004", "correo4@comfenalco.edu.co", "Testing y Validación"},
        {"Integrante 5", "COD-005", "correo5@comfenalco.edu.co", "Documentación"},
        {"Integrante 6", "COD-006", "correo6@comfenalco.edu.co", "Gráficas y Visualización"},
    };
    // ─────────────────────────────────────────────────────────────────────────

    private static final Color[] AVATAR_COLORS = {
        new Color(13, 71, 161),
        new Color(0, 120, 100),
        new Color(120, 0, 150),
        new Color(160, 60, 0),
        new Color(0, 100, 160),
        new Color(180, 0, 50),
    };

    public IntegrantesPanel() {
        setLayout(new BorderLayout());
        setBackground(Constantes.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(Constantes.COLOR_PRIMARIO);
        hdr.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel titulo = new JLabel("👥  Integrantes del Proyecto");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Métodos Numéricos - Tecnológico Comfenalco");
        sub.setFont(new Font("SansSerif", Font.ITALIC, 13));
        sub.setForeground(new Color(180, 215, 255));

        JPanel hdrtxt = new JPanel(new BorderLayout(0, 4));
        hdrtxt.setOpaque(false);
        hdrtxt.add(titulo, BorderLayout.CENTER);
        hdrtxt.add(sub, BorderLayout.SOUTH);
        hdr.add(hdrtxt, BorderLayout.WEST);
        add(hdr, BorderLayout.NORTH);

        // Grid de tarjetas
        JPanel grid = new JPanel(new GridLayout(0, 3, 16, 16));
        grid.setBackground(Constantes.COLOR_FONDO);
        grid.setBorder(new EmptyBorder(24, 24, 24, 24));

        for (int i = 0; i < INTEGRANTES.length; i++) {
            grid.add(crearTarjeta(INTEGRANTES[i], AVATAR_COLORS[i % AVATAR_COLORS.length], i + 1));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(String[] datos, Color colorAvatar, int numero) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Constantes.COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_BORDE, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorAvatar);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                String letra = datos[0].substring(0, 1).toUpperCase();
                int x = (getWidth() - fm.stringWidth(letra)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(letra, x, y);
            }
        };
        avatar.setPreferredSize(new Dimension(70, 70));
        avatar.setOpaque(false);

        JPanel avatarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarWrapper.setOpaque(false);
        avatarWrapper.add(avatar);

        // Información
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel lblNombre = new JLabel(datos[0]);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNombre.setForeground(Constantes.COLOR_TEXTO);
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblRol = new JLabel(datos[3]);
        lblRol.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblRol.setForeground(colorAvatar);
        lblRol.setAlignmentX(CENTER_ALIGNMENT);

        JPanel badgeCod = crearBadge("Cód: " + datos[1], new Color(230, 240, 255));
        badgeCod.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblEmail = new JLabel("✉ " + datos[2]);
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEmail.setForeground(Constantes.COLOR_TEXTO_SEC);
        lblEmail.setAlignmentX(CENTER_ALIGNMENT);

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(4));
        info.add(lblRol);
        info.add(Box.createVerticalStrut(8));
        info.add(badgeCod);
        info.add(Box.createVerticalStrut(6));
        info.add(lblEmail);

        card.add(avatarWrapper, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        // Número de integrante
        JLabel numLbl = new JLabel("#" + numero);
        numLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        numLbl.setForeground(Constantes.COLOR_TEXTO_SEC);
        numLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(numLbl, BorderLayout.SOUTH);

        return card;
    }

    private JPanel crearBadge(String texto, Color fondo) {
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(Constantes.COLOR_PRIMARIO);
        badge.add(lbl);
        return badge;
    }
}
