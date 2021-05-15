package userinterfaceresources;

import Controllers.Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

public class Board extends JPanel implements MouseListener, MouseMotionListener {

    int mouseX, mouseY, newMouseX, newMouseY;
    int squareSize = 64;
    private static JLabel labelEstado;
    private static String movimientoFinal;
    private static String chessBoardAux[][] = new String[8][8];
    
    //Este mÃ©todo se manda a llamar con repaint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        for(int i = 0; i <64; i+=2){
            
            /*
            *  Vamos incrementando las casillas, para construir primero una casilla clara y despuÃ©s una oscura.
            *  primero se construye una casilla blanca en le esquina superior izquierda, despuÃ©s dependiendo 
            *  de los valores en la segunda fila empezarÃ¡ una casilla negra y despuÃ©s una negra
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
            
            
            //Pintamos la pieza en el lugar que le corresponde
            if(j != -1 && k != -1){
                g.drawImage(piezas, 10+((i%8) * squareSize)  , 10+((i/8) * squareSize) , 10+((i%8+1) * squareSize) , 10+((i/8+1) * squareSize), j*200, k*200, (j+1)*200, (k*200)+200, this);
            }
        }
        
       g.setColor(Color.BLACK);
       for (int i=0;i<8;i++) {
            for (int j=0;j<8;j++) {
                if (!chessBoardAux[i][j].equals(Main.tableroPrueba[i][j])) {
                    g.drawRoundRect(j*squareSize+10+3, i*squareSize+10+3, squareSize-6, squareSize-6, 10, 10);
                    g.drawRoundRect(j*squareSize+10+4, i*squareSize+10+4, squareSize-8, squareSize-8, 10, 10);
                }
            }
       }
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Si se hace un click dentro del tablero
        if(e.getX() < (8 * squareSize)+10  && e.getY() < (8 * squareSize)+10 ){
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getX() < 10+(8 * squareSize) && e.getY() < 10+(8 * squareSize) ){
            newMouseX = e.getX();
            newMouseY = e.getY();

            if(e.getButton() == MouseEvent.BUTTON1){
                String dragMove = ""; 
                //Si es un movimiento de peon y coronaciÃ³n
                if(newMouseY/squareSize == 0 && mouseY/squareSize == 1 && "P".equals(Main.tableroPrueba[mouseY/squareSize][mouseX/squareSize])){
                    dragMove = ""+(mouseX/squareSize)+(newMouseX/squareSize)+Main.tableroPrueba[(newMouseY/squareSize)][(newMouseX/squareSize)]+"DP";
                } else{ 
                    dragMove = ""+(mouseY/squareSize)+(mouseX/squareSize)+(newMouseY/squareSize)+(newMouseX/squareSize)+Main.tableroPrueba[newMouseY/squareSize][newMouseX/squareSize];
                }
                
                String movimientosPosibles = Main.generaMovimientos();
                if(movimientosPosibles.replaceAll(dragMove, "").length() < movimientosPosibles.length()){
                    Window win = SwingUtilities.getWindowAncestor(this);
                    JDialog dialog = new JDialog(win, "Espere", Dialog.ModalityType.APPLICATION_MODAL);
                    dialog.setUndecorated(true);
                    dialog.setSize(0, 0);
                    dialog.setLocation(10, squareSize*8+20);
                    dialog.add(new JLabel("Espere..."));
                    
                    Main.makeMove(dragMove);
                    Main.historial += 1;
                    copyBoard();
                    Main.giraTablero();
                    
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            labelEstado.setText("Pensando en las opciones:\n " + Main.generaMovimientos());
                            movimientoFinal = Main.alphaBeta(Main.profundidadGlobal, 1000000, -1000000, "", 0); 
                            Main.makeMove(movimientoFinal);
                            Main.historial += 1;
                            return null;
                        }
                    };
                    
                    worker.addPropertyChangeListener((PropertyChangeEvent pce) -> {
                        if (pce.getPropertyName().equals("state")) {
                            if (pce.getNewValue() == SwingWorker.StateValue.DONE) {
                                labelEstado.setText("Movimiento hecho: " + movimientoFinal);
                                dialog.dispose();
                            }
                        }
                    });
                    
                    worker.execute();
                    dialog.setVisible(true);
                    
                    Main.giraTablero();
                    repaint();
                    
                    
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    
    public static void main(String[] args) {
        
        Main.inicializaReyes();
        JFrame frame = new JFrame();
        Board board = new Board();
        labelEstado = new JLabel(String.format("%20s", " "));
        labelEstado.setBorder(new TitledBorder("Estado del juego"));
        frame.add(board);
        frame.add(labelEstado, BorderLayout.SOUTH);
        frame.setSize(700, 800);
        frame.setLocation(2400, 0);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        copyBoard();
    }
    
    
    private static void copyBoard(){
        for (int i=0;i<8;i++) {
            System.arraycopy(Main.tableroPrueba[i], 0, chessBoardAux[i], 0, 8);
        }
    }
}
