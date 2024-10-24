package view;


import java.awt.Color;
import java.awt.Dimension;


import javax.swing.JPanel;


/**
 * Classe che rappresenta graficamente il pannello su cui vi sono il mazzo di pesca e il mazzo di scarto e il label colore
 * con in aggiunta dinamicamente il bottone UNO! il bottone passaturno e il pannello del cambio colore
 */
public class PannelloMazzi  extends JPanel{
	
	private BottoneMazzoPesca bottoneMazzoPesca;
	private BottonePassaTurno bottonePassa;
	private BottoneUno bottoneUno;
	private LabelMazzoScarto labelMazzoScarto;
	private LabelColore labelColore;
	private PannelloCambiaColore pannelloCambia;
	
	/**
	 * Metodo costruttore che imposta i valori grafici di ogni componente
	 */
	public PannelloMazzi() {
		
		//components
		bottoneMazzoPesca = new BottoneMazzoPesca();
	
		labelMazzoScarto = new LabelMazzoScarto();
		labelMazzoScarto.setPreferredSize(new Dimension(100,150));
		
		bottonePassa = new BottonePassaTurno();
		bottonePassa.setPreferredSize(new Dimension(50,50));
		
		labelColore = new LabelColore();
		labelColore.setPreferredSize(new Dimension(50,50));
	
		pannelloCambia = new PannelloCambiaColore();
		pannelloCambia.setBackground(Color.BLACK);
		
		bottoneUno = new BottoneUno();
		bottoneUno.setPreferredSize(new Dimension(50,50));
		
		
		//Layout
		setLayout(null);
		
		bottoneMazzoPesca.setBounds(260, 80, 100, 150);
		labelMazzoScarto.setBounds(560, 80, 100, 150);
		labelColore.setBounds(440, 130, 50, 50);
		pannelloCambia.setBounds(260, 250, 400, 60);
		bottoneUno.setBounds(440,60,50,50);
		bottonePassa.setBounds(370, 130, 50, 50);
		
		// adding components
		add(bottoneMazzoPesca);
		add(labelMazzoScarto);
		add(labelColore);
		add(pannelloCambia);
		add(bottoneUno);
		add(bottonePassa);
	}

	public BottoneMazzoPesca getBottoneMP() {
		return bottoneMazzoPesca;
	}
	
	public LabelMazzoScarto getLabelMS() {
		return labelMazzoScarto;
	}
	
	public LabelColore getLabelColore() {
		return labelColore;
	}
	
	public PannelloCambiaColore getPannelloCambiaColore() {
		return pannelloCambia;
	}
	
	public BottoneUno getBottoneUno() {
		return bottoneUno;
	}
	
	public BottonePassaTurno getTurno() {
		return bottonePassa;
	}
	
}
