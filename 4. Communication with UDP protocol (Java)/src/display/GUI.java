package display;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	public static JTextField IpAdresa = null;
	public static JTextField velkostVzorky = null;
	public static JTextField pocetVzoriek = null;
	public static JTextField retazec = null;
	
	private JButton ButtonPoslatSubor = null;
	private JButton ButtonPoslat = null;
	private JButton ButtonPoslatZly = null;
	public static JButton ButtonPrijat = null;
	public static JTextArea text = null;
	public static JTextArea textFinal = null;
	
	public GUI() {
		setTitle("Komunikacia s vyuzitim UDP protokolu");
		setSize(1000,600);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JLabel ip = new JLabel("Cielova IP adresa:");
		ip.setLocation(20, 20);
		ip.setSize(150, 20);
		ip.setVisible(true);
		add(ip);
		IpAdresa = new JTextField("192.168.55.125");
		IpAdresa.setLocation(155, 20);
		IpAdresa.setSize(100, 20);
		IpAdresa.setVisible(true);
		add(IpAdresa);
		
		JLabel vzorka = new JLabel("Velkost ramca:");
		vzorka.setLocation(20, 50);
		vzorka.setSize(150, 20);
		vzorka.setVisible(true);
		add(vzorka);
		velkostVzorky = new JTextField("30");
		velkostVzorky.setLocation(155, 50);
		velkostVzorky.setSize(100, 20);
		velkostVzorky.setVisible(true);
		add(velkostVzorky);
		
		JLabel retazecLabel = new JLabel("Retazec:");
		retazecLabel.setLocation(20, 80);
		retazecLabel.setSize(150, 20);
		retazecLabel.setVisible(true);
		add(retazecLabel);
		retazec = new JTextField("abc");
		retazec.setLocation(155, 80);
		retazec.setSize(100, 20);
		retazec.setVisible(true);
		add(retazec);
		
		ButtonPoslat = new JButton("poslat msg");
		ButtonPoslat.setLocation(20,120);
		ButtonPoslat.setSize(100,20);
		ButtonPoslat.addActionListener(this);
		add(ButtonPoslat);
		
		ButtonPoslatZly = new JButton("poslat msg chybne");
		ButtonPoslatZly.setLocation(170,120);
		ButtonPoslatZly.setSize(150,20);
		ButtonPoslatZly.addActionListener(this);
		add(ButtonPoslatZly);
		
		ButtonPoslatSubor = new JButton("poslat subor");
		ButtonPoslatSubor.setLocation(170,160);
		ButtonPoslatSubor.setSize(150,20);
		ButtonPoslatSubor.addActionListener(this);
		add(ButtonPoslatSubor);
		
		ButtonPrijat = new JButton("prijimat");
		ButtonPrijat.setLocation(20,160);
		ButtonPrijat.setSize(100,20);
		ButtonPrijat.addActionListener(this);
		ButtonPrijat.setVisible(true);
		add(ButtonPrijat);
		
		//pravy panel
		text = new JTextArea();
		text.setEditable(false);
		text.setFont(new Font("Monospaced",Font.PLAIN,13));// neproporcovany vypis
		JScrollPane panel = null;
		panel = new JScrollPane(text);
		panel.setSize(530,530);
		panel.setLocation(420,20);
		panel.setAutoscrolls(true);
		add(panel);
		
		//lavy panel
		textFinal = new JTextArea();
		textFinal.setEditable(false);
		textFinal.setFont(new Font("Monospaced",Font.PLAIN,13));
		JScrollPane panelFinal = null;
		panelFinal = new JScrollPane(textFinal);
		panelFinal.setSize(360,330);
		panelFinal.setLocation(20,220);
		panelFinal.setAutoscrolls(true);
		add(panelFinal);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(ButtonPoslat))
			Threads.StartSenderThread(IpAdresa.getText(),Integer.parseInt(velkostVzorky.getText()),
					retazec.getText(), 0);
		
		if(e.getSource().equals(ButtonPoslatZly))
			Threads.StartSenderThread(IpAdresa.getText(),Integer.parseInt(velkostVzorky.getText()),
					retazec.getText(), 1);
		
		if(e.getSource().equals(ButtonPoslatSubor))
			Threads.StartSenderThread(IpAdresa.getText(),Integer.parseInt(velkostVzorky.getText()),
					retazec.getText(), 2);
		
		if(e.getSource().equals(ButtonPrijat))
			Threads.StartReceiverThread();
	}
}
