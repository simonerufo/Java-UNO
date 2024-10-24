package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Classe che modella il comportamento del mazzo di scarto del gioco Uno,
 * questa classe è osservabile
 */
public class MazzoScarto extends Mazzo implements Subject{
	private List<Carta> mazzo = new LinkedList<Carta>();
	private List<Observer> observers = new ArrayList<>();
	/**
	 * Metodo che inserisce in testa al mazzo di scarto la carta passata come argomento,
	 *  notificando gli observers
	 */
	public Carta aggiungi(Carta c) {
		mazzo.add(c);
		notifyObservers();
		return c;
	}
	/**
	 * Metodo che rimuove la carta passata come argomento,
	 * notificando gli observers
	 */
	public void rimuovi(Carta c) {
		mazzo.remove(c);
		notifyObservers();
	}

	public List<Carta> getMazzo() {
		return mazzo;
	}
	/**
	 * Metodo che ritorna la carta in cima al mazzo di scarto
	 */
	public Carta getTesta() {
		LinkedList<Carta> l = (LinkedList<Carta>)mazzo;
		if (!l.isEmpty())
			return l.getLast();
		return null;
	}
	/**
	 * Metodo che aggiunge un observer alla lista
	 */
	@Override
	public void register(Observer o) {
		o.update(this);
		observers.add(o);
	}
	/**
	 * Metodo che rimuove un observer dalla lista
	 */
	@Override
	public void unregister(Observer o) {
		observers.remove(o);	
	}
	/**
	 * Metodo che notifica gli observers, aggiornandoli
	 */
	@Override
	public void notifyObservers() {
		observers.forEach(o->o.update(this));
		
	}
}
