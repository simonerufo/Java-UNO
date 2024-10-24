package view;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
/**
 * Classe che visualizza il bottone per il passaggio del turno del giocatore principale
 */
public class BottonePassaTurno extends JButton {
	private final ImageIcon ICON = new ImageIcon("src/view/assets/passaturno_button.png");
	/**
	 * Metodo costruttore che imposta i valori del bottone
	 */
	public BottonePassaTurno() {
		setIcon(new ImageIcon(ICON.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		setVisible(false);
		setEnabled(false);
	}
}
