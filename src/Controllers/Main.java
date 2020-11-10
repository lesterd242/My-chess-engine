
package Controllers;

import java.lang.reflect.Array;
import java.util.Arrays;
import javax.swing.JFrame;
import userinterfaceresources.Board;


public class Main {

    static int posicionReyB = 0, posicionReyN;
    
    
    
    public static void main(String[] args) {
//        Board board = new Board();
//        JFrame frame = new JFrame();
//        frame.add(board);
//        frame.setSize(1000, 900);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        makeMove("6050 ");//Haciendo un movimiento manual
        System.out.println(generaMovimientos());
        makeMove("6444 ");
        for(int x = 0; x < 8; x++){
            System.out.println(Arrays.toString(tableroPrueba[x]));
        }
    }
    //m??todo para generar un movimiento
    public static void makeMove(String movimiento){
        if(movimiento.charAt(4) != 'P'){//Si no es una coronación
            //x1,y1,x2,y2,piezacapturada
            
            //Ponemos la pieza de la casilla de origen a la casilla final
            tableroPrueba[Character.getNumericValue(movimiento.charAt(2))][Character.getNumericValue(movimiento.charAt(3))] = tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))];
            //La casilla de inicio ahora tiene estar vacia
            tableroPrueba[Character.getNumericValue(movimiento.charAt(0))][Character.getNumericValue(movimiento.charAt(1))] = " ";
        }else{
            //column1,column2,piezacapturada,nuevapieza,P
            
            //Se coloca la casilla del peon en blanco y la nueva casilla con la pieza a coronar
            tableroPrueba[1][Character.getNumericValue(movimiento.charAt(0))]= " ";
            tableroPrueba[0][Character.getNumericValue(movimiento.charAt(1))] = String.valueOf(movimiento.charAt(3));
        }
    }
    
    public static void undoMove(String movimiento){
        
    }
    
    private static String generaMovimientos(){
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
                    tableroPrueba[row][col] = " ";//En la posición donde está el rey la actualizamos, como se movio entonces es un lugar vacio
                    tableroPrueba[row-1+j/3][col-1+j%3] = "R";//Movemos al rey a la casilla que se evalu?? que tiene una pieza o esta disponible (movimiento v??lido)
                    int posicionTemporal = posicionReyB;//Guardamos la posición del rey
                    posicionReyB = i+(j/3)*8+j%3-9;//Simplemente restamos la cantidad de casillas para empezar en la casilla 00 y después en la 01, para incrementar la columna se usa el modulo
                    if(reySeguro()){
                        lista = lista + row + col + (row-1+j/3) + (col-1+j%3) + piezaAnterior;
                    }
                    tableroPrueba[row][col] = "R";//Volvemos a colocar el rey en su posicion original
                    tableroPrueba[row-1+j/3][col-1+j%3] = piezaAnterior;//Volvemos a colocar a la pieza que se comio en su lugar original, si es una casilla vacía también se coloca
                    posicionReyB = posicionTemporal;
                }   
            }    
            } catch (Exception e) {
                
                System.out.println("Error REY (" + j +") " + e.toString());
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

        for (int j = -1; j <= 1; j++) {//Cuando es -1 va a validar la fila anterior, cuando es 0 va a validar la misma fila hacìa la izquierda o derecha, cuando es 1, va a validar la fila siguiente
            for (int k = -1; k <= 1; k++) {//Cuando es -1 va a validar hacia la izquierda, cuando es 0 va a validar al centro, cuando es 1 va a validar a la derecha 
                if (j != 0 || k != 0) {//Para que no valide su propia posici??n
                    try {
                        while (" ".equals(tableroPrueba[row + temp * j][col + temp * k])) {

                            piezaAnterior = tableroPrueba[row + temp * j][col + temp * k];//obtenemos la pieza anterior, quiza se podría quitar ya que solo valida espacios en blanco
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
                            piezaAnterior = tableroPrueba[row + temp * j][col + temp * k];//obtenemos la pieza anterior, quiza se podría quitar ya que solo valida espacios en blanco
                            tableroPrueba[row][col] = " ";//En la casilla donde estaba la dama ahora es un lugar vacio
                            tableroPrueba[row + temp * j][col + temp * k] = "D";//Ponemos a la dama en el nuevo lugar

                            if (reySeguro()) {
                                lista = lista + row + col + (row + temp * j) + (col + temp * k) + piezaAnterior;//Obtenemos la lista, desde la posicion de la que parte hacia el destino y la pieza si es que hay
                            }

                            tableroPrueba[row][col] = "D";//Colocamos a la dama en su lugar de origen
                            tableroPrueba[row + temp * j][col + temp * k] = piezaAnterior;
                        }
                    } catch (Exception e) {
                        System.out.println("Error dama");
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
            for (int k = -1; k <= 1; k += 2) {//-1 para las columnas izquierdas, 1 para las columnas derechas, así aumenta la columna junto con la fila
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
                        System.err.println("Error Alfil");
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
                   
                   tableroPrueba[row][col + temp * j] = "T";
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
                System.err.println("Error torre");     
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
                System.err.println("Error torre");     
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
                        if(reySeguro()){
                            lista = lista + row + col + (row + j) + (col + k * 2) + piezaAnterior;   
                        }
                        tableroPrueba[row][col] = "C";
                        tableroPrueba[row + j][col + k * 2] = piezaAnterior;
                    }
                } catch (Exception e) {
                    System.out.println("Error CABALLO");
                }
                
                try {
                    if (Character.isLowerCase(tableroPrueba[row + j * 2][col + k].charAt(0)) || " ".equals(tableroPrueba[row + j * 2][col + k])) {
                        piezaAnterior = tableroPrueba[row + j * 2] [col + k];
                        tableroPrueba[row][col] = " ";
                        if(reySeguro()){
                            lista = lista + row + col + (row + j * 2) + (col + k) + piezaAnterior;   
                        }
                        tableroPrueba[row][col] = "C";
                        tableroPrueba[row + j * 2][col + k] = piezaAnterior;
                    }
                } catch (Exception e) {
                    System.out.println("Error CABALLO");
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
                System.out.println("Error peon " + e.getMessage());
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
                System.out.println("Error peon " + e.getMessage());
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
            
            
            
    
    private static boolean reySeguro(){
        int temp = 1;
        //Este método se ejecuta en cada movimiento de piezas
        //Básicamente checa si el rey esta en jaque por cada movimiento de piezas en cada posicion temporal
        for(int j = -1; j <=1; j+=2){
            for (int k = -1; k <= 1; k+=2) {
                try {
                    while (" ".equals(tableroPrueba[posicionReyB/8+temp*j][posicionReyB%8+temp*k])) {                        
                        temp++;
                    }
                    //Como el alfil y una dama se mueven de manera similar (diagonales) podemos saber si esta en jaque por un alfil o por una dama
                    if("a".equals(tableroPrueba[posicionReyB/8+temp*j][posicionReyB%8+temp*k]) || "d".equals(tableroPrueba[posicionReyB/8+temp*j][posicionReyB%8+temp*k])){
                        System.out.println("No es un movimiento valido");
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("Exception running rey seguro " + e.getMessage());
                }
                temp = 1;
            }
        }
        
        temp = 1;
         
        //De manera similar ocupamos los movimientos de la torre y ya que la dama también comparte los mismos movimientos ocupamos esta validación
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
        
        //Para el yobaca checamos primero la superior e inferior y despues la fila inferior más uno y la fila superior mas uno
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
    

//     private static final String tableroPrueba[][] = {
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," "," "," "},
//        {"a"," "," "," "," "," "," "," "},
//        {" "," "," "," "," "," ","P"," "},
//        {"P"," "," "," "," "," "," ","P"},
//        {" "," "," "," "," "," "," "," "},
//    };

       private static final String tableroPrueba[][] = {
        {"t", "a", "c", "d", "r", "a", "c", "t"},
        {"p", "p", "p", "p", "p", "p", "p", "p"},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {"P", "P", "P", "P", "P", "P", "P", "P"},
        {"T", "C", "A", "D", "R", "A", "C", "T"},
    };
}