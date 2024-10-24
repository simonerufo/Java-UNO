package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.ControllerGame;
import model.Carta;
import model.GiocatorePrincipale;
import model.Observer;

/**
 * Classe che visualizza graficamente il giocatore tramite un pannello con avatar,nickname e livello
 * e un pannello che rappresenta graficamente la mano del bot
 */
public class PannelloGiocatorePrincipale extends JPanel{
	private PannelloInfoGP pannelloInfo;
	private ManoGiocatorePrincipale manoGP;
	
	/**
	 * Metodo costruttore che imposta il layout grafico della classe
	 */
	public PannelloGiocatorePrincipale(){
		pannelloInfo = new PannelloInfoGP();
		pannelloInfo.setPreferredSize(new Dimension(1200,70));
		
		manoGP = new ManoGiocatorePrincipale();
		manoGP.setBorder(BorderFactory.createEmptyBorder());
		manoGP.setPreferredSize(new Dimension(1200,100));
		
		manoGP.setBackground(new Color(255,224,89));
		pannelloInfo.setBackground(new Color(255,224,89));
		setLayout(new BorderLayout(0,10));
		
		add(pannelloInfo,BorderLayout.PAGE_START);
		add(manoGP,BorderLayout.CENTER);
	}
	
	public ManoGiocatorePrincipale getMano() {
		return manoGP;
	}
	
	public PannelloInfoGP getPannelloInfo() {
		return pannelloInfo;
	}
	
	/**
	 * Classe interna che visualizza graficamente la mano del Giocatore
	 */
	public class ManoGiocatorePrincipale extends JPanel {
		List<BottoneCarta> mano = new ArrayList<>(); //raccolta di tutte le carte presenti nella mano del giocatore
		Carta ultimaGiocata;
		/**
		 * Costruttore che imposta la rappresentazione grafica della mano
		 */
		public ManoGiocatorePrincipale() {
			setLayout(new FlowLayout(FlowLayout.CENTER,-15,0));
		}
		
		public List<BottoneCarta> getMano(){
			return mano;
		}
		/**
		 * dato un bottone carta come parametro, 
		 * lo rimuove dalla mano del giocatore
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
		
		public Carta getUltimaGiocata() {
			return ultimaGiocata;
		}
		/**
		 * data una carta come parametro, 
		 * il metodo aggiunge il suo corrispettivo bottone carta 
		 * alla rappresentazione grafica della mano del giocatore
		 */
		public void addMano(Carta c) {
			BottoneCarta carta = new BottoneCarta(BottoneCarta.GP,c);
			mano.add(carta);
			add(carta);
			repaint();
			revalidate();
		}

	}
	/**
	 * Classe interna che rappresenta la visualizzazione grafica delle informazioni del giocatore
	 * questa classe Osserva il comportamento nel model del Giocatore Principale
	 */
	public class PannelloInfoGP extends JPanel implements Observer {
		private JLabel avatar;
		private JTextField nome,
						   lvl,
						   punti;
		/**
		 * Costruttore della classe che imposta i valori grafici di essa
		 */
		public PannelloInfoGP() {
			avatar = new JLabel();
			avatar.setHorizontalAlignment(JLabel.CENTER);
			avatar.setPreferredSize(new Dimension(60,60));
			avatar.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			
			nome = new JTextField();
			nome.setText("NOME");
			nome.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			nome.setHorizontalAlignment(SwingConstants.CENTER);
			nome.setFont(new Font("Serif",Font.BOLD,20));
			nome.setForeground(Color.RED);
			nome.setEditable(false);
			nome.setSelectionColor(null);
			nome.setSelectedTextColor(Color.red);
			nome.setPreferredSize(new Dimension(100,60));
			
			lvl = new JTextField();
			lvl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			lvl.setFont(new Font("Serif",Font.BOLD,16));
			lvl.setForeground(Color.RED);
			lvl.setEditable(false);
			lvl.setSelectionColor(null);
			lvl.setSelectedTextColor(Color.red);
			lvl.setPreferredSize(new Dimension(80,60));
			
			setLayout(new FlowLayout());
			add(avatar);
			add(nome);
			add(lvl);
		
		}
		
		
		public JTextField getNome() {
			return nome;
		}

		/**
		 * Metodo implementato dall'interfaccia funzionale Observer
		 * che aggiorna lo stato del nome, l'avatar e il livello graficamente
		 * segnalando quando è il suo turno
		 */
		@Override
		public void update(Object o) {
			GiocatorePrincipale g = (GiocatorePrincipale)o;
			nome.setText(g.getNickname());
			avatar.setIcon(new ImageIcon(g.getAvatar()));
			lvl.setText("LV. " + Integer.toString(g.getLivello()));
			lvl.setHorizontalAlignment(JTextField.CENTER);

			if(ControllerGame.getGiocatoreAttivo().equals(g)) {
				avatar.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
				nome.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
				lvl.setBorder(BorderFactory.createLineBorder(Color.red, 5));
			}
			else {
				avatar.setBorder(BorderFactory.createLineBorder(Color.black, 2));
				nome.setBorder(BorderFactory.createLineBorder(Color.black, 2));
				lvl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			}
		}
	}

	
}
