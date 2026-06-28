package components;

import static components.AlphaBeta.posicionReyB;
import static components.AlphaBeta.tableroPrueba;

public class MoveMaker {

    //m??todo para generar un movimiento
    public static void makeMove(String movimiento, int player, boolean esFinal){
        try {
            if (movimiento.charAt(4) != 'P' && movimiento.charAt(4) != 'E') {//Si no es una coronacion y tampoco un enroque

                String pieza = tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))];
                if ("R".equals(pieza)) {
                    //Actualizamos la posicion del rey blanco obteniendo la fila y despues sumando la columna
                    posicionReyB = 8 * Character.getNumericValue(movimiento.charAt(2)) + Character.getNumericValue(movimiento.charAt(3));
                }
                /*
                 * x1,y1,x2,y2,piezacapturada
                 * Ponemos la pieza de la casilla de origen a la casilla final
                 */
                tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))] = tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))];
                //La casilla de inicio ahora tiene estar vacia
                tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))] = " ";
            } else {
                //column1,column2,piezacapturada,nuevapieza,P estructura de la coronacion
                //Se coloca la casilla del peon en blanco y la nueva casilla con la pieza a coronar
                tableroPrueba[1][Character.getNumericValue(movimiento.charAt(0))] = " ";
                tableroPrueba[0][Character.getNumericValue(movimiento.charAt(1))] = String.valueOf(movimiento.charAt(3));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
