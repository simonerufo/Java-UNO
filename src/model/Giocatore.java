package model;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controller.ControllerGame;

import model.CartaJolly.Jolly;

/**
 * Classe  astratta che regola il comportamento di un Giocatore di Uno, questa classe è osservabile
 */
public abstract class Giocatore implements Subject{
	private List<Observer> observers = new ArrayList<>(); // lista degli osservatori della classe

	private List<Carta> mano = new ArrayList<Carta>(); // mano del giocatore
	private Carta cartaGiocata;                        // ultima carta giocata

	private String nickname; //nome del giocatore
	private String avatar;  // avatar del giocatore
	
	
	/**
	 * Metodo che permette di registrare un'osservatore del giocatore
	 */
	@Override
	public void register(Observer o) {
		o.update(this);
		observers.add(o);
	}
	/**
	 * Metodo che rimuove un osservatore del giocatore
	 */
	@Override
	public void unregister(Observer o) {
		observers.remove(o);
	}
	/**
	 * Metodo che manda una notifica all'osservatore che si aggiornerà
	 */
	@Override
	public void notifyObservers() {
		observers.forEach(o->o.update(this));
	}
	

	public List<Carta> getMano() {
		return mano;
	}

	public Carta getCartaGiocata() {
		return cartaGiocata;
	}
	
	
	/**
	 * Metodo che aggiunge una carta alla mano del giocatore
	 */
	public void addMano(Carta carta) {
		mano.add(carta);
	}
	
	public void setMano(List<Carta>mano) {
		this.mano = mano;
	}
	/**
	 * Metodo che rimuove dalla mano la carta giocata
	 */
	public Carta removeMano(Carta e) {
		mano.remove(e);
		//imposto la carta rimossa come ultima carta giocata
		cartaGiocata = e;
		return e;
	}

	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String name) {
		this.nickname = name;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String path) {
		this.avatar = path;
	}

	/**
	 * Metodo che decreta quali carte è possibile giocare dalla mano, ricavando informazioni dal controller
	 */
	public List<Carta> carteGiocabili(){
		List<Carta> giocabili = new ArrayList<Carta>();
		//carte colore giocabili
		List<Carta> carteCol = getMano().stream()
				.filter(x-> !(ControllerGame.checkCarta(x))) // se la carta è una carta colore
				.map(x->(CartaColore)x) //faccio il cast ad essa
				.filter( x->(ControllerGame.getColore() != null //se la carta non è nulla, è dello stesso colore giocabile oppure è dello stesso simbolo giocabile
							&&
							ControllerGame.getColore().equals((x.getColore()))
							|| (ControllerGame.getSimbolo().equals(x.getSimbolo()))))
				.map(x->(Carta)x) 
				.collect(Collectors.toList()); //allora è giocabile
		
		//carte jolly giocabili
		List<Carta> carteJol = getMano().stream()
				.filter(x-> ControllerGame.checkCarta(x)) // se la carta è una carta jolly allora è giocabile
				.collect(Collectors.toList());
		
		giocabili.addAll(carteCol);
		giocabili.addAll(carteJol);
		
		//carta pescaquattro NON giocabile
		//stream di controllo se ho qualche carta colore giocabile
		CartaJolly pq = new CartaJolly(Jolly.PESCAQUATTRO);
		List<CartaColore> colore = giocabili.stream()
				.filter(x-> !(ControllerGame.checkCarta(x)))
				.map(x->(CartaColore)x)
				.filter(x->ControllerGame.getColore().equals(x.getColore()))
				.collect(Collectors.toList());
		//controllo se posso giocare il pescaquattro
		// se ho il pescaquattro in mano, ma ho qualche carta colore giocabile
		//allora rimuovo il pescaquattro dalle carte giocabili
		if ((giocabili.contains(pq)) && !(colore.isEmpty()))
			giocabili = giocabili.stream()
			.filter(x->!(x.equals(pq)))
			.collect(Collectors.toList());
		
		return giocabili;
	}

}

    