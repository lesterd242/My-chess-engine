package evaluations;

import Controllers.Main;

public class Rating {
    public static int rating(int list, int depth){
        int counter = 0;
        counter += rateAttack();
        counter += rateMaterial();
        counter += rateMoveability();
        counter += ratePositional();
        Main.giraTablero();
        counter -= rateAttack();
        counter -= rateMaterial();
        counter -= rateMoveability();
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
    private static int rateMoveability(){
        return 0;
    }
    private static int ratePositional(){
        return 0;
    }
}