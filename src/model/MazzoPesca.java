package model;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

import model.CartaColore.Colore;
import model.CartaJolly.Jolly;

/**
 * Classe che modella il comportamento concreto di un Mazzo da cui pescare nel gioco di uno, questa classe è osservabile
 */
public class MazzoPesca extends Mazzo implements Subject{
	private List<Carta> mazzo = new Stack<Carta>();
	private List<Observer> observers = new ArrayList<>();

	
	/**
	 * METODO COSTRUTTORE DEL MAZZO DI PESCA:
	 * per ogni colore di gioco (BLU,ROSSO,GIALLO,VERDE) inizializzo il campo mazzo inserendo una carta in esso del segno e colore adeguato
	 * inserisco due carte jolly per ogni colore, mentre le carte colorate sono 25 per ogni colore 
	 */
	public MazzoPesca() {
		for (Colore colore : Colore.values()) {
			mazzo.add(new CartaColore(Simbolo.ZERO,colore));
			mazzo.add(new CartaColore(Simbolo.UNO,colore));
			mazzo.add(new CartaColore(Simbolo.DUE,colore));
			mazzo.add(new CartaColore(Simbolo.TRE,colore));
			mazzo.add(new CartaColore(Simbolo.QUATTRO,colore));
			mazzo.add(new CartaColore(Simbolo.CINQUE,colore));
			mazzo.add(new CartaColore(Simbolo.SEI,colore));
			mazzo.add(new CartaColore(Simbolo.SETTE,colore));
			mazzo.add(new CartaColore(Simbolo.OTTO,colore));
			mazzo.add(new CartaColore(Simbolo.NOVE,colore));
			mazzo.add(new CartaColore(Simbolo.SALTA,colore));
			mazzo.add(new CartaColore(Simbolo.INVERTI,colore));
			mazzo.add(new CartaColore(Simbolo.PESCADUE,colore));
			mazzo.add(new CartaJolly(Jolly.CAMBIACOLORE));
			mazzo.add(new CartaColore(Simbolo.UNO,colore));
			mazzo.add(new CartaColore(Simbolo.DUE,colore));
			mazzo.add(new CartaColore(Simbolo.TRE,colore));
			mazzo.add(new CartaColore(Simbolo.QUATTRO,colore));
			mazzo.add(new CartaColore(Simbolo.CINQUE,colore));
			mazzo.add(new CartaColore(Simbolo.SEI,colore));
			mazzo.add(new CartaColore(Simbolo.SETTE,colore));
			mazzo.add(new CartaColore(Simbolo.OTTO,colore));
			mazzo.add(new CartaColore(Simbolo.NOVE,colore));
			mazzo.add(new CartaColore(Simbolo.SALTA,colore));
			mazzo.add(new CartaColore(Simbolo.INVERTI,colore));
			mazzo.add(new CartaColore(Simbolo.PESCADUE,colore));
			mazzo.add(new CartaJolly(Jolly.PESCAQUATTRO));
			}
	}
	/**
	 * Metodo che restituisce quante carte ci sono nel mazzo
	 */
	public int getSize() {
		return mazzo.size();
	}
	
	/**
	 * Metodo che restituisce l'intera lista delle carte presenti nel mazzo
	 */
	public List<Carta> getMazzo(){
		return (List<Carta>)mazzo;
	}
	
	/**
	 * Metodo che ritorna la prima carta in cima al mazzo di pesca e notifica gli osservatori
	 */
	public Carta pesca() {
		Stack<Carta> m = (Stack<Carta>)this.mazzo;
		Carta carta = null;
		if (!(m.isEmpty())) {
			carta = m.pop();
			notifyObservers();
			return carta;
		}
		return carta;
	}
	/**
	 * Metodo che inserisce nel mazzo la carta presa in input
	 */
	public void inserisci(Carta c) {
		mazzo.add(c);
	}
	/**
	 * Metodo che si occupa di mescolare il mazzo di carte
	 */
	public void mischia() {
		Collections.shuffle(mazzo);
	}
	/**
	 * Metodo che ritorna una carta estratta casualmente dal mazzo
	 */
	public Carta estrai() {
		mischia();
		return mazzo.get(new Random().nextInt(getSize()));
	}
	/**
	 * Metodo che visualizza il mazzo sottoforma di stringa
	 */
	@Override
	public String toString() {
		return mazzo.stream()
				.map(x-> x.toString())
				.collect(Collectors.joining("\n"));
	}
	/**
	 * Metodo che aggiunge alla lista di osservatori un nuovo osservatore
	 */
	@Override
	public void register(Observer o) {
		o.update(this);
		observers.add(o);
		
	}
	/**
	 * Metodo che rimuove un osservatore dalla lista di osservatori
	 */
	@Override
	public void unregister(Observer o) {
		observers.remove(o);
		
	}
	/**
	 * Metodo che manda una notifica agli osservatori tramite l'update di questi
	 */
	@Override
	public void notifyObservers() {
		observers.forEach(o->o.update(this));
	}

}
