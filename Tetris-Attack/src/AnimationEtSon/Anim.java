package AnimationEtSon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Anim extends Thread implements ImageObserver {

	protected int posX, posY, hauteur, largeur, ind, freq;
	protected boolean actif = true;
	protected Image[] img;
	protected JPanel p;

	public Anim(String[] str, int posX, int posY, int largeur, int hauteur, int freq, JPanel p) {
		this.posX = posX;
		this.posY = posY;
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.freq = freq;
		this.p = p;
		this.ind = 0;
		img = new Image[str.length];
		for (int i = 0; i < str.length; i++) {
			this.img[i] = (new ImageIcon(this.getClass().getResource(str[i]))).getImage();
		}
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void changerImages(String[] newStr){
		this.img = new Image[newStr.length];
		for (int i = 0; i < newStr.length; i++) {
			this.img[i] = (new ImageIcon(this.getClass().getResource(newStr[i]))).getImage();
		}
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void paintComponent(Graphics g) {
		Toolkit.getDefaultToolkit().sync();
		g.drawImage(this.img[this.ind], this.posX, this.posY, this.hauteur + this.posX, this.largeur + this.posY, 0, 0,
				this.img[this.ind].getWidth(this), this.img[this.ind].getHeight(this), null);
	}

	public void stopper() {
		this.actif = false;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public Image[] getImg() {
		return img;
	}

	public void setImg(Image[] img) {
		this.img = img;
	}

	public JPanel getP() {
		return p;
	}

	public void setP(JPanel p) {
		this.p = p;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (actif) {
			try {
				this.ind = (this.ind + 1) % this.img.length;
				this.p.repaint();
				Thread.sleep(this.freq);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

}
