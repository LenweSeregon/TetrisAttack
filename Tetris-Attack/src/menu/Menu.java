package menu;

import javax.swing.JFrame;
import javax.swing.JPanel;

import AnimationEtSon.JouerSon;
import menuOption.MyJPan;
import menuOption.OptionSon;
import mvc_view.PlayPan;

/**
 * Menu est la classe de la fenetre principale du jeu qui s'affiche
 * apres l'animation de l'accueil
 * 
 */

public class Menu extends JFrame {

	/*
	 * Tableau des panels du menu
	 */
	private JPanel[] panels = new JPanel[5];

	/*
	 * Musique
	 */
	private JouerSon musique;
	private Thread threadMusique;
	
	/**
	 * Constructeur de la classe Menu
	 * Il permet d'initialiser la Frame, les panels qu'il contiendra
	 * et la musique du menu
	 * 
	 */
	public Menu() {
		this.setTitle("Tetris Attack");
		this.setSize(1100, 860);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setUndecorated(true);

		initPanels();
		affichePan(0);

		/*
		 * Musique
		 */
		this.son();
		OptionSon.setSon();
		

		this.pack();
		this.setVisible(true);
	}

	/**
	 * Methode permettant d'initialiser les panels des differents choix
	 * dans le options
	 */
	public void initPanels() {
		this.panels[0] = new ChoicesPanel(this);
		this.panels[1] = new PlayPan(this);
		this.panels[2] = new HowToPlayPan(this);
		this.panels[3] = new MyJPan(this);
		this.panels[4] = new CreditsPan(this);
	}

	/**
	 * Methode permettant de faire le rafraichissement de panel
	 * @param n
	 * 			Numero du panel dans le tableau "panels"
	 */
	public void affichePan(int n) {
		this.setContentPane(panels[n]);
		this.repaint();
		this.validate();
	}
	
	/**
	 * Methode permettant de lancer la musique qui correspond
	 * au menu
	 */
	public void son(){
		try {
			this.musique.stopper();			
		} catch (NullPointerException e){
			//
		}
		this.musique = new JouerSon("/ressources/Audio/audioMenu.wav", true);
		threadMusique = new Thread(new Runnable() {
			public void run() {
				try {
					Menu.this.musique.play();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
		threadMusique.start();
	}
	

}
