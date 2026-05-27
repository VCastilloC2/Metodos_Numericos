package com.metodosnumericos.ui.panels;

import com.metodosnumericos.charts.GraficaIntegracion;
import com.metodosnumericos.logic.MetodosIntegracion;
import com.metodosnumericos.models.ResultadoIntegracion;
import com.metodosnumericos.ui.MainFrame;
import com.metodosnumericos.utils.Constantes;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;

/**
 * Panel reutilizable para todos los métodos de integración numérica.
 * Cambia de comportamiento según el metodoId recibido.
 */
public class MetodoPanel extends JPanel {

    private final MainFrame frame;
    private final String metodoId;

    // Campos de entrada
    private JTextField txtFuncion;
    private JTextField txtA;
    private JTextField txtB;
    private JTextField txtDelta;
    private JTextField txtN;

    // Tabla
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Resultado
    private JLabel lblSumatoria;
    private JLabel lblResultado;
    private JLabel lblDeltaCalc;

    // Área gráfica
    private JPanel panelGrafica;

    public MetodoPanel(MainFrame frame, String metodoId) {
        this.frame    = frame;
        this.metodoId = metodoId;
        setLayout(new BorderLayout(0, 0));
        setBackground(Constantes.COLOR_FONDO);
        construirUI();
    }

    // ─── Construcción de la UI ────────────────────────────────────────────────

    private void construirUI() {
        // Header del método
        add(crearHeader(), BorderLayout.NORTH);

        // Panel central dividido: formulario izq + tabla/gráfica der
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelFormulario(),
            crearPanelResultados()
        );
        split.setDividerLocation(420);
        split.setResizeWeight(0.35);
        split.setBorder(null);
        split.setBackground(Constantes.COLOR_FONDO);
        add(split, BorderLayout.CENTER);
    }

    // ─── Header ──────────────────────────────────────────────────────────────

    private JPanel crearHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(Constantes.COLOR_PRIMARIO);
        hdr.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel(obtenerNombreMetodo());
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JLabel formula = new JLabel("<html><i>" + obtenerFormula() + "</i></html>");
        formula.setFont(new Font("Serif", Font.ITALIC, 13));
        formula.setForeground(new Color(180, 215, 250));

        hdr.add(titulo, BorderLayout.WEST);
        hdr.add(formula, BorderLayout.EAST);
        return hdr;
    }

    // ─── Formulario izquierdo ─────────────────────────────────────────────────

    private JScrollPane crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Constantes.COLOR_FONDO);
        panel.setBorder(new EmptyBorder(16, 16, 16, 8));

        // ── Función ──
        panel.add(crearLabelSeccion("Función f(x)"));
        txtFuncion = crearCampo("sqrt(x+5)", "Ej: x^2+sin(x), sqrt(x+5), exp(x)");
        panel.add(txtFuncion);
        panel.add(Box.createVerticalStrut(4));
        panel.add(crearBotonesRapidos());
        panel.add(Box.createVerticalStrut(12));

        // ── Límites ──
        panel.add(crearLabelSeccion("Límites de integración"));
        JPanel filaLimites = new JPanel(new GridLayout(1, 2, 8, 0));
        filaLimites.setOpaque(false);
        filaLimites.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        JPanel panA = new JPanel(new BorderLayout(0, 2));
        panA.setOpaque(false);
        panA.add(crearEtiqueta("Límite inferior  a ="), BorderLayout.NORTH);
        txtA = crearCampo("0", "Límite inferior");
        panA.add(txtA, BorderLayout.CENTER);

        JPanel panB = new JPanel(new BorderLayout(0, 2));
        panB.setOpaque(false);
        panB.add(crearEtiqueta("Límite superior  b ="), BorderLayout.NORTH);
        txtB = crearCampo("19", "Límite superior");
        panB.add(txtB, BorderLayout.CENTER);

        filaLimites.add(panA);
        filaLimites.add(panB);
        panel.add(filaLimites);
        panel.add(Box.createVerticalStrut(12));

        // ── Delta y N ──
        panel.add(crearLabelSeccion("Parámetros"));
        JPanel filaParams = new JPanel(new GridLayout(1, 2, 8, 0));
        filaParams.setOpaque(false);
        filaParams.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        JPanel panDelta = new JPanel(new BorderLayout(0, 2));
        panDelta.setOpaque(false);
        panDelta.add(crearEtiqueta("Δ (Delta) ="), BorderLayout.NORTH);
        txtDelta = crearCampo(obtenerDeltaDefault(), "Paso Δ");
        txtDelta.setToolTipText("Si no se ingresa, se calcula automáticamente");
        panDelta.add(txtDelta, BorderLayout.CENTER);

        JPanel panN = new JPanel(new BorderLayout(0, 2));
        panN.setOpaque(false);
        panN.add(crearEtiqueta("n (particiones) ="), BorderLayout.NORTH);
        txtN = crearCampo(obtenerNDefault(), "Número de particiones");
        // Bloquear n para métodos fijos
        if (esNFijo()) txtN.setEnabled(false);
        panN.add(txtN, BorderLayout.CENTER);

        filaParams.add(panDelta);
        filaParams.add(panN);
        panel.add(filaParams);
        panel.add(Box.createVerticalStrut(20));

        // ── Botones ──
        panel.add(crearPanelBotones());
        panel.add(Box.createVerticalStrut(16));

        // ── Info fórmula ──
        panel.add(crearTarjetaFormula());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        return scroll;
    }

    private JPanel crearBotonesRapidos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));

        String[][] btns = {
            {"x²","x^2"}, {"x³","x^3"}, {"√","sqrt()"}, 
            {"sin","sin()"}, {"cos","cos()"}, {"tan","tan()"}, 
            {"log","log()"}, {"ln","ln()"}, {"π","pi"}, 
            {"e","e"}, {"exp","exp()"}
        };

        for (String[] b : btns) {
            JButton btn = new JButton(b[0]);
            btn.setFont(new Font("SansSerif", Font.BOLD, 11));
            btn.setMargin(new Insets(2, 6, 2, 6));
            btn.setBackground(Constantes.COLOR_PRIMARIO);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setToolTipText("Insertar: " + b[1]);
            final String insertar = b[1];
            btn.addActionListener(e -> {
                String actual = txtFuncion.getText();
                int pos = txtFuncion.getCaretPosition();
                txtFuncion.setText(actual.substring(0, pos) + insertar + actual.substring(pos));
                txtFuncion.setCaretPosition(pos + insertar.length());
                txtFuncion.requestFocus();
            });
            p.add(btn);
        }
        return p;
    }

    private JPanel crearPanelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        JButton btnCalc  = crearBotonPrincipal("▶  Calcular", Constantes.COLOR_PRIMARIO);
        JButton btnGraf  = crearBotonPrincipal("📊  Graficar", new Color(0, 130, 100));
        JButton btnLimp  = crearBotonSecundario("✕  Limpiar");

        btnCalc.addActionListener(e -> calcular());
        btnGraf.addActionListener(e -> graficar());
        btnLimp.addActionListener(e -> limpiar());

        p.add(btnCalc);
        p.add(btnGraf);
        p.add(btnLimp);
        return p;
    }

    private JPanel crearTarjetaFormula() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(232, 240, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_PRIMARIO_CLR, 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));

        JLabel titulo = new JLabel("Fórmula:");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        titulo.setForeground(Constantes.COLOR_PRIMARIO);

        JLabel formula = new JLabel("<html>" + obtenerFormulaHTML() + "</html>");
        formula.setFont(new Font("Serif", Font.ITALIC, 13));
        formula.setForeground(Constantes.COLOR_TEXTO);

        card.add(titulo, BorderLayout.NORTH);
        card.add(formula, BorderLayout.CENTER);
        return card;
    }

    // ─── Panel de resultados derecho ─────────────────────────────────────────

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Constantes.COLOR_FONDO);
        panel.setBorder(new EmptyBorder(16, 8, 16, 16));

        // Tabla de procedimiento
        panel.add(crearPanelTabla(), BorderLayout.CENTER);

        // Pie: sumatoria + resultado
        panel.add(crearPanelPie(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Constantes.COLOR_TARJETA);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_BORDE, 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Encabezado de tabla
        JLabel lblTitulo = new JLabel("  Tabla de Procedimiento");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBackground(Constantes.COLOR_TABLA_HEADER);
        lblTitulo.setOpaque(true);
        lblTitulo.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.add(lblTitulo, BorderLayout.NORTH);

        // Modelo de tabla
        String[] columnas = {"i", "xᵢ", "f(xᵢ)", "Operación", "Resultado Parcial"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setFont(Constantes.FUENTE_MONO);
        tabla.setRowHeight(26);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionBackground(new Color(200, 220, 255));

        // Renderizador con filas alternas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : Constantes.COLOR_TABLA_ALT);
                }
                setFont(Constantes.FUENTE_MONO);
                if (col >= 1) setHorizontalAlignment(RIGHT);
                else setHorizontalAlignment(CENTER);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        // Header personalizado
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(new Color(180, 200, 230));
        header.setForeground(Constantes.COLOR_PRIMARIO);
        header.setReorderingAllowed(false);

        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelPie() {
        JPanel pie = new JPanel(new GridLayout(3, 1, 0, 4));
        pie.setOpaque(false);
        pie.setBorder(new EmptyBorder(8, 0, 0, 0));

        lblDeltaCalc = crearLabelResultado("Δ calculado: —", Constantes.COLOR_TEXTO_SEC);
        lblSumatoria = crearLabelResultado("Sumatoria: —", Constantes.COLOR_TEXTO_SEC);
        lblResultado = crearLabelResultado("Resultado integral: —", Constantes.COLOR_PRIMARIO);
        lblResultado.setFont(new Font("SansSerif", Font.BOLD, 16));

        pie.add(lblDeltaCalc);
        pie.add(lblSumatoria);
        pie.add(lblResultado);
        return pie;
    }

    // ─── Lógica de cálculo ────────────────────────────────────────────────────

    private void calcular() {
        // Validar campos
        String funcion = txtFuncion.getText().trim();
        if (funcion.isEmpty()) {
            mostrarError("Ingrese una función f(x).");
            return;
        }

        double a, b;
        try {
            a = Double.parseDouble(txtA.getText().trim());
            b = Double.parseDouble(txtB.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("Los límites a y b deben ser números válidos.");
            return;
        }

        if (a >= b) {
            mostrarError("El límite inferior 'a' debe ser menor que el superior 'b'.");
            return;
        }

        int n = obtenerN(a, b);
        if (n <= 0) return;

        // Mostrar loader en el label
        lblResultado.setText("Calculando...");
        lblResultado.setForeground(Constantes.COLOR_ADVERTENCIA);

        // Calcular en hilo aparte para no bloquear UI
        SwingWorker<ResultadoIntegracion, Void> worker = new SwingWorker<>() {
            @Override protected ResultadoIntegracion doInBackground() {
                return ejecutarMetodo(funcion, a, b, n);
            }
            @Override protected void done() {
                try {
                    ResultadoIntegracion res = get();
                    mostrarResultado(res);
                } catch (InterruptedException | ExecutionException ex) {
                    mostrarError("Error inesperado: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private ResultadoIntegracion ejecutarMetodo(String funcion, double a, double b, int n) {
        return switch (metodoId) {
            case MainFrame.PANEL_TRAPECIO     -> MetodosIntegracion.trapecio(funcion, a, b, n);
            case MainFrame.PANEL_BOOLE        -> MetodosIntegracion.jorgeBoole(funcion, a, b);
            case MainFrame.PANEL_SIMPSON13    -> MetodosIntegracion.simpson13(funcion, a, b);
            case MainFrame.PANEL_SIMPSON38    -> MetodosIntegracion.simpson38(funcion, a, b);
            case MainFrame.PANEL_SIMP_ABIERTO -> MetodosIntegracion.simpsonAbierto(funcion, a, b, n);
            default -> throw new IllegalStateException("Método desconocido: " + metodoId);
        };
    }

    private void mostrarResultado(ResultadoIntegracion res) {
        if (!res.isExitoso()) {
            mostrarError(res.getMensajeError());
            return;
        }

        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Llenar tabla
        for (ResultadoIntegracion.FilaTabla fila : res.getFilas()) {
            modeloTabla.addRow(new Object[]{
                fila.indice,
                String.format("%.6f", fila.xi),
                String.format("%.10f", fila.fxi),
                fila.operacion,
                String.format("%.10f", fila.resultado)
            });
        }

        // Actualizar etiquetas
        lblDeltaCalc.setText(String.format("  Δ = %.6f", res.getDelta()));
        lblSumatoria.setText(String.format("  Sumatoria = %.10f", res.getSumatoria()));
        lblResultado.setText(String.format("  I = %.10f", res.getIntegral()));
        lblResultado.setForeground(Constantes.COLOR_EXITO);

        // Actualizar campo delta en el formulario
        txtDelta.setText(String.format("%.6f", res.getDelta()));
    }

    private void graficar() {
        String funcion = txtFuncion.getText().trim();
        if (funcion.isEmpty()) { mostrarError("Ingrese una función f(x)."); return; }

        double a, b;
        try {
            a = Double.parseDouble(txtA.getText().trim());
            b = Double.parseDouble(txtB.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("Ingrese valores válidos para a y b.");
            return;
        }

        int n = obtenerN(a, b);
        if (n <= 0) return;

        GraficaIntegracion.mostrarGrafica(frame, funcion, a, b, n, obtenerNombreMetodo());
    }

    private int obtenerN(double a, double b) {
        if (esNFijo()) {
            return switch (metodoId) {
                case MainFrame.PANEL_BOOLE     -> 4;
                case MainFrame.PANEL_SIMPSON13 -> 2;
                case MainFrame.PANEL_SIMPSON38 -> 3;
                default -> 2;
            };
        }
        try {
            int n = Integer.parseInt(txtN.getText().trim());
            if (n <= 0) throw new NumberFormatException();

            // Simpson Abierto requiere n par
            if (metodoId.equals(MainFrame.PANEL_SIMP_ABIERTO) && n % 2 != 0) {
                mostrarError("Para Simpson Abierto, n debe ser un número PAR.");
                return -1;
            }
            return n;
        } catch (NumberFormatException e) {
            mostrarError("El número de particiones debe ser un entero positivo.");
            return -1;
        }
    }

    private void limpiar() {
        txtFuncion.setText("");
        txtA.setText("0");
        txtB.setText("");
        txtDelta.setText(obtenerDeltaDefault());
        if (!esNFijo()) txtN.setText(obtenerNDefault());
        modeloTabla.setRowCount(0);
        lblSumatoria.setText("Sumatoria: —");
        lblResultado.setText("Resultado integral: —");
        lblResultado.setForeground(Constantes.COLOR_TEXTO);
        lblDeltaCalc.setText("Δ calculado: —");
    }

    private void mostrarError(String mensaje) {
        lblResultado.setText("  Error: " + mensaje);
        lblResultado.setForeground(Constantes.COLOR_ERROR);
        JOptionPane.showMessageDialog(this, mensaje, "Error de validación",
            JOptionPane.WARNING_MESSAGE);
    }

    // ─── Helpers por método ───────────────────────────────────────────────────

    private String obtenerNombreMetodo() {
        return switch (metodoId) {
            case MainFrame.PANEL_TRAPECIO      -> "Método Trapezoidal";
            case MainFrame.PANEL_BOOLE         -> "Método Jorge Boole";
            case MainFrame.PANEL_SIMPSON13     -> "Método Simpson 1/3 - Newton-Cotes";
            case MainFrame.PANEL_SIMPSON38     -> "Método Simpson 3/8 - Newton-Cotes";
            case MainFrame.PANEL_SIMP_ABIERTO  -> "Simpson Abierto (n par)";
            default -> "Método de Integración";
        };
    }

    private String obtenerFormula() {
        return switch (metodoId) {
            case MainFrame.PANEL_TRAPECIO      -> "I = (Δ/2)[f(x₀) + 2f(x₁) + ... + f(xₙ)]";
            case MainFrame.PANEL_BOOLE         -> "I = (2Δ/45)[7f(x₁)+32f(x₂)+12f(x₃)+32f(x₄)+7f(x₅)]";
            case MainFrame.PANEL_SIMPSON13     -> "I = (Δ/3)[f(x₁) + 4f(x₂) + f(x₃)]";
            case MainFrame.PANEL_SIMPSON38     -> "I = (3Δ/8)[f(x₁)+3f(x₂)+3f(x₃)+f(x₄)]";
            case MainFrame.PANEL_SIMP_ABIERTO  -> "I = (Δ/3)[f(x₁)+4f(x₂)+2f(x₃)+4f(x₄)+...+f(xₙ)]";
            default -> "";
        };
    }

    private String obtenerFormulaHTML() {
        return switch (metodoId) {
            case MainFrame.PANEL_TRAPECIO ->
                "I = <b>Δ/2</b> [f(x<sub>0</sub>) + 2f(x<sub>1</sub>) + ... + 2f(x<sub>n-1</sub>) + f(x<sub>n</sub>)]" +
                "<br>Δ = (b - a) / n";
            case MainFrame.PANEL_BOOLE ->
                "I = <b>2Δ/45</b> [7f(x<sub>1</sub>) + 32f(x<sub>2</sub>) + 12f(x<sub>3</sub>) + 32f(x<sub>4</sub>) + 7f(x<sub>5</sub>)]" +
                "<br>Δ = (b - a) / 4";
            case MainFrame.PANEL_SIMPSON13 ->
                "I = <b>Δ/3</b> [f(x<sub>1</sub>) + 4f(x<sub>2</sub>) + f(x<sub>3</sub>)]" +
                "<br>Δ = (b - a) / 2";
            case MainFrame.PANEL_SIMPSON38 ->
                "I = <b>3Δ/8</b> [f(x<sub>1</sub>) + 3f(x<sub>2</sub>) + 3f(x<sub>3</sub>) + f(x<sub>4</sub>)]" +
                "<br>Δ = (b - a) / 3";
            case MainFrame.PANEL_SIMP_ABIERTO ->
                "I = <b>Δ/3</b> [f(x<sub>1</sub>) + 4f(x<sub>2</sub>) + 2f(x<sub>3</sub>) + ... + f(x<sub>n</sub>)]" +
                "<br>Δ = (b - a) / n &nbsp;&nbsp; (n debe ser PAR)";
            default -> "";
        };
    }

    private String obtenerDeltaDefault() {
        return "9.5"; // Como en la imagen: Δ = 9.5 para a=0, b=19, n=2
    }

    private String obtenerNDefault() {
        return switch (metodoId) {
            case MainFrame.PANEL_BOOLE     -> "4";
            case MainFrame.PANEL_SIMPSON13 -> "2";
            case MainFrame.PANEL_SIMPSON38 -> "3";
            default -> "2";
        };
    }

    private boolean esNFijo() {
        return metodoId.equals(MainFrame.PANEL_BOOLE)
            || metodoId.equals(MainFrame.PANEL_SIMPSON13)
            || metodoId.equals(MainFrame.PANEL_SIMPSON38);
    }

    // ─── Helpers de componentes ───────────────────────────────────────────────

    private JLabel crearLabelSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(Constantes.COLOR_PRIMARIO);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Constantes.FUENTE_PEQUEÑA);
        lbl.setForeground(Constantes.COLOR_TEXTO_SEC);
        return lbl;
    }

    private JTextField crearCampo(String valorDefault, String tooltip) {
        JTextField tf = new JTextField(valorDefault);
        tf.setFont(Constantes.FUENTE_NORMAL);
        tf.setToolTipText(tooltip);
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_BORDE, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    private JButton crearBotonPrincipal(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 36));
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBackground(new Color(220, 230, 240));
        btn.setForeground(Constantes.COLOR_TEXTO);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
        return btn;
    }

    private JLabel crearLabelResultado(String texto, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(color);
        lbl.setBackground(Constantes.COLOR_TARJETA);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Constantes.COLOR_BORDE, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        return lbl;
    }
}