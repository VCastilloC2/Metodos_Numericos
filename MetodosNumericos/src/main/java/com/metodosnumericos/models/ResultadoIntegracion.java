package com.metodosnumericos.models;

import java.util.List;
import java.util.ArrayList;

/**
 * Modelo que contiene los resultados de un cálculo de integración numérica.
 */
public class ResultadoIntegracion {

    private double integral;           // Resultado final
    private double delta;              // Δ (paso)
    private double a;                  // Límite inferior
    private double b;                  // Límite superior
    private int    n;                  // Número de particiones
    private String funcion;            // f(x) original
    private String metodo;             // Nombre del método
    private List<FilaTabla> filas;     // Filas para mostrar en tabla
    private double sumatoria;          // Suma antes de multiplicar por Δ/k
    private boolean exitoso;
    private String mensajeError;

    public ResultadoIntegracion() {
        filas = new ArrayList<>();
        exitoso = true;
    }

    // ─── Fila de tabla ────────────────────────────────────────────────────────

    public static class FilaTabla {
        public int    indice;
        public double xi;
        public double fxi;
        public String operacion;   // descripción de la operación (ej: "4*f(x)")
        public double resultado;   // valor parcial

        public FilaTabla(int indice, double xi, double fxi, String operacion, double resultado) {
            this.indice    = indice;
            this.xi        = xi;
            this.fxi       = fxi;
            this.operacion = operacion;
            this.resultado = resultado;
        }
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public double getIntegral()    { return integral; }
    public void setIntegral(double integral) { this.integral = integral; }

    public double getDelta()       { return delta; }
    public void setDelta(double delta) { this.delta = delta; }

    public double getA()           { return a; }
    public void setA(double a)     { this.a = a; }

    public double getB()           { return b; }
    public void setB(double b)     { this.b = b; }

    public int getN()              { return n; }
    public void setN(int n)        { this.n = n; }

    public String getFuncion()     { return funcion; }
    public void setFuncion(String funcion) { this.funcion = funcion; }

    public String getMetodo()      { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public List<FilaTabla> getFilas() { return filas; }
    public void agregarFila(FilaTabla fila) { filas.add(fila); }

    public double getSumatoria()   { return sumatoria; }
    public void setSumatoria(double sumatoria) { this.sumatoria = sumatoria; }

    public boolean isExitoso()     { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }

    public String getMensajeError() { return mensajeError; }
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
        this.exitoso = false;
    }

    @Override
    public String toString() {
        return String.format("Integral(%s) [%s] de %.4f a %.4f = %.8f",
            funcion, metodo, a, b, integral);
    }
}
