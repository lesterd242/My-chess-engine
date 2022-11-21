package Controllers;

import evaluations.Rating;
import java.util.Random;

public class Main {

    private static int posicionReyB, posicionReyN;
    public static int humanAsWhite = 1;
    public static int profundidadGlobal = 7;
    public static String piezaOrigen = "";
    public static String movimientoOrigen = "";
    public static short historial = 0;
    // para guardar el estado de los enroques
    private static boolean enroques[][] = {{false, false, false, false},//Enroques hechos corto y largo por bando
                                           {false, false, false, false},//torres de la columa a,b por bando, si se movieron
                                           {false, false}};//reyes, si se movieron
    
    public static void inicializaReyes(){
        //Obtenemos la posicion de los reyes al principio
        while(!"R".equals(tableroPrueba[posicionReyB/8][posicionReyB%8])){
            posicionReyB++;
        }
        
        while (!"r".equals(tableroPrueba[posicionReyN/8][posicionReyN%8])) {
            posicionReyN++;
        }
    }
    
    /*
     *     
     */
    public static String alphaBeta(int profundidad, int beta, int alpha, String move, int player){
        //Formato de retorno 1234b#### (movimiento, pieza, puntuacion)
        
        String lista = generaMovimientos();
        if(profundidad == 0 || lista.length() == 0){
            return move+(Rating.rating(lista.length(),  profundidad, player)+(player*2-1));//Retornamos si se alcanzo la profundidad maxima o si no hay movimientos disponibles
        }

        lista = lmr(lista);
        player = 1-player;
        for(int i = 0; i < lista.length(); i+=5){//Como un movimiento se compone de 5 caracteres, incrementamos el contador en 5
            if(profundidad == profundidadGlobal){
                //Utils.imprimirTablero(tableroPrueba, 0, move);
                piezaOrigen = lista.substring(i, (i + 5)).substring(0, 2);
                piezaOrigen = tableroPrueba[Integer.parseInt(piezaOrigen.substring(0, 1))][Integer.parseInt(piezaOrigen.substring(1, 2))];
                movimientoOrigen = lista.substring(i, (i + 5)); 
            }
            makeMove(lista.substring(i, (i + 5)));//Obtenemos un movimiento
            giraTablero();
            //Se llama recursivamente el metodo, enviando la profundidad menos uno, hasta que sea cero y el movimiento de la lista
            String stringReturn = alphaBeta(profundidad - 1, beta, alpha, (lista.substring(i, (i + 5))), player);
            int valor = Integer.valueOf(stringReturn.substring(5));
            giraTablero();
            undoMove(lista.substring(i, (i+5)));
            if (player == 0) {
                if(valor <= beta){//Beta negro
                    beta = valor;
                    if(profundidad == profundidadGlobal){
                        move = stringReturn.substring(0, 5);
                    }
                }
            } else { 
                if(valor > alpha){//Alpha blanco
                    alpha = valor;
                    if(profundidad == profundidadGlobal){
                        move = stringReturn.substring(0, 5);
                    }
                }
            }
            if(alpha >= beta){
                if(player == 0){
                    return move + beta;
                }else{
                    return move + alpha;
                }
            }
        }
        if(player == 0){
            return move + beta;
        }else{
            return move + alpha;
        }
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
    public static void makeMove(String movimiento){
        try {
            if (movimiento.charAt(4) != 'P') {//Si no es una coronacion
                //x1,y1,x2,y2,piezacapturada

                //Ponemos la pieza de la casilla de origen a la casilla final
                tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))] = tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))];
                //La casilla de inicio ahora tiene estar vacia
                tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))] = " ";

                if ("R".equals(tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))])) {
                    //Actualizamos la posicion del rey blanco obteniendo la fila y despues sumando la 
                    posicionReyB = 8 * Character.getNumericValue(movimiento.charAt(2)) + Character.getNumericValue(movimiento.charAt(3));
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
    
    private static void undoMove(String movimiento){
           if(movimiento.charAt(4) != 'P'){//Si no es una coronacion
            //x1,y1,x2,y2,piezacapturada
            
            //Se devuelve la pieza a su antiguo lugar
            tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))] = tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))];
            //La casilla a la que se mueve ahora la llenamos con la pieza capturada o el espacio vacio
            tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))] = String.valueOf(movimiento.charAt(4));
            
            //Actualizamos la posicion del rey
            if("R".equals(tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))])){
                //Multiplicamos 8 por el numero de la fila y despues sumamos la columna
                posicionReyB = 8 * Character.getNumericValue(movimiento.charAt(0)) + Character.getNumericValue(movimiento.charAt(1));
            }
        }else{
            //column1,column2,piezacapturada,nuevapieza,P estructura de la coronaciÃ³n 
            
            //Se coloca el peon en la casilla desde la que corono y la pieza que capturo en el lugar a coronar, si es espacio en blanco se coloca eso
            tableroPrueba[1][Character.getNumericValue(movimiento.charAt(0))]= "P";
            tableroPrueba[0][Character.getNumericValue(movimiento.charAt(1))] = String.valueOf(movimiento.charAt(2));

        }      
    }
    
    public static String generaMovimientos(){
        
        
        
        long init, fin;
        init = System.currentTimeMillis();
        String lista = "";
        for(int x = 0; x <64; x++){
            switch(tableroPrueba[x/8][x%8]){
                case "R":
                    lista += movimientosRey(x);
                    break;
                case "D":
                    lista += movimientosDama(x);
                    break;
                case "A":
                    lista += movimientosAlfil(x);
                    break;
                case "T":
                    lista += movimientosTorre(x);
                    break;
                case "C":
                    lista += movimientosCaballo(x);
                    break;
                case "P":
                    lista += movimientosPeon(x);
            }
        }
        fin = System.currentTimeMillis();
        //System.out.println("Tiempo en generar movimientos " + (fin - init) + " ms.");
        return lista;
    }
    
    
    private static String movimientosRey(int i){
        String lista = "";
        String piezaAnterior;
        
        int row = i/8;//Obtenemos la fila
        //row--;//Como empieza desde la fila arriba del rey, restamos
        int col = i%8;//Obtenemos la columna
        //col--;//Como empieza desde la columna a la izquierda del rey restamos
        
        for (int j = 0; j < 9; j++) {
            try {
             if(j != 4){
                if(Character.isLowerCase(tableroPrueba[row-1+j/3][col-1+j%3].charAt(0)) || " ".equals(tableroPrueba[row-1+j/3][col-1+j%3])){
                    piezaAnterior = tableroPrueba[row-1+j/3][col-1+j%3];//obtenemos la pieza que esta actualmente en la casilla que se esta evaluando
                    tableroPrueba[row][col] = " ";//En la posiciÃ³n donde estÃ¡ el rey la actualizamos, como se movio entonces es un lugar vacio
                    tableroPrueba[row-1+j/3][col-1+j%3] = "R";//Movemos al rey a la casilla que se evalu?? que tiene una pieza o esta disponible (movimiento v??lido)
                    int posicionTemporal = posicionReyB;//Guardamos la posiciÃ³n del rey
                    posicionReyB = i+(j/3)*8+j%3-9;//Simplemente restamos la cantidad de casillas para empezar en la casilla 00 y despuÃ©s en la 01, para incrementar la columna se usa el modulo
                    if(reySeguro()){
                        lista = lista + row + col + (row-1+j/3) + (col-1+j%3) + piezaAnterior;
                    }
                    tableroPrueba[row][col] = "R";//Volvemos a colocar el rey en su posicion original
                    tableroPrueba[row-1+j/3][col-1+j%3] = piezaAnterior;//Volvemos a colocar a la pieza que se comio en su lugar original, si es una casilla vacÃ­a tambiÃ©n se coloca
                    posicionReyB = posicionTemporal;
                }   
            }    
            } catch (Exception e) {
            }
        }
        
        return lista; 
    }
    
    private static String movimientosDama(int i) {
        String lista = "";
        String piezaAnterior;
        int row = i / 8;
        int col = i % 8;

        int temp = 1;

        for (int j = -1; j <= 1; j++) {//Cuando es -1 va a validar la fila anterior, cuando es 0 va a validar la misma fila hacÃ¬a la izquierda o derecha, cuando es 1, va a validar la fila siguiente
            for (int k = -1; k <= 1; k++) {//Cuando es -1 va a validar hacia la izquierda, cuando es 0 va a validar al centro, cuando es 1 va a validar a la derecha 
                if (j != 0 || k != 0) {//Para que no valide su propia posici??n
                    try {
                        while (" ".equals(tableroPrueba[row + temp * j][col + temp * k])) {

                            piezaAnterior = tableroPrueba[row + temp * j][col + temp * k];//obtenemos la pieza anterior, quiza se podrÃ­a quitar ya que solo valida espacios en blanco
                            tableroPrueba[row][col] = " ";//En la casilla donde estaba la dama ahora es un lugar vacio
                            tableroPrueba[row + temp * j][col + temp * k] = "D";//Ponemos a la dama en el nuevo lugar

                            if (reySeguro()) {
                                lista = lista + row + col + (row + temp * j) + (col + temp * k) + piezaAnterior;//Obtenemos la lista, desde la posicion de la que parte hacia el destino y la pieza si es que hay
                            }

                            tableroPrueba[row][col] = "D";//Colocamos a la dama en su lugar de origen
                            tableroPrueba[row + temp * j][col + temp * k] = piezaAnterior;
                            temp++;//incrementamos la variable auxiliar para que incrementa la fila o la columna
                        }

                        if (Character.isLowerCase(tableroPrueba[row + temp * j][col + temp * k].charAt(0))) {
                            piezaAnterior = tableroPrueba[row + temp * j][col + temp * k];//obtenemos la pieza anterior, quiza se podrÃ­a quitar ya que solo valida espacios en blanco
                            tableroPrueba[row][col] = " ";//En la casilla donde estaba la dama ahora es un lugar vacio
                            tableroPrueba[row + temp * j][col + temp * k] = "D";//Ponemos a la dama en el nuevo lugar

                            if (reySeguro()) {
                                lista = lista + row + col + (row + temp * j) + (col + temp * k) + piezaAnterior;//Obtenemos la lista, desde la posicion de la que parte hacia el destino y la pieza si es que hay
                            }

                            tableroPrueba[row][col] = "D";//Colocamos a la dama en su lugar de origen
                            tableroPrueba[row + temp * j][col + temp * k] = piezaAnterior;
                        }
                    } catch (Exception e) {
                    }
                }
                temp = 1;
            }
        }
        return lista;
    }
    
    private static String movimientosAlfil(int i) {
        String lista = "";
        int row = i / 8;
        int col = i % 8;
        String piezaAnterior;
        int temp = 1;

        for (int j = -1; j <= 1; j += 2) {//Ahora solo es -1 o 1, -1 para las filas superiores, 1 para las filas inferiores 
            for (int k = -1; k <= 1; k += 2) {//-1 para las columnas izquierdas, 1 para las columnas derechas, asÃ­ aumenta la columna junto con la fila
                    try {
                        while (" ".equals(tableroPrueba[row + temp * j][col + temp * k])) {//Mientras sea un espacio vacio

                            piezaAnterior = tableroPrueba[row + temp * j][col + temp * k];
                            tableroPrueba[row][col] = " ";
                            tableroPrueba[row + temp * j][col + temp * k] = "A"; 
                            if (reySeguro()) {
                                lista = lista + row + col + (row + temp * j) + (col + temp * k) + piezaAnterior;
                            }
                            
                            tableroPrueba[row][col] = "A";
                            tableroPrueba[row + temp * j][col + temp * k] = piezaAnterior;
                            temp++;
                        }
                        if (Character.isLowerCase(tableroPrueba[row + temp * j][col + temp * k].charAt(0))) {
                            
                            piezaAnterior = tableroPrueba[row + temp * j][col + temp * k];
                            tableroPrueba[row][col] = " ";
                            tableroPrueba[row + temp * j][col + temp * k] = "A";
                            if (reySeguro()) {
                                lista = lista + row + col + (row + temp * j) + (col + temp * k) + piezaAnterior;
                            }
                            tableroPrueba[row][col] = "A";
                            tableroPrueba[row + temp * j][col + temp * k] = piezaAnterior;
                        }
                    } catch (Exception e) {
                    }
                temp = 1;
            }
        }

        return lista;
    }
    
    private static String movimientosTorre(int i) {
        String lista = "";
        int row = i/8;
        int col = i%8;
        int temp = 1;
        String piezaAnterior;
        
        for(int j= -1; j<=1; j+=2){//Para las filas, -1 fila izquierda, 1 fila derecha
            try {
                while (" ".equals(tableroPrueba[row][col + temp * j])) {
                   piezaAnterior = tableroPrueba[row][col + temp * j];
                   tableroPrueba[row][col] = " ";
                   tableroPrueba[row][col + temp * j] = "T";
                   if(reySeguro()){
                       lista = lista + row + col + row + (col + temp * j) + piezaAnterior;
                   }
                   
                   tableroPrueba[row][col] = "T";
                   tableroPrueba[row][col + temp * j] = piezaAnterior;
                   temp++;
                }
                
                if(Character.isLowerCase(tableroPrueba[row][col + temp * j].charAt(0))){
                    piezaAnterior = tableroPrueba[row][col + temp * j];
                    tableroPrueba[row][col] = " ";
                    tableroPrueba[row][col + temp * j] = "T";
                    if(reySeguro()){
                        lista = lista + row + col + row + (col + temp * j) + piezaAnterior;
                    }
                    
                    tableroPrueba[row][col] = "T";
                    tableroPrueba[row][col + temp * j] = piezaAnterior;
                }
            } catch (Exception e) {
            }
            temp = 1;
            
            try {
                while (" ".equals(tableroPrueba[row + temp * j][col])) {
                   piezaAnterior = tableroPrueba[row  + temp * j][col];
                   tableroPrueba[row][col] = " ";
                   tableroPrueba[row  + temp * j][col] = "T";
                   if(reySeguro()){
                       lista = lista + row + col + (row  + temp * j) + (col) + piezaAnterior;
                   }
                   
                   tableroPrueba[row][col] = "T";
                   tableroPrueba[row  + temp * j][col] = piezaAnterior;
                   temp++;
                }
                
                if(Character.isLowerCase(tableroPrueba[row + temp * j][col].charAt(0))){
                    piezaAnterior = tableroPrueba[row + temp * j][col];
                    tableroPrueba[row][col] = " ";
                    tableroPrueba[row+ temp * j][col] = "T";
                    if(reySeguro()){
                        lista = lista + row + col  + (row + temp * j) + (col) + piezaAnterior;
                    }
                    
                    tableroPrueba[row][col] = "T";
                    tableroPrueba[row + temp * j][col] = piezaAnterior;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }
        return lista;
    }
    
    private static String movimientosCaballo(int i) {
        int row = i / 8;
        int col = i % 8;
        String lista = "";
        String piezaAnterior;

        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    if (Character.isLowerCase(tableroPrueba[row + j][col + k * 2].charAt(0)) || " ".equals(tableroPrueba[row + j][col + k * 2])) {
                        piezaAnterior = tableroPrueba[row + j][col + k * 2];
                        tableroPrueba[row][col] = " ";
                        tableroPrueba[row + j] [col + k * 2] = "C";
                        if(reySeguro()){
                            lista = lista + row + col + (row + j) + (col + k * 2) + piezaAnterior;   
                        }
                        tableroPrueba[row][col] = "C";
                        tableroPrueba[row + j][col + k * 2] = piezaAnterior;
                    }
                } catch (Exception e) {
                }
                
                try {
                    if (Character.isLowerCase(tableroPrueba[row + j * 2][col + k].charAt(0)) || " ".equals(tableroPrueba[row + j * 2][col + k])) {
                        piezaAnterior = tableroPrueba[row + j * 2] [col + k];
                        tableroPrueba[row][col] = " ";
                        tableroPrueba[row + j * 2] [col + k] = "C";
                        if(reySeguro()){
                            lista = lista + row + col + (row + j * 2) + (col + k) + piezaAnterior;   
                        }
                        tableroPrueba[row][col] = "C";
                        tableroPrueba[row + j * 2][col + k] = piezaAnterior;
                    }
                } catch (Exception e) {
                }

            }
        }

        return lista;
    }
    
    private static String movimientosPeon(int i) {
        String lista = "";
        String piezaAnterior; 
        int row = i/8;
        int col = i%8;
        int temp = 1;
        
        for (int j = -1; j <=1; j+=2) {
            try {
                if(Character.isLowerCase(tableroPrueba[row-1][col+j].charAt(0)) && i >= 16 ){//captura
                    piezaAnterior = tableroPrueba[row-1][col+j];
                    tableroPrueba[row][col] = " ";
                    tableroPrueba[row-1][col+j] = "P";
                    if(reySeguro()){
                        lista = lista + row + col + (row-1) + (col+j)+piezaAnterior;
                    }
                    tableroPrueba[row][col] = "P";
                    tableroPrueba[row-1][col+j] = piezaAnterior;
                }
            } catch (Exception e) {
            }
            
             try {
                if(Character.isLowerCase(tableroPrueba[row-1][col+j].charAt(0)) && i < 16 ){//captura y coronacion
                    String piezasCoronar[] = {"D", "T", "A", "C"};//coronamos las posibles piezas
                    for (int k = 0; k < piezasCoronar.length; k++) {
                        piezaAnterior = tableroPrueba[row - 1][col + j];
                        tableroPrueba[row][col] = " ";
                        tableroPrueba[row - 1][col + j] = piezasCoronar[k];
                        if (reySeguro()) {
                            lista = lista + col + (col+j) + piezaAnterior + piezasCoronar[k] + "P";
                        }
                        tableroPrueba[row][col] = "P";
                        tableroPrueba[row - 1][col + j] = piezaAnterior;
                    }
                    
                }
            } catch (Exception e) {
            }
        }
        
        try {//Un avance de peon
            if(" ".equals(tableroPrueba[row-1][col]) && i >= 16){
                 piezaAnterior = tableroPrueba[row-1][col]; 
                 tableroPrueba[row][col] = " ";
                 tableroPrueba[row-1][col] = "P";
                 if(reySeguro()){
                     lista = lista + row + col + (row-1) + col + piezaAnterior;
                 }
                 tableroPrueba[row][col] = "P";
                 tableroPrueba[row-1][col] = " ";
                 
            }
        } catch (Exception e) {
        }
        
        try {//Doble avance de peon
            if(" ".equals(tableroPrueba[row-1][col]) && " ".equals(tableroPrueba[row-2][col]) && i >= 48){
                 piezaAnterior = tableroPrueba[row-1][col]; 
                 tableroPrueba[row][col] = " ";
                 tableroPrueba[row-2][col] = "P";
                 if(reySeguro()){
                     lista = lista + row + col + (row-2) + col + piezaAnterior;
                 }
                 tableroPrueba[row][col] = "P";
                 tableroPrueba[row-2][col] = " ";
                 
            }
        } catch (Exception e) {
        }
      
        try {//Coronacion sin captura
            if (" ".equals(tableroPrueba[row - 1][col]) && i < 16) {
                String piezasCoronar[] = {"D", "T", "A", "C"};//coronamos las posibles piezas
                for (int k = 0; k < piezasCoronar.length; k++) {
                    piezaAnterior = tableroPrueba[row - 1][col];
                    tableroPrueba[row][col] = " ";
                    tableroPrueba[row - 1][col] = piezasCoronar[k];
                    if (reySeguro()) {
                        lista = lista + col + (col) + piezaAnterior + piezasCoronar[k] + "P";
                    }
                    tableroPrueba[row][col] = "P";
                    tableroPrueba[row - 1][col] = piezaAnterior;
                }
            }
        } catch (Exception e) {
        }

        return lista;
        
    }
    
    public static boolean reySeguro(){
        int temp = 1;
        //Este mÃ©todo se ejecuta en cada movimiento de piezas
        //BÃ¡sicamente checa si el rey esta en jaque por cada movimiento de piezas en cada posicion temporal
        for(int j = -1; j <=1; j+=2){
            for (int k = -1; k <= 1; k+=2) {
                try {
                    while (" ".equals(tableroPrueba[posicionReyB/8+temp*j][posicionReyB%8+temp*k])) {                        
                        temp++;
                    }
                    //Como el alfil y una dama se mueven de manera similar (diagonales) podemos saber si esta en jaque por un alfil o por una dama
                    if("a".equals(tableroPrueba[posicionReyB/8+temp*j][posicionReyB%8+temp*k]) || "d".equals(tableroPrueba[posicionReyB/8+temp*j][posicionReyB%8+temp*k])){
                        return false;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }
        
        temp = 1;
         
        //De manera similar ocupamos los movimientos de la torre y ya que la dama tambiÃ©n comparte los mismos movimientos ocupamos esta validaciÃ³n
        for (int i = -1; i <= 1; i+=2) {
            try {
                while (" ".equals(tableroPrueba[posicionReyB/8][posicionReyB%8+temp*i])) {
                    temp++;
                }
                if("t".equals(tableroPrueba[posicionReyB/8][posicionReyB%8+temp*i]) || "d".equals(tableroPrueba[posicionReyB/8][posicionReyB%8+temp*i])){
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
            try {
                while (" ".equals(tableroPrueba[posicionReyB/8+temp*i][posicionReyB%8])) {
                    temp++;
                }
                if("t".equals(tableroPrueba[posicionReyB/8+temp*i][posicionReyB%8]) || "d".equals(tableroPrueba[posicionReyB/8+temp*i][posicionReyB%8])){
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }
        
        //Para el yobaca checamos primero la superior e inferior y despues la fila inferior mÃ¡s uno y la fila superior mas uno
         for (int i = -1; i <=2; i+=2) {
            for (int j = -1; j <=2; j+=2) {
                try {
                    if("c".equals(tableroPrueba[posicionReyB/8+i][posicionReyB%8 + j *2])){
                        return false;
                    }
                } catch (Exception e) {
                }
                
                try {
                    if("c".equals(tableroPrueba[posicionReyB/8+i*2][posicionReyB%8+j])){
                        return false; 
                    }
                } catch (Exception e) {
                }
            }
        }
         
        //Para el rey checamos arriba, la fila en la que se encuentra y abajo
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i != 0 ||  j != 0){
                    try {
                        if("r".equals(tableroPrueba[posicionReyB/8+i][posicionReyB%8+j])){
                            return false;
                        } 
                    } catch (Exception e) {
                    }
                }
                    
            }
        }
        
        //para el peon
        if(posicionReyB >= 16){
            try {
                if("p".equals(tableroPrueba[posicionReyB/8-1][posicionReyB%8-1])){
                    return false;
                }
            } catch (Exception e) {}
            
            try {
                if("p".equals(tableroPrueba[posicionReyB/8-1][posicionReyB%8+1])){
                    return false;
                }
            } catch (Exception e) {
            }
        }
        
        return true;
    }
    
    private static String lmr(String listaPreLtr){        
        String listaCoronacion = "";
        String listaCapturas = "";
        String listaFront = "";
        String listaIgual = "";
        String listaRestante = "";
        String item;
        int origen, destino;
        for (int x = 0; x < listaPreLtr.length(); x+=5){
            item = listaPreLtr.substring(x, x+5);
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
//        {"r"," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," ","R"," "," ","T"},
//    };

       public static final String tableroPrueba[][] = {
        {"t", "c", "a", "d", "r", "a", "c", "t"},
        {"p", "p", "p", "p", "p", "p", "p", "p"},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {"P", "P", "P", "P", "P", "P", "P", "P"},
        {"T", "C", "A", "D", "R", "A", "C", "T"},
    };       
}
