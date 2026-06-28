package components;

import static components.AlphaBeta.posicionReyB;
import static components.AlphaBeta.tableroPrueba;

public class UndoMove {
    public static void undoMove(String movimiento, int player) {
        if(movimiento.charAt(4) != 'P' && movimiento.charAt(4) != 'E'){//Si no es una coronacion y tampoco un enroque
            String pieza = tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))];
            //Actualizamos la posicion del rey
            if("R".equals(pieza)){
                //Multiplicamos 8 por el numero de la fila y despues sumamos la columna
                posicionReyB = 8 * Character.getNumericValue(movimiento.charAt(0)) + Character.getNumericValue(movimiento.charAt(1));
            }

            //x1,y1,x2,y2,piezacapturada
            //Se devuelve la pieza a su antiguo lugar
            tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))] = tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))];
            //La casilla a la que se mueve ahora la llenamos con la pieza capturada o el espacio vacio
            tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))] = String.valueOf(movimiento.charAt(4));

        } else {
            //column1,column2,piezacapturada,nuevapieza,P estructura de la coronacion

            //Se coloca el peon en la casilla desde la que corono y la pieza que capturo en el lugar a coronar, si es espacio en blanco se coloca eso
            tableroPrueba[1][Character.getNumericValue(movimiento.charAt(0))]= "P";
            tableroPrueba[0][Character.getNumericValue(movimiento.charAt(1))] = String.valueOf(movimiento.charAt(2));

        }
    }
}
