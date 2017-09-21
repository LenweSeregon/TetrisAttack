package menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import AnimationEtSon.AnimMv;
import AnimationEtSon.JouerSon;

/**
 * HowToPlayPan est la classe qui correspond au panel de l'affichage
 * de l'aide du jeu
 *
 */
public class HowToPlayPan extends JPanel implements KeyListener {

	private Menu menu;
	/*
	 * Numéro de l'image de l'aide
	 */
	private int clic;
	private String name_photo;
	/*
	 * Son pour le déplacement
	 */
	private JouerSon deplacement;
	private Thread threadDeplacement;
	/*
	 * Animation Yoshi
	 */
	private String[] imgYoshi = new String[23];
	private AnimMv animYoshi;

	/**
	 * Constructeur de la classe HowToPlayPan
	 * Il permet d'initialiser la première image de l'aide
	 * @param menu
	 */
	public HowToPlayPan(Menu menu) {
		this.setPreferredSize(new Dimension(1100, 860));
		this.setLayout(null);
		this.addKeyListener(this);

		this.menu = menu;
		this.clic = 1;
		this.name_photo = "rules1";
		
		/*
		 * Animation Yoshi
		 */
		for (int i = 0; i < 23; i++){
			imgYoshi[i] = "/ressources/Menu/GifYoshi/yoshi" + i + ".png";
		}
		this.animYoshi = new AnimMv(imgYoshi, 630, 350, 298, 350, 50, this, 410, 350, 4);
		this.animYoshi.start();
	}

	/**
	 * Methodes qui permettent la gestion du clavier
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			if(HowToPlayPan.this.clic == 3){
				HowToPlayPan.this.clic = 3;
			}else{
				HowToPlayPan.this.clic += 1;
			}
			if (HowToPlayPan.this.clic == 1) {
				HowToPlayPan.this.name_photo = "rules1";
			}
			if (HowToPlayPan.this.clic == 2) {
				HowToPlayPan.this.name_photo = "rules2";
			}
			if (HowToPlayPan.this.clic == 3) {
				HowToPlayPan.this.name_photo = "rules3";
			}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			if(HowToPlayPan.this.clic == 1){
				HowToPlayPan.this.clic = 1;
			}else{
				HowToPlayPan.this.clic -= 1;
			}
			if (HowToPlayPan.this.clic == 1) {
				HowToPlayPan.this.name_photo = "rules1";
			}
			if (HowToPlayPan.this.clic == 2) {
				HowToPlayPan.this.name_photo = "rules2";
			}
			if (HowToPlayPan.this.clic == 3) {
				HowToPlayPan.this.name_photo = "rules3";
			}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			menu.affichePan(0);
		}
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
					HowToPlayPan.this.deplacement.play();
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		this.threadDeplacement.start();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Méthode qui permet l'affichage des ressources de type images
	 */
	public void paintComponent(Graphics g) {
		this.requestFocus();
		Toolkit.getDefaultToolkit().sync();
		Image fond = null;
		Image select = null;
		Image borderPink = null;
		Image howToPlay = null;
		Image border_blue = null;
		Image rules = null;
		try {
			fond = ImageIO.read(this.getClass().getResource("/ressources/Menu/menu.png")).getScaledInstance(this.getWidth(), this.getHeight(),Image.SCALE_DEFAULT);
			select = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelSelect.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			borderPink = ImageIO.read(this.getClass().getResource("/ressources/Menu/borderPink.png")).getScaledInstance(382, 78, Image.SCALE_DEFAULT);
			howToPlay = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelHelp.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			border_blue = ImageIO.read(this.getClass().getResource("/ressources/Menu/littleBorderBlue.png")).getScaledInstance(382, 77, Image.SCALE_DEFAULT);
			rules = ImageIO.read(this.getClass().getResource("/ressources/Menu/" + this.name_photo + ".png")).getScaledInstance(400, 400, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(fond, 0, 0, null);
		g.drawImage(select, 100, 100, null);
		g.drawImage(borderPink, 84, 86, null);
		g.drawImage(howToPlay, 156, 205, null);
		g.drawImage(border_blue, 140, 192, null);
		g.drawImage(rules, 200, 270, null);
		this.animYoshi.paintComponent(g);
	}
	
}
