package evaluations;

import Controllers.Main;
import utils.Utils;

public class Rating {
    
    private static final int PAWNBOARD[][] = {
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {15, 15, 25, 40, 40, 20, 20, 20},
        { 5, 10, 15, 30, 30, 10, 10, 10},
        { 5,  0, 10, 20, 20,  5,  5,  5},
        { 5, 15, -5,  5,  5,-15, 10, 10},
        {15, 15, 15,-10,-10, 15, 15, 10},
        { 0,  0,  0,  0,  0,  0,  0,  0}
    };
    
    private static final int KNIGHTBOARD[][] = {
        {-15,-10,-10,-10,-10,-10,-10,-15},
        {-10, -5, -5, -5, -5, -5, -5,-10},
        {-10,  0, 15,  5,  5, 15,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 15,  5,  5, 15,  0,-10},
        {-10, -5, -5, -5, -5, -5, -5,-10},
        {-15, -5,-10,-10,-10,-10,-10,-15}
    };
    
    private static final int QUEENBBOARD[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0, -5},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,-10,  5,  5,  0,  0,-10},
        {-10,  0,  5,  5,  5,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    
    private final static int ROOKBOARD[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  5, 10, 10, 10, 10,  5,  0}};
    
    private final static int BISHOPBOARD[][]={
        {  0,-10,-10,-10,-10,-10,-10,  0},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 20, 20, 20, 20,  0,-10},
        {-10, 10, 15, 15, 15, 15, 10,-10},
        {-10, 15,  0,  0,  0,  0, 15,-10},
        {  0,-10,-10,-10,-10,-10,-10,  0}};
    
    public static int rating(int list, int depth){
        int counter = 0;
        /*
         * Si viene de un nodo beta, entonces se giro el tablero para evaluar 
         * desde la posición de las blancas.
         */        
        counter += rateAttack();
        counter += rateMaterial();
        counter += checkStaleMate(list, depth);
        counter += rateMoveability(list);
        counter += ratePositional();
        Main.giraTablero();
        counter -= rateAttack();
        counter -= rateMaterial();
        counter -= rateMoveability(Main.generaMovimientos().length());
        counter -= ratePositional();
        Main.giraTablero();
        return -(counter+depth*50);
    }
    
    private static int rateAttack(){
        return 0;
    }
    private static int rateMaterial(){
        int counter = 0;
        
        for (int i = 0; i < 64; i++) {
            switch (Main.tableroPrueba[i / 8][i % 8]) {
                case "P":
                    counter += 100;
                    break;
                case "C":
                    counter += 300;
                    break;
                case "A":
                    counter += 300;
                    break;
                case "T":
                    counter += 500;
                    break;
                case "D":
                    counter += 800;
                    break;
            }
        }
        
        return counter;
    }
    
    private static int rateMoveability(int list) {
        int counter = list;
        return counter;
    }
    
    private static int checkStaleMate(int list, int depth){
        
        int counter = 0;
        if (list == 0) {
            if (!Main.reySeguro()) {
                counter += -(depth == 0 ? 25000:25000*depth);
            } else {
                counter += -(depth == 0 ? 12000:12000*depth);
            }
        }
        
        return counter;
    }
    
    private static int ratePositional(){
        int counter = 0;
        
        for (int i = 0; i < 64; i++) {
            switch(Main.tableroPrueba[i/8][i%8]){
                case "P":
                    counter += PAWNBOARD[i/8][i%8];
                    break;
                case "C":
                    counter += KNIGHTBOARD[i/8][i%8];
                break;
                case "D":
                    counter += QUEENBBOARD[i/8][i%8];
                    break;
                case "T":
                    counter += ROOKBOARD[i/8][i%8];
                    break;
                case "A":
                    counter += BISHOPBOARD[i/8][i%8];
                    break;
            }
        }
        
        return counter;
    }
}
