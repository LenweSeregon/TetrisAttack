package menuOption;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

import AnimationEtSon.JouerSon;

public class Joueur1 extends JPanel implements KeyListener{

	CaseTouche[] tab;
	//int[] touche;
	public static String[] touche = new String[5];
	private MyJPan j;
	private int imgx = 215;
	private int imgy = 252;
	private int indiceTab;
	private boolean entrerTouche;
	private Font f;
	
	/*
	 * Son des touches
	 */
	private JouerSon deplacement;
	private Thread threadDeplacement;
	
	public Joueur1(MyJPan mp){
		
		tab = new CaseTouche[5];
		Joueur1.initTableau();
		j = mp;
		f = new Font("Calibri",Font.BOLD, 30);
		
		indiceTab=0;
		entrerTouche=false;
		
		this.addKeyListener(this);
		
	}
	
	public static void initTableau(){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File fichierXML = new File("sauvegarde.xml");
			Document xml = builder.parse(fichierXML);

			Element root = xml.getDocumentElement();
			Node joueur = root.getElementsByTagName("un").item(0);
			
			for(int i=0;i<joueur.getChildNodes().getLength();i++){
				touche[i]=joueur.getChildNodes().item(i).getAttributes().getNamedItem("value").getNodeValue();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sauvXML(){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File fichierXML = new File("sauvegarde.xml");
			Document xml = builder.parse(fichierXML);

			Element root = xml.getDocumentElement();
			Node joueur = root.getElementsByTagName("un").item(0);
			
			for(int i=0;i<joueur.getChildNodes().getLength();i++){
				joueur.getChildNodes().item(i).getAttributes().getNamedItem("value").setNodeValue(touche[i]);
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
	
	public static String[] getTableau(){
		Joueur1.initTableau();
		return touche;
	}
	
	public boolean collision(KeyEvent e){
		String[] ct2 = j.getTabJ2();
		for(int j=0;j<ct2.length;j++){
			String s = ""+e.getKeyChar();
			if(s.equals(ct2[j])){
				return true;
			}
		}
		for(int k=0;k<touche.length;k++){
			String s = ""+e.getKeyChar();
			if(k!=indiceTab){
				if(s.equals(touche[k])){
					return true;
				}
			}else{
				continue;
			}
		}
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_UP){
			if(entrerTouche==false){
				if(indiceTab==0){
					indiceTab=4;
				}else{
					indiceTab--;
				}
				
				if(imgy<=252){
					imgy = 470;
				}else{
					imgy -= 55;
				}
			}else{
				if(collision(e)==false){
					String s = ""+e.getKeyChar();
					touche[indiceTab]=s;
				}
				entrerTouche=false;
			}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			if(entrerTouche==false){
				if(indiceTab==4){
					indiceTab=0;
				}else{
					indiceTab++;
				}
				
				if(imgy>=470){
					imgy = 252;
				}else{
					imgy += 55;
				}
			}else{
				if(collision(e)==false){
					String s = ""+e.getKeyChar();
					touche[indiceTab]=s;
				}
				entrerTouche=false;
			}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			sauvXML();
			j.retourMenu();
		}else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if(entrerTouche==false){
				entrerTouche=true;
			}else{
				if(collision(e)==false){
					String s = ""+e.getKeyChar();
					touche[indiceTab]=s;
				}
				entrerTouche=false;
			}
		}else{
			if(entrerTouche==true){
				if(collision(e)==false){
					String s = ""+e.getKeyChar();
					touche[indiceTab]=s;
				}
				entrerTouche=false;
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
					Joueur1.this.deplacement.play();
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
		Image cadrePlayer = null;
		Image curseur = null;
		try {
			img = ImageIO.read(this.getClass().getResource("/ressources/Menu/menu.png")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
			cadrePlayer = ImageIO.read(this.getClass().getResource("/ressources/Menu/player1.png")).getScaledInstance(600,400, Image.SCALE_DEFAULT);
			curseur = ImageIO.read(this.getClass().getResource("/ressources/Menu/cursor.gif")).getScaledInstance(35,35, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, null);
		g.drawImage(cadrePlayer, 250, 150, null);
		g.drawImage(curseur, imgx, imgy, null);
		g.setColor(new Color(70,131,244));
		g.setFont(f);
		g.drawString(touche[0].toUpperCase(), 700, 280);
		g.drawString(touche[1].toUpperCase(), 700, 335);
		g.drawString(touche[2].toUpperCase(), 700, 390);
		g.drawString(touche[3].toUpperCase(), 700, 445);
		g.drawString(touche[4].toUpperCase(), 700, 500);
	}
	
}
