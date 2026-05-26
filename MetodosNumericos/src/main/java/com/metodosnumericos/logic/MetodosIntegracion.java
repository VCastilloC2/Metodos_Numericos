package com.metodosnumericos.logic;

import com.metodosnumericos.models.ResultadoIntegracion;
import com.metodosnumericos.models.ResultadoIntegracion.FilaTabla;
import com.metodosnumericos.utils.EvaluadorMatematico;

/**
 * Implementación de todos los métodos de integración numérica.
 *
 * Métodos disponibles:
 *   1. Trapecio
 *   2. Jorge Boole
 *   3. Simpson 1/3 (Newton-Cotes cerrado)
 *   4. Simpson 3/8 (Newton-Cotes cerrado)
 *   5. Simpson Abierto (n particiones pares)
 */
public class MetodosIntegracion {

    // ═══════════════════════════════════════════════════════════════════════════
    // 1. MÉTODO TRAPEZOIDAL
    // I = (Δ/2)[f(x0) + 2f(x1) + 2f(x2) + ... + 2f(x_{n-1}) + f(xn)]
    // Δ = (b - a) / n
    // ═══════════════════════════════════════════════════════════════════════════

    public static ResultadoIntegracion trapecio(String funcion, double a, double b, int n) {
        ResultadoIntegracion res = new ResultadoIntegracion();
        res.setFuncion(funcion);
        res.setA(a);
        res.setB(b);
        res.setN(n);
        res.setMetodo("Trapecio");

        try {
            double delta = (b - a) / n;
            res.setDelta(delta);

            double suma = 0.0;

            for (int i = 0; i <= n; i++) {
                double xi  = a + i * delta;
                double fxi = EvaluadorMatematico.evaluar(funcion, xi);
                double coef;
                String op;

                if (i == 0 || i == n) {
                    coef = 1.0;
                    op = "f(x" + i + ")";
                } else {
                    coef = 2.0;
                    op = "2·f(x" + i + ")";
                }

                double parcial = coef * fxi;
                suma += parcial;
                res.agregarFila(new FilaTabla(i, xi, fxi, op, parcial));
            }

            res.setSumatoria(suma);
            res.setIntegral((delta / 2.0) * suma);

        } catch (Exception e) {
            res.setMensajeError(EvaluadorMatematico.mensajeError(e));
        }

        return res;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 2. MÉTODO JORGE BOOLE
    // I = (2Δ/45)[7f(x1) + 32f(x2) + 12f(x3) + 32f(x4) + 7f(x5)]
    // Δ = (b - a) / 4
    // Requiere exactamente 4 subintervalos (5 puntos)
    // ═══════════════════════════════════════════════════════════════════════════

    public static ResultadoIntegracion jorgeBoole(String funcion, double a, double b) {
        ResultadoIntegracion res = new ResultadoIntegracion();
        res.setFuncion(funcion);
        res.setA(a);
        res.setB(b);
        res.setN(4);
        res.setMetodo("Jorge Boole");

        try {
            double delta = (b - a) / 4.0;
            res.setDelta(delta);

            double[] coefs = {7, 32, 12, 32, 7};
            String[] ops   = {"7·f(x1)", "32·f(x2)", "12·f(x3)", "32·f(x4)", "7·f(x5)"};
            double suma = 0.0;

            for (int i = 0; i <= 4; i++) {
                double xi  = a + i * delta;
                double fxi = EvaluadorMatematico.evaluar(funcion, xi);
                double parcial = coefs[i] * fxi;
                suma += parcial;
                res.agregarFila(new FilaTabla(i + 1, xi, fxi, ops[i], parcial));
            }

            res.setSumatoria(suma);
            res.setIntegral((2.0 * delta / 45.0) * suma);

        } catch (Exception e) {
            res.setMensajeError(EvaluadorMatematico.mensajeError(e));
        }

        return res;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 3. MÉTODO SIMPSON 1/3 (Newton-Cotes)
    // I = (Δ/3)[f(x1) + 4f(x2) + f(x3)]
    // Δ = (b - a) / 2
    // Requiere exactamente 2 subintervalos (3 puntos)
    // ═══════════════════════════════════════════════════════════════════════════

    public static ResultadoIntegracion simpson13(String funcion, double a, double b) {
        ResultadoIntegracion res = new ResultadoIntegracion();
        res.setFuncion(funcion);
        res.setA(a);
        res.setB(b);
        res.setN(2);
        res.setMetodo("Simpson 1/3 - Newton-Cotes");

        try {
            double delta = (b - a) / 2.0;
            res.setDelta(delta);

            double[] coefs = {1, 4, 1};
            String[] ops   = {"f(x1)", "4·f(x2)", "f(x3)"};
            double suma = 0.0;

            for (int i = 0; i <= 2; i++) {
                double xi  = a + i * delta;
                double fxi = EvaluadorMatematico.evaluar(funcion, xi);
                double parcial = coefs[i] * fxi;
                suma += parcial;
                res.agregarFila(new FilaTabla(i + 1, xi, fxi, ops[i], parcial));
            }

            res.setSumatoria(suma);
            res.setIntegral((delta / 3.0) * suma);

        } catch (Exception e) {
            res.setMensajeError(EvaluadorMatematico.mensajeError(e));
        }

        return res;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 4. MÉTODO SIMPSON 3/8 (Newton-Cotes)
    // I = (3Δ/8)[f(x1) + 3f(x2) + 3f(x3) + f(x4)]
    // Δ = (b - a) / 3
    // Requiere exactamente 3 subintervalos (4 puntos)
    // ═══════════════════════════════════════════════════════════════════════════

    public static ResultadoIntegracion simpson38(String funcion, double a, double b) {
        ResultadoIntegracion res = new ResultadoIntegracion();
        res.setFuncion(funcion);
        res.setA(a);
        res.setB(b);
        res.setN(3);
        res.setMetodo("Simpson 3/8 - Newton-Cotes");

        try {
            double delta = (b - a) / 3.0;
            res.setDelta(delta);

            double[] coefs = {1, 3, 3, 1};
            String[] ops   = {"f(x1)", "3·f(x2)", "3·f(x3)", "f(x4)"};
            double suma = 0.0;

            for (int i = 0; i <= 3; i++) {
                double xi  = a + i * delta;
                double fxi = EvaluadorMatematico.evaluar(funcion, xi);
                double parcial = coefs[i] * fxi;
                suma += parcial;
                res.agregarFila(new FilaTabla(i + 1, xi, fxi, ops[i], parcial));
            }

            res.setSumatoria(suma);
            res.setIntegral((3.0 * delta / 8.0) * suma);

        } catch (Exception e) {
            res.setMensajeError(EvaluadorMatematico.mensajeError(e));
        }

        return res;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 5. MÉTODO SIMPSON ABIERTO (n particiones pares)
    // I = (Δ/3)[f(x1) + 4f(x2) + 2f(x3) + 4f(x4) + ... + f(xn)]
    // Δ = (b - a) / n   (n debe ser par)
    // Usa los puntos internos (sin incluir extremos a y b)
    // ═══════════════════════════════════════════════════════════════════════════

    public static ResultadoIntegracion simpsonAbierto(String funcion, double a, double b, int n) {
        ResultadoIntegracion res = new ResultadoIntegracion();
        res.setFuncion(funcion);
        res.setA(a);
        res.setB(b);
        res.setN(n);
        res.setMetodo("Simpson Abierto");

        // Validar que n sea par
        if (n % 2 != 0) {
            res.setMensajeError("El número de particiones debe ser PAR para Simpson Abierto.");
            return res;
        }

        try {
            double delta = (b - a) / n;
            res.setDelta(delta);

            double suma = 0.0;

            for (int i = 1; i <= n - 1; i++) {  // Puntos internos
                double xi  = a + i * delta;
                double fxi = EvaluadorMatematico.evaluar(funcion, xi);
                double coef;
                String op;

                if (i % 2 != 0) {          // Índice impar → coeficiente 4
                    coef = 4.0;
                    op = "4·f(x" + i + ")";
                } else {                    // Índice par → coeficiente 2
                    coef = 2.0;
                    op = "2·f(x" + i + ")";
                }

                double parcial = coef * fxi;
                suma += parcial;
                res.agregarFila(new FilaTabla(i, xi, fxi, op, parcial));
            }

            // También incluir los extremos con coeficiente 1
            double fa = EvaluadorMatematico.evaluar(funcion, a);
            double fb = EvaluadorMatematico.evaluar(funcion, b);
            // En Simpson abierto clásico los extremos NO se incluyen.
            // Sin embargo la variante compuesta los incluye:
            suma += fa + fb;
            res.agregarFila(new FilaTabla(0,   a, fa, "f(x0)", fa));
            res.agregarFila(new FilaTabla(n,   b, fb, "f(xn)", fb));

            res.setSumatoria(suma);
            res.setIntegral((delta / 3.0) * suma);

        } catch (Exception e) {
            res.setMensajeError(EvaluadorMatematico.mensajeError(e));
        }

        return res;
    }

    // ─── Utilidad: calcular Δ según método ───────────────────────────────────

    public static double calcularDelta(String metodo, double a, double b, int n) {
        return switch (metodo) {
            case "BOOLE"      -> (b - a) / 4.0;
            case "SIMPSON13"  -> (b - a) / 2.0;
            case "SIMPSON38"  -> (b - a) / 3.0;
            default           -> (b - a) / n;
        };
    }
}
