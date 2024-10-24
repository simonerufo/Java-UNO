package controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.*;
import model.CartaColore.Colore;
import model.CartaJolly.Jolly;
import view.BottoneCarta;
import view.BottoneMazzoPesca;
import view.BottonePassaTurno;
import view.BottoneUno;
import view.GameFrame;
import view.PannelloGiocatoreDX;
import view.PannelloGiocatorePrincipale;
import view.PannelloGiocatoreSX;
import view.PannelloGiocatoreTOP;
import view.PannelloMazzi;
import view.PannelloStats;

/**
 * Classe controller del gioco, questa controlla e gestisce le comunicazioni tra model e view, 
 * gestendo gli input del Giocatore Principale tramite l'implementazione di ActionListener
 * e occupandosi dei Giocatori PC (gestiti da un'intelligenza artificiale).
 * La classe ha lo scopo di rendere il gioco funzionante, partendo dall'avvio di esso 
 * fino alla fine della partita.
 * la classe rispetta il pattern "Singleton"
 */
public class ControllerGame implements ActionListener {
	private static ControllerGame instance; //Istanza della classe
	// percorsi e file della classe
	private final File PERCORSO_DATI_GP = new File("src/data/GiocatorePrincipale.txt");
	private final File NOMI = new File("src/data/nomi.txt");
	private final File AVATAR = new File("src/data/Avatar.txt");
	private final String CARTA_PESCATA = "src/view/assets/sound/card_draw.wav";
	private final String SCONFITTA = "src/view/assets/sound/lose_sound.wav";
	private final String CARTA_GIOCATA = "src/view/assets/sound/card_played.wav";
	private final String BOTTONE = "src/view/assets/sound/button_pressed.wav";
	private final String MISCHIA = "src/view/assets/sound/deck_shuffle.wav";
	private final String TURNO = "src/view/assets/sound/player_turn.wav";
	private final String UNO_FAIL = "src/view/assets/sound/uno_draw.wav";
	private final String UNO = "src/view/assets/sound/uno_sound.wav";
	private final String VITTORIA = "src/view/assets/sound/win_sound.wav";
	private final String START = "src/view/assets/sound/game_start.wav";
	
	private MazzoPesca mp;
	private MazzoScarto ms;
	private static List<Giocatore> turno = new LinkedList<>(); //campo che si occupa della gestione del turno dei giocatori
	private static Simbolo simboloGiocabile;             
	private static CartaColore.Colore coloreGiocabile;
	private Timer timer = new Timer();                         //campo che regola l'ordine con cui vengono attivate le azioni
	
	private Giocatore p1 = GiocatorePrincipale.getInstance();
	//bots
	private Giocatore p2 = new GiocatoreComp(),
			 		  p3 = new GiocatoreComp(),
			 		  p4 = new GiocatoreComp();
	//view dei giocatori
	private PannelloGiocatorePrincipale pannelloP1;
	private PannelloGiocatoreSX pannelloP2;
	private PannelloGiocatoreTOP pannelloP3;
	private PannelloGiocatoreDX pannelloP4;
	private PannelloMazzi pannelloMazzi;
	//frame principale di gioco
	private GameFrame frame;
	
	/**
	 * implementazione del pattern singleton
	 * se la classe non è stata istanziata allora genera un'istanza
	 * altrimenti ritorna la classe già istanziata.
	 */
	public static ControllerGame getInstance() {
		if (instance != null)
			return instance;
		return new ControllerGame();
	}
	
	/**
	 * metodo costruttore della classe 
	 * questo metodo mette in comunicazione model e view, tramite l'inizializzazione dei campi della classe.
	 * Lo scopo di tale metodo è quello di caricare tutte le informazioni che si occupano dell'avvio della partita
	 * (prepara e visualizza i mazzi di scarto e pesca, 
	 * imposta i nomi e gli avatar dei bot, 
	 * rende visualizzabili i giocatori, prepara ed avvia il turno, e registra gli observers ai corrispettivi subjects)
	 */
	private ControllerGame() {
		//inizializzazione dei campi d'istanza
		mp = new MazzoPesca();
		ms = new MazzoScarto();
		
		frame = new GameFrame();
		//iniziallizzo lo stato della view con nome e avatar
		
		ArrayList<String> names = (ArrayList<String>)Utilities.getArrayDaFile(NOMI);
		
		p2.setNickname(names.remove(new Random().nextInt(names.size())));
		p3.setNickname(names.remove(new Random().nextInt(names.size())));
		p4.setNickname(names.remove(new Random().nextInt(names.size())));
		
		ArrayList<String> avatars = (ArrayList<String>)Utilities.getArrayDaFile(AVATAR);

		p2.setAvatar(avatars.remove(new Random().nextInt(avatars.size())));
		p3.setAvatar(avatars.remove(new Random().nextInt(avatars.size())));
		p4.setAvatar(avatars.remove(new Random().nextInt(avatars.size())));
		
		//inizializzo i componenti della view
		
		pannelloP1 = frame.getPannelloGP();
		pannelloP2 = frame.getPannelloGSX();
		pannelloP3 = frame.getPannelloGT();
		pannelloP4 = frame.getPannelloGDX();
		
		//setter del turno
		inizializzaTurno();
		
		//registro gli observers ai subjects
		mp.register(frame.getPannelloMazzi().getBottoneMP());
		p1.register(pannelloP1.getPannelloInfo());
		p2.register(pannelloP2.getPannelloInfo());
		p3.register(pannelloP3.getPannelloInfo());
		p4.register(pannelloP4.getPannelloInfo());
		
		//preparo il mazzo
		mp.mischia();
		pescaIniziale();
		//aggiorno la view del mazzo
		pannelloMazzi = frame.getPannelloMazzi();
		//aggiorno il model del mazzo di scarto con la carta iniziale
		cartaIniziale(mp.pesca());
		//registro l'observer del mazzo di scarto
		ms.register(pannelloMazzi.getLabelMS());
		Utilities.playSound(START);	
		
		//AVVIO IL GIOCO DOPO 2,5 SECONDI
		TimerTask avvia = new TimerTask() {
			@Override
			public void run() {
				giocaTurno();
			}
		};
		timer.schedule(avvia, 2500); 
	}
	
	/**
	 * Metodi getter e setter della classe
	 */
	public static Colore getColore() {
		return coloreGiocabile;
	}
	
	public static void setColore(Colore colore) {
		coloreGiocabile = colore;
	}
	
	public static Simbolo getSimbolo() {
		return simboloGiocabile;
	}
	
	public static void setSimbolo(Simbolo s) {
		simboloGiocabile = s;
	}
	/**
	 * Metodo che ritorna il giocatore in testa al turno
	 */
	public static Giocatore getGiocatoreAttivo() {
		return turno.get(0);
	}
	
	/**
	 * metodo getter che ritorna la rappresentazione grafica corretta in base alla carta data in input
	 */
	public static ImageIcon getCarta(Carta c) {
		//se la carta è null non visualizzare niente
		if (c == null)
			return null;
		//se la carta è una carta colore
		if (!(checkCarta(c))) {
			//ROSSO
			if (c.equals(new CartaColore(Simbolo.ZERO,Colore.ROSSO)))
				return new ImageIcon("src/view/assets/Red_0.png");
			else if (c.equals(new CartaColore(Simbolo.UNO,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_1.png");
			else if (c.equals(new CartaColore(Simbolo.DUE,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_2.png");
			else if (c.equals(new CartaColore(Simbolo.TRE,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_3.png");
			else if (c.equals(new CartaColore(Simbolo.QUATTRO,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_4.png");
			else if (c.equals(new CartaColore(Simbolo.CINQUE,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_5.png");
			else if (c.equals(new CartaColore(Simbolo.SEI,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_6.png");
			else if (c.equals(new CartaColore(Simbolo.SETTE,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_7.png");
			else if (c.equals(new CartaColore(Simbolo.OTTO,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_8.png");
			else if (c.equals(new CartaColore(Simbolo.NOVE,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_9.png");
			else if (c.equals(new CartaColore(Simbolo.INVERTI,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_Reverse.png");
			else if (c.equals(new CartaColore(Simbolo.PESCADUE,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_Draw.png");
			else if (c.equals(new CartaColore(Simbolo.SALTA,Colore.ROSSO)))
				return  new ImageIcon("src/view/assets/Red_Skip.png");
			//BLU
			else if (c.equals(new CartaColore(Simbolo.ZERO,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_0.png");
			else if (c.equals(new CartaColore(Simbolo.UNO,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_1.png");
			else if (c.equals(new CartaColore(Simbolo.DUE,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_2.png");
			else if (c.equals(new CartaColore(Simbolo.TRE,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_3.png");
			else if (c.equals(new CartaColore(Simbolo.QUATTRO,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_4.png");
			else if (c.equals(new CartaColore(Simbolo.CINQUE,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_5.png");
			else if (c.equals(new CartaColore(Simbolo.SEI,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_6.png");
			else if (c.equals(new CartaColore(Simbolo.SETTE,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_7.png");
			else if (c.equals(new CartaColore(Simbolo.OTTO,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_8.png");
			else if (c.equals(new CartaColore(Simbolo.NOVE,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_9.png");
			else if (c.equals(new CartaColore(Simbolo.INVERTI,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_Reverse.png");
			else if (c.equals(new CartaColore(Simbolo.PESCADUE,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_Draw.png");
			else if (c.equals(new CartaColore(Simbolo.SALTA,Colore.BLU)))
				return  new ImageIcon("src/view/assets/Blue_Skip.png");
			//VERDE
			else if (c.equals(new CartaColore(Simbolo.ZERO,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_0.png");
			else if (c.equals(new CartaColore(Simbolo.UNO,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_1.png");
			else if (c.equals(new CartaColore(Simbolo.DUE,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_2.png");
			else if (c.equals(new CartaColore(Simbolo.TRE,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_3.png");
			else if (c.equals(new CartaColore(Simbolo.QUATTRO,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_4.png");
			else if (c.equals(new CartaColore(Simbolo.CINQUE,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_5.png");
			else if (c.equals(new CartaColore(Simbolo.SEI,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_6.png");
			else if (c.equals(new CartaColore(Simbolo.SETTE,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_7.png");
			else if (c.equals(new CartaColore(Simbolo.OTTO,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_8.png");
			else if (c.equals(new CartaColore(Simbolo.NOVE,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_9.png");
			else if (c.equals(new CartaColore(Simbolo.INVERTI,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_Reverse.png");
			else if (c.equals(new CartaColore(Simbolo.PESCADUE,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_Draw.png");
			else if (c.equals(new CartaColore(Simbolo.SALTA,Colore.VERDE)))
				return  new ImageIcon("src/view/assets/Green_Skip.png");
			//GIALLO
			else if (c.equals(new CartaColore(Simbolo.ZERO,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_0.png");
			else if (c.equals(new CartaColore(Simbolo.UNO,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_1.png");
			else if (c.equals(new CartaColore(Simbolo.DUE,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_2.png");
			else if (c.equals(new CartaColore(Simbolo.TRE,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_3.png");
			else if (c.equals(new CartaColore(Simbolo.QUATTRO,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_4.png");
			else if (c.equals(new CartaColore(Simbolo.CINQUE,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_5.png");
			else if (c.equals(new CartaColore(Simbolo.SEI,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_6.png");
			else if (c.equals(new CartaColore(Simbolo.SETTE,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_7.png");
			else if (c.equals(new CartaColore(Simbolo.OTTO,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_8.png");
			else if (c.equals(new CartaColore(Simbolo.NOVE,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_9.png");
			else if (c.equals(new CartaColore(Simbolo.INVERTI,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_Reverse.png");
			else if (c.equals(new CartaColore(Simbolo.PESCADUE,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_Draw.png");
			else if (c.equals(new CartaColore(Simbolo.SALTA,Colore.GIALLO)))
				return  new ImageIcon("src/view/assets/Yellow_Skip.png");
			
		}
		//se la carta è un jolly
	 if (checkCarta(c)) {
		 //CAMBIACOLORE
			if (c.equals(new CartaJolly(Jolly.CAMBIACOLORE)))
				return  new ImageIcon("src/view/assets/Wild.png");
		//PESCAQUATTRO
			if (c.equals(new CartaJolly(Jolly.PESCAQUATTRO)))
				return  new ImageIcon("src/view/assets/Wild_Draw.png");
		}
		return null;
	}

	/**
	 * Metodo che si occupa di inizializzare lo stato delle mani dei giocatori
	 * facendogli pescare 7 carte ciascuno, aggiornando sia model che view
	 */
	private void pescaIniziale() {
		
		int v = 0;
		//TUTTI I GIOCATORI PESCANO 7 CARTE
		while (v < 7) {	
			//AGGIORNO LO STATO DELLA MANO DEI GIOCATORI NEL MODEL
			turno
			.forEach(x->
			x.addMano(mp.pesca()));
			v++;
		}
		//AGGIORNO LO STATO DELLA MANO DEI GIOCATORI NELLA VIEW
		p1.getMano()
		.forEach(carta ->
				 pannelloP1
				 .getMano()
				 .addMano(carta));
		
		p2.getMano()
		.forEach(carta ->
				 pannelloP2
				 .getMano()
				 .addMano(carta));
		
		p3.getMano()
		.forEach(carta ->
				 pannelloP3
				 .getMano()
				 .addMano(carta));
		
		p4.getMano()
		.forEach(carta ->
				 pannelloP4
				 .getMano()
				 .addMano(carta));
	}
	/**
	 * Metodo che si occupa di far pescare due carte al giocatore successivo al primo,
	 * aggiornando gli stati del model e della view, e dopo la pesca, di passare il turno 
	 * al giocatore successivo al giocatore che ha pescato
	 */
	private void pescaDue() {
		//aggiorno il turno
		LinkedList<Giocatore> ll = new LinkedList<>();
		ll.addAll(turno);
		ll.addLast(ll.removeFirst()); //IL GIOCATORE CHE HA GIOCATO IL PESCADUE VA IN CODA AL TURNO
		turno = ll.stream().collect(Collectors.toList());
		updateFirst();
		updateLast();
		ll.addLast(ll.removeFirst()); // IL GIOCATORE CHE PESCA DUE CARTE SALTA IL TURNO;
		turno = ll.stream().collect(Collectors.toList());
		
		//IL GIOCATORE PESCA DUE CARTE (aggiornando lo stato della mano del giocatore nel model e nella view)
		TimerTask pesca = new TimerTask() {
			@Override
			public void run() {
				Giocatore g = turno.get(turno.size()-1); // il giocatore che riceve le carte
				if (mp.getSize() >= 2) { //se il mazzo ha almeno due carte
					Carta c1 = mp.pesca();
					Carta c2 = mp.pesca();
					if (g.equals(p1)) {
						p1.addMano(c1);
						p1.addMano(c2);
						pannelloP1.getMano().addMano(c1);
						pannelloP1.getMano().addMano(c2);
					}
					if (g.equals(p2)) {
						p2.addMano(c1);
						p2.addMano(c2);
						pannelloP2.getMano().addMano(c1);
						pannelloP2.getMano().addMano(c2);
					}
					if (g.equals(p3)) {
						p3.addMano(c1);
						p3.addMano(c2);
						pannelloP3.getMano().addMano(c1);
						pannelloP3.getMano().addMano(c2);
					}
					if (g.equals(p4)) {
						p4.addMano(c1);
						p4.addMano(c2);
						pannelloP4.getMano().addMano(c1);
						pannelloP4.getMano().addMano(c2);
					}
					Utilities.playSound(CARTA_PESCATA);
				}
				else { //altrimenti pesco le carte rimanenti del mazzo
					if (g.equals(p1)) {
						Carta c = null;
						for(int i = 0;i<mp.getSize();i++) {
							c = mp.pesca();
							p1.addMano(c);
							pannelloP1.getMano().addMano(c);
						}	
					}
					if (g.equals(p2)) {
						Carta c = null;
						for(int i = 0;i<mp.getSize();i++) {
							c = mp.pesca();
							p2.addMano(c);
							pannelloP2.getMano().addMano(c);
						}	
					}
					if (g.equals(p3)) {
						Carta c = null;
						for(int i = 0;i<mp.getSize();i++) {
							c = mp.pesca();
							p3.addMano(c);
							pannelloP3.getMano().addMano(c);
						}	
					}
					if (g.equals(p4)) {
						Carta c = null;
						for(int i = 0;i<mp.getSize();i++) {
							c = mp.pesca();
							p4.addMano(c);
							pannelloP4.getMano().addMano(c);
						}	
					}
					Utilities.playSound(CARTA_PESCATA);
				}
			}
		};
		timer.schedule(pesca, 1000); // la pesca avviene un secondo dopo aver passato il turno
		//aggiorno la visualizzazione del turno
		updateFirst();
		updateLast();
	}
	/**
	 * Metodo che si occupa di far decidere il colore giocabile al giocatore attivo, 
	 * di far pescare quattro carte al giocatore successivo e di passare il turno al giocatore successivo
	 * al giocatore che ha pescato.
	 * Il tutto aggiornando lo stato del model e della view, e distinguendo il giocatore che gioca la carta 
	 * (se è il giocatore principale o se è un bot).
	 */
	private void pescaQuattro() {
		//inizialmente faccio scegliere il colore al giocatore corrente
		//GIOCATORE PRINCIPALE
		Giocatore current = getGiocatoreAttivo();
		if (checkGiocatore(current)) {
			TimerTask abilita = new TimerTask() {
				@Override
				public void run() {
					pannelloMazzi.getPannelloCambiaColore().setEnabled(true);
					pannelloMazzi.getPannelloCambiaColore().setVisible(true);
					setPannelloCambiaColoreListener();
				}
			};
			timer.schedule(abilita, 500);//PERMETTO AL GIOCATORE DI VISUALIZZARE I BOTTONI PER CAMBIARE COLORE
		}
		//COMPUTER
		else {
			//AGGIORNO IL COLORE 
			//scegliendo un colore random tra i quattro possibili colori
			Colore col = Arrays.asList(Colore.values())
					 .get(new Random()
							 .nextInt(Colore.values().length));
			coloreGiocabile = col;
			simboloGiocabile = Simbolo.JOLLY;
			//aggiorno il turno
			LinkedList<Giocatore> ll = new LinkedList<>();
			ll.addAll(turno);
			ll.addLast(ll.removeFirst()); //IL GIOCATORE CHE HA GIOCATO IL PESCAQUATTRO VA IN CODA AL TURNO
			turno = ll.stream().collect(Collectors.toList());
			updateFirst();
			updateLast();
			ll.addLast(ll.removeFirst()); // IL GIOCATORE CHE PESCA QUATTRO CARTE SALTA IL TURNO;
			turno = ll.stream().collect(Collectors.toList());
			//AGGIORNO LO STATO DELLA VIEW
			//cambio lo stato della visualizzazione del colore
			TimerTask colore = new TimerTask() {
				@Override
				public void run() {
					pannelloMazzi.getLabelColore().cambiaColore(col);
				}
			};
			timer.schedule(colore, 500);
			//aggiorno lo stato del model e della view, aggiungendo le quattro nuove carte nella mano del giocatore
			TimerTask pesca = new TimerTask() {
				@Override
				public void run() {
					Giocatore g = turno.get(turno.size()-1); // il giocatore che deve pescare
					
					if (mp.getSize() >= 4) { // se il mazzo di pesca ha almeno 4 carte
						
						Carta c1 = mp.pesca();
						Carta c2 = mp.pesca();
						Carta c3 = mp.pesca();
						Carta c4 = mp.pesca();
						
						if (g.equals(p1)) {
							p1.addMano(c1); p1.addMano(c2); p1.addMano(c3); p1.addMano(c4);
							pannelloP1.getMano().addMano(c1); pannelloP1.getMano().addMano(c2);
							pannelloP1.getMano().addMano(c3); pannelloP1.getMano().addMano(c4);
						}
						if (g.equals(p2)) {
							p2.addMano(c1); p2.addMano(c2); p2.addMano(c3); p2.addMano(c4);
							pannelloP2.getMano().addMano(c1); pannelloP2.getMano().addMano(c2);
							pannelloP2.getMano().addMano(c3); pannelloP2.getMano().addMano(c4);
						}
						if (g.equals(p3)) {
							p3.addMano(c1); p3.addMano(c2); p3.addMano(c3); p3.addMano(c4);
							pannelloP3.getMano().addMano(c1); pannelloP3.getMano().addMano(c2);
							pannelloP3.getMano().addMano(c3); pannelloP3.getMano().addMano(c4);
						}
						if (g.equals(p4)) {
							p4.addMano(c1); p4.addMano(c2); p4.addMano(c3); p4.addMano(c4);
							pannelloP4.getMano().addMano(c1); pannelloP4.getMano().addMano(c2);
							pannelloP4.getMano().addMano(c3); pannelloP4.getMano().addMano(c4);
						}
						Utilities.playSound(CARTA_PESCATA);
					}
					else { // altrimenti pesco le carte rimanenti nel mazzo
						if (g.equals(p1)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p1.addMano(c);
								pannelloP1.getMano().addMano(c);
							}	
						}
						if (g.equals(p2)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p2.addMano(c);
								pannelloP2.getMano().addMano(c);
							}	
						}
						if (g.equals(p3)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p3.addMano(c);
								pannelloP3.getMano().addMano(c);
							}	
						}
						if (g.equals(p4)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p4.addMano(c);
								pannelloP4.getMano().addMano(c);
							}	
						}
						Utilities.playSound(CARTA_PESCATA);
					}
						
				}
			};
			timer.schedule(pesca, 700);
		}
	}
	
	/**
	 * Metodo che si occupa di aggiornare il turno, il colore ed il simbolo giocabile, 
	 * in base all'ultima carta giocata dai giocatori o in base alla carta in testa al mazzo di scarto.
	 */
	private void aggiornaTurno() {
		Carta effetto = ms.getTesta(); //carta in cima al mazzo di scarto
		Carta ultimaGiocata = getGiocatoreAttivo().getCartaGiocata(); //ultima carta giocata dal giocatore in testa al turno
		// giocatore attivo ancora deve passare il turno!
			if (checkCarta(effetto)){ // CARTA JOLLY
				// se il giocatore ha passato il turno -> null, altrimenti cast a cartaJolly
				CartaJolly jolly = (CartaJolly)effetto;
				CartaJolly ug = checkCarta(ultimaGiocata) == true ? (CartaJolly)ultimaGiocata : null; 
				// se ho passato il turno scorso, ignoro l'effetto della carta
				if (ug == null) { 
					turnoDefault();
				} 
				//se l'ultima carta giocate era un cambiacolore attiva l'effetto di essa
				if (ug != null && jolly.getJolly().equals(Jolly.CAMBIACOLORE) && ug.getJolly().equals(Jolly.CAMBIACOLORE)) {
					cambiaColore();
				} 
				//se l'ultima carta giocate era un pescaquattro attiva l'effetto di essa
				if (ug != null && jolly.getJolly().equals(Jolly.PESCAQUATTRO) && ug.getJolly().equals(Jolly.PESCAQUATTRO)) {
					pescaQuattro();
				}
			}
			else { // CARTA COLORE
					// se il giocatore ha passato il turno -> null, altrimenti cast a cartaColore
					CartaColore cartaCol = (CartaColore)effetto;
					CartaColore ug = checkCarta(ultimaGiocata) != true ?  (CartaColore)ultimaGiocata : null;
					//aggiorno lo stato di model e view per il colore giocabile e il simbolo giocabile
					coloreGiocabile = cartaCol.getColore();
					pannelloMazzi.getLabelColore().cambiaColore(coloreGiocabile);
					simboloGiocabile = cartaCol.getSimbolo();
					//TURNO PASSATO
					if (ug == null)
						turnoDefault();
					else {
							switch(cartaCol.getSimbolo()) {
								//PESCADUE
								case PESCADUE -> {
									//se l'ultima carta giocata non è null ed è un pescadue
									if (ug != null && ug.getSimbolo().equals(Simbolo.PESCADUE)) {
										pescaDue();
									}
									//altrimenti passa il turno al giocatore successivo
									else
										turnoDefault();
								}
								//INVERTI
								case INVERTI ->{
									//se l'ultima carta giocata non è null ed è un inverti
									if (ug != null && ug.getSimbolo().equals(Simbolo.INVERTI))
										inverti();
									else
										turnoDefault();
								} 
								//SALTA
								case SALTA -> {
									//se l'ultima carta giocata non è null ed è un salta turno
									if (ug != null && ug.getSimbolo().equals(Simbolo.SALTA)) {
										turnoDefault(); //aggiorna il turno 
										turnoDefault(); // e fai saltare il turno al giocatore attivo
									}
									else
										turnoDefault();
								}
								//CARTA COLORE
								//se la carta giocata è una carta colore senza simboli speciali
								default -> turnoDefault(); //aggiorna il turno normalmente
							}
					}
				}
			giocaTurno(); //RENDO GIOCABILE IL TURNO AL GIOCATORE SUCCESSIVO
	}
	
	/**
	 * Metodo che riordina i mazzi di pesca e di scarto, 
	 * lasciando nel mazzo di scarto solo la testa, 
	 * le restanti carte del mazzo verranno mischiate nel mazzo di pesca
	 */
	private void riordinaMazzi() {
		Utilities.playSound(MISCHIA);
		//AGGIORNO IL MODEL
		//estraggo la testa dal mazzo di scarto
		Carta t = ms.getTesta();
		ms.rimuovi(t);
		//inserisco nel mazzo di pesca tutte le carte del mazzo di scarto
		ms.getMazzo()
		.forEach(x-> mp.inserisci(x));
		//rimuovo dal mazzo di scarto tutte le carte del mazzo di pesca
		mp.getMazzo()
		.forEach(y-> ms.rimuovi(y));
		//mescolo il mazzo di pesca
		mp.mischia();
		//aggiungo la carta in testa al mazzo di scarto
		ms.aggiungi(t);
	}
	/**
	 * Metodo che aggiorna la visualizzazione del turno,
	 * e rende giocabile il turno al giocatore in testa ad esso.
	 */
	private void giocaTurno() {
		// se il mazzo di pesca non ha carte, rimescola il mazzo.
		if (mp.getSize() == 0)
			riordinaMazzi();

		Giocatore current = getGiocatoreAttivo();
		//aggiorno la visualizzazione del turno al giocatore in testa
		updateFirst();
		updateLast();
			//se il giocatore è il giocatore Principale
			if (checkGiocatore(current)) {
				giocabiliGP();		
			}
			
			//se il giocatore è un bot
			else {
				giocabiliPC();
			}
	}
	
	

	/**
	 * Metodo che aggiorna il model e le partite giocate nel file GiocatorePrincipale.txt
	 */
	private void incrementaPartiteGiocate() {
		GiocatorePrincipale g = (GiocatorePrincipale)p1;
		//incremento le partite giocate nel file
		Utilities.aggiornaFile(PERCORSO_DATI_GP,(g.getPartiteGiocate()+1)+"",1);
		//aggiorno lo stato delle partite giocate nel model
		g.setPartiteGiocate(g.getPartiteGiocate()+1); //aggiorno lo stato del model
	}
	
	/**
	 * Metodo che aggiorna il model e le partite vinte nel file GiocatorePrincipale.txt
	 */
	private void incrementaVinte() {
		GiocatorePrincipale g = (GiocatorePrincipale)p1;
		//incremento le partite vinte nel file
		Utilities.aggiornaFile(PERCORSO_DATI_GP,(g.getVinte()+1)+"",2);
		//aggiorno lo stato delle partite vinte nel model
		g.setVinte(g.getVinte());
	}
	/**
	 * Metodo che aggiorna il model e le partite perse nel file GiocatorePrincipale.txt
	 */
	private void incrementaPerse() {
		GiocatorePrincipale g = (GiocatorePrincipale)p1;
		
		//incremento le partite perse nel file
		Utilities.aggiornaFile(PERCORSO_DATI_GP,(g.getPerse()+1)+"",3);
		//aggiorno lo stato delle partite perse nel model
		g.setPerse(g.getPerse());
	}
	
	/**
	 *Metodo che aggiorna il model e il livello nel file GiocatorePrincipale.txt 
	 */
	private void incrementaLivello() {
		GiocatorePrincipale g = (GiocatorePrincipale)p1;
		//se il livelo del giocatore è multiplo di 3 ed inferiore di 100
		if ( g.getVinte() % 3 == 0 && g.getLivello() < 100) {
			//incremento le partite perse nel file
			Utilities.aggiornaFile(PERCORSO_DATI_GP,(g.getLivello()+1)+"",4);
			//aggiorno il livello nel model
			g.setLivello(g.getLivello());
		}
	}
	/**
	 * Metodo che si occupa di inizializzare il campo d'istanza turno e di visualizzarlo
	 */
	private void inizializzaTurno() {
		//aggiungo i giocatori
		turno.add(p1); turno.add(p2); turno.add(p3); turno.add(p4);
		//genero una mappa le cui chiavi sono i giocatori, 
		//e i valori sono un intero estratto casualmente
		Map<Giocatore,Integer> t = new HashMap<Giocatore,Integer>();
		turno.forEach(x->t.put(x, mp.estrai().getEstrazione()));
		//estraggo il giocatore che ha estratto il numero maggiore
		Optional<Entry<Giocatore, Integer>> max = 
		t.entrySet()
		.stream()
		.max(Comparator.comparing(Map.Entry::getValue));
		
		//ordino il turno in base al giocatore che ha estratto il massimo
		if  (p2 == max.get().getKey()){
			turno.remove(0);
			turno.add(p1);
		}
		
		if (p3 == max.get().getKey()) {
			turno.remove(0);
			turno.remove(0);
			turno.add(p1);
			turno.add(p2);
		}
		
		if (p4 == max.get().getKey()) {
			turno.remove(0);
			turno.remove(0);
			turno.remove(0);
			turno.add(p1);
			turno.add(p2);
			turno.add(p3);
		}
		//aggiorno la visualizzazione del turno
		updateFirst();
		updateLast();

	}
	/**
	 * Metodo ricorsivo che si occupa di visualizzare la prima carta in testa al mazzo di scarto, 
	 * e di attivare gli effetti correlati alla carta presa in input
	 */
	private void cartaIniziale(Carta prima){
		//se la carta è una carta colore
		if (!(checkCarta(prima))) {
			//inizializzo il colore il simbolo giocabile ed il turno in base alla carta in input
			CartaColore p = (CartaColore)prima;
			switch(p.getSimbolo()) {
			    	case INVERTI -> {
			    		//inizia a giocare il giocatore alla destra del giocatore in testa al turno, e il turno segue il senso anti-orario 
			    		inverti();
			    		coloreGiocabile = p.getColore();
			    		pannelloMazzi.getLabelColore().cambiaColore(coloreGiocabile); //aggiorno la view del colore
			    		simboloGiocabile = p.getSimbolo();
			    		ms.aggiungi(prima);                                           //aggiungo la carta in testa al mazzo scarto
			    	}
			    	case SALTA -> {
			    		//inizia a giocare il giocatore alla destra del giocatore in testa al turno
			    		turnoDefault();
			    		coloreGiocabile = p.getColore();
			    		pannelloMazzi.getLabelColore().cambiaColore(coloreGiocabile);
			    		simboloGiocabile = p.getSimbolo();
			    		ms.aggiungi(prima);
			    	}
			    	
			    	default -> {
			    		coloreGiocabile = p.getColore();
			    		pannelloMazzi.getLabelColore().cambiaColore(coloreGiocabile);
			    		simboloGiocabile = p.getSimbolo();
			    		ms.aggiungi(prima);	
			    	}
			}
		
		}
		else { // se la carta è un jolly
			CartaJolly j = (CartaJolly)prima;
			simboloGiocabile = Simbolo.JOLLY; 
			//PESCAQUATTRO
			if (j.getJolly().equals(Jolly.PESCAQUATTRO)){
				mp.inserisci(prima); 
				mp.mischia(); // inserisco nel mazzo di pesca la carta presa in input e mescolo il mazzo
				cartaIniziale(mp.pesca()); // richiamo la funzione con la prossima carta in cima al mazzo di pesca
			}
			//CAMBIACOLORE
			else {
					Giocatore current = getGiocatoreAttivo();
					ms.aggiungi(prima);
				//GIOCATORE PRINCIPALE
				if (checkGiocatore(current)) {
					//aggiorno la view rendendo visibile il pannello cambia colore
					TimerTask abilita = new TimerTask() {
						@Override
						public void run() {
							pannelloMazzi.getPannelloCambiaColore().setEnabled(true);
							pannelloMazzi.getPannelloCambiaColore().setVisible(true);
							setPannelloCambiaColoreListener();
						}
					};
					timer.schedule(abilita, 1000);
				}
				// COMPUTER
				else {
					//inizializzo un colore casuale
					Colore col = Arrays.asList(Colore.values())
							 .get(new Random()
									 .nextInt(Colore.values().length));
					coloreGiocabile = col;
					//aggiorno lo stato della view
					TimerTask colore = new TimerTask() {
						@Override
						public void run() {
							pannelloMazzi.getLabelColore().cambiaColore(col);
						}
					};
					timer.schedule(colore, 1000);
				}
			}
		}
	}
	/**
	 * Metodo che si occupa di invertire il senso del turno 
	 * rendendo visibile il giocatore che giocherà la prossima mano
	 * dopo che la carta colore con il simbolo inverti è stata giocata
	 */
	private void inverti(){
		//estraggo il giocatore in coda al turno
		LinkedList<Giocatore> ll = new LinkedList<Giocatore>();
		ll.addAll(turno);
		Giocatore p = ll.removeLast();
		//inizializzo una mappa le cui chiavi sono i giocatori e i suoi valori sono le posizioni nel turno dei giocatori
		Map<Giocatore,Integer> m = new HashMap<Giocatore,Integer>();
		ll.forEach(x->m.put(x,ll.indexOf(x)));
		ll.clear();
		//inverto l'ordine delle posizioni dei giocatori nel turno
		List<Giocatore> l = m.entrySet().stream() //estraggo le coppie chiave valore
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) //inverto l'ordine
				.map(Map.Entry::getKey) //estraggo il giocatore scartando la sua posizione
				.collect(Collectors.toList()); // lo aggiungo alla lista
		
		//aggiorno il turno aggiungendo in testa il giocatore inizialmente estratto
		// e di seguito aggiungendo la lista appena invertita
		ll.addAll(l);
		ll.addFirst(p);
		turno = ll.stream().collect(Collectors.toList());
		
		//aggiorno la visualizzazione del turno
		updateFirst();
		updateLast();
	}
	
	/**
	 * Metodo che si occupa di cambiare il colore giocabile, aggiornando model e view,
	 * in base al giocatore che ha giocato la carta jolly cambiacolore
	 */
	private void cambiaColore() {
		//GIOCATORE PRINCIPALE
		Giocatore current = getGiocatoreAttivo();
		if (checkGiocatore(current)) {
			//abilito il pannello che permette al giocatore principale di cambiare colore
			TimerTask abilita = new TimerTask() {
				@Override
				public void run() {
					pannelloMazzi.getPannelloCambiaColore().setEnabled(true);
					pannelloMazzi.getPannelloCambiaColore().setVisible(true);
					setPannelloCambiaColoreListener();
				}
			};
			timer.schedule(abilita, 500); // attivo il timer
		}
		//COMPUTER
		else {
			//AGGIORNO IL COLORE
			//estraendo un colore random tra quelli disponibili
			Colore col = Arrays.asList(Colore.values())
					 .get(new Random()
							 .nextInt(Colore.values().length));
			coloreGiocabile = col;
			simboloGiocabile = Simbolo.JOLLY;
			//aggiorno il turno
			LinkedList<Giocatore> ll = new LinkedList<>();
			ll.addAll(turno);
			ll.addLast(ll.removeFirst()); //IL GIOCATORE CHE HA GIOCATO IL CAMBIACOLORE VA IN CODA AL TURNO
			turno = ll.stream().collect(Collectors.toList());
			//visualizzo il turno aggiornato
			updateFirst();
			updateLast();
			// AGGIORNA LO STATO DELLA VIEW DEL COLORE
			TimerTask colore = new TimerTask() {
				@Override
				public void run() {
					pannelloMazzi.getLabelColore().cambiaColore(col);
				}
			};
			timer.schedule(colore, 500);//attivo il timer
		}
		
	}
	
	/**
	 * Metodo che si occupa di far passare il turno al giocatore principale
	 * aggiornando lo stato della view del pannello del giocatore facendo visualizzare un bottone passa turno
	 */
	private void passaTurnoGP() {
		//l'ultima carta pescata diventa giocabile
		pannelloP1.getMano().getMano()
		.get(pannelloP1.getMano().getMano().size()-1)
		.addActionListener(this);
		//diventa possibile passare il turno visualizzando il bottone passaturno
		pannelloMazzi.getTurno().addActionListener(this);
		pannelloMazzi.getTurno().setVisible(true);
		pannelloMazzi.getTurno().setEnabled(true);
		//rimuovo il listener dal mazzo di pesca
		pannelloMazzi.getBottoneMP().removeActionListener(this);
	}
	
	/**
	 * Metodo che si occupa dell'aggiornamento dello stato del turno e della sua visualizzazione
	 */
	private void turnoDefault() {
		//aggiungo in coda alla lista a puntatori il giocatore in testa
		LinkedList<Giocatore> ll = new LinkedList<Giocatore>();
		ll.addAll(turno);
		ll.addLast(ll.removeFirst());
		//e aggiorno il campo d'istanza turno
		turno = ll.stream().collect(Collectors.toList());
		//aggiorno la visualizzazione del turno
		updateFirst();
		updateLast();
	}
	
	/**
	 * Metodo che torna vero se la carta passata in input è un Jolly 
	 */
	public static boolean checkCarta(Carta c) {
		if (c == null)
			return false;
		if (c.getClass().equals(CartaJolly.class))
			return true;
		return false;
	}
	
	/**
	 *  Metodo che torna vero se il giocatore in input è il giocatore principale
	 */
	public static boolean checkGiocatore(Giocatore g) {
		if (g.getClass().equals(GiocatorePrincipale.class))
			return true;
		return false;
	}
	
	/**
	 * Metodo che si occupa di rendere cliccabili le carte possibili da giocare
	 */
	private void giocabiliGP() {
		Utilities.playSound(TURNO);
		//lista di tutte le carte giocabili del Giocatore principale
		List<Carta> giocabili = p1.carteGiocabili();
		//rendo cliccabili le carte che si possono giocare
		if (!(giocabili.isEmpty())) {
					
					pannelloP1.getMano()
							  .getMano()
							  .stream()
							  .filter(c->giocabili.contains
										(c.getCarta())).collect(Collectors.toList())
											.forEach(carta-> carta.addActionListener(this));
		}
		//rendo cliccabile il mazzo di pesca, anche se non ho carte giocabili
		pannelloMazzi.getBottoneMP().addActionListener(this);
	}
	
	/**
	 * Metodo che si occupa di gestire le carte possibili da giocare dei bot
	 * aggiornando il model e la view,
	 * e aggiornando successivamente il turno del giocatore in base alla carta giocata
	 */
	private void giocabiliPC() {
		Utilities.playSound(TURNO);
		TimerTask gioca = new TimerTask() {
			@Override
			public void run() {
				//P2
				if (getGiocatoreAttivo().equals(p2)) {
					List<Carta> giocabili = p2.carteGiocabili();
					//se il bot ha carte giocabili
					if (!giocabili.isEmpty()) {
						
					   BottoneCarta scarto= pannelloP2
											.getMano()
											.getMano()
											.stream()
											.filter(c-> giocabili.contains
													(c.getCarta()))
											.collect(Collectors.toList()) // array di bottonicarta giocabili
											.get(new Random().nextInt(giocabili.size())); //gioca una carta casuale tra le possibili giocate
					    Utilities.playSound(CARTA_GIOCATA);
					    //aggiorno lo stato della mano e del mazzo di scarto
						pannelloP2.getMano()
								  .rimuovi(scarto);
						p2.removeMano(scarto.getCarta());
						ms.aggiungi(scarto.getCarta());

					}
					else { //se il bot NON ha carte giocabili
						Utilities.playSound(CARTA_PESCATA);
						//pesca una carta e aggiorna la mano del bot
						Carta pesca = mp.pesca();
						p2.addMano(pesca);
						pannelloP2.getMano().addMano(pesca);
						// se la carta che ho pescato è giocabile
						TimerTask passa = new TimerTask() {
							@Override
							public void run() {
								// se la carta pescata è un Jolly
								if (checkCarta(pesca)) {
									Utilities.playSound(CARTA_GIOCATA);
									// se la carta è un cambiacolore allora può essere scartata
									if (pesca.equals(new CartaJolly(Jolly.CAMBIACOLORE))) {
										p2.removeMano(pesca);
										//bottonecarta cambiacolore
										pannelloP2.getMano()
										.rimuovi(pannelloP2.getMano().getMano()
										.get(pannelloP2.getMano().getMano().size()-1));
										ms.aggiungi(pesca);
									}
									// se la carta è un pescaquattro controllo se è giocabile
									if(pesca.equals(new CartaJolly(Jolly.PESCAQUATTRO))) {
										if (p2.carteGiocabili().contains(new CartaJolly(Jolly.PESCAQUATTRO))) {
											p2.removeMano(pesca);
											//bottonecarta pescaquattro
											pannelloP2.getMano()
											.rimuovi(pannelloP2.getMano().getMano()
											.get(pannelloP2.getMano().getMano().size()-1));
											ms.aggiungi(pesca);
										}
									}
								}
								else {
									Utilities.playSound(CARTA_GIOCATA);
									CartaColore c = (CartaColore)pesca;
									//controllo se essa è giocabile
									if(c.getColore().equals(getColore())
									   || c.getSimbolo().equals(getSimbolo())) {
										p2.removeMano(pesca);
										//bottonecarta carta colore
										pannelloP2.getMano()
										.rimuovi(pannelloP2.getMano().getMano()
										.get(pannelloP2.getMano().getMano().size()-1));
										ms.aggiungi(pesca);
									}
								}
							}
						};
						timer.schedule(passa, 500);
					}
				}
				//P3
				if (getGiocatoreAttivo().equals(p3)) {
					List<Carta> giocabili = p3.carteGiocabili();
					if (!giocabili.isEmpty()) {
						
						 BottoneCarta scarto = pannelloP3
								 			   .getMano()
								 			   .getMano()
								 			   .stream()
								 			   .filter(c-> giocabili.contains
								 					   (c.getCarta()))
								 			  .collect(Collectors.toList()) // array di bottonicarta giocabili
								 			   .get(new Random().nextInt(giocabili.size()));
						Utilities.playSound(CARTA_GIOCATA);
						pannelloP3.getMano()
								  .rimuovi(scarto);
						p3.removeMano(scarto.getCarta());
						ms.aggiungi(scarto.getCarta());
					}
					else {
						Utilities.playSound(CARTA_PESCATA);
						Carta pesca = mp.pesca();
						p3.addMano(pesca);
						pannelloP3.getMano().addMano(pesca);
						//se la carta pescata è giocabile
						TimerTask passa = new TimerTask() {
							@Override
							public void run() {
								// se la carta pescata è un Jolly
								if (checkCarta(pesca)) {
									Utilities.playSound(CARTA_GIOCATA);
									// se la carta è un cambiacolore allora può essere scartata
									if (pesca.equals(new CartaJolly(Jolly.CAMBIACOLORE))) {
										p3.removeMano(pesca);
										//bottonecarta cambiacolore
										pannelloP3.getMano()
										.rimuovi(pannelloP3.getMano().getMano()
										.get(pannelloP3.getMano().getMano().size()-1));
										ms.aggiungi(pesca);
									}
									// se la carta è un pescaquattro controllo se è giocabile
									if(pesca.equals(new CartaJolly(Jolly.PESCAQUATTRO))) {
										if (p3.carteGiocabili().contains(new CartaJolly(Jolly.PESCAQUATTRO))) {
											p3.removeMano(pesca);
											//bottonecarta pescaquattro
											pannelloP3.getMano()
											.rimuovi(pannelloP3.getMano().getMano()
											.get(pannelloP3.getMano().getMano().size()-1));
											ms.aggiungi(pesca);
										}
									}
								}
								else {
									Utilities.playSound(CARTA_GIOCATA);
									CartaColore c = (CartaColore)pesca;
									//controllo se essa è giocabile
									if(c.getColore().equals(getColore())
									   || c.getSimbolo().equals(getSimbolo())) {
										p3.removeMano(pesca);
										//bottonecarta carta colore
										pannelloP3.getMano()
										.rimuovi(pannelloP3.getMano().getMano()
										.get(pannelloP3.getMano().getMano().size()-1));
										ms.aggiungi(pesca);
									}
								}
							
							}
						};
						timer.schedule(passa, 500);
					}
				}
				//P4
				if (getGiocatoreAttivo().equals(p4)) {
					List<Carta> giocabili = p4.carteGiocabili();
					if (!giocabili.isEmpty()) {
						
						 BottoneCarta scarto = pannelloP4
								 			   .getMano()
								 			   .getMano()
								 			   .stream()
								 			   .filter(c-> giocabili.contains
								 					   (c.getCarta()))
								 			   .collect(Collectors.toList()) // array di bottonicarta giocabili
								 			   .get(new Random().nextInt(giocabili.size()));
						Utilities.playSound(CARTA_GIOCATA);
						pannelloP4.getMano()
								  .rimuovi(scarto);
						p4.removeMano(scarto.getCarta());
						ms.aggiungi(scarto.getCarta());
					}
					else {
						Utilities.playSound(CARTA_PESCATA);
						Carta pesca = mp.pesca();
						p4.addMano(pesca);
						pannelloP4.getMano().addMano(pesca);
						//se la carta pescata è giocabile
						TimerTask passa = new TimerTask() {
							@Override
							public void run() {
								// se la carta pescata è un Jolly
								if (checkCarta(pesca)) {
									Utilities.playSound(CARTA_GIOCATA);
									// se la carta è un cambiacolore allora può essere scartata
									if (pesca.equals(new CartaJolly(Jolly.CAMBIACOLORE))) {
										p4.removeMano(pesca);
										//bottonecarta cambiacolore
										pannelloP4.getMano()
										.rimuovi(pannelloP4.getMano().getMano()
										.get(pannelloP4.getMano().getMano().size()-1));
										ms.aggiungi(pesca);
									}
									// se la carta è un pescaquattro controllo se è giocabile
									if(pesca.equals(new CartaJolly(Jolly.PESCAQUATTRO))) {
										if (p4.carteGiocabili().contains(new CartaJolly(Jolly.PESCAQUATTRO))) {
											p4.removeMano(pesca);
											//bottonecarta pescaquattro
											pannelloP4.getMano()
											.rimuovi(pannelloP4.getMano().getMano()
											.get(pannelloP4.getMano().getMano().size()-1));
											ms.aggiungi(pesca);
										}
									}
								}
								else {
									Utilities.playSound(CARTA_GIOCATA);
									CartaColore c = (CartaColore)pesca;
									//controllo se essa è giocabile
									if(c.getColore().equals(getColore())
									   || c.getSimbolo().equals(getSimbolo())) {
										p4.removeMano(pesca);
										//bottonecarta carta colore
										pannelloP4.getMano()
										.rimuovi(pannelloP4.getMano().getMano()
										.get(pannelloP4.getMano().getMano().size()-1));
										ms.aggiungi(pesca);
									}
								}
							}
						};
						timer.schedule(passa, 500);
					}
				}
			}
		};
		timer.schedule(gioca, 2000); //il bot fa la sua mossa dopo due secondi
		
		TimerTask aggiorna = new TimerTask() {
			@Override
			public void run() {
				//PC TERMINA LE CARTE NELLA MANO
				if (getGiocatoreAttivo().getMano().size() == 0) {
					fineMatch();
				}
				// PC RIMANE CON UNA SOLA CARTA NELLA MANO
				else if (getGiocatoreAttivo().getMano().size() == 1) {
					unoPC();
				}
				//PC AGGIORNA IL TURNO NORMALMENTE
				else {
					aggiornaTurno();
				}
				
			}
		};
		timer.schedule(aggiorna, 3000); // aggiorno il turno dopo tre secondi
	}
	
	/**
	 * Metodo che aggiorna la visualizzazione del turno del giocatore in testa
	 * richiamando l'update del pannello informazioni del giocatore attivo
	 */
	private void updateFirst() {
		if (getGiocatoreAttivo().equals(p1)) {
			pannelloP1.getPannelloInfo().update(p1);
		}
		if (getGiocatoreAttivo().equals(p2)) {
			pannelloP2.getPannelloInfo().update(p2);
		}
		if (getGiocatoreAttivo().equals(p3)) {
			pannelloP3.getPannelloInfo().update(p3);
		}
		
		if (getGiocatoreAttivo().equals(p4)) {
			pannelloP4.getPannelloInfo().update(p4);
		}
	}
	
	/**
	 * Metodo che aggiorna la visualizzazione del turno del giocatore in coda
	 * richiamando l'update del pannello informazioni del'ultimo giocatore
	 */
	private void updateLast() {
		Giocatore last = turno.get(3);
		if (last.equals(p1)) {
			pannelloP1.getPannelloInfo().update(p1);
		}
		if (last.equals(p2)) {
			pannelloP2.getPannelloInfo().update(p2);
		}
		
		if (last.equals(p3)) {
			pannelloP3.getPannelloInfo().update(p3);
		}
		
		if (last.equals(p4)) {
			pannelloP4.getPannelloInfo().update(p4);
		}
	}
	
	/**
	 * Metodo che si occupa di distinguere se il jolly è stato giocato dal giocatore principale, 
	 * se è un cambiacolore oppure un pesca quattro, aggiornando il turno di conseguenza
	 */
	private void actionJollyGP() {
		//CAMBIACOLORE
		//se il cambiacolore non è stato giocato dal giocatore principale ma è stato giocato in precedenza
		//allora gioca il turno regolarmente
	 if (p1.getCartaGiocata() == null && ms.getTesta().equals(new CartaJolly(Jolly.CAMBIACOLORE))) {
			giocabiliGP();
		}
		
	 else {
		 //altrimenti passa il turno al giocatore successivo
		if (p1.getCartaGiocata().equals(new CartaJolly(Jolly.CAMBIACOLORE))) {
			TimerTask passa = new TimerTask() {
				@Override
				public void run() {
					turnoDefault();
					giocaTurno();
				}
			};
			timer.schedule(passa, 1000);
		}
		
		//PESCAQUATTRO
		if (p1.getCartaGiocata().equals(new CartaJolly(Jolly.PESCAQUATTRO))) {
			TimerTask aggiorna = new TimerTask() {
				@Override
				public void run() {
					//AGGIORNO IL TURNO AL GIOCATORE SUCCESSIVO CHE PESCHERA' 4 CARTE
					LinkedList<Giocatore> ll = new LinkedList<>();
					ll.addAll(turno);
					ll.addLast(ll.removeFirst());
					turno = ll.stream().collect(Collectors.toList());
					updateFirst();
					updateLast();	
					
					Giocatore g = getGiocatoreAttivo(); // il giocatore che deve pescare
					
					if (mp.getSize() >= 4) { // se il mazzo di pesca ha almeno 4 carte
						
						Carta c1 = mp.pesca();
						Carta c2 = mp.pesca();
						Carta c3 = mp.pesca();
						Carta c4 = mp.pesca();
						
						if (g.equals(p2)) {
							p2.addMano(c1); p2.addMano(c2); p2.addMano(c3); p2.addMano(c4);
							pannelloP2.getMano().addMano(c1); pannelloP2.getMano().addMano(c2);
							pannelloP2.getMano().addMano(c3); pannelloP2.getMano().addMano(c4);
						}
						if (g.equals(p3)) {
							p3.addMano(c1); p3.addMano(c2); p3.addMano(c3); p3.addMano(c4);
							pannelloP3.getMano().addMano(c1); pannelloP3.getMano().addMano(c2);
							pannelloP3.getMano().addMano(c3); pannelloP3.getMano().addMano(c4);
						}
						if (g.equals(p4)) {
							p4.addMano(c1); p4.addMano(c2); p4.addMano(c3); p4.addMano(c4);
							pannelloP4.getMano().addMano(c1); pannelloP4.getMano().addMano(c2);
							pannelloP4.getMano().addMano(c3); pannelloP4.getMano().addMano(c4);
						}
					}
					else {
						if (g.equals(p2)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p2.addMano(c);
								pannelloP2.getMano().addMano(c);
							}	
						}
						if (g.equals(p3)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p3.addMano(c);
								pannelloP3.getMano().addMano(c);
							}	
						}
						if (g.equals(p4)) {
							Carta c = null;
							for(int i = 0;i<mp.getSize();i++) {
								c = mp.pesca();
								p4.addMano(c);
								pannelloP4.getMano().addMano(c);
							}	
						}
					}
					Utilities.playSound(CARTA_PESCATA);
					//AGGIORNO IL TURNO AL GIOCATORE SUCCESSIVO
					ll.addLast(ll.removeFirst());
					turno = ll.stream().collect(Collectors.toList());
					updateFirst();
					updateLast();
					giocaTurno();
				}
			};
			timer.schedule(aggiorna, 2000); // aggiorno il turno dopo due secondi
		}
	}
   }
	/**
	 * Metodo che gestisce il comportamento del giocatore principale quando rimane con una sola carta nella mano
	 * rendendo visibile il bottone uno
	 */
	private void unoGP() {
		//rendo visibile il bottone uno e lo rendo utilizzabile
		pannelloMazzi.getBottoneUno().addActionListener(this);
		pannelloMazzi.getBottoneUno().setVisible(true);
		pannelloMazzi.getBottoneUno().setEnabled(true);
		
		//ABILITO IL BOTTONE PER 3 SECONDI
		//se entro 3 secondi il bottone non viene cliccato il giocatore principale pescherà 2 carte
		TimerTask visibile = new TimerTask() {
			@Override
			public void run() {
				Utilities.playSound(UNO_FAIL);
				if (mp.getSize() >= 2) {
					Carta c1 = mp.pesca();
					Carta c2 = mp.pesca();
					p1.addMano(c1);p1.addMano(c2);
					pannelloP1.getMano().addMano(c1);
					pannelloP1.getMano().addMano(c2);
				}
				else {
					Carta c = null;
					for (int i = 0;i<mp.getSize();i++) {
						c = mp.pesca();
						p1.addMano(c);
						pannelloP1.getMano().addMano(c);
					}
				}
				Utilities.playSound(CARTA_PESCATA);
				removeActionListeners();
			}
		};
		timer.schedule(visibile, 3000);

		TimerTask aggiorna = new TimerTask() {
			@Override
			public void run() {
				aggiornaTurno();
			}
		};
		timer.schedule(aggiorna, 3500);
	}
	
	/**
	 * Metodo che si occupa di gestire il comportamento dei bot,
	 *  nel caso in cui essi abbiano una sola carta in mano
	 */
	private void unoPC() {
		int valore = new Random().nextInt(10); // 1/10 possibilità di pescare due carte
		GiocatoreComp g = (GiocatoreComp) getGiocatoreAttivo(); // GIOCATORE CHE HA UNA CARTA IN MANO
		if (valore == 0) {
			Utilities.playSound(UNO_FAIL);
			//AGGIORNO LO STATO DEL MODEL E DELLA VIEW DELLA MANO DEL BOT
			//il mazzo ha almeno due carte
			if (mp.getSize() >= 2) {
				Carta c1 = mp.pesca();
				Carta c2 = mp.pesca();
				if (g.equals(p2)) {
					p2.addMano(c1);p2.addMano(c2);
					pannelloP2.getMano().addMano(c1);
					pannelloP2.getMano().addMano(c2);
				}
				if (g.equals(p3)) {
					p3.addMano(c1);p3.addMano(c2);
					pannelloP3.getMano().addMano(c1);
					pannelloP3.getMano().addMano(c2);
				}
				if (g.equals(p4)) {
					p4.addMano(c1);p4.addMano(c2);
					pannelloP4.getMano().addMano(c1);
					pannelloP4.getMano().addMano(c2);
				}
			}
			//il mazzo ha meno di due carte
			else {
				Carta c = null;
				if (g.equals(p2)) {
					for(int i=0;i<mp.getSize();i++) {
						c = mp.pesca();
						p2.addMano(c);
						pannelloP2.getMano().addMano(c);
					}
				}
				if (g.equals(p3)) {
					for(int i=0;i<mp.getSize();i++) {
						c = mp.pesca();
						p3.addMano(c);
						pannelloP3.getMano().addMano(c);
					}
				}
				if (g.equals(p4)) {
					for(int i=0;i<mp.getSize();i++) {
						c = mp.pesca();
						p4.addMano(c);
						pannelloP4.getMano().addMano(c);
					}
				}
			}
			Utilities.playSound(CARTA_PESCATA);
		}
		else {
			Utilities.playSound(UNO);
		}
		//passo il turno al giocatore successivo dopo due secondi
		TimerTask aggiorna = new TimerTask() {
			@Override
			public void run() {
				aggiornaTurno();
			}
		};
		timer.schedule(aggiorna, 2000); // il turno viene aggiornato dopo due secondi
	}
	
	/**
	 * Metodo che rende funzionante il pannello per il cambio colore, aggiornando la view 
	 */
	private void setPannelloCambiaColoreListener() {
		pannelloMazzi.getPannelloCambiaColore().getRosso().addActionListener(this);
		pannelloMazzi.getPannelloCambiaColore().getVerde().addActionListener(this);
		pannelloMazzi.getPannelloCambiaColore().getBlu().addActionListener(this);
		pannelloMazzi.getPannelloCambiaColore().getGiallo().addActionListener(this);
	}
	
	/**
	 * Metodo che disabilità i bottoni gestiti dal giocatore principale aggiornando la view
	 */
	private void removeActionListeners() {
		//rimuovo action listener dalla mano
		pannelloP1.getMano().getMano().forEach(b -> b.removeActionListener(this));
		//rimuovo dal mazzo
		pannelloMazzi.getBottoneMP().removeActionListener(this);
		//rimuovo dal pannello colori
		pannelloMazzi.getPannelloCambiaColore().getRosso().removeActionListener(this);
		pannelloMazzi.getPannelloCambiaColore().getVerde().removeActionListener(this);
		pannelloMazzi.getPannelloCambiaColore().getBlu().removeActionListener(this);
		pannelloMazzi.getPannelloCambiaColore().getGiallo().removeActionListener(this);
		//rimuovo dal bottone uno
		pannelloMazzi.getBottoneUno().removeActionListener(this);
		pannelloMazzi.getBottoneUno().setVisible(false);
		pannelloMazzi.getBottoneUno().setEnabled(false);
		//rimuovo dal bottone passa turno
		pannelloMazzi.getTurno().setEnabled(false);
		pannelloMazzi.getTurno().setVisible(false);
		pannelloMazzi.getTurno().removeActionListener(this);
	}
	
	/**
	 * Metodo che conclude la partita aggiornando la view del frame principale
	 * con la schermata finale di gioco
	 */
	private void fineMatch() {
		//aggiorno lo stato della view
		//imposto un timer a due secondi dopo che il giocatore ha terminato le carte nella mano
		//modificando il frame principale del gioco
		TimerTask attiva = new TimerTask() {
			@Override
			public void run() {
				timer.cancel();
				frame.setLayout(null);
				frame.remove(pannelloMazzi);
				frame.remove(pannelloP1);
				frame.remove(pannelloP2);
				frame.remove(pannelloP3);
				frame.remove(pannelloP4);
				frame.setSize(600,600);
				frame.setLocationRelativeTo(null);
				frame.add(frame.getFinale());
				frame.setIconImage(new ImageIcon("src/view/assets/Uno_Button.png").getImage());
				frame.revalidate();
				frame.repaint();
				
			}
		};
		timer.schedule(attiva, 2000);
		//attivo e aggiorno la view della schermata finale
		frame.getFinale().getStats().addActionListener(this);
		frame.getFinale().getAvatar().setHorizontalAlignment(JLabel.CENTER);
		frame.getFinale().getAvatar()
		.setIcon(new ImageIcon(
				new ImageIcon
				(getGiocatoreAttivo().getAvatar())
				.getImage()
				.getScaledInstance(100, 100,Image.SCALE_DEFAULT))); //avatar
		frame.getFinale().getMessaggio() //messaggio di vittoria dei giocatori
		.setText(getGiocatoreAttivo().getNickname() + " won, Congrats!"); 
		incrementaPartiteGiocate();
		//se il giocatore principale ha vinto la partita
		if (checkGiocatore(getGiocatoreAttivo())) {
			Utilities.playSound(VITTORIA);
			incrementaVinte();
			incrementaLivello();
		}
		//se la partita è stata vinta da un bot
		else {
			Utilities.playSound(SCONFITTA);
			incrementaPerse();
		}
	}
	
	/**
	 * Metodo implementato dall'intefaccia funzionale ActionListener,
	 * questo metodo prende in input un ActionEvent e gestisce le azioni
	 * correlato ad esso (al click di un componente, si attiva l'effetto scelto),
	 * gestendo così tutte le azioni possibili del giocatore principale
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//BOTTONE CHE GIOCA UNA CARTA
		if (e.getSource().getClass().equals(BottoneCarta.class)){
			Utilities.playSound(CARTA_GIOCATA);
			//aggiorno la view  e il model della mano del giocatore e aggiorno lo stato del mazzo di scarto
			pannelloP1.getMano().rimuovi((BottoneCarta)e.getSource());
			ms.aggiungi(p1.removeMano(pannelloP1.getMano().getUltimaGiocata()));
			removeActionListeners();
			
			//FINE TURNO
			TimerTask aggiorna = new TimerTask() {
				@Override
				public void run() {
					//il giocatore principale conclude le carte in mano
					if (p1.getMano().size() == 0) {
						fineMatch();
					}
					//se il giocatore principale resta con una sola carta in mano
					else if (p1.getMano().size() == 1) {
						unoGP();
					}
					//aggiornamento del turno di default
					else  {
						aggiornaTurno();
					}
				}
			};
			timer.schedule(aggiorna, 1000); //attiva l'aggiornamento dopo un secondo
		}
		//BOTTONE CHE PESCA UNA CARTA
		
		if (e.getSource().getClass().equals(BottoneMazzoPesca.class)) {
			Carta pescata = mp.pesca();
			Utilities.playSound(CARTA_PESCATA);
			p1.addMano(pescata);
			pannelloP1.getMano().addMano(pescata);
			
			TimerTask aggiorna = new TimerTask() {
				@Override
				public void run() {
					aggiornaTurno();
				}
			};
			
			if (checkCarta(pescata)) {
				// se la carta è un cambiacolore allora può essere scartata
				if (pescata.equals(new CartaJolly(Jolly.CAMBIACOLORE))) {
					passaTurnoGP();
				}
				// se la carta è un pescaquattro controllo se è giocabile
				if(pescata.equals(new CartaJolly(Jolly.PESCAQUATTRO))) {
					if (p1.carteGiocabili().contains(new CartaJolly(Jolly.PESCAQUATTRO))) {
						passaTurnoGP();
					}
					// altrimenti passo il turno
					else {
						removeActionListeners();
						timer.schedule(aggiorna, 1000);
					}
				}
			}
			// se la carta è una carta colore
			else {
				CartaColore c = (CartaColore)pescata;
				//controllo se essa è giocabile
				if(c.getColore().equals(getColore())
				   || c.getSimbolo().equals(getSimbolo())) {
					passaTurnoGP();
				}
				//altrimenti passo il turno
				else {
					removeActionListeners();
					timer.schedule(aggiorna, 1000);
				}
			}
		}
		
		//BOTTONE UNO
		if (e.getSource().getClass().equals(BottoneUno.class)) {
			Utilities.playSound(UNO);
			removeActionListeners();
			//cancello le operazioni che sarebbero avvenute successivamente
			timer.cancel();
			// e reimposto un nuovo timer
			this.timer = new Timer();
			TimerTask aggiorna = new TimerTask() {
				@Override
				public void run() {
					aggiornaTurno();
				}
			};
			timer.schedule(aggiorna, 1000);//attivo l'aggiornamento del turno dopo un secondo
		}
		
		//BOTTONE PASSATURNO
		if (e.getSource().getClass().equals(BottonePassaTurno.class)) {
			Utilities.playSound(BOTTONE);
			removeActionListeners();
			TimerTask aggiorna = new TimerTask() {
				@Override
				public void run() {
					//passo il turno al giocatore successivo
					turnoDefault();
					giocaTurno();
				}
			};
			timer.schedule(aggiorna,1000);
		}
		//BOTTONI CAMBIA COLORE
		//ROSSO
		if (e.getSource().equals(pannelloMazzi.getPannelloCambiaColore().getRosso())) {
			Utilities.playSound(BOTTONE);
			//aggiorno lo stato del colore e del simbolo giocabile
			setColore(Colore.ROSSO);
			setSimbolo(Simbolo.JOLLY);
			//disattivo il pannello cambia colore
			pannelloMazzi.getLabelColore().cambiaColore(Colore.ROSSO);
			pannelloMazzi.getPannelloCambiaColore().setVisible(false);
			pannelloMazzi.getPannelloCambiaColore().setEnabled(false);
			removeActionListeners();
			actionJollyGP(); //aggiorno il turno in base alla carta giocata (cambiacolore o pescaquattro)
			
		}
		//VERDE
		if (e.getSource().equals(pannelloMazzi.getPannelloCambiaColore().getVerde())) {
			Utilities.playSound(BOTTONE);
			setColore(Colore.VERDE);
			setSimbolo(Simbolo.JOLLY);
			pannelloMazzi.getLabelColore().cambiaColore(Colore.VERDE);
			pannelloMazzi.getPannelloCambiaColore().setVisible(false);
			pannelloMazzi.getPannelloCambiaColore().setEnabled(false);
			removeActionListeners();
			actionJollyGP();
		}
		//BLU
		if (e.getSource().equals(pannelloMazzi.getPannelloCambiaColore().getBlu())) {
			Utilities.playSound(BOTTONE);
			setColore(Colore.BLU);
			setSimbolo(Simbolo.JOLLY);
			pannelloMazzi.getLabelColore().cambiaColore(Colore.BLU);
			pannelloMazzi.getPannelloCambiaColore().setVisible(false);
			pannelloMazzi.getPannelloCambiaColore().setEnabled(false);
			removeActionListeners();
			actionJollyGP();
		}
		//GIALLO
		if (e.getSource().equals(pannelloMazzi.getPannelloCambiaColore().getGiallo())) {
			Utilities.playSound(BOTTONE);
			setColore(Colore.GIALLO);
			setSimbolo(Simbolo.JOLLY);
			pannelloMazzi.getLabelColore().cambiaColore(Colore.GIALLO);
			pannelloMazzi.getPannelloCambiaColore().setVisible(false);
			pannelloMazzi.getPannelloCambiaColore().setEnabled(false);
			removeActionListeners();
			actionJollyGP();
		}
			
		//BOTTONE STATS (SCHERMATA FINALE)
		if (e.getSource().equals(frame.getFinale().getStats())) {
			Utilities.playSound(BOTTONE);
			//apro una nuova finestra delle statistiche del giocatore
			ControllerStats stats = ControllerStats.getInstance();
			PannelloStats frameStats = stats.getFrame().getStats();
			//AGGIORNO IL FRAME DELLE STATS
			frameStats.getCambia().setVisible(false);
			frameStats.getNickname().setEditable(false);
			frameStats.getNickname().setFocusable(false);
			frameStats.revalidate();
			frameStats.repaint();
			
		}
	}
}
