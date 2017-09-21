package AnimationEtSon;

import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class AnimMv extends Anim {

	private int posHMin, posHMax, mvt;
	
	public AnimMv(String[] str, int posX, int posY, int largeur, int hauteur, int freq, JPanel p, int posHMax, int posHMin, int mvt) {
		super(str, posX, posY, hauteur, largeur, freq, p);
		// TODO Auto-generated constructor stub
		this.posHMax = posHMax;
		this.posHMin = posHMin;
		this.mvt = mvt;
	}
	
	public void paintComponent(Graphics g) {
		Toolkit.getDefaultToolkit().sync();
		g.drawImage(this.img[this.ind], this.posX, this.posY, this.hauteur + this.posX, this.largeur + this.posY, 0, 0,
				this.img[this.ind].getWidth(this), this.img[this.ind].getHeight(this), null);
	}
	
	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (this.actif) {
			try {
				this.ind = (this.ind + 1) % this.img.length;
				this.p.repaint();
				Thread.sleep(this.freq);
				if (this.mvt > 0){
					if (this.posY < this.posHMax) {
						this.posY += this.mvt;
					} else  {
						this.mvt = -this.mvt;
						this.posY += this.mvt;
					}
				} else {
					if (this.posY > this.posHMin) {
						this.posY += this.mvt;
					} else  {
						this.mvt = -this.mvt;
						this.posY += this.mvt;
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}

}
