package controller;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Classe che raccoglie una serie di metodi utili 
 * per il funzionamento generale del gioco 
 * di lettura e aggiornamento di file (e di lettura audio)
 */
public class Utilities {
	/**
	 * Metodo che restituisce una lista di stringhe derivanti dal file letto in input
	 */
	public static List<String> getArrayDaFile(File file){
		List<String> result = null;
		
		try {
			//colleziona tutte le stringhe del file linea per linea in una lista
			 result = Files.lines(Paths.get(file.getPath()))
					 	  .collect(Collectors.toList());
			 } 
		// se il file non esiste o il percorso è sbagliato cattura l'eccezione
		catch (IOException e) {
			e.printStackTrace();
			}
		return result;
	}
	
	/**
	 * Metodo che aggiorna il file letto da input 
	 * con una stringa in input 
	 * alla linea data in input
	 */
	public static void aggiornaFile(File file,String str,int lineNum) {
		try {
			//lista di stringhe delle righe lette
			ArrayList<String> array =(ArrayList<String>)Utilities.getArrayDaFile(file);
			//aggiorna la riga con la stringa in input
			array.remove(lineNum);
			array.add(lineNum, str);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			array.forEach(s->{
				try {
					bw.write(s+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Metodo che presi come parametri un file ed un indice, torna la stringa che fa parte d
	 */
	public static String getData(File file,int index) {
		return getArrayDaFile(file).get(index);
	}
	/**
	 * Metodo che si occupa della riproduzione di un suono dato un file.wav in input
	 */
	public static void playSound(String path) {
		//leggo il file
		File file = new File(path);
		try {
			//se il file è valido leggo e riproduco il suono
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		}
		//altrimenti catturo le eccezioni
		//formato non valido
		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		//file non valido o percorso errato
		catch(IOException e1) {
			e1.printStackTrace();
		}
		//linea non disponibile
		catch(LineUnavailableException e2) {
			e2.printStackTrace();
		}
	}
	
}

	