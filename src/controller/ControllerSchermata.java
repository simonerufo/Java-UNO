package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.SchermataPrincipale;
/**
 * Classe che gestisce la visualizzazione ed il comportamento della schermata principale di gioco
 * questa classe può essere istanziata una volta sola
 */
public class ControllerSchermata implements ActionListener{
	private static ControllerSchermata instance;
	private SchermataPrincipale sc = new SchermataPrincipale();
	private final String BOTTONE = "src/view/assets/sound/button_pressed.wav";
	
	/**
	 * Metodo che ritorna l'unica istanza possibile della classe
	 */
	public static ControllerSchermata getInstance() {
		if (instance != null)
			return instance;
		return new ControllerSchermata();
	}
	/**
	 * Metodo costruttore della classe che rende utilizzabili i bottoni della schermata
	 */
	private ControllerSchermata() {
		//bottone avvio gioco
		sc.getGame().addActionListener(this);
		//bottone personalizzazione e statistiche del giocatore
		sc.getStats().addActionListener(this);
	}
	/**
	 * Metodo implementato dall'interfaccia funzionale ActionListener
	 * che regola il funzionamento dei componenti al loro click
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//BOTTONE GIOCO
		if (e.getSource() == sc.getGame()) {
			//avvia il gioco e chiudi la finestra principale
			Utilities.playSound(BOTTONE);
			ControllerGame.getInstance();
			sc.dispose();
		}
		// BOTTONE STATS
		//apri la finestra delle statistiche
		if (e.getSource() == sc.getStats()) {
			Utilities.playSound(BOTTONE);
			ControllerStats.getInstance();
		}
		
	}
	
	
}
