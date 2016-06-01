package maquina_multicinta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;
/**
 *
 * @author Adseglocdom
 */
public class Maquina_Multicinta {

   public static void main(String[] args) {
        String ruta_archivo_transicisiones = "";
        String cadena_a_evaluar = "";
        int numero_de_cintas = 0;
        BufferedReader ingreso_por_consola = new BufferedReader(new InputStreamReader(System.in));
        try {
            ruta_archivo_transicisiones = ingreso_por_consola.readLine();
            cadena_a_evaluar = ingreso_por_consola.readLine();
            numero_de_cintas = Integer.parseInt(ingreso_por_consola.readLine());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Maquina_Multicinta Maquina_Turing_MultiCinta = new Maquina_Multicinta();
        File archivo = new File(ruta_archivo_transicisiones);
        try {
            FileReader lector_archivo = new FileReader(archivo);
            BufferedReader cintas = new BufferedReader(lector_archivo);
            String cinta = cintas.readLine().toString();
            while (cinta != null) {
                Maquina_Turing_MultiCinta.agregarTransicion(cinta);
                cinta = cintas.readLine();
            }
            lector_archivo.close();
            Maquina_Turing_MultiCinta.procesar(cadena_a_evaluar, numero_de_cintas);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Vector<String[]> transiciones = null;

    public Maquina_Multicinta() {
        this.transiciones = new Vector<String[]>();
    }

    public void agregarTransicion(String nuevaTransicion) {
        String[] transicion = nuevaTransicion.split(",");
        this.transiciones.add(transicion);
    }

    public void procesar(String cadena_a_evaluar, int numero_de_cintas) {
        boolean cadena_valida = false;
        String log = "";
        String estado_actual = "q0";
        int index[] = new int[numero_de_cintas];
        for (int i = 0; i < (index.length); i++) //Move trough the input
        {
            index[i] = 1; //Add input to the tape
        }
        String[][] cintas = new String[numero_de_cintas][cadena_a_evaluar.length() + 2];
        cintas[0][0] = cintas[0][cintas[0].length - 1] = "blank";
        for (int i = 1; i < (cintas[0].length - 1); i++) {
            cintas[0][i] = cadena_a_evaluar.charAt(i - 1) + "";
        }
        for (int k = 1; k < (numero_de_cintas); k++) {
            for (int i = 0; i < (cintas[0].length); i++) {
                cintas[k][i] = "blank";
            }
        }
        while (!estado_actual.equals("qf") || !cintas[0][index[0]].equals("blank")) {
            log += ("Estado: " + estado_actual);
            for (int j = 0; j < numero_de_cintas; j++) {
                log += "\tCinta " + (j + 1) + ": " + cintas[j][index[j]] + "\tIndex: " + index[j];
            }
            log += "\n";
            int i;
            for (i = 0; i < this.transiciones.size(); i++) {
                if (this.transiciones.get(i)[0].equals(estado_actual)) {
                    boolean transicion_correcta = false;
                    int k;
                    for (k = 1; k <= numero_de_cintas; k++) {
                        if (this.transiciones.get(i)[k].equals(cintas[k - 1][index[k - 1]])) {
                            transicion_correcta = true;
                        } else {
                            k = numero_de_cintas + 1;
                            transicion_correcta = false;
                        }
                    }
                    if (transicion_correcta) {
                        for (k = 1; k <= numero_de_cintas; k++) {
                            estado_actual = this.transiciones.get(i)[1 + numero_de_cintas];
                            cintas[k - 1][index[k - 1]] = this.transiciones.get(i)[1 + numero_de_cintas + 1 + (k - 1) * numero_de_cintas];
                            if (this.transiciones.get(i)[1 + numero_de_cintas + 1 + (k - 1) * numero_de_cintas + 1].equals(">")) {
                                index[k - 1] += 1;
                            } else {
                                if (this.transiciones.get(i)[1 + numero_de_cintas + 1 + (k - 1) * numero_de_cintas + 1].equals("<")) {
                                    index[k - 1] -= 1;
                                }
                            }
                        }
                        break;
                    }
                }
            }
            if (i == this.transiciones.size()) {
                break;
            }
        }        
        log += ("Estado: " + estado_actual);
            for (int j = 0; j < numero_de_cintas; j++) {
                log += "\tCinta " + (j + 1) + ": " + cintas[j][index[j]] + "\tIndex: " + index[j];
            }
            log += "\n";
        if (estado_actual.equals("qf") && cintas[0][index[0]].equals("blank")) {
            cadena_valida = true;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        try {
            File file = new File("Resultados.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter fileWriter = new BufferedWriter(fw);
            bw.write(log);
            if (cadena_valida) {
                bw.write("\nComputo terminado");
                fileWriter.write("\nComputo terminado");
            } else {
                bw.write("\nComputo rechazado");
                fileWriter.write("\nComputo rechazado");
            }
            bw.flush();
            fileWriter.newLine();
            fileWriter.flush();
            bw.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}