package mvc_view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Garbage implements ImageObserver {

	private int posX, posY, hauteur, largeur;
	private boolean actif = true;
	private Image img;
	private JPanel p;

	public Garbage(String str, int posX, int posY, int hauteur, int largeur, JPanel p) {
		this.posX = posX;
		this.posY = posY;
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.p = p;
		img = (new ImageIcon(this.getClass().getResource(str))).getImage();
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void paintComponent(Graphics g) {
		Toolkit.getDefaultToolkit().sync();
		g.drawImage(this.img, this.posX, this.posY, this.hauteur + this.posX, this.largeur + this.posY, 0, 0,
				this.img.getWidth(this), this.img.getHeight(this), null);
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
