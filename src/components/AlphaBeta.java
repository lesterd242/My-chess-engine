package components;

import evaluations.Rating;

import static components.MoveGenerator.generaMovimientos;
import static components.MoveOrder.orderMoves;
import static components.UndoMove.undoMove;

public class AlphaBeta {

    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int MOVE_LENGTH = 5;
    public static final int DEFAULT_SEARCH_DEPTH = 4;

    static int posicionReyB, posicionReyN;
    public static int searchDepth = DEFAULT_SEARCH_DEPTH;
    public static String piezaOrigen = "";
    public static String movimientoOrigen = "";
    public static short historial = 0;
    // para guardar el estado de los enroques
    public static boolean matrizTorres[] = {false, false, false, false};// Torres de la columa a,h por bando, si se movieron
    public static boolean matrizReyes[] = {false, false};// Reyes, si se movieron
    
    public static int[] contadorReyes = {0, 0};// Guarda el registro de los movimientos de los reyes
    
    public static void inicializaReyes(){
        //Obtenemos la posicion de los reyes al principio
        while(!"R".equals(tableroPrueba[posicionReyB/8][posicionReyB%8])){
            posicionReyB++;
        }
        
        while (!"r".equals(tableroPrueba[posicionReyN/8][posicionReyN%8])) {
            posicionReyN++;
        }
    }

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

            MoveMaker.makeMove(candidateMove, currentPlayer, false);
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
