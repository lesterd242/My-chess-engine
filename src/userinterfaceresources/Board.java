package userinterfaceresources;

import Controllers.Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener, MouseMotionListener {

    private int mouseX, mouseY, newMouseX, newMouseY;
    private int squareSize = 64;
    private static int mouseDrag[][][]=new int[8][8][2];
    static String chessBoardAux[][]=new String[8][8];
    private static boolean ANTIREPEAT = false;
    
    
    //Este método se manda a llamar con repaint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        if(!ANTIREPEAT){
            copyBoard();
            ANTIREPEAT = true;
        }
        
        for(int i = 0; i <64; i+=2){
            
            /*
            *  Vamos incrementando las casillas, para construir primero una casilla clara y después una oscura.
            *  primero se construye una casilla blanca en le esquina superior izquierda, después dependiendo 
            *  de los valores en la segunda fila empezará una casilla negra y después una negra
            */
            //Casilla clara
            g.setColor(new Color(255, 200, 100));
            g.fillRect(10+((i%8+(i/8)%2)*squareSize), 10+((i/8)*squareSize), squareSize, squareSize);
            //Casilla oscura
            g.setColor(new Color(150, 50, 30));
            g.fillRect(10+(((i+1)%8 - ((i+1)/8)%2)*squareSize), 10+(((i+1)/8)*squareSize), squareSize, squareSize);
        }
        
        Image piezas;
        piezas = new ImageIcon("chess.png").getImage();
        
        for (int i = 0; i < 64; i++) {
            int k = -1 , j = k;

            switch (Main.tableroPrueba[i / 8][i % 8]) {
                case "p":
                    j = 5;
                    k = 1;
                    break;
                case "P":
                    j = 5;
                    k = 0;
                    break;
                case "t":
                    j = 4;
                    k = 1;
                    break;
                case "T":
                    j = 4;
                    k = 0;
                    break;
                case "c":
                    j = 3;
                    k = 1;
                    break;
                case "C":
                    j = 3;
                    k = 0;
                    break;
                case "a":
                    j = 2;
                    k = 1;
                    break;
                case "A":
                    j = 2;
                    k = 0;
                    break;
                case "d":
                    j = 1;
                    k = 1;
                    break;
                case "D":
                    j = 1;
                    k = 0;
                    break;
                case "r":
                    j = 0;
                    k = 1;
                    break;
                case "R":
                    j = 0;
                    k = 0;
                    break;
            }
            
            //Obtenemos 
            if(j != -1 && k != -1){
                g.drawImage(piezas, 10+((i%8) * squareSize)  , 10+((i/8) * squareSize) , 10+((i%8+1) * squareSize) , 10+((i/8+1) * squareSize), j*200, k*200, (j+1)*200, (k*200)+200, this);
            }
        }
        utils.Utils.imprimirTablero(chessBoardAux, 3, "Uno");
        utils.Utils.imprimirTablero(Main.tableroPrueba, 3, "Dos");

       g.setColor(Color.BLACK);
       for (int i=0;i<8;i++) {
            for (int j=0;j<8;j++) {
                if (!chessBoardAux[i][j].equals(Main.tableroPrueba[i][j])) {
                    g.drawRoundRect(j*squareSize+10+3, i*squareSize+10+3, squareSize-6, squareSize-6, 10, 10);
                    g.drawRoundRect(j*squareSize+10+4, i*squareSize+10+4, squareSize-8, squareSize-8, 10, 10);
                    System.out.println("Diferentes");
                }
            }
        }
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Si se hace un click dentro del tablero
        if(e.getX() < (8 * squareSize)+10  && e.getY() < (8 * squareSize)+10 ){
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getX() < 10+(8 * squareSize) && e.getY() < 10+(8 * squareSize) ){
            newMouseX = e.getX();
            newMouseY = e.getY();
            
            if(e.getButton() == MouseEvent.BUTTON1){
                String dragMove = ""; 
                //Si es un movimiento de peon y coronación
                if(newMouseY/squareSize == 0 && mouseY/squareSize == 1 && "P".equals(Main.tableroPrueba[mouseY/squareSize][mouseX/squareSize])){
                    dragMove = ""+(mouseX/squareSize)+(newMouseX/squareSize)+Main.tableroPrueba[(newMouseY/squareSize)][(newMouseX/squareSize)]+"DP";
                } else{ 
                    dragMove = ""+(mouseY/squareSize)+(mouseX/squareSize)+(newMouseY/squareSize)+(newMouseX/squareSize)+Main.tableroPrueba[newMouseY/squareSize][newMouseX/squareSize];
                }
                String movimientosPosibles = Main.generaMovimientos();
                if(movimientosPosibles.replaceAll(dragMove, "").length() < movimientosPosibles.length()){
                    Main.makeMove(dragMove);
                    copyBoard();
                    Main.giraTablero();
                    Main.getLabelStatus().setText("\"Pensando\"...");
                    String moveDone = Main.alphaBeta(Main.profundidadGlobal, 1000000, -1000000, "", 0);
                    Main.getLabelStatus().setText("Movimiento hecho: " + moveDone);
                    Main.makeMove(moveDone);
                    Main.giraTablero();
                    repaint();
                }
            }
        }
    }
    
    private static void copyBoard(){
        for (int i=0;i<8;i++) {
            System.arraycopy(Main.tableroPrueba[i], 0, chessBoardAux[i], 0, 8);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    
    /*Aleatorio
     @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getX() < (8 * squareSize) + 10 && e.getY() < (8 * squareSize) + 10){
            newMouseX = e.getX();
            newMouseY = e.getY();
            
            if(e.getButton() == MouseEvent.BUTTON1){
                String dragMove = ""; 
                //Si es un movimiento de peon y coronación
                if(newMouseY/squareSize == 0 && mouseY/squareSize == 1 && "P".equals(Main.tableroPrueba[mouseY/squareSize][mouseX/squareSize])){
                    dragMove = ""+(mouseX/squareSize)+(newMouseX/squareSize)+Main.tableroPrueba[(newMouseY/squareSize)][(newMouseX/squareSize)]+"DP";
                } else{ 
                    dragMove = ""+(mouseY/squareSize)+(mouseX/squareSize)+(newMouseY/squareSize)+(newMouseX/squareSize)+Main.tableroPrueba[newMouseY/squareSize][newMouseX/squareSize];
                }
                
                String movimientosPosibles = Main.generaMovimientos();
                if(movimientosPosibles.replaceAll(dragMove, "").length() < movimientosPosibles.length()){
                    Main.makeMove(dragMove);
                    
                    Main.giraTablero();
                    movimientosPosibles = Main.generaMovimientos();
                    int longitud = (movimientosPosibles.length()/5);
                    Random rnd = new Random();
                    int indice = rnd.nextInt(longitud);
                    while(indice % 5 != 0){
                        indice = rnd.nextInt(longitud);
                    }
                    String movimientoRespuesta = movimientosPosibles.substring(indice*5, (indice*5)+5);
                    Main.makeMove(movimientoRespuesta);
                    Main.giraTablero();
                }
            }
            repaint();
        }
    }
    */
    

}
