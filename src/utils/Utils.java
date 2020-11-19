
package utils;

import java.util.Arrays;

public class Utils {
    public static void imprimirTablero(String tablero[][], int opc, String movimiento){
        
        if(opc == 1){
            System.out.println("movimiento deshecho " + movimiento);
        }else{
            System.out.println("movimiento hecho " + movimiento);
        }
        
        for(int x = 0; x < 8; x++){
            System.out.println(Arrays.toString(tablero[x]).replace('[', '|').replace(']', '|'));
        }
        System.out.println("");
        System.out.println("");
    }
}
