package menuOption;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import AnimationEtSon.JouerSon;


public class OptionSon extends JPanel implements KeyListener{
	
	private MyJPan j;
	private int imgx = 410;
	private int imgy = 308;
	private static boolean onEnable;
	private static boolean offEnable;
	
	/*
	 * Son des touches
	 */
	private JouerSon deplacement;
	private Thread threadDeplacement;
	
	public OptionSon(MyJPan mp){
		j = mp;
		
		OptionSon.initSon();
		repaint();
		
		this.addKeyListener(this);
	}
	
	public static void initSon(){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File fichierXML = new File("sauvegarde.xml");
			Document xml = builder.parse(fichierXML);

			Element root = xml.getDocumentElement();
			Node joueur = root.getElementsByTagName("son").item(0);
			
			String on = "";
			String off = "";
			
			on=joueur.getChildNodes().item(0).getAttributes().getNamedItem("value").getNodeValue();
			
			if(on.equals("True")){
				onEnable = true;
				offEnable = false;
			}else{
				onEnable = false;
				offEnable = true;
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setSon(){
		OptionSon.initSon();
		if(!onEnable){
			JouerSon.couperSon();
		}
	}
	
	public void sauvXML(){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File fichierXML = new File("sauvegarde.xml");
			Document xml = builder.parse(fichierXML);

			Element root = xml.getDocumentElement();
			Node joueur = root.getElementsByTagName("son").item(0);
			
			if(onEnable){
				joueur.getChildNodes().item(0).getAttributes().getNamedItem("value").setNodeValue("True");
			}else{
				joueur.getChildNodes().item(0).getAttributes().getNamedItem("value").setNodeValue("False");
			}
			
			Transformer t;
			
			try {
				t = TransformerFactory.newInstance().newTransformer();
				StreamResult XML = new StreamResult("sauvegarde.xml");
				t.transform(new DOMSource(root), XML);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_UP){
				if(imgy<=308){
					imgy = 383;
				}else{
					imgy -= 75;
				}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
				if(imgy>=383){
					imgy = 308;
				}else{
					imgy += 75;
				}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			sauvXML();
			j.retourMenu();
		}else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if(imgy >= 383){
				onEnable=false;
				offEnable=true;
				JouerSon.couperSon();
			}else if (!onEnable){
				onEnable=true;
				offEnable=false;
				JouerSon.activerSon();
				j.menu.son();
			}
			repaint();
		}
		/*
		 * Son au d�placement
		 */
		this.deplacement = new JouerSon(
				"/ressources/Audio/audioDeplacement.wav", false);
		try {
			threadDeplacement.interrupt();
		} catch (NullPointerException ex) {
			//
		}
		threadDeplacement = new Thread(new Runnable() {
			public void run() {
				try {
					OptionSon.this.deplacement.play();
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		this.threadDeplacement.start();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g) {
		this.requestFocus();
		Image img = null;
		Image on = null;
		Image off = null;
		Image curseur = null;
		try {
			img = ImageIO.read(this.getClass().getResource("/ressources/Menu/menu.png")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
			on = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelFullscreenOn.png")).getScaledInstance(200, 50, Image.SCALE_DEFAULT);
			off = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelFullscreenOff.png")).getScaledInstance(200, 50, Image.SCALE_DEFAULT);
			curseur = ImageIO.read(this.getClass().getResource("/ressources/Menu/cursor.gif")).getScaledInstance(35,35, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, null);
		if(onEnable){
			g.drawImage(on, 450, 300, null);
			
			Color c = new Color(190,190,190,190);
			g.setColor(c);
			g.drawImage(off, 450, 375, null);
			g.fillRect(450, 300, 201, 51);
		}else{
			g.drawImage(on, 450, 300, null);
			Color c = new Color(190,190,190,190);
			g.setColor(c);
			g.drawImage(off, 450, 375, null);
			g.fillRect(450, 375, 200, 50);
		}
		g.drawImage(curseur, imgx, imgy, null);
	}
	
}
