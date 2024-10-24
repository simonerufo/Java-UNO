package view;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
/**
 * Classe che visualizza graficamente il bottone UNO! destinato all'uso del giocatore principale
 */
public class BottoneUno extends JButton{
	
	private final ImageIcon ICON = new ImageIcon("src/view/assets/Uno_Button.png");
	/**
	 * Metodo costruttore che imposta i valori grafici del bottone
	 */
	public BottoneUno() {
		setIcon(new ImageIcon(ICON.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		setVisible(false);
		setEnabled(false);
	}
}
