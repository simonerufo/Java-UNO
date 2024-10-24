package model;
/**
 * Interfaccia utile al design pattern Observer,Observable, qualunque classe la implementi verrà resa "Osservabile" dalle classi che verranno registrate
 */
public interface Subject {
	public void register(Observer o);
	public void unregister(Observer o);
	public void notifyObservers();
}
