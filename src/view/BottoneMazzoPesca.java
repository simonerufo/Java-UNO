package view;

import java.awt.Dimension;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.MazzoPesca;
import model.Observer;

/**
 * Classe che rappresenta graficamente il mazzo di pesca (come un bottone), 
 * questa classe Osserva la classe mazzo pesca nel model e viene aggiornata ad ogni suo cambio di stato.
 */
public class BottoneMazzoPesca extends JButton implements Observer{
	private final ImageIcon ICONA_MAZZO = new ImageIcon("src/view/assets/retro.png");
	private final ImageIcon ICONA_MAZZO_VUOTO = new ImageIcon("src/view/assets/Update_Card.png");
	/**
	 * Metodo Costruttore che definisce la taglia e l'immagine visualizzata del mazzo
	 */
	public BottoneMazzoPesca() {
		setPreferredSize(new Dimension(100,150));
		setIcon(new ImageIcon((ICONA_MAZZO).getImage().getScaledInstance(100, 150, Image.SCALE_DEFAULT)));
	}
	/**
	 * Metodo implementato dall'interfaccia funzionale Observer,
	 * il quale aggiornerà il bottone se il mazzo dovesse terminare le carte,
	 * o se il mazzo avesse ancora carte disponibili
	 */
	@Override
	public void update(Object o) {
		MazzoPesca mp = (MazzoPesca)o;
		setIcon(new ImageIcon(ICONA_MAZZO_VUOTO
							  .getImage()
							  .getScaledInstance(100, 150, Image.SCALE_DEFAULT)));
		Timer timer = new Timer();
		TimerTask aggiorna = new TimerTask() {
			@Override
			public void run() {
				if (mp.getSize() == 0) 
					setIcon(new ImageIcon(ICONA_MAZZO_VUOTO
							 			  .getImage()
							 			  .getScaledInstance(100, 150, Image.SCALE_DEFAULT)));
				else
					setIcon(new ImageIcon((ICONA_MAZZO).getImage().getScaledInstance(100, 150, Image.SCALE_DEFAULT)));
			}
		};
		timer.schedule(aggiorna, 50);
	}
	
}
