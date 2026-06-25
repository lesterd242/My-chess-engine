package components;

import evaluations.Rating;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static components.MoveGenerator.generaMovimientos;
import static components.MoveGenerator.reySeguro;

public class Main {

    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int MOVE_LENGTH = 5;
    public static final int DEFAULT_SEARCH_DEPTH = 6;

    static int posicionReyB, posicionReyN;
    public static int searchDepth = DEFAULT_SEARCH_DEPTH;
    public static String piezaOrigen = "";
    public static String movimientoOrigen = "";
    public static short historial = 0;
    // para guardar el estado de los enroques
    public static boolean matrizTorres[] = {false, false, false, false};// Torres de la columa a,h por bando, si se movieron
    public static boolean matrizReyes[] = {false, false};// Reyes, si se movieron
    
    public static int[] contadorTorres = {0, 0, 0, 0}; // Guarda el registro de los movimientos de las torres
    public static int[] contadorReyes = {0, 0};// Guarda el registro de los movimientos de los reyes
    
    public static void inicializaReyes(){
        //Obtenemos la posicion de los reyes al principio
        while(!"R".equals(tableroPrueba[posicionReyB/8][posicionReyB%8])){
            posicionReyB++;
        }
        
        while (!"r".equals(tableroPrueba[posicionReyN/8][posicionReyN%8])) {
            posicionReyN++;
        }
        setEnroquesTrue();
    }
    
    public static void setEnroquesTrue() {
    	
    	// Blanco
    	if(posicionReyB != 60) {
    		matrizReyes[0] = true;
    	}
    	
    	if(tableroPrueba[7][7].charAt(0) != 'T') {
    		matrizTorres[1] = true;
    	}
    	
    	if(tableroPrueba[7][0].charAt(0) != 'T') {
    		matrizTorres[0] = true;
    	}
    	
    	// Negro
    	if(posicionReyN != 4) {
    		matrizReyes[1] = true;
    	}
    	
    	if(tableroPrueba[0][7].charAt(0) != 't') {
    		matrizTorres[3] = true;
    	}
    	
    	if(tableroPrueba[0][0].charAt(0) != 't') {
    		matrizTorres[2] = true;
    	}
    }
    
    public static void sumaEnroques(String movimiento, int turno, boolean isFinal){
    	turno = 1-turno;
        String pieza = tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))];
        if(pieza.equals("T")){
            if(Character.getNumericValue(movimiento.charAt(0))==7 && Character.getNumericValue(movimiento.charAt(1))==0){
                if(turno == 1){
                    System.out.println("Es de torre blanca columna a");
                    matrizTorres[0] = true;
                    contadorTorres[0]++;
                } else {
                    System.out.println("Es de torre negra columna h");
                    matrizTorres[3] = true;
                    contadorTorres[3]++;
                }
            } else if(Character.getNumericValue(movimiento.charAt(0))==7 && Character.getNumericValue(movimiento.charAt(1))==7){
                 if(turno == 1){
                    System.out.println("Es de torre blanca columna h");
                    matrizTorres[1] = true;
                	contadorTorres[1]++;
                } else {
                	System.out.println("Es de torre negra columna a");
                	matrizTorres[2] = true;
                	contadorTorres[2]++;
                }
            }
		} else if (pieza.equals("R")) {
			if (turno == 1) {
				matrizReyes[0] = true;
				contadorReyes[0]++;
				if(isFinal) {
					/*
					 * Se incrementa  en un valor que no se podria alcanzar en un juego 
					 * para, que una vez que la jugada elegida por el humano o la maquina 
					 * sea de rey, no se pueda volver a 0 para esta propiedad.
					 */
					contadorReyes[0] += 200;
				}
			} else {
				matrizReyes[1] = true;
				contadorReyes[1]++;
				if(isFinal) {
					contadorReyes[1] += 200;
				}
			}
        }
    }
    
    public static void restaEnroques(String movimiento, int turno){
    	turno = 1-turno;
        String pieza = tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))];
        if(pieza.equals("T")){
            BiConsumer<Integer, Integer> setContador = (ind, cant) -> {
            	matrizTorres[ind] = !(cant == 0);
            };
            
            if(Character.getNumericValue(movimiento.charAt(0))==7 && Character.getNumericValue(movimiento.charAt(1))==0){
                if(turno == 1){
                    System.out.println("Es de torre blanca columna a");
                    setContador.accept(0, --contadorTorres[0]);
                } else {
                    System.out.println("Es de torre negra columna h");
                    setContador.accept(3, --contadorTorres[3]);
                }
            } else if(Character.getNumericValue(movimiento.charAt(0))==7 && Character.getNumericValue(movimiento.charAt(1))==7){
                 if(turno == 1){
                    System.out.println("Es de torre blanca columna h");
                    setContador.accept(1, --contadorTorres[1]);
                } else {
                	System.out.println("Es de torre negra columna a");
                	setContador.accept(2, --contadorTorres[2]);
                }
            }
		} else if (pieza.equals("R")) {
			if (turno == 1) {
				contadorReyes[0]--;
				if(contadorReyes[0] == 0) {
					matrizReyes[0] = false;
				} else {
					matrizReyes[0] = true;
				}
			} else {
				contadorReyes[1]--;
				if(contadorReyes[1] == 0) {
					matrizReyes[1] = false;
				} else {
					matrizReyes[1] = true;
				}
			}
        }
    }
    
    /*
     *     
     */
    public static String alphaBeta(int depth, int beta, int alpha, String bestMove, int previousPlayer) {
        // Return format: five-character move followed by its numeric score.
        int currentPlayer = opponentOf(previousPlayer);
        String moves = generaMovimientos(currentPlayer);
        if (depth == 0 || moves.isEmpty()) {
            int score = Rating.rating(moves.length(), depth, currentPlayer) + (currentPlayer * 2 - 1);
            return bestMove + score;
        }

        moves = orderMoves(moves);
        for (int i = 0; i < moves.length(); i += MOVE_LENGTH) {
            String candidateMove = moves.substring(i, i + MOVE_LENGTH);
            if (depth == searchDepth) {
                piezaOrigen = candidateMove.substring(0, 2);
                piezaOrigen = tableroPrueba[Integer.parseInt(piezaOrigen.substring(0, 1))][Integer.parseInt(piezaOrigen.substring(1, 2))];
                movimientoOrigen = candidateMove;
            }

            makeMove(candidateMove, currentPlayer, false);
            giraTablero();
            String searchResult = alphaBeta(depth - 1, beta, alpha, candidateMove, currentPlayer);
            int score = Integer.parseInt(searchResult.substring(MOVE_LENGTH));
            giraTablero();
            undoMove(candidateMove, currentPlayer);

            if (currentPlayer == BLACK) {
                if (score <= beta) {
                    beta = score;
                    if (depth == searchDepth) {
                        bestMove = searchResult.substring(0, MOVE_LENGTH);
                    }
                }
            } else {
                if (score > alpha) {
                    alpha = score;
                    if (depth == searchDepth) {
                        bestMove = searchResult.substring(0, MOVE_LENGTH);
                    }
                }
            }

            if (alpha >= beta) {
                return bestMove + (currentPlayer == BLACK ? beta : alpha);
            }
        }

        return bestMove + (currentPlayer == BLACK ? beta : alpha);
    }

    private static int opponentOf(int player) {
        return 1 - player;
    }
    
    public static void giraTablero(){
        String temp;
        int row, col;
        /*
         * Se cambia la posicion de cada una de las piezas, como si el tablero 
         * girara 360 grados sobre su propio eje, la primera vez que se
         * invoque este metodo sera el turno de las negras y despues de las blancas y 
         * asi sucesivamente.
         */
        for (int i = 0; i < 32; i++) {
            row = i/8;
            col = i%8;
            //Primero se convierte a lowwer case o upper case y se guarda ese valor
            if(Character.isUpperCase(tableroPrueba[row][col].charAt(0))){
                temp = tableroPrueba[row][col].toLowerCase();
            } else{
                temp = tableroPrueba[row][col].toUpperCase();
            }
            
            //Colocamos el valor de la parte inferior en la parte superior 
            if(Character.isUpperCase(tableroPrueba[7-row][7-col].charAt(0))){
                tableroPrueba[row][col] = tableroPrueba[7-row][7-col].toLowerCase();
            } else{
                tableroPrueba[row][col] = tableroPrueba[7-row][7-col].toUpperCase();
            }
            
            /*
             * El valor que se guarda de la parte superior se guarda en la parte
             * inferior
             */
            tableroPrueba[7-row][7-col] = temp;
        }
        
        
        int reyTemp = posicionReyB;
        posicionReyB = 63 - posicionReyN;
        posicionReyN=63-reyTemp;
    }
    
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
            } else if(movimiento.charAt(4) == 'E') { // si es un enroque
            	sumaEnroques(movimiento, player, esFinal);
            	/*
            	 * x1,y1,x2,y2,E estructura del enroque
            	 * */
            	
            	tableroPrueba[7][Character.getNumericValue(movimiento.charAt(1))] = " ";
            	tableroPrueba[7][Character.getNumericValue(movimiento.charAt(3))] = "R";
            	
            	if(movimiento.charAt(3) == '6') { // si es enroque corto blanco
            		tableroPrueba[7][7] = " ";
                	tableroPrueba[7][5] = "T";
            	} else if(movimiento.charAt(3) == '2'){ // si es enroque largo blanco
            		tableroPrueba[7][0] = " ";
                	tableroPrueba[7][3] = "T";
            	} else if(movimiento.charAt(3) == '1') { // si es enroque corto negro
            		tableroPrueba[7][0] = " ";
                	tableroPrueba[7][2] = "T";
            	} else { // si es enroque largo negro
            		tableroPrueba[7][7] = " ";
                	tableroPrueba[7][4] = "T";
            	}
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
    
    public static void undoMove(String movimiento, int player){
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
            
        } else if(movimiento.charAt(4) == 'E') { // si es un enroque
        	restaEnroques(movimiento, player);
        	/*
        	 * x1,y1,x2,y2,E estructura del enroque
        	 * */
        	
        	tableroPrueba[7][Character.getNumericValue(movimiento.charAt(1))] = "R";
        	tableroPrueba[7][Character.getNumericValue(movimiento.charAt(3))] = " ";
        	
        	if(movimiento.charAt(3) == '6') { // si es enroque corto blanco
        		tableroPrueba[7][7] = "T";
            	tableroPrueba[7][5] = " ";
        	} else if(movimiento.charAt(3) == '2'){ // si es enroque largo blanco
        		tableroPrueba[7][0] = "T";
            	tableroPrueba[7][3] = " ";
        	} else if(movimiento.charAt(3) == '1') { // si es enroque corto negro
        		tableroPrueba[7][0] = "T";
            	tableroPrueba[7][2] = " ";
        	} else { // si es enroque largo negro
        		tableroPrueba[7][7] = "T";
            	tableroPrueba[7][4] = " ";
        	}
        	
        }else{
            //column1,column2,piezacapturada,nuevapieza,P estructura de la coronacion 
            
            //Se coloca el peon en la casilla desde la que corono y la pieza que capturo en el lugar a coronar, si es espacio en blanco se coloca eso
            tableroPrueba[1][Character.getNumericValue(movimiento.charAt(0))]= "P";
            tableroPrueba[0][Character.getNumericValue(movimiento.charAt(1))] = String.valueOf(movimiento.charAt(2));

        }      
    }
    
    
	public static String puedeEnrocar(int player) {
		player = 1-player;
		int indexR, indexTA, indexTB, posicionA = 1, posicionB = 2, posicionC=3;
		String enroqueMov = "";
		if(player == 1) {
			indexR = 0;
			indexTA = 0;
			indexTB = 1;
		} else {
			indexR = 1;
			indexTA = 2;
			indexTB = 3;
			posicionA = -1;
			posicionB = -2;
			posicionC = -3;
		}
		Predicate<Integer> condicion = pos -> {
			tableroPrueba[7][(posicionReyB % 8) + pos] = "R";
			tableroPrueba[7][posicionReyB % 8] = " ";
			posicionReyB += pos;
			boolean esSeguro = reySeguro();
			posicionReyB -= pos;
			tableroPrueba[7][(posicionReyB % 8) + pos] = " ";
			tableroPrueba[7][posicionReyB % 8] = "R";
			return esSeguro;
		};

		Predicate<Integer> checarEspacios = pos -> {
			return tableroPrueba[7][(posicionReyB % 8) + pos].equals(" ");
		};

		if (!matrizReyes[indexR]) {
			try { // corto
				if (!matrizTorres[indexTB]) {
					if (checarEspacios.test(posicionA) && checarEspacios.test(posicionB)) {
						if (condicion.test(posicionA) && condicion.test(posicionB)) {
							enroqueMov += "7" + (posicionReyB % 8) + "7" + ((posicionReyB % 8) + posicionB) + "E";
						}
					}
				}
			} catch (Exception e) {

			}
			
			posicionA = posicionA*-1;
			posicionB = posicionB*-1;
			posicionC = posicionC*-1;
			
			try { // largo
				if (!matrizTorres[indexTA]) { 
					if (checarEspacios.test(posicionA) && checarEspacios.test(posicionB) && checarEspacios.test(posicionC)) {
						if (condicion.test(posicionA) && condicion.test(posicionB)) {
							enroqueMov += "7" + (posicionReyB % 8) + "7" + ((posicionReyB % 8) + posicionB) + "E";
						}
					}
				}
			} catch (Exception e) {

			}
		}
		return enroqueMov;
	}
    
    private static String orderMoves(String listaPreLtr) {
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

//     public static final String tableroPrueba[][] = {
//        {" "," "," "," "," "," "," ","r"},
//        {" "," "," "," "," "," ","T"," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," ","R"," "," "," ","a"},
//    };
    
  public static final String tableroPrueba[][] = {
  {"t", "c", "a", "d", "r", "a", "c", "t"},
  {"p", "p", "p", "p", "p", "p", "p", "p"},
  {" ", " ", " ", " ", " ", " ", " ", " "},
  {" ", " ", " ", " ", " ", " ", " ", " "},
  {" ", " ", " ", " ", " ", " ", " ", " "},
  {" ", " ", " ", " ", " ", " ", " ", " "},
  {"P", "P", "P", "P", "P", "P", "P", "P"},
  {"T", "C", "A", "D", "R", "A", "C", "T"},};
  
}
