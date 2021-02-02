
package utils;

import java.util.Arrays;

public class Utils {
    public static void imprimirTablero(String tablero[][], int opc, String movimiento){
        
        switch (opc) {
            case 1:
                System.out.println("movimiento deshecho " + movimiento);
                break;
            case 0:
                System.out.println("movimiento hecho " + movimiento);
                break;
            case 2:
                System.out.println("Se gira tablero");
                break;
        }
        
        for(int x = 0; x < 8; x++){
            System.out.println(Arrays.toString(tablero[x]).replace('[', '|').replace(']', '|'));
        }
        System.out.println("");
        System.out.println("");
    }
}
