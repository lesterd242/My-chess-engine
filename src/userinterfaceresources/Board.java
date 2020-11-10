package userinterfaceresources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener, MouseMotionListener {

    int x = 10;
    int y = 10;
    int squareSize = 64;

    //Este método se manda a llamar con repaint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        for(int i = 0; i <64; i+=2){
            g.setColor(new Color(255, 200, 100));
            //vamos incrementando las casillas, para construir primero una casilla clara y después una oscura 
            g.fillRect(10+(i%8+(i/8)%2)*squareSize, 10+(i/8)*squareSize, squareSize, squareSize);
            g.setColor(new Color(150, 50, 30));
            g.fillRect(10+((i+1)%8 - ((i+1)/8)%2)*squareSize, 10+((i+1)/8)*squareSize, squareSize, squareSize);
        }
        
       
        Image img = new ImageIcon("chess.png").getImage();
        g.drawImage(img, x, y, x + 400, y + 200, 0, 0, 1000, 500, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
//        repaint(); 
    }

}
