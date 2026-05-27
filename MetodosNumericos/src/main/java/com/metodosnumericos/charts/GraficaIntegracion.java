package com.metodosnumericos.charts;

import com.metodosnumericos.utils.Constantes;
import com.metodosnumericos.utils.EvaluadorMatematico;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;

/**
 * Clase para generar y mostrar la gráfica de la función con el área bajo la curva.
 * Usa JFreeChart para gráficas de alta calidad.
 */
public class GraficaIntegracion {

    /**
     * Muestra un diálogo con la gráfica de la función f(x) y el área bajo la curva.
     *
     * @param parent  ventana padre
     * @param funcion expresión matemática
     * @param a       límite inferior
     * @param b       límite superior
     * @param n       número de particiones (para los puntos Xi)
     * @param metodo  nombre del método
     */
    public static void mostrarGrafica(JFrame parent, String funcion,
                                      double a, double b, int n, String metodo) {
        JDialog dialogo = new JDialog(parent, "Gráfica — " + metodo, true);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(850, 600);
        dialogo.setLocationRelativeTo(parent);

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(Constantes.COLOR_PRIMARIO);
        hdr.setBorder(new EmptyBorder(10, 16, 10, 16));
        JLabel lblT = new JLabel("📊  Gráfica de f(x) = " + funcion);
        lblT.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblT.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Método: " + metodo + "   |   [" + a + ", " + b + "]   |   n = " + n);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(180, 215, 255));
        JPanel hdrTexto = new JPanel(new BorderLayout(0,2));
        hdrTexto.setOpaque(false);
        hdrTexto.add(lblT, BorderLayout.CENTER);
        hdrTexto.add(lblSub, BorderLayout.SOUTH);
        hdr.add(hdrTexto, BorderLayout.WEST);
        dialogo.add(hdr, BorderLayout.NORTH);

        // Crear la gráfica
        try {
            JFreeChart chart = crearGrafica(funcion, a, b, n);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setMouseWheelEnabled(true);  // Zoom con rueda
            chartPanel.setZoomInFactor(0.8);
            chartPanel.setZoomOutFactor(1.25);
            chartPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
            dialogo.add(chartPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errLbl = new JLabel("Error al generar gráfica: " + e.getMessage(), SwingConstants.CENTER);
            errLbl.setForeground(Constantes.COLOR_ERROR);
            dialogo.add(errLbl, BorderLayout.CENTER);
        }

        // Pie con botón cerrar
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setBackground(new Color(245, 248, 252));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(Constantes.COLOR_PRIMARIO);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        pie.add(btnCerrar);
        dialogo.add(pie, BorderLayout.SOUTH);

        dialogo.setVisible(true);
    }

    /**
     * Crea el objeto JFreeChart con la función, el área sombreada y los puntos Xi.
     */
    private static JFreeChart crearGrafica(String funcion, double a, double b, int n) throws Exception {
        // ── Dataset 1: Línea de la función (intervalo ampliado) ──────────────
        XYSeries serieFuncion = new XYSeries("f(x) = " + funcion);
        double margen  = (b - a) * 0.15;
        double xMin    = a - margen;
        double xMax    = b + margen;
        int    puntos  = 500;
        double paso    = (xMax - xMin) / puntos;

        for (int i = 0; i <= puntos; i++) {
            double x = xMin + i * paso;
            try {
                double y = EvaluadorMatematico.evaluar(funcion, x);
                if (!Double.isNaN(y) && !Double.isInfinite(y) && Math.abs(y) < 1e8) {
                    serieFuncion.add(x, y);
                }
            } catch (Exception ignored) {}
        }

        // ── Dataset 2: Área bajo la curva (solo [a,b]) ───────────────────────
        XYSeries serieArea = new XYSeries("Área");
        serieArea.add(a, 0.0);
        double pasoArea = (b - a) / 300;
        for (int i = 0; i <= 300; i++) {
            double x = a + i * pasoArea;
            try {
                double y = EvaluadorMatematico.evaluar(funcion, x);
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    serieArea.add(x, y);
                }
            } catch (Exception ignored) {}
        }
        serieArea.add(b, 0.0);

        // ── Dataset 3: Puntos Xi ─────────────────────────────────────────────
        XYSeries serieXi = new XYSeries("Puntos xᵢ");
        double delta = (b - a) / n;
        for (int i = 0; i <= n; i++) {
            double xi = a + i * delta;
            try {
                double fxi = EvaluadorMatematico.evaluar(funcion, xi);
                serieXi.add(xi, fxi);
            } catch (Exception ignored) {}
        }

        // ── Colecciones ──────────────────────────────────────────────────────
        XYSeriesCollection datasetLinea = new XYSeriesCollection(serieFuncion);
        XYSeriesCollection datasetArea  = new XYSeriesCollection(serieArea);
        XYSeriesCollection datasetXi    = new XYSeriesCollection(serieXi);

        // ── Plot ─────────────────────────────────────────────────────────────
        NumberAxis ejeX = new NumberAxis("X");
        NumberAxis ejeY = new NumberAxis("F(x) = Y");
        ejeX.setAutoRangeIncludesZero(false);
        ejeY.setAutoRangeIncludesZero(true);

        // Área (renderer de área)
        XYAreaRenderer rendererArea = new XYAreaRenderer();
        rendererArea.setSeriesPaint(0, new Color(25, 118, 210, 60));

        // Línea de la función
        XYLineAndShapeRenderer rendererLinea = new XYLineAndShapeRenderer(true, false);
        rendererLinea.setSeriesPaint(0, new Color(13, 71, 161));
        rendererLinea.setSeriesStroke(0, new BasicStroke(2.5f));

        // Puntos Xi
        XYLineAndShapeRenderer rendererXi = new XYLineAndShapeRenderer(false, true);
        rendererXi.setSeriesPaint(0, new Color(220, 0, 0));
        rendererXi.setSeriesShape(0, new Ellipse2D.Double(-5, -5, 10, 10));

        XYPlot plot = new XYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(200, 210, 220));
        plot.setRangeGridlinePaint(new Color(200, 210, 220));
        plot.setOutlinePaint(new Color(180, 200, 220));

        // Dataset 0 = área, 1 = línea, 2 = puntos
        plot.setDataset(0, datasetArea);
        plot.setRenderer(0, rendererArea);
        plot.setDataset(1, datasetLinea);
        plot.setRenderer(1, rendererLinea);
        plot.setDataset(2, datasetXi);
        plot.setRenderer(2, rendererXi);

        plot.setDomainAxis(ejeX);
        plot.setRangeAxis(ejeY);
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 0);
        plot.mapDatasetToRangeAxis(2, 0);

        // ── Chart ────────────────────────────────────────────────────────────
        JFreeChart chart = new JFreeChart(
            "∫ f(x) dx   [" + a + ", " + b + "]",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        chart.setBackgroundPaint(Color.WHITE);
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));

        return chart;
    }
}
