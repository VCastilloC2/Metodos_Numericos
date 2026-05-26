package com.metodosnumericos.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Utilidad para evaluar expresiones matemáticas como strings.
 * Soporta: sin, cos, tan, log, ln, sqrt, exp, pi, e, potencias con ^
 */
public class EvaluadorMatematico {

    /**
     * Evalúa f(x) para un valor dado de x.
     *
     * @param funcion  expresión como "sqrt(x+5)", "x^2+sin(x)", etc.
     * @param x        valor de la variable
     * @return resultado de f(x)
     * @throws Exception si la expresión es inválida
     */
    public static double evaluar(String funcion, double x) throws Exception {
        // Preprocesar: reemplazar notaciones comunes
        String expr = preprocesar(funcion);

        Expression e = new ExpressionBuilder(expr)
                .variable("x")
                .build()
                .setVariable("x", x);

        double resultado = e.evaluate();

        if (Double.isNaN(resultado) || Double.isInfinite(resultado)) {
            throw new ArithmeticException("Resultado indefinido para x = " + x);
        }
        return resultado;
    }

    /**
     * Valida que la expresión sea evaluable en el punto x=1.
     * @param funcion expresión a validar
     * @return true si es válida
     */
    public static boolean esValida(String funcion, double puntoTest) {
        try {
            evaluar(funcion, puntoTest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Preprocesa la expresión para compatibilidad con exp4j.
     * - ln(x) → log(x)    (exp4j usa log para logaritmo natural)
     * - log10(x) → log10(x) queda igual (exp4j soporta log10)
     * - pi → pi  (ya soportado)
     * - e → e   (ya soportado como constante)
     */
    private static String preprocesar(String funcion) {
        if (funcion == null) return "0";
        String expr = funcion.trim();

        // ln → log (logaritmo natural en exp4j es log())
        expr = expr.replaceAll("\\bln\\b", "log");

        // log sin base → asumir natural (ya es log en exp4j)
        // log10 se mantiene si exp4j lo soporta, si no convertir manualmente
        // exp4j soporta log10 directamente

        return expr;
    }

    /**
     * Devuelve un mensaje de error amigable para el usuario.
     */
    public static String mensajeError(Exception e) {
        String msg = e.getMessage();
        if (msg == null) return "Error desconocido en la función.";
        if (msg.contains("Unknown function")) return "Función desconocida. Use: sin, cos, tan, sqrt, log, ln, exp";
        if (msg.contains("variable")) return "Variable no definida. Use 'x' como variable.";
        if (msg.contains("definida")) return msg;
        return "Error en la expresión: " + msg;
    }
}
