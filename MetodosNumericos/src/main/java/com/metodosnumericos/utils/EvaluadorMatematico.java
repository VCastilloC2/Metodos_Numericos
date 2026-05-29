package com.metodosnumericos.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 * Utilidad para evaluar expresiones matemáticas como strings.
 * Soporta: sin, cos, tan, log, ln, sqrt, cbrt, nthrt, exp, pi, e, potencias con ^
 */
public class EvaluadorMatematico {

    // ── Raíz cúbica real: funciona con números negativos ─────────────────────
    // Math.cbrt(-8) = -2  ✓   (pow(-8, 1.0/3) = NaN en Java)
    private static final Function CBRT = new Function("cbrt", 1) {
        @Override
        public double apply(double... args) {
            return Math.cbrt(args[0]);
        }
    };

    // ── Raíz n-ésima real: nthrt(base, n) ────────────────────────────────────
    // Si n es entero impar y base negativa, devuelve el resultado real correcto
    private static final Function NTHRT = new Function("nthrt", 2) {
        @Override
        public double apply(double... args) {
            double base = args[0];
            double n    = args[1];
            if (base < 0) {
                long ni = Math.round(n);
                if (ni % 2 != 0) {
                    return -Math.pow(-base, 1.0 / n);  // raíz impar de negativo ✓
                } else {
                    return Double.NaN;  // raíz par de negativo no existe en reales
                }
            }
            return Math.pow(base, 1.0 / n);
        }
    };

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Evalúa f(x) para un valor dado de x.
     */
    public static double evaluar(String funcion, double x) throws Exception {
        String expr = preprocesar(funcion);

        Expression e = new ExpressionBuilder(expr)
                .variable("x")
                .functions(CBRT, NTHRT)   // registrar funciones personalizadas
                .build()
                .setVariable("x", x);

        double resultado = e.evaluate();

        if (Double.isNaN(resultado) || Double.isInfinite(resultado)) {
            throw new ArithmeticException("Resultado indefinido para x = " + x);
        }
        return resultado;
    }

    /**
     * Valida que la expresión sea evaluable en el punto dado.
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
     */
    private static String preprocesar(String funcion) {
        if (funcion == null) return "0";
        String expr = funcion.trim();

        // ln(  →  log(   (exp4j usa log() para logaritmo natural)
        expr = expr.replaceAll("\\bln\\(", "log(");

        // pow(algo, 1.0/3)  →  cbrt(algo)   para que funcione con negativos
        expr = expr.replaceAll("pow\\((.+?),\\s*1\\.0/3\\)", "cbrt($1)");

        // pow(algo, 1.0/4)  →  nthrt(algo, 4)
        expr = expr.replaceAll("pow\\((.+?),\\s*1\\.0/4\\)", "nthrt($1,4)");

        return expr;
    }

    /**
     * Devuelve un mensaje de error amigable para el usuario.
     * @param e
     * @return 
     */
    public static String mensajeError(Exception e) {
        String msg = e.getMessage();
        if (msg == null) return "Error desconocido en la función.";
        if (msg.contains("Unknown function"))
            return "Función desconocida. Use: sin, cos, tan, sqrt, cbrt, log, ln, exp";
        if (msg.contains("variable"))
            return "Variable no definida. Use 'x' como variable.";
        if (msg.contains("definida")) return msg;
        return "Error en la expresión: " + msg;
    }
}