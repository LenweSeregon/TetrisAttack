package mvc_view;

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
import menu.Menu;
import mvc_controller.New_Controller;
import mvc_global_enums.Grid_position;
import mvc_model.Grid;

/**
 * PlayPan est la classe qui correspond au panel de lancement de partie
 * On a le choix entre jouer seul ou à 2
 *
 */
public class PlayPan extends JPanel implements KeyListener {

	private Menu menu;
	/*
	 * Coordonnées de la flèche
	 */
	private int xCursor = 213;
	private int yCursor = 300;
	/*
	 * Musique
	 */
	private JouerSon deplacement;
	private Thread threadDeplacement;
	/*
	 * Animation Yoshi
	 */
	private String[] imgYoshi = new String[23];
	private AnimMv animYoshi;

	/**
	 * Constructeur de la classe PlayPan
	 * @param menu
	 * 				JFrame de base du jeu
	 */
	public PlayPan(Menu menu) {
		this.setPreferredSize(new Dimension(1100, 860));
		this.setLayout(null);
		this.addKeyListener(this);
		
		this.menu = menu;
		
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
	 * Méthode permettant de lancer le panel d'une partie à un joueur
	 */
	public void mod_1p() {
		Grid model_left = new Grid(13, 6, 68, Grid_position.LEFT);
		New_Controller ctrl_left = new New_Controller(model_left, "LEFT");

		Board_view view = new Board_view(12, 6, 68, ctrl_left, null);
		model_left.add_observer(view);

		ctrl_left.init_grid();
		ctrl_left.bind_controller(null);

		new Thread(ctrl_left).start();
		menu.setContentPane(view);
		view.setFocusable(true);
		view.requestFocusInWindow();
		menu.repaint();
		menu.revalidate();
	}

	/**
	 * Méthode permettant de lancer le panel d'une partie à 2 joueurs
	 */
	public void mod_1v1() {
		Grid model_left = new Grid(13, 6, 68, Grid_position.LEFT);
		New_Controller ctrl_left = new New_Controller(model_left, "LEFT");

		Grid model_right = new Grid(13, 6, 68, Grid_position.RIGHT);
		New_Controller ctrl_right = new New_Controller(model_right, "RIGHT");

		Board_view view = new Board_view(12, 6, 68, ctrl_left, ctrl_right);

		model_left.add_observer(view);
		model_right.add_observer(view);

		ctrl_left.init_grid();
		ctrl_right.init_grid();

		ctrl_left.bind_controller(ctrl_right);
		ctrl_right.bind_controller(ctrl_left);

		new Thread(ctrl_left).start();
		new Thread(ctrl_right).start();
		menu.setContentPane(view);
		view.setFocusable(true);
		view.requestFocusInWindow();
		menu.repaint();
		menu.revalidate();
	}

	/**
	 * Méthodes qui permettent la gestion du clavier
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_UP){
			if(this.yCursor > 300){
				this.yCursor -= 50;
				repaint();
			}
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			if(this.yCursor < 350){
				this.yCursor += 50;
				repaint();
			}
		}else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if(this.yCursor == 300){
				mod_1p();
			}else if(this.yCursor == 350){
				mod_1v1();
			}
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			menu.affichePan(0);
		}
		this.deplacement = new JouerSon("/ressources/Audio/audioDeplacement.wav", false);
		try {
			threadDeplacement.interrupt();
		} catch (NullPointerException ex) {
			//
		}
		threadDeplacement = new Thread(new Runnable() {
			public void run() {
				try {
					PlayPan.this.deplacement.play();
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
	 * Méthode qui permet l'affichage des ressources de types image
	 */
	public void paintComponent(Graphics g) {
		this.requestFocus();
		Toolkit.getDefaultToolkit().sync();
		Image fond = null;
		Image fleche = null;
		Image select = null;
		Image borderPink = null;
		Image labelPlay = null;
		Image border_blue = null;
		Image onePlayer = null;
		Image twoPlayers = null;
		Image border_blue2 = null;
		try {
			fond = ImageIO.read(this.getClass().getResource("/ressources/Menu/menu.png")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
			fleche = ImageIO.read(this.getClass().getResource("/ressources/Menu/cursor.gif")).getScaledInstance(35, 35, Image.SCALE_DEFAULT);
			select = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelSelect.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			borderPink = ImageIO.read(this.getClass().getResource("/ressources/Menu/borderPink.png")).getScaledInstance(382, 78, Image.SCALE_DEFAULT);
			labelPlay = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelPlay.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			border_blue = ImageIO.read(this.getClass().getResource("/ressources/Menu/littleBorderBlue.png")).getScaledInstance(382, 77, Image.SCALE_DEFAULT);
			onePlayer = ImageIO.read(this.getClass().getResource("/ressources/Menu/label1Player.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			twoPlayers = ImageIO.read(this.getClass().getResource("/ressources/Menu/label2Player.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			border_blue2 = ImageIO.read(this.getClass().getResource("/ressources/Menu/littleBorderBlue.png")).getScaledInstance(382, 147, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(fond, 0, 0, null);
		g.drawImage(select, 100, 100, null);
		g.drawImage(borderPink, 84, 86, null);
		g.drawImage(labelPlay, 156, 205, null);
		g.drawImage(border_blue, 140, 192, null);
		g.drawImage(onePlayer, 200, 293, null);
		g.drawImage(twoPlayers, 200, 343, null);
		g.drawImage(border_blue2, 184, 269, null);
		g.drawImage(fleche, this.xCursor, this.yCursor, null);
		this.animYoshi.paintComponent(g);
	}

}
