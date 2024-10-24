package model;

@FunctionalInterface
/**
 * Interfaccia funzionale la quale permette di aggiornare un oggetto che ne sta "osservando" un altro,tramite una notifica dall'oggetto osservato
 */
public interface Observer {
	void update(Object o);
}
