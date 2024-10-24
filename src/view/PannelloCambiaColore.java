package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
/**
 * Classe che visualizza graficamente il pannello per il cambio del colore da parte del giocatore principale
 */
public class PannelloCambiaColore extends JPanel{
	
	private JButton rosso;
	private JButton verde;
	private JButton blu;
	private JButton giallo;
	/**
	 * Metodo Costruttore del pannello, imposta i valori grafici dei componenti
	 */
	public PannelloCambiaColore() {
		//inizializzo il pannello
		setVisible(false);
		setEnabled(false);
		
		//inizializzazione dei bottoni
		rosso = new JButton();
		rosso.setBackground(new Color(216,0,0));
		rosso.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		
		verde = new JButton();
		verde.setBackground(new Color(0,192,0));
		verde.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		
		blu = new JButton();
		blu.setBackground(new Color(0,0,192)
				);
		blu.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		
		giallo = new JButton();
		giallo.setBackground(new Color(248,160,0));
		giallo.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		
		//layout
		setLayout(null);
		rosso.setBounds(20, 5, 50, 50);
		verde.setBounds(120, 5, 50, 50);
		blu.setBounds(220, 5, 50, 50);
		giallo.setBounds(320, 5, 50, 50);
		
		//aggiungo i components
		add(rosso);
		add(verde);
		add(blu);
		add(giallo);
	}
	
	
	public JButton getRosso() {
		return rosso;
	}
	
	public JButton getVerde() {
		return verde;
	}
	
	public JButton getBlu() {
		return blu;
	}
	
	public JButton getGiallo() {
		return giallo;
	}

}
