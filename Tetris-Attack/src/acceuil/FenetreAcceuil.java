package acceuil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import AnimationEtSon.JouerSon;
import menu.Menu;

public class FenetreAcceuil extends JFrame implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanAnimation anim;

	private Thread t;
	private boolean replay;

	private JouerSon s;

	public FenetreAcceuil() {
		super("Tetris Attack");
		this.setSize(new Dimension(1100, 860));
		this.setBackground(Color.DARK_GRAY);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.anim = new PanAnimation();
		this.getContentPane().add(anim);

		this.s = new JouerSon("/ressources/Audio/audioAccueil.wav", true);

		this.pack();
		this.setVisible(true);

		this.addKeyListener(this);

		t = new Thread(new Runnable() {
			public void run() {
				try {
					FenetreAcceuil.this.s.play();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
		t.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void keyPressed(KeyEvent arg0) {
		this.dispose();
		this.s.stopper();
		new Menu();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
