package model;

import java.util.Objects;
/**
 *  Classe che descrive il comportamento di una carta colorata del gioco di Uno
 */
public class CartaColore extends Carta {
	/**
	 * Enum che raccoglie i colori possibili di una carta
	 */
	public enum Colore {BLU,ROSSO,GIALLO,VERDE}
	
	private Simbolo simbolo;
	private Colore colore;
	private int punteggio; // valore intero che indica quanti punti vale una carta
	
	/**
	 * Metodo costruttore che prende in input un simbolo ed un colore e li assegna alla carta
	 */
	public CartaColore(Simbolo simbolo, Colore colore) {
		this.simbolo = simbolo;
		this.colore = colore;
		// in base al simbolo in input imposto un punteggio
		this.punteggio = 
				switch(this.simbolo) {
				case UNO ->  1;
				case DUE ->  2;
				case TRE -> 3;
				case QUATTRO -> 4;
				case CINQUE ->  5;
				case SEI ->  6;
				case SETTE ->  7;
				case OTTO -> 8;
				case NOVE ->  9;
				default -> 0;
			};
	}

	public Simbolo getSimbolo() {
		return this.simbolo;
	}
	public Colore getColore() {
		return this.colore;
	}
	
	
	/**
	 * colore e simbolo della carta visualizzati sotto forma di stringa
	 */
	@Override 
	public String toString() {
		return this.colore +": " + this.simbolo;
	}
	@Override
	public int getEstrazione() {
		return this.punteggio;
	}
	/**
	 * Metodo equals che permette di decretare se una carta è uguale ad un'altra
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		CartaColore c = (CartaColore)o;
		
		return Objects.equals(colore, c.getColore()) 
				&& Objects.equals(simbolo, c.getSimbolo()) 
				&& Objects.equals(punteggio, c.getEstrazione());
	}
}
