package model;

import java.io.File;

import controller.Utilities;


/**
 * Classe che definisce e modella il comportamento del Giocatore Principale di Uno, questa classe può essere istanziata una sola volta
 */

public class GiocatorePrincipale extends Giocatore {
	private static GiocatorePrincipale instance;
	private static final File DATI = new File("src/data/GiocatorePrincipale.txt"); // file dei dati relaviti al giocatore principale
	private int partiteGiocate; // numero di partite giocate
	private int vinte;		   // numero di partite vinte
	private int perse;		   // numero di partite perse
	private int livello;		   // valore intero del livello del giocatore
	
	/**
	 * Metodo getter dell'unica istanza del Giocatore
	 */
	public static GiocatorePrincipale getInstance() {
		if (instance != null)
			return instance;
		return new GiocatorePrincipale();
	}
	/**
	 * Metodo costruttore privato che riceve i dati dal controller relativi al giocatore
	 * ed inizializza i campi d'istanza
	 */
	private GiocatorePrincipale() {
		super();
		setNickname(Utilities.getData(DATI,0));
		setAvatar(Utilities.getData(DATI,5));
		partiteGiocate = Integer.parseInt(Utilities.getData(DATI, 1));
		vinte = Integer.parseInt(Utilities.getData(DATI, 2));
		perse = Integer.parseInt(Utilities.getData(DATI, 3));
		livello = Integer.parseInt(Utilities.getData(DATI, 4));
	}

	public int getPartiteGiocate() {
		return partiteGiocate;
	}
	public void setPartiteGiocate(int partite) {
		partiteGiocate = partite;
		notifyObservers();
	}
	
	public int getVinte() {
		return vinte;
	}
	public void setVinte(int vinte) {
		this.vinte = vinte;
		notifyObservers();
	}
	
	public int getPerse() {
		return perse;
	}
	public void setPerse(int perse) {
		this.perse = perse; 
		notifyObservers();
	}
	
	public int getLivello() {
		return livello;
	}
	
	public  void setLivello(int livello) {
		this.livello = livello;
	}

}
