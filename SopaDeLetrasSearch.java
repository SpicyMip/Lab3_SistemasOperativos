import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SopaDeLetrasSearch {
    private static volatile boolean palabraEncontrada = false;

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
            SopaDeLetrasTask tareaPrincipal = new SopaDeLetrasTask(matriz, palabraBuscar, 0, dimension, 0, dimension, umbral);
            String resultado= tareaPrincipal.buscar();
            if (resultado.equals("")) {
                System.out.println("No se encontró la palabra");
            } else {
                System.out.println(resultado);
                long tiempoFin = System.nanoTime();
                long tiempoEjecucion = (tiempoFin - tiempoInicio) / 1000000;
                System.out.println("El tiempo de ejecución fue de " + tiempoEjecucion + " milisegundos");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SopaDeLetrasTask {
        private char[][] matriz;
        private String palabraBuscar;
        private int filaInicio, filaFin, columnaInicio, columnaFin;
        private int umbral;

        public SopaDeLetrasTask(char[][] matriz, String palabraBuscar, int filaInicio, int filaFin, int columnaInicio, int columnaFin, int umbral) {
            this.matriz = matriz;
            this.palabraBuscar = palabraBuscar;
            this.filaInicio = filaInicio;
            this.filaFin = filaFin;
            this.columnaInicio = columnaInicio;
            this.columnaFin = columnaFin;
            this.umbral = umbral;
        }
        public String buscar(){
            String resultadoHorizontal = buscarPalabraEnCuadranteHorizontal();
            String resultadoVertical = buscarPalabraEnCuadranteVertical();
            return resultadoHorizontal + resultadoVertical;
        }

        private String buscarPalabraEnCuadranteHorizontal() {
            StringBuilder resultado = new StringBuilder();
            for (int i = filaInicio; i < filaFin; i++) {
                for (int j = columnaInicio; j <= columnaFin - umbral; j++) {
                    if (palabraEncontrada) {
                        return resultado.toString(); // Salir si la palabra ya se encontró
                    }
                    StringBuilder palabra = new StringBuilder();
                    for (int k = 0; k < umbral; k++) {
                        palabra.append(matriz[i][j + k]);
                    }
                    if (palabra.toString().equals(palabraBuscar)) {
                        palabraEncontrada = true; // Marcar que se encontró la palabra
                        resultado.append("Palabra encontrada en: fila " + (i + 1) + ", columna " + (j + 1) + " a columna " + (j + umbral));
                        return resultado.toString();
                    }
                }
            }
            return resultado.toString();
        }

        private String buscarPalabraEnCuadranteVertical() {
            StringBuilder resultado = new StringBuilder();
            for (int i = filaInicio; i <= filaFin - umbral; i++) {
                for (int j = columnaInicio; j < columnaFin; j++) {
                    if (palabraEncontrada) {
                        return resultado.toString(); // Salir si la palabra ya se encontró
                    }
                    StringBuilder palabra = new StringBuilder();
                    for (int k = 0; k < umbral; k++) {
                        palabra.append(matriz[i + k][j]);
                    }
                    if (palabra.toString().equals(palabraBuscar)) {
                        palabraEncontrada = true; // Marcar que se encontró la palabra
                        resultado.append("Palabra encontrada en: fila " + (i + 1) + " a fila " + (i + umbral) + ", columna " + (j + 1));
                        return resultado.toString();
                    }
                }
            }
            return resultado.toString();
        }
    }
}
