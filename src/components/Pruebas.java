package components;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import utils.Utils;

public class Pruebas {

	static JButton button = new JButton("enviar");
	
	public static void window() {
		JFrame frame = new JFrame();
		
		JTextField field = new JTextField();
		button.addActionListener((e) -> {
			MoveMaker.makeMove(field.getText() + " ", 0, true);
			Utils.imprimirTablero(AlphaBeta.tableroPrueba, 0, null);
			AlphaBeta.giraTablero();
			String movimientoFinal = AlphaBeta.alphaBeta(AlphaBeta.searchDepth, 1000000, -1000000, "", AlphaBeta.BLACK);
			MoveMaker.makeMove(movimientoFinal, 1, true);
			AlphaBeta.giraTablero();
			Utils.imprimirTablero(AlphaBeta.tableroPrueba, 0, movimientoFinal);
		});
		
		frame.add(field, BorderLayout.NORTH);
		frame.add(button, BorderLayout.CENTER);
		
		frame.setSize(200, 160);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void setEnviroment() {
		AlphaBeta.inicializaReyes();
		Utils.imprimirTablero(AlphaBeta.tableroPrueba, 0, null);
		window();
	}
	
	public static void main(String[] args) {
		setEnviroment();
	}
}
