package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 * Classe che rappresenta graficamente un pannello sulla quale vi sono tutti i dati del giocatore principale
 * tra cui avatar,nickname,livello, partite giocate, vinte e perse.
 */
public class PannelloStats extends JPanel {
	private final ImageIcon BACKGROUND = new ImageIcon(
														new ImageIcon("src/view/assets/schermataprincipale.png")
														.getImage()
														.getScaledInstance(600, 600, Image.SCALE_DEFAULT));
	private JLabel background;
	private JLabel avatar;
	private JButton cambiaAvatar;
	private JTextField nickname,
					   lvl,
					   vinte,
					   perse,
					   giocate;
	/**
	 * Costruttore della classe che ne imposta i valori grafici 
	 */
	public PannelloStats() {
		//inizializzo i componenti
		background = new JLabel();
		background.setIcon(BACKGROUND);
		
		
		avatar = new JLabel();
		avatar.setOpaque(true);
		avatar.setBackground(Color.black);
		avatar.setBorder(BorderFactory.createLineBorder(Color.white, 4));
		avatar.setHorizontalAlignment(JLabel.CENTER);
		cambiaAvatar = new JButton("UPLOAD");
		
		nickname = new JTextField();
		nickname.setDocument(new LimitJTextField(8));
		nickname.setBackground(Color.black);
		nickname.setHorizontalAlignment(SwingConstants.CENTER);
		nickname.setFont(new Font("Serif",Font.BOLD,20));
		nickname.setForeground(Color.RED);

		lvl = new JTextField();
		lvl.setEditable(false);
		lvl.setBackground(Color.black);
		lvl.setHorizontalAlignment(SwingConstants.CENTER);
		lvl.setFont(new Font("Serif",Font.BOLD,20));
		lvl.setForeground(Color.white);
		
		giocate = new JTextField();
		giocate.setEditable(false);
		giocate.setBackground(Color.black);
		giocate.setHorizontalAlignment(SwingConstants.CENTER);
		giocate.setFont(new Font("Serif",Font.BOLD,20));
		giocate.setForeground(Color.white);
		
		vinte = new JTextField();
		vinte.setEditable(false);
		vinte.setBackground(Color.black);
		vinte.setHorizontalAlignment(SwingConstants.CENTER);
		vinte.setFont(new Font("Serif",Font.BOLD,20));
		vinte.setForeground(Color.white);
		
		perse = new JTextField();
		perse.setEditable(false);
		perse.setBackground(Color.black);
		perse.setHorizontalAlignment(SwingConstants.CENTER);
		perse.setFont(new Font("Serif",Font.BOLD,20));
		perse.setForeground(Color.white);
		
		//Layout
		setLayout(null);
		background.setLayout(null);
		
		//posizionamento dei componenti
		background.setBounds(0,0,600,600);
		avatar.setBounds(250, 50, 100, 100);
		cambiaAvatar.setBounds(250,160,100,30);
		nickname.setBounds(100,200, 400, 50);
		lvl.setBounds(100, 265, 400, 50);
		giocate.setBounds(100,325 , 400, 50);
		vinte.setBounds(100, 385, 400, 50);
		perse.setBounds(100, 445, 400, 50);
		
		//aggiungo i componenti
		add(background);
		background.add(avatar);
		background.add(cambiaAvatar);
		background.add(nickname);
		background.add(lvl);
		background.add(giocate);
		background.add(vinte);
		background.add(perse);
	}
	/**
	 * Classe interna che permette di limitare la lunghezza di un JTextField
	 */
	class LimitJTextField extends PlainDocument 
	{
	   private int max;
	   /**
	    * Metodo costruttore che prende un intero come paramentro e inizializza il campo d'istanza
	    */
	   LimitJTextField(int max) {
	      super();
	      this.max = max;
	   }
	   /**
	    * Metodo che limita il JTextfield tramite il campo d'istanza max
	    */
	   public void insertString(int offset, String text, AttributeSet attr) throws BadLocationException {
	      if (text == null)
	         return;
	      if ((getLength() + text.length()) <= max) {
	         super.insertString(offset, text, attr);
	      }
	   }
	}

	public JLabel getAvatar() {
		return avatar;
	}
	public JButton getCambia() {
		return cambiaAvatar;
	}
	public JTextField getNickname() {
		return nickname;
	}
	public JTextField getLevel() {
		return lvl;
	}
	public JTextField getGiocate() {
		return giocate;
	}
	public JTextField getVinte() {
		return vinte;
	}
	public JTextField getPerse() {
		return perse;
	}
}
