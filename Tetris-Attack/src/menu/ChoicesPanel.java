package menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import AnimationEtSon.AnimMv;
import AnimationEtSon.JouerSon;

/**
 * ChoicesPanel est la classe qui correspond au panel d'accueil des options Il
 * permet de faire le choix entre toutes les options possibles dont celle de
 * lancer une partie
 * 
 */
@SuppressWarnings("serial")
public class ChoicesPanel extends JPanel implements KeyListener {

	private Menu menu;
	/*
	 * Choix des options
	 */
	private static final int NB_CHOIX = 5;
	private MenuChoices[] choices;
	/*
	 * Coordonnï¿½es de la flï¿½che
	 */
	private int xCursor = 215;
	private int yCursor = 257;
	/*
	 * Son des dï¿½placements
	 */
	private JouerSon deplacement;
	private Thread threadDeplacement;
	/*
	 * Animation Yoshi
	 */
	private String[] imgYoshi = new String[23];
	private AnimMv animYoshi;

	/**
	 * Constructeur de la classe ChoicesPanel Il permet d'initialiser le panel
	 * de "base" des options et de gï¿½rer les actions du clavier
	 * 
	 * @param menu
	 *            JFrame de base du jeu
	 */
	public ChoicesPanel(Menu menu) {
		this.setPreferredSize(new Dimension(1100, 860));
		this.setLayout(null);
		this.addKeyListener(this);

		this.menu = menu;
		this.choices = new MenuChoices[NB_CHOIX];

		initChoices();

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
	 * Mï¿½thode permettant d'initialiser les diffï¿½rents choix des options dans le
	 * tableau "choices"
	 */
	public void initChoices() {
		int x = 200;
		int y = 250;
		for (int i = 0; i < NB_CHOIX; i++) {
			MenuChoices c = new MenuChoices();
			c.setIndX(x);
			c.setIndY(y);
			this.choices[i] = c;
			y += 50;
		}
		this.choices[0].setName("labelPlay");
		this.choices[1].setName("labelHelp");
		this.choices[2].setName("labelOption");
		this.choices[3].setName("labelCredit");
		this.choices[4].setName("labelExitGame");
	}

	/**
	 * Mï¿½thodes qui permettent la gestion du clavier
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (yCursor <= 550 && yCursor >= 300) {
				yCursor -= 50;
				repaint();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (yCursor >= 250 && yCursor <= 450) {
				yCursor += 50;
				repaint();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (yCursor > ChoicesPanel.this.choices[0].getIndY() && yCursor < ChoicesPanel.this.choices[1].getIndY()) {
				menu.affichePan(1);
			} else if (yCursor > ChoicesPanel.this.choices[1].getIndY()
					&& yCursor < ChoicesPanel.this.choices[2].getIndY()) {
				menu.affichePan(2);
			} else if (yCursor > ChoicesPanel.this.choices[2].getIndY()
					&& yCursor < ChoicesPanel.this.choices[3].getIndY()) {
				menu.affichePan(3);
			} else if (yCursor > ChoicesPanel.this.choices[3].getIndY()
					&& yCursor < ChoicesPanel.this.choices[4].getIndY()) {
				menu.affichePan(4);
			} else if (yCursor > ChoicesPanel.this.choices[4].getIndY() && yCursor < 700) {
				menu.dispose();
				System.exit(0);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menu.dispose();
			System.exit(0);
		}
		/*
		 * Musique des déplacements
		 */
		ChoicesPanel.this.deplacement = new JouerSon("/ressources/Audio/audioDeplacement.wav", false);
		try {
			threadDeplacement.interrupt();
		} catch (NullPointerException ex) {
			//
		}
		threadDeplacement = new Thread(new Runnable() {
			public void run() {
				try {
					ChoicesPanel.this.deplacement.play();
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		ChoicesPanel.this.threadDeplacement.start();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Mï¿½thode paintComponent qui permet l'affichage de toutes les ressources de
	 * type images
	 */
	public void paintComponent(Graphics g) {
		this.requestFocus();
		Toolkit.getDefaultToolkit().sync();
		Image fond = null;
		Image select = null;
		Image fleche = null;
		Image choix1 = null;
		Image choix2 = null;
		Image choix3 = null;
		Image choix4 = null;
		Image choix5 = null;
		Image borderBlue = null;
		Image borderPink = null;
		try {
			fond = ImageIO.read(this.getClass().getResource("/ressources/Menu/menu.png"))
					.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
			select = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelSelect.png"))
					.getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			fleche = ImageIO.read(this.getClass().getResource("/ressources/Menu/cursor.gif")).getScaledInstance(35, 35,
					Image.SCALE_DEFAULT);
			choix1 = ImageIO.read(this.getClass().getResource("/ressources/Menu/" + choices[0].getName() + ".png"))
					.getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			choix2 = ImageIO.read(this.getClass().getResource("/ressources/Menu/" + choices[1].getName() + ".png"))
					.getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			choix3 = ImageIO.read(this.getClass().getResource("/ressources/Menu/" + choices[2].getName() + ".png"))
					.getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			choix4 = ImageIO.read(this.getClass().getResource("/ressources/Menu/" + choices[3].getName() + ".png"))
					.getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			choix5 = ImageIO.read(this.getClass().getResource("/ressources/Menu/" + choices[4].getName() + ".png"))
					.getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			borderBlue = ImageIO.read(this.getClass().getResource("/ressources/Menu/borderBlue.png"))
					.getScaledInstance(382, 276, Image.SCALE_DEFAULT);
			borderPink = ImageIO.read(this.getClass().getResource("/ressources/Menu/borderPink.png"))
					.getScaledInstance(382, 78, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(fond, 0, 0, null);
		g.drawImage(select, 100, 100, null);
		g.drawImage(choix1, choices[0].getIndX(), choices[0].getIndY(), null);
		g.drawImage(choix2, choices[1].getIndX(), choices[1].getIndY(), null);
		g.drawImage(choix3, choices[2].getIndX(), choices[2].getIndY(), null);
		g.drawImage(choix4, choices[3].getIndX(), choices[3].getIndY(), null);
		g.drawImage(choix5, choices[4].getIndX(), choices[4].getIndY(), null);
		g.drawImage(fleche, this.xCursor, this.yCursor, null);
		g.drawImage(borderBlue, 184, 237, null);
		g.drawImage(borderPink, 84, 86, null);
		this.animYoshi.paintComponent(g);
	}

}
