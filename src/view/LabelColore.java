package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import model.CartaColore.Colore;
/**
 * Classe che visualizza graficamente il colore giocabile dai giocatori
 */
public class LabelColore extends JLabel {
	/**
	 * Metodo costruttore che imposta i valori grafici del JLabel
	 */
	public LabelColore() {
		this.setOpaque(true);
		this.setBackground(Color.black);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 4));
	}

	/**
	 * Metodo che prende un parametro colore,
	 *  ed in base ad esso imposta il colore visualizzato nel label
	 */
	public void cambiaColore(Colore colore) {
		//COLORE NON IMPOSTATO
		if (colore == null) {
			this.setBackground(Color.LIGHT_GRAY);
		}
		//BLU
		if (colore.equals(Colore.BLU)) {
			this.setBackground(new Color(0,0,192));
		}
		//ROSSO
		if (colore.equals(Colore.ROSSO)) {
			this.setBackground(new Color(216,0,0));
		}
		//VERDE
		if (colore.equals(Colore.VERDE)) {
			this.setBackground(new Color(0,192,0));
		}
		//GIALLO
		if (colore.equals(Colore.GIALLO)) {
			this.setBackground(new Color(248,160,0));
		}
		//aggiorna
		this.revalidate();
	}
	
	
}
