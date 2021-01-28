package evaluations;

import Controllers.Main;
import utils.Utils;

public class Rating {
    
    public static int rating(int list, int depth){
        int counter = 0;
        counter += rateAttack();
        counter += rateMaterial();
        counter += rateMoveability(list);
        counter += checkStaleMate(list, depth);
        counter += ratePositional();
        Main.giraTablero();
        counter -= rateAttack();
        counter -= rateMaterial();
        counter -= rateMoveability(list);
        counter -= checkStaleMate(Main.generaMovimientos().length(), depth);
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
        return 0;
    }
}
