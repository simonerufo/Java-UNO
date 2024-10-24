package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Classe che visualizza graficamente la schermata di gioco 
 * e ne imposta i vari pannelli destinati al giocatore principale ed ai bots
 */
public class GameFrame extends JFrame {
	private final Image LOGO = new ImageIcon("src/view/assets/Uno_Button.png").getImage();
	
	private PannelloMazzi pannelloMazzi;
	private PannelloGiocatorePrincipale pannelloGP; //GIOCATORE PRINCIPALE
	private PannelloGiocatoreSX pannelloP2;         // PANNELLO SX
	private	PannelloGiocatoreTOP pannelloP3;        // PANNELLO TOP
	private PannelloGiocatoreDX pannelloP4;         // PANNELLO DX
	private SchermataFinale sf;            
	/**
	 * Metodo costruttore che inizializza tutti i campi d'istanza della classe e ne regola i valori grafici
	 */
	public GameFrame() {
		super("JUno - let's play!");
		
		pannelloMazzi = new PannelloMazzi();
		pannelloMazzi.setPreferredSize(new Dimension(360,360));
		pannelloMazzi.setBorder(BorderFactory.createLineBorder(Color.black, 4));
		pannelloMazzi.setBackground(new Color(22,128,57));
		
		pannelloP2 = new PannelloGiocatoreSX();
		pannelloP2.setBackground(new Color(255,224,89));
		pannelloP2.setPreferredSize(new Dimension(180,360));
		
		pannelloP3 = new PannelloGiocatoreTOP();
		pannelloP3.setBackground(new Color(255,224,89));
		pannelloP3.setPreferredSize(new Dimension(1200,180));
		
		pannelloP4 = new PannelloGiocatoreDX();
		pannelloP4.setBackground(new Color(255,224,89));
		pannelloP4.setPreferredSize(new Dimension(180,360));
		
		pannelloGP = new PannelloGiocatorePrincipale();
		pannelloGP.setBackground(new Color(255,224,89));
		pannelloGP.setPreferredSize(new Dimension(1200,180));
	
		sf = new SchermataFinale();
		
		//Layout
		setLayout(new BorderLayout());
		add(pannelloMazzi,BorderLayout.CENTER);
		add(pannelloGP,BorderLayout.SOUTH);
		add(pannelloP2,BorderLayout.WEST);
		add(pannelloP4,BorderLayout.EAST);
		add(pannelloP3,BorderLayout.NORTH);
		
		//Frame settings
		setSize(1280,720);
		
		setLocationRelativeTo(null);
		
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
		
		setIconImage(LOGO);
	}

	public PannelloMazzi getPannelloMazzi() {
		return pannelloMazzi;
	}
	
	public  PannelloGiocatorePrincipale getPannelloGP() {
		return pannelloGP;
	}
	
	public PannelloGiocatoreSX getPannelloGSX() {
		return pannelloP2;
	}
	
	public PannelloGiocatoreTOP getPannelloGT() {
		return pannelloP3;
	}
	
	public PannelloGiocatoreDX getPannelloGDX() {
		return pannelloP4;
	}
	
	public SchermataFinale getFinale() {
		return sf;
	}
}
