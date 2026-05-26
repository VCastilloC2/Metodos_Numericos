# Métodos Numéricos - Instrucciones de Compilación

## Opción 1: Maven (Recomendado - NetBeans)

1. Abrir el proyecto en **NetBeans**
2. Click derecho en el proyecto → "Clean and Build"
3. Maven descargará automáticamente las dependencias:
   - exp4j 0.4.8 (evaluación de funciones)
   - JFreeChart 1.5.4 (gráficas)
   - FlatLaf 3.4 (interfaz moderna)
4. Ejecutar con: Run → Run Project

## Opción 2: Comando Maven (terminal)
```bash
cd MetodosNumericos
mvn clean package
java -jar target/MetodosNumericos-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Opción 3: IntelliJ IDEA
1. File → Open → Seleccionar carpeta MetodosNumericos
2. Maven se detecta automáticamente
3. Run → Run 'MainApp'

## Dependencias (pom.xml las descarga automáticamente)
- net.objecthunter:exp4j:0.4.8
- org.jfree:jfreechart:1.5.4
- com.formdev:flatlaf:3.4
- com.formdev:flatlaf-extras:3.4

## Estructura del proyecto
```
MetodosNumericos/
├── pom.xml
└── src/main/java/com/metodosnumericos/
    ├── ui/
    │   ├── MainApp.java          ← Punto de entrada
    │   ├── MainFrame.java        ← Ventana principal + navegación
    │   └── panels/
    │       ├── InicioPanel.java  ← Pantalla de bienvenida
    │       ├── MetodoPanel.java  ← Panel reutilizable para todos los métodos
    │       ├── IntegrantesPanel.java
    │       └── InfoPanel.java
    ├── logic/
    │   └── MetodosIntegracion.java ← Todos los cálculos matemáticos
    ├── models/
    │   └── ResultadoIntegracion.java
    ├── utils/
    │   ├── Constantes.java        ← Colores y estilos
    │   └── EvaluadorMatematico.java
    └── charts/
        └── GraficaIntegracion.java ← Gráficas con JFreeChart
```
