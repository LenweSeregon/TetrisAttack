package menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CreditsPan extends JPanel implements KeyListener {
	
	private Menu menu;
	
	public CreditsPan(Menu menu) {
		this.addKeyListener(this);
		this.menu = menu;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menu.affichePan(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g) {
		this.requestFocus();
		Image credit = null;
		try {
			credit = ImageIO.read(this.getClass().getResource("/ressources/Menu/credit_tetris.png")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(credit, 0, 0, null);
	}

}