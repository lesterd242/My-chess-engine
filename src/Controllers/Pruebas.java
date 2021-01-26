package Controllers;

public class Pruebas {
    public static void main(String[] args) {
       
    }
    
    private static String mezclaLista(String lista) {
        Random rnd = new Random();
        //System.out.println("Imprimiendo lista a desordenar " + lista);
        String nuevaLista = "";

        if (lista != null && !lista.equals("")) {
            if (lista.length() == 5) {
                return lista;
            } 
            int numeroJugadas = lista.length() / 5;
            int nuevosIndices[] = new int[numeroJugadas];
            String listaDividida[] = new String[numeroJugadas];
            numeroJugadas = 0;
            for (int i = 0; i < lista.length();) {
                listaDividida[numeroJugadas] = lista.substring(i, i + 5);
                numeroJugadas++;
                i = i + 5;
            }
            
            numeroJugadas = lista.length() / 5;
            int x = 1;
            int indiceRnd;
            boolean bandera;
            try {
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
                
            } catch (Exception e) {
                System.out.println(e.toString());
                int error = 0;
            }
        } else {
            return lista;
        }

        return nuevaLista;
    }
    
    
}
