package model;

import java.util.Objects;
/**
 * Classe che definisce una carta jolly del gioco di uno
 */
public class CartaJolly extends Carta {
	//Enum che dichiara i due tipi di jolly
	public enum Jolly {CAMBIACOLORE,PESCAQUATTRO}

	private final Simbolo simbolo = Simbolo.JOLLY; // il simbolo del jolly è sempre un jolly
	private Jolly jolly;
	
	/**
	 * Metodo costruttore della carta jolly che imposta il jolly in input come campo d'istanza
	 */
	public CartaJolly(Jolly jolly) {
		this.jolly = jolly;
	}

	public  Jolly getJolly() {
		return this.jolly;
	}
	public Simbolo getSimbolo() {
		return this.simbolo;
	}
	
	/**
	 * Metodo che visualizza il nome del jolly sotto forma di stringa
	 */
	@Override
	public String toString() {
		return this.jolly.toString();
	}
	@Override
	public int getEstrazione() {
		return 0;
	}
	/**
	 * Metodo equals che decreta se una  carta jolly è uguale ad un' altra
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
				return false;
		CartaJolly c = (CartaJolly) o;
		
		return Objects.equals(jolly, c.getJolly());
	}
	
}
