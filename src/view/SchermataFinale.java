package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Classe che rappresenta graficamente il pannello che sancisce la fine della partita di UNO,
 * esso raccoglie un label con l'avatar del giocatore che ha vinto, un messaggio con il nickname del vincitore
 * e un bottone con le informazioni relative al giocatore principale
 */
public class SchermataFinale extends JPanel{
	private final ImageIcon STATS = new ImageIcon("src/view/assets/stats_button2.png");
	
	private JLabel avatar;
	private JTextField messaggio;
	private JButton stats;
	
	/**
	 * Metodo costruttore della classe, il cui inizializza i valori grafici dei vari componenti della classe
	 */
	public SchermataFinale() {
		//inizializzo il pannello
		setPreferredSize(new Dimension(600,600));
		setBackground(Color.black);
		setBounds(0,0,600,600);
		//inizializzo i componenti
		avatar = new JLabel();
		avatar.setBounds(250, 100, 100, 100);
		avatar.setBorder(BorderFactory.createLineBorder(Color.black,4));
		
		messaggio = new JTextField();
		messaggio.setBounds(100, 225, 400, 50);
		messaggio.setHorizontalAlignment(SwingConstants.CENTER);
		messaggio.setFont(new Font("Serif", Font.BOLD, 20));
		messaggio.setForeground(Color.WHITE);
		messaggio.setBackground(Color.black);
		messaggio.setEditable(false);
		messaggio.setFocusable(false);
		
		stats = new JButton();
		stats.setBounds(200, 375, 200, 50);
		stats.setIcon(STATS);

		
		//Layout
		setLayout(null);

		//aggiungo i componenti
		add(avatar);
		add(messaggio);
		add(stats);
	}

	public JLabel getAvatar() {
		return avatar;
	}
	public JTextField getMessaggio() {
		return messaggio;
	}
	public JButton getStats() {
		return stats;
	}

}

