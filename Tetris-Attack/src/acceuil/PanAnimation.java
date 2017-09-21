package acceuil;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import AnimationEtSon.Anim;

public class PanAnimation extends JPanel{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String[] imgTouche = {"/ressources/Accueil/pushKey1.png","/ressources/Accueil/pushKey2.png"};
	private String[] imgFrame = new String[132];
	
	private Anim animTouche;
	private Anim animFrame;
	
	private Image fond;
	
	public PanAnimation(){
		this.setPreferredSize(new Dimension(1100,860));
		this.setSize(new Dimension(1100,860));
		
		for (int i = 0; i <= 131; i++){
			imgFrame[i] = "/ressources/Accueil/fond/frame_"+i+".png";
		}
		
		this.animTouche = new Anim(imgTouche, (this.getWidth()/2)-200, this.getHeight()-100, 60, 400, 400, this);
		this.animFrame = new Anim(imgFrame, 0, 0, this.getHeight(), this.getWidth(), 30, this);
		
		this.animTouche.start();
		this.animFrame.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Toolkit.getDefaultToolkit().sync();

		this.animFrame.paintComponent(g);
		this.animTouche.paintComponent(g);
	}
	
}
