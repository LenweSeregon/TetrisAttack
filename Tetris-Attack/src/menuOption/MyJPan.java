package menuOption;

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
import menu.HowToPlayPan;
import menu.Menu;

public class MyJPan extends JPanel implements KeyListener{
	
	protected Menu menu;

	private Joueur1 j1;
	private Joueur2 j2;
	private OptionSon os;
	
	private String[] ct1;
	private String[] ct2;
	
	private int indicePanel;
	private int imgx = 215;
	private int imgy = 283;
	/*
	 * Son des touches
	 */
	private JouerSon deplacement;
	private Thread threadDeplacement;
	/*
	 * Animation Yoshi
	 */
	private String[] imgYoshi = new String[23];
	private AnimMv animYoshi;
	
	public MyJPan(Menu menu){
		j1 = new Joueur1(this);
		j2 = new Joueur2(this);
		os = new OptionSon(this);
		this.menu = menu;
		indicePanel=0;
		
		this.setLayout(null);
		this.addKeyListener(this);
		this.setFocusable(true);
		
		/*
		 * Animation Yoshi
		 */
		for (int i = 0; i < 23; i++){
			imgYoshi[i] = "/ressources/Menu/GifYoshi/yoshi" + i + ".png";
		}
		this.animYoshi = new AnimMv(imgYoshi, 630, 350, 298, 350, 50, this, 410, 350, 4);
		this.animYoshi.start();
	}
	
	public String[] getTabJ1(){
		return this.ct1=j1.getTableau();
	}
	
	public String[] getTabJ2(){
		return this.ct1=j2.getTableau();
	}
	
	public void retourMenu(){
		menu.setContentPane(this);
		menu.repaint();
		menu.revalidate();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_UP){
			if(indicePanel==0){
				indicePanel=2;
			}else{
				indicePanel--;
			}
			
			if(imgy<=283){
				imgy = 383;
			}else{
				imgy -= 50;
			}
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			if(indicePanel==2){
				indicePanel=0;
			}else{
				indicePanel++;
			}
			
			if(imgy>=383){
				imgy = 283;
			}else{
				imgy += 50;
			}
			
			repaint();
		}else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if(indicePanel==0){
				menu.setContentPane(j1);
				menu.repaint();
				menu.revalidate();
			}else if(indicePanel==1){
				menu.setContentPane(j2);
				menu.repaint();
				menu.revalidate();
			}else if(indicePanel==2){
				menu.setContentPane(os);
				menu.repaint();
				menu.revalidate();
			}
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			menu.affichePan(0);
		}
		/*
		 * Son au d�placement
		 */
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
					MyJPan.this.deplacement.play();
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		this.threadDeplacement.start();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g) {
		Toolkit.getDefaultToolkit().sync();
		this.requestFocus();
		Image select = null;
		Image img = null;
		Image img2 = null;
		Image img3 = null;
		Image img4 = null;
		Image img5 = null;
		Image option = null;
		Image borderPink = null;
		Image borderBlue = null;
		try {
			select = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelSelect.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			img = ImageIO.read(this.getClass().getResource("/ressources/Menu/menu.png")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
			img2 = ImageIO.read(this.getClass().getResource("/ressources/Menu/cursor.gif")).getScaledInstance(35,35, Image.SCALE_DEFAULT);
			img3 = ImageIO.read(this.getClass().getResource("/ressources/Menu/label1Player.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			img4 = ImageIO.read(this.getClass().getResource("/ressources/Menu/label2Player.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			img5 = ImageIO.read(this.getClass().getResource("/ressources/Menu/labelAudio.png")).getScaledInstance(350, 50, Image.SCALE_DEFAULT);
			borderPink = ImageIO.read(this.getClass().getResource("/ressources/Menu/borderPink.png")).getScaledInstance(382, 78, Image.SCALE_DEFAULT);
			borderBlue = ImageIO.read(this.getClass().getResource("/ressources/Menu/borderBlue.png")).getScaledInstance(380, 170, Image.SCALE_DEFAULT);
			option = ImageIO.read(this.getClass().getResource("/ressources/Menu/option2.png")).getScaledInstance(380, 60, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(img, 0, 0, null);
		g.drawImage(img3, 200, 275, null);
		g.drawImage(img4, 200, 325, null);
		g.drawImage(img5, 200, 375, null);
		g.drawImage(borderPink, 84, 86, null);
		g.drawImage(borderBlue, 185, 265, null);
		g.drawImage(select, 100, 100, null);
		g.drawImage(option, 156, 205, null);
		g.drawImage(img2, imgx, imgy, null);
		this.animYoshi.paintComponent(g);
	}
	
}
