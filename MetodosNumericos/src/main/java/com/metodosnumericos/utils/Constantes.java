package com.metodosnumericos.utils;

import java.awt.*;

/**
 * Constantes globales de la aplicación: colores, fuentes, dimensiones.
 */
public class Constantes {

    // ─── Colores modo claro ───────────────────────────────────────────────────

    public static Color COLOR_PRIMARIO      = new Color(13, 71, 161);   // Azul marino
    public static Color COLOR_PRIMARIO_CLR  = new Color(25, 118, 210);  // Azul medio
    public static Color COLOR_ACENTO        = new Color(0, 188, 212);   // Cyan
    public static Color COLOR_FONDO         = new Color(245, 248, 252); // Blanco azulado
    public static Color COLOR_SIDEBAR       = new Color(18, 52, 99);    // Azul muy oscuro
    public static Color COLOR_SIDEBAR_HOVER = new Color(30, 80, 140);   // Hover sidebar
    public static Color COLOR_TEXTO_SIDEBAR = new Color(180, 210, 240); // Texto sidebar
    public static Color COLOR_TARJETA       = Color.WHITE;
    public static Color COLOR_TEXTO         = new Color(20, 40, 70);
    public static Color COLOR_TEXTO_SEC     = new Color(90, 110, 140);
    public static Color COLOR_BORDE         = new Color(210, 225, 240);
    public static Color COLOR_EXITO         = new Color(0, 150, 100);
    public static Color COLOR_ERROR         = new Color(200, 50, 50);
    public static Color COLOR_ADVERTENCIA   = new Color(230, 130, 0);
    public static Color COLOR_TABLA_HEADER  = new Color(13, 71, 161);
    public static Color COLOR_TABLA_ALT     = new Color(235, 243, 253);

    // ─── Fuentes ──────────────────────────────────────────────────────────────

    public static final Font FUENTE_TITULO   = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FUENTE_SUBTIT   = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FUENTE_NORMAL   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUEÑA  = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FUENTE_MONO     = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font FUENTE_FORMULA  = new Font("Serif", Font.ITALIC, 14);

    // ─── Dimensiones ─────────────────────────────────────────────────────────

    public static final int RADIO_BORDE   = 10;
    public static final int PADDING       = 16;
    public static final int GAP           = 10;

    // ─── Modo oscuro / claro ──────────────────────────────────────────────────

    public static void aplicarModoOscuro() {
        COLOR_FONDO         = new Color(18, 18, 18);
        COLOR_TARJETA       = new Color(30, 30, 30);
        COLOR_TEXTO         = new Color(220, 230, 240);
        COLOR_TEXTO_SEC     = new Color(140, 160, 180);
        COLOR_BORDE         = new Color(50, 60, 80);
        COLOR_TABLA_ALT     = new Color(35, 40, 50);
    }

    public static void aplicarModoClaro() {
        COLOR_FONDO         = new Color(245, 248, 252);
        COLOR_TARJETA       = Color.WHITE;
        COLOR_TEXTO         = new Color(20, 40, 70);
        COLOR_TEXTO_SEC     = new Color(90, 110, 140);
        COLOR_BORDE         = new Color(210, 225, 240);
        COLOR_TABLA_ALT     = new Color(235, 243, 253);
    }
}
