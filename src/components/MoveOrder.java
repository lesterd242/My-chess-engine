package components;

import java.util.Random;

public class MoveOrder {

    public static final int MOVE_LENGTH = 5;

    public static String orderMoves(String listaPreLtr) {
        String listaCoronacion = "";
        String listaCapturas = "";
        String listaFront = "";
        String listaIgual = "";
        String listaRestante = "";
        String item;
        int origen, destino;
        for (int x = 0; x < listaPreLtr.length(); x += MOVE_LENGTH) {
            item = listaPreLtr.substring(x, x + MOVE_LENGTH);
            if(Character.isLowerCase(item.charAt(4))){//si es una captura
                listaCapturas += item;
            } else if(item.charAt(4) == 'P') {//si es una coronacion
                listaCoronacion += item;
            } else {
                origen = Integer.parseInt(item.substring(0, 2));
                destino = Integer.parseInt(item.substring(2, 4));
                if(String.valueOf(origen).substring(0, 1).equals(String.valueOf(destino).substring(0, 1))){//si es un movimiento en la misma fila
                    listaIgual += item;
                } else if(origen > destino) {// si es una jugada hacia adelante
                    listaFront += item;
                } else {//si es una jugada hacia atras
                    listaRestante += item;
                }
            }
        }
        return listaCoronacion + listaCapturas + mezclaLista(listaFront) + mezclaLista(listaIgual) + mezclaLista(listaRestante);
    }



    private static String mezclaLista(String lista) {
        Random rnd = new Random();
        //System.out.println("Imprimiendo lista a desordenar " + lista);
        String nuevaLista = "";

        if (lista != null && !lista.equals("")) {
            if (lista.length() == MOVE_LENGTH) {
                return lista;
            }
            int numeroJugadas = lista.length() / MOVE_LENGTH;
            int nuevosIndices[] = new int[numeroJugadas];
            String listaDividida[] = new String[numeroJugadas];
            numeroJugadas = 0;
            for (int i = 0; i < lista.length();) {
                listaDividida[numeroJugadas] = lista.substring(i, i + MOVE_LENGTH);
                numeroJugadas++;
                i = i + MOVE_LENGTH;
            }

            numeroJugadas = lista.length() / MOVE_LENGTH;
            int x = 1;
            int indiceRnd;
            boolean bandera;

            while (x < numeroJugadas) {
                bandera = true;
                indiceRnd = rnd.nextInt(numeroJugadas);
                for (int i = 0; i < numeroJugadas; i++) {
                    if (indiceRnd == nuevosIndices[i]) {
                        bandera = false;
                        if (!bandera) {
                            break;
                        }
                    }
                }
                if (bandera) {
                    nuevosIndices[x - 1] = indiceRnd;
                    x++;
                }
            }

            for (int i = 0; i < numeroJugadas; i++) {
                nuevaLista += listaDividida[nuevosIndices[i]];
            }

        } else {
            return lista;
        }
        return nuevaLista;
    }
}
