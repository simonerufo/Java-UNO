package view;


import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.ControllerGame;

import model.Carta;
import model.MazzoScarto;
import model.Observer;
/**
 * Classe che visualizza graficamente il mazzo di scarto nel model (Osservato da questa classe)
 */
public class LabelMazzoScarto extends JLabel implements Observer{
	
	/**
	 * Metodo implementato dall'interfaccia Observer,
	 * aggiornamento dovuto dalla notifica del subject,
	 * visualizza la carta aggiornata in cima al mazzo di scarto
	 */
	@Override
	public void update(Object o) {
		
		MazzoScarto ms = (MazzoScarto)o;

		Carta testa = ms.getTesta();
		
		setIcon(new ImageIcon(new ImageIcon("src/view/assets/Update_Card.png")
				.getImage()
				.getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
		
		Timer timer = new Timer();
		TimerTask gioca = new TimerTask() {
			@Override
			public void run() {
				// se la carta ancora non è stata impostata
				if (testa == null || ControllerGame.getCarta(testa) == null)
					setIcon(new ImageIcon(
							new ImageIcon("src/view/assets/logo.png")
							.getImage()
							.getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
				//altrimenti
				else
					setIcon(new ImageIcon(ControllerGame.getCarta(testa)
							.getImage()
							.getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
			}
		};
		timer.schedule(gioca, 50);
	}
	
}
