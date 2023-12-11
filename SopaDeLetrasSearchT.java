import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SopaDeLetrasSearchT {
    private static volatile boolean palabraEncontrada = false;
    private static volatile String resultadoT="";
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sopa_de_letras.txt"));
            int dimension = Integer.parseInt(reader.readLine().trim());
            String palabraBuscar = reader.readLine().trim();
            char[][] matriz = new char[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                String linea = reader.readLine().trim();
                String[] letras = linea.split(" ");
                for (int j = 0; j < dimension; j++) {
                    matriz[i][j] = letras[j].charAt(0);
                }
            }
            reader.close();

            int umbral = palabraBuscar.length();
            long tiempoInicio = System.nanoTime();
            SopaDeLetrasThreads inicio= new SopaDeLetrasThreads(matriz, palabraBuscar, 0, dimension, 0, dimension, umbral);
            inicio.start();
            
            try {
                inicio.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(resultadoT);
            long tiempoFin = System.nanoTime();
            long tiempoEjecucion = (tiempoFin - tiempoInicio) / 1000000;
            System.out.println("El tiempo de ejecución fue de " + tiempoEjecucion + " milisegundos");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class SopaDeLetrasThreads extends Thread  {
        private char[][] matriz;
        private String palabraBuscar;
        private int filaInicio, filaFin, columnaInicio, columnaFin;
        private int umbral;

        public SopaDeLetrasThreads(char[][] matriz, String palabraBuscar, int filaInicio, int filaFin, int columnaInicio, int columnaFin, int umbral) {
            this.matriz = matriz;
            this.palabraBuscar = palabraBuscar;
            this.filaInicio = filaInicio;
            this.filaFin = filaFin;
            this.columnaInicio = columnaInicio;
            this.columnaFin = columnaFin;
            this.umbral = umbral;
        }
        @Override
        public void run(){
            
            int dimension= filaFin-filaInicio;
            if(dimension<=umbral){
                buscarPalabraEnCuadranteHorizontal();
                buscarPalabraEnCuadranteVertical();                
            }else{
                int mitadFila = filaInicio + dimension / 2;
                int mitadColumna = columnaInicio + dimension / 2;
                SopaDeLetrasThreads cuadranteNorteOeste = new SopaDeLetrasThreads(matriz, palabraBuscar, filaInicio, mitadFila, columnaInicio, mitadColumna, umbral);
                cuadranteNorteOeste.start();
                SopaDeLetrasThreads cuadranteNorteEste = new SopaDeLetrasThreads(matriz, palabraBuscar, filaInicio, mitadFila, mitadColumna, columnaFin, umbral);
                cuadranteNorteEste.start();
                SopaDeLetrasThreads cuadranteSurOeste = new SopaDeLetrasThreads(matriz, palabraBuscar, mitadFila, filaFin, columnaInicio, mitadColumna, umbral);
                cuadranteSurOeste.start();
                SopaDeLetrasThreads cuadranteSurEste = new SopaDeLetrasThreads(matriz, palabraBuscar, mitadFila, filaFin, mitadColumna, columnaFin, umbral);
                cuadranteSurEste.start();
            
                try {
                    cuadranteNorteOeste.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    cuadranteNorteEste.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    cuadranteSurOeste.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    cuadranteSurEste.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        private void buscarPalabraEnCuadranteHorizontal() {
            StringBuilder resultado = new StringBuilder();
            for (int i = filaInicio; i < filaFin; i++) {
                for (int j = columnaInicio; j <= columnaFin - umbral; j++) {
                    if (palabraEncontrada) {
                        return; // Salir si la palabra ya se encontró
                    }
                    StringBuilder palabra = new StringBuilder();
                    for (int k = 0; k < umbral; k++) {
                        palabra.append(matriz[i][j + k]);
                    }
                    if (palabra.toString().equals(palabraBuscar)) {
                        resultado.append("Palabra encontrada en: fila " + (i + 1) + ", columna " + (j + 1) + " a columna " + (j + umbral));
                        resultadoT= resultado.toString();
                        palabraEncontrada = true; // Marcar que se encontró la palabra
                    }
                }
            }
            return;
        }

        private void buscarPalabraEnCuadranteVertical() {
            StringBuilder resultado = new StringBuilder();
            for (int i = filaInicio; i <= filaFin - umbral; i++) {
                for (int j = columnaInicio; j < columnaFin; j++) {
                    if (palabraEncontrada) {
                        return; // Salir si la palabra ya se encontró
                    }
                    StringBuilder palabra = new StringBuilder();
                    for (int k = 0; k < umbral; k++) {
                        palabra.append(matriz[i + k][j]);
                    }
                    if (palabra.toString().equals(palabraBuscar)) {

                        resultado.append("Palabra encontrada en: fila " + (i + 1) + " a fila " + (i + umbral) + ", columna " + (j + 1));
                        resultadoT= resultado.toString();
                        palabraEncontrada = true; // Marcar que se encontró la palabra
                    }
                }
            }
            return;
        }
    }
}