package clock;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import AnimationEtSon.Anim;

public class Clock extends JPanel implements Runnable, ActionListener {

	private long start;
	private long clock;

	private String[] strFond = new String[16];
	private Anim animFond;

	private Image label = (new ImageIcon(this.getClass().getResource(
			"/ressources/Game/labelTime.png"))).getImage();
	private Image virgule = (new ImageIcon(this.getClass().getResource(
			"/ressources/Game/Chiffres/virgule.png"))).getImage();

	private String[] strChiffre = new String[10];

	public Clock(int width, int height) {
		this.start = System.currentTimeMillis();
		this.clock = 0;

		for (int i = 1; i <= 16; i++) {
			this.strFond[i - 1] = "/ressources/Game/backgroundTimer/frame-" + i
					+ ".png";
		}

		for (int i = 0; i < 10; i++) {
			strChiffre[i] = "/ressources/Game/Chiffres/chiffreRouge" + i
					+ ".png";
		}

		this.animFond = new Anim(strFond, 0, 0, width, height+100
				, 60, this);
		this.animFond.start();
	}

	public void update() {
		this.clock = System.currentTimeMillis() - this.start;
	}

	public void paintComponent(Graphics g2) {
		/* Black back */
		Graphics2D g = (Graphics2D) g2;

		this.animFond.paintComponent(g);
		g.drawImage(this.label, 10, 10, this.getWidth() - 10, 30, 0, 0,
				this.label.getWidth(this), this.label.getHeight(this), this);
		// g.setColor(java.awt.Color.white);
		// g.fillRect(0, 0, this.getWidth(), this.getHeight());

		/* Affiche temps */
		int Cmin = (int) (this.get_clock_as_second() / 60);
		int Dsec = (int) (((int) (this.get_clock_as_second() % 60)) / 10);
		int Usec = (int) (this.get_clock_as_second() % 10);

		/* Affiche min */
		Image min = (new ImageIcon(this.getClass()
				.getResource(strChiffre[Cmin]))).getImage();
		g.drawImage(min, 50, 100, 70, 130, 0, 0, min.getWidth(this),
				min.getHeight(this), this);

		/* Separateur */
		g.drawImage(virgule, 72, 100, 78, 115, 0, 0, virgule.getWidth(this),
				virgule.getHeight(this), this);

		/* Affiche dizaine sec */
		Image dizSec = (new ImageIcon(this.getClass().getResource(
				strChiffre[Dsec]))).getImage();
		g.drawImage(dizSec, 80, 100, 100, 130, 0, 0, min.getWidth(this),
				min.getHeight(this), this);

		/* Affiche unit sec */
		Image unitSec = (new ImageIcon(this.getClass().getResource(
				strChiffre[Usec]))).getImage();
		g.drawImage(unitSec, 105, 100, 125, 130, 0, 0, min.getWidth(this),
				min.getHeight(this), this);

	}

	public long get_clock_as_second() {
		return this.clock / 1000;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void run() {
		while (true) {
			this.update();
		}
	}
}
