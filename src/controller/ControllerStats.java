package controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import view.StatsFrame;

/**
 * Classe che gestisce la personalizzazione del pannello info del giocatore, 
 * e la visualizzazione delle statistiche di esso
 */
public class ControllerStats implements ActionListener {
	private static final File INFO = new File("src/data/GiocatorePrincipale.txt"); // Info del giocatore (file.txt)

	private static ControllerStats instance; //Istanza unica della classe
	
	private StatsFrame frame = new StatsFrame(); // frame delle statistiche
	
	/**
	 * Metodo che ritorna la classe se già istanziata, 
	 * altrimenti ne istanzia una nuova
	 */
	public static ControllerStats getInstance() {
		if (instance != null)
			return instance;
		return new ControllerStats();
	}
	
	/**
	 * Metodo costruttore di classe,
	 * aggiorna la view rendendo visibili e attivi i componenti della schermata
	 */
	private ControllerStats() {
		//aggiorno lo stato della view
		//immagine iniziale
		frame.getStats().getAvatar().setIcon(new ImageIcon(
											 new ImageIcon
											 (getAvatar())
											 .getImage()
											 .getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
		//nome iniziale
		frame.getStats().getNickname().setText(getNickname());
		//statistiche del giocatore
		frame.getStats().getLevel().setText("LEVEL :" + " "+ Utilities.getArrayDaFile(INFO).get(4));
		frame.getStats().getGiocate().setText("MATCHES :" + " "+ Utilities.getArrayDaFile(INFO).get(1));
		frame.getStats().getVinte().setText("WIN :" + " "+ Utilities.getArrayDaFile(INFO).get(2));
		frame.getStats().getPerse().setText("LOSE :" + " "+ Utilities.getArrayDaFile(INFO).get(3));
		//action listeners
		frame.getStats().getCambia().addActionListener(this);
		frame.getStats().getNickname().addActionListener(this);
	}

	public static String getAvatar() {
		return Utilities.getArrayDaFile(INFO).get(5);
	}
	
	public static String getNickname() {
			return Utilities.getArrayDaFile(INFO).get(0);
	}
	
	public StatsFrame getFrame() {
		return frame;
	}
	
	/**
	 * Metodo implementato dall'interfaccia funzionale ActionListener che permette di scatenare
	 * un evento al click di un componente attivo
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//AVATAR CHOOSER
		if (e.getSource().equals(frame.getStats().getCambia())) {
			//messaggio 
			JOptionPane.showMessageDialog(null,"upload your image in this directory (60x60px)");
			//dichiaro un fileChooser e imposto la cartella dove inserire le immagini
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("src/view/assets/avatars"));
			//variabili per check di validità dell'immagine da impostare
			int valido = fileChooser.showOpenDialog(null);
			boolean check = fileChooser.getSelectedFile().getAbsolutePath().endsWith("png");
			
			//se l'immagine impostata è valida
			if (valido == JFileChooser.APPROVE_OPTION && check) {
				//immagine visualizzata nel pannello
				ImageIcon avatar = new ImageIcon(new ImageIcon(fileChooser
												 .getSelectedFile()
												 .getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
				
				frame
				.getStats()
				.getAvatar()
				.setIcon(avatar);
				//aggiorno il file del GiocatorePrincipale con il path dell'immagine
				Utilities.aggiornaFile(INFO,fileChooser
							.getSelectedFile()
							.getPath()
							,5);
			}
			//messaggio di errore
			else {
				JOptionPane.showMessageDialog(null,"Image not valid, extension must be .png");
			}
		}
		//NAME
		if(e.getSource().equals(frame.getStats().getNickname())) {
			String name = frame.getStats().getNickname().getText();
			// se il nome è valido aggiorno il nome nel file del giocatore principale
			if (!(name.equals("") 
					|| name.contains(" "))) {
				frame.getStats().getNickname().setEditable(false);
				Utilities.aggiornaFile(INFO,name,0);
			}
			//messaggio di errore
			else {
				JOptionPane.showMessageDialog(null,"insert a valid nickname, \n"
						+ "no empty name or spaces allowed");
			}
		}
	}
}
