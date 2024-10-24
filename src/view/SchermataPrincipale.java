package view;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe che rappresenta graficamente la schermata di avvio del videogioco
 */
public class SchermataPrincipale extends JFrame {
	
	private JLabel labelBackground; // Immagine di sfondo
	private JButton bottoneStats;   // bottone di avvio del frame statistiche
	private JButton	bottoneGame;    // bottone di avvio del frame di gioco
	
	private final ImageIcon LOGO = new ImageIcon("src/view/assets/Uno_Button.png");
	private final ImageIcon BACKGROUND = new ImageIcon("src/view/assets/schermataprincipale.png");
	private final ImageIcon START = new ImageIcon("src/view/assets/start_button.png");
	private final ImageIcon STATS =	new ImageIcon("src/view/assets/stats_button.png");
	/**
	 * Metodo costruttore della schermata, essa imposta i valori grafici dei componenti della classe
	 */
	public SchermataPrincipale() {
		super("JUno!");
		
		//componenti
		labelBackground = new JLabel();
		labelBackground.setBounds(0, 0, 1200, 720);
		labelBackground.setIcon(BACKGROUND);
		
		bottoneGame = new JButton();
		bottoneGame.setBounds(300,200,600,50);
		bottoneGame.setIcon(START);
		
		bottoneStats = new JButton();
		bottoneStats.setBounds(300,400,600,50);
		bottoneStats.setIcon(STATS);
		
		//Layout pannello
		setLayout(null);
		labelBackground.setLayout(null);
		
		//aggiungo i componenti
		
		
		labelBackground.add(bottoneGame);
		labelBackground.add(bottoneStats);
		add(labelBackground);
		
		// inizializzo il frame
		setIconImage(LOGO.getImage());
		setVisible(true);
		setSize(new Dimension(1200,720));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JButton getGame() {
		return bottoneGame;
	}
	
	public JButton getStats() {
		return bottoneStats;
	}

 
}