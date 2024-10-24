package view;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
/**
 * Classe che rappresenta il frame sulla quale vi è un pannello con le informazioni relative al giocatore principale
 */
public class StatsFrame extends JFrame {
	
	private PannelloStats pannelloStats;
	private final Image LOGO = new ImageIcon("src/view/assets/Uno_Button.png").getImage();
	/**
	 * Costruttore che imposta i valori grafici del pannello delle statistiche e del frame
	 */
	public StatsFrame() {
		super("JUno - player's stats");
		//inizializzo i componenti
		pannelloStats = new PannelloStats();
		//Layout
		setLayout(null);
		pannelloStats.setBounds(0, 0, 600, 600);
		//aggiungo i componenti
		add(pannelloStats);
		
		//inizializzazione del frame
		setIconImage(LOGO);
		setSize(600,600);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public PannelloStats getStats() {
		return pannelloStats;
	}
}
