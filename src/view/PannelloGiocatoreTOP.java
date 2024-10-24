package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.ControllerGame;
import model.Carta;
import model.GiocatoreComp;
import model.Observer;


/**
 * Classe che visualizza graficamente il bot in alto tramite un pannello con avatar e nickname
 * e un pannello che rappresenta graficamente la mano del bot
 */
public class PannelloGiocatoreTOP extends JPanel {

	private PannelloInfoTOP pannelloInfo;
	private  ManoGiocatoreTOP manoGiocatore;
	/**
	 * Metodo costruttore che imposta il layout grafico della classe
	 */
	public PannelloGiocatoreTOP() {
		
		setLayout(new BorderLayout(15,10));
		
		pannelloInfo = new PannelloInfoTOP();
		pannelloInfo.setBackground(new Color(255,224,89));
		
		manoGiocatore = new ManoGiocatoreTOP();
		manoGiocatore.setPreferredSize(new Dimension(0,100));
		manoGiocatore.setBackground(new Color(255,224,89));
		
		add(manoGiocatore,BorderLayout.NORTH);
		add(pannelloInfo,BorderLayout.CENTER);
	}

	public PannelloInfoTOP getPannelloInfo() {
		return pannelloInfo;
	}
	
	public ManoGiocatoreTOP getMano() {
		return manoGiocatore;
	}
	/**
	 * Classe interna che rappresenta la visualizzazione grafica delle informazioni del bot
	 * questa classe Osserva il comportamento nel model del Giocatore
	 */
	public class PannelloInfoTOP extends JPanel implements Observer{
		private JLabel avatar;
		private JTextField nome;
		/**
		 * Costruttore della classe che imposta i valori grafici di essa
		 */
		public PannelloInfoTOP() {
			//componenti
			avatar = new JLabel();
			avatar.setPreferredSize(new Dimension(60,60));
			avatar.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			avatar.setHorizontalAlignment(JLabel.CENTER);
			
			nome = new JTextField();
			nome.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			nome.setHorizontalAlignment(SwingConstants.CENTER);
			nome.setFont(new Font("Serif",Font.BOLD,12));
			nome.setForeground(Color.RED);
			nome.setEditable(false);
			nome.setFocusable(false);
			
			nome.setPreferredSize(new Dimension(80,20));
			//layout
			setLayout(new FlowLayout());
			//aggiungo i componenti
			add(avatar);
			add(nome);
		}
		/**
		 * Metodo implementato dall'interfaccia funzionale Observer
		 * che aggiorna lo stato del nome e l'avatar graficamente
		 * e segnala quando è il suo turno
		 */
		@Override
		public void update(Object o) {
			GiocatoreComp g = (GiocatoreComp)o;
			nome.setText(g.getNickname());	
			avatar.setIcon(new ImageIcon(g.getAvatar()));
			//bot attivo
			if(ControllerGame.getGiocatoreAttivo().equals(g)) {
				avatar.setBorder(BorderFactory.createLineBorder(Color.red, 5));
				nome.setBorder(BorderFactory.createLineBorder(Color.red, 5));
			 }
			//bot non attivo
			else {
				avatar.setBorder(BorderFactory.createLineBorder(Color.black, 2));
				nome.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			 }
		}
	}
	
	/**
	 * Classe interna che visualizza graficamente la mano del bot
	 */
	public class ManoGiocatoreTOP extends JPanel {
		private List<BottoneCarta> mano = new ArrayList<>();
		private Carta ultimaGiocata;
		/**
		 * Costruttore della classe che imposta i valori grafici della classe
		 */
		public ManoGiocatoreTOP() {
			setLayout(new FlowLayout(FlowLayout.CENTER,-40,0));
		}
		
		public List<BottoneCarta> getMano() {
			return mano;
		}
		/**
		 * dato un bottone carta come parametro, 
		 * lo rimuove dalla mano del bot 
		 * e ne restituisce la carta rimossa
		 */
		public Carta rimuovi(BottoneCarta bc) {
			mano.remove(bc);
			remove(bc);
			repaint();
			revalidate();
			ultimaGiocata = bc.getCarta();
			return ultimaGiocata;
		}
		/**
		 * data una carta come parametro, 
		 * il metodo aggiunge il suo corrispettivo bottone carta 
		 * alla rappresentazione grafica della mano del giocatore
		 */
		public void addMano(Carta c) {
			BottoneCarta carta = new BottoneCarta(BottoneCarta.VERTICAL,c);
			mano.add(carta);
			add(carta);
			revalidate();
		}
	}
}
