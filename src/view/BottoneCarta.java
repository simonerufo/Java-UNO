package view;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import controller.ControllerGame;

import model.Carta;

/**
 * Classe che definisce la visualizzazione grafica di un bottone la cui rappresentazione è una carta di gioco di Uno
 */
public class BottoneCarta extends JButton{
	public static final int HORIZONTAL = 0, // COSTANTE ORIZZONTALE
			  				   TOP = 1,     // COSTANTE GIOCATORE TOP
							   GP = 2;      // COSTANTE GIOCATORE PRINCIPALE
	private final ImageIcon RETRO_ICON_H = new ImageIcon("src/view/assets/Retro_H.png");
	private final ImageIcon RETRO_ICON_V = new ImageIcon("src/view/assets/Retro_V.png");
	private Carta carta;                   // CARTA CHE VERRA' RAPPRESENTATA GRAFICAMENTE
	
	/**
	 * Costruttore della classe, che accetta come parametri la posizione della carta, 
	 * che decreterà quale immagine caricare
	 * (orizzontale, verticale o in mano al giocatore principale)
	 * e il parametro carta che inizializzerà il campo d'istanza
	 */
	public BottoneCarta(int position,Carta c) {
		this.carta = c;
		this.setEnabled(false);
		//Giocatore DX e SX
		if(position == HORIZONTAL) {
			this.setDisabledIcon(new ImageIcon(RETRO_ICON_H.getImage().getScaledInstance(100, 70, Image.SCALE_DEFAULT)));
			this.setPreferredSize(new Dimension(100,70));
			this.setIcon(new ImageIcon(RETRO_ICON_H.getImage().getScaledInstance(100, 70, Image.SCALE_DEFAULT)));
		}
		//GIOCATORE TOP
		if (position == TOP) {
			this.setDisabledIcon(new ImageIcon(RETRO_ICON_V.getImage().getScaledInstance(70,100,Image.SCALE_DEFAULT)));
			this.setPreferredSize(new Dimension(70,100));
			this.setIcon(new ImageIcon(RETRO_ICON_V.getImage().getScaledInstance(70,100,Image.SCALE_DEFAULT)));
		}
		// GIOCATORE PRINCIPALE
		if (position == GP) {
			
			this.setEnabled(true);
			this.setPreferredSize(new Dimension(70,100));
			this.setIcon(new ImageIcon(ControllerGame.getCarta(c)
							.getImage()
							.getScaledInstance(70, 100, Image.SCALE_DEFAULT)));
		}
	}

	public Carta getCarta() {
		return this.carta;
	}
	

}
