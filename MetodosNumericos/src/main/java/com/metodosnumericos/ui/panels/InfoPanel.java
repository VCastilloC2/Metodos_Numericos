package com.metodosnumericos.ui.panels;

import com.metodosnumericos.utils.Constantes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Panel de información teórica de cada método numérico.
 */
public class InfoPanel extends JPanel {

    public InfoPanel() {
        setLayout(new BorderLayout());
        setBackground(Constantes.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(Constantes.COLOR_PRIMARIO);
        hdr.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel titulo = new JLabel("ℹ  Información Teórica - Métodos de Integración");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        hdr.add(titulo, BorderLayout.WEST);
        add(hdr, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Constantes.COLOR_FONDO);
        contenido.setBorder(new EmptyBorder(20, 24, 24, 24));

        // Introducción
        contenido.add(crearTarjetaInfo("📌 ¿Qué es la Integración Numérica?",
            "La integración numérica es un conjunto de métodos para calcular " +
            "aproximaciones numéricas de integrales definidas cuando no es posible " +
            "encontrar una antiderivada en forma cerrada, o cuando la función solo se " +
            "conoce en puntos discretos. Estos métodos se basan en aproximar el área " +
            "bajo la curva usando figuras geométricas simples o polinomios interpolantes.",
            Constantes.COLOR_PRIMARIO));

        contenido.add(Box.createVerticalStrut(12));

        // Métodos
        String[][] metodos = {
            {
                "1. Método Trapezoidal",
                "Aproxima el área bajo la curva usando trapezoides. Divide el intervalo [a,b] " +
                "en n subintervalos de igual tamaño Δ=(b-a)/n y aproxima cada subintervalo " +
                "con un trapecio.\n\n" +
                "Fórmula: I = (Δ/2)[f(x₀) + 2f(x₁) + 2f(x₂) + ... + 2f(xₙ₋₁) + f(xₙ)]\n\n" +
                "✔ Ventajas: Simple, aplicable a cualquier número de particiones.\n" +
                "✘ Desventajas: Error de orden O(h²), menor precisión que Simpson."
            },
            {
                "2. Método de Jorge Boole",
                "Regla de Newton-Cotes de orden 4. Usa 5 puntos con coeficientes específicos " +
                "para mayor precisión. Requiere exactamente 4 subintervalos (Δ=(b-a)/4).\n\n" +
                "Fórmula: I = (2Δ/45)[7f(x₁) + 32f(x₂) + 12f(x₃) + 32f(x₄) + 7f(x₅)]\n\n" +
                "✔ Ventajas: Alta precisión, exacta para polinomios de grado ≤ 5.\n" +
                "✘ Desventajas: Fijo en 4 subintervalos, menos flexible."
            },
            {
                "3. Método Simpson 1/3 (Newton-Cotes)",
                "Aproxima con parábolas usando 3 puntos. Requiere 2 subintervalos (Δ=(b-a)/2). " +
                "Es exacta para polinomios de grado ≤ 3.\n\n" +
                "Fórmula: I = (Δ/3)[f(x₁) + 4f(x₂) + f(x₃)]\n\n" +
                "✔ Ventajas: Más precisa que el trapecio, error de orden O(h⁴).\n" +
                "✘ Desventajas: Requiere n par de subintervalos."
            },
            {
                "4. Método Simpson 3/8 (Newton-Cotes)",
                "Variante de Simpson que usa 4 puntos con polinomios cúbicos. Requiere " +
                "exactamente 3 subintervalos (Δ=(b-a)/3).\n\n" +
                "Fórmula: I = (3Δ/8)[f(x₁) + 3f(x₂) + 3f(x₃) + f(x₄)]\n\n" +
                "✔ Ventajas: Exacta para polinomios de grado ≤ 3, ligeramente más precisa que Simpson 1/3.\n" +
                "✘ Desventajas: Fija en 3 subintervalos."
            },
            {
                "5. Método Simpson Abierto (Compuesto)",
                "Versión compuesta de Simpson 1/3 para n subintervalos pares. Aplica la " +
                "fórmula de Simpson repetidamente sobre pares de subintervalos.\n\n" +
                "Fórmula: I = (Δ/3)[f(x₀) + 4f(x₁) + 2f(x₂) + 4f(x₃) + ... + 4f(xₙ₋₁) + f(xₙ)]\n" +
                "Δ = (b-a)/n  (n DEBE ser par)\n\n" +
                "✔ Ventajas: Flexible, n puede ser cualquier número par, alta precisión.\n" +
                "✘ Desventajas: n debe ser par obligatoriamente."
            }
        };

        Color[] colores = {
            new Color(13, 71, 161),
            new Color(0, 120, 100),
            new Color(120, 0, 150),
            new Color(160, 60, 0),
            new Color(0, 100, 160),
        };

        for (int i = 0; i < metodos.length; i++) {
            contenido.add(crearTarjetaInfo(metodos[i][0], metodos[i][1], colores[i]));
            if (i < metodos.length - 1) contenido.add(Box.createVerticalStrut(12));
        }

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaInfo(String titulo, String contenido, Color acento) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Constantes.COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, acento),
            BorderFactory.createCompoundBorder(
                new LineBorder(Constantes.COLOR_BORDE, 1),
                new EmptyBorder(14, 16, 14, 16)
            )
        ));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(acento);

        // Convertir texto a HTML para saltos de línea
        String html = "<html>" + contenido
            .replace("\n\n", "<br><br>")
            .replace("\n", "<br>")
            .replace("✔", "✔")
            .replace("✘", "✘")
            + "</html>";

        JLabel lblContenido = new JLabel(html);
        lblContenido.setFont(Constantes.FUENTE_NORMAL);
        lblContenido.setForeground(Constantes.COLOR_TEXTO);
        lblContenido.setVerticalAlignment(SwingConstants.TOP);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblContenido, BorderLayout.CENTER);
        return card;
    }
}
