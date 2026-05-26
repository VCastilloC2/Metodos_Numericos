package com.metodosnumericos.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada principal de la aplicación.
 * Métodos Numéricos - Integración Definida
 * Fundación Universitaria Tecnológico Comfenalco
 */
public class MainApp {

    public static void main(String[] args) {
        // Configurar propiedades de FlatLaf ANTES de inicializar
        FlatLightLaf.setup();
        configurarUIDefaults();

        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error al iniciar la aplicación:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Configura los valores por defecto del Look & Feel.
     */
    private static void configurarUIDefaults() {
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 8);
        UIManager.put("ProgressBar.arc", 6);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("TabbedPane.showTabSeparators", true);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
    }
}
