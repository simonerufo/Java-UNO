package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
 * Classe che visualizza graficamente il bot destro tramite un pannello con avatar e nickname
 * e un pannello che rappresenta graficamente la mano del bot
 */
public class PannelloGiocatoreDX extends JPanel {
	
	private PannelloInfoDX pannelloInfo;
	private  ManoGiocatoreDX manoGiocatore;
	/**
	 * Metodo costruttore che imposta il layout grafico della classe
	 */
	public PannelloGiocatoreDX() {
		//Layout
		setLayout(new BorderLayout(15,10));
		
		//setting dei componenti
		pannelloInfo = new PannelloInfoDX();
		pannelloInfo.setPreferredSize(new Dimension(60,0));
		
		manoGiocatore = new ManoGiocatoreDX();
		manoGiocatore.setPreferredSize(new Dimension(100,0));
		
		manoGiocatore.setBackground(new Color(255,224,89));
		pannelloInfo.setBackground(new Color(255,224,89));
		
		//aggiungo i componenti
		add(pannelloInfo,BorderLayout.CENTER);
		add(manoGiocatore,BorderLayout.EAST);
	}

	public PannelloInfoDX getPannelloInfo() {
		return pannelloInfo;
	}
	
	public ManoGiocatoreDX getMano() {
		return manoGiocatore;
	}
	/**
	 * Classe interna che rappresenta la visualizzazione grafica delle informazioni del bot
	 * questa classe Osserva il comportamento nel model del Giocatore
	 */
	public class PannelloInfoDX extends JPanel implements Observer{
		private JLabel avatar;
		private JTextField nome;
		/**
		 * Costruttore della classe che imposta i valori grafici di essa
		 */
		public PannelloInfoDX() {
			//setting dei componenti
			avatar = new JLabel();
			avatar.setPreferredSize(new Dimension(60,60));
			avatar.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			avatar.setBounds(5, 120, 60, 60);
			avatar.setHorizontalAlignment(JLabel.CENTER);
			
			nome = new JTextField();
			nome.setHorizontalAlignment(SwingConstants.CENTER);
			nome.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			nome.setFont(new Font("Serif",Font.BOLD,12));
			nome.setForeground(Color.RED);
			nome.setEditable(false);
			nome.setFocusable(false);
			nome.setBounds(5, 185, 60, 20);
		
			//Layout
			setLayout(null);
			
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
	public class ManoGiocatoreDX extends JPanel {
		private List<BottoneCarta> mano = new ArrayList<>(); //raccolta di tutte le carte presenti nella mano del giocatore
		private Carta ultimaGiocata;        
		
		/**
		 * Costruttore della classe che imposta i valori grafici della classe
		 */
		public ManoGiocatoreDX() {
			
			setLayout(new FlowLayout(FlowLayout.CENTER,0,-60));
			add(Box.createRigidArea(new Dimension(100,200)));
			
		}
		/**
		 * Metodo che restituisce la mano
		 */
		public List<BottoneCarta> getMano(){
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
			BottoneCarta carta = new BottoneCarta(BottoneCarta.HORIZONTAL,c);
			mano.add(carta);
			add(carta);
			revalidate();
		}
	}
}
