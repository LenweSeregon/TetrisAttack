package mvc_view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Selector_view extends JComponent {

	private int pos_x;
	private int pos_y;
	private int size_selector;
	private int size_per_selector;

	/**
	 * Constructeur de la classe qui représente la vue du selecteur dans le jeu
	 * 
	 * @param pos_x
	 *            la position X du selecteur
	 * @param pos_y
	 *            la position Y du selecteur
	 * @param size_per_selector
	 *            la taille par selecteur
	 * @param size_selector
	 *            la taille du selecteur
	 */
	public Selector_view(int pos_x, int pos_y, int size_per_selector, int size_selector) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.size_selector = size_selector;
		this.size_per_selector = size_per_selector;
	}

	/**
	 * Méthode permettant de choisir la position X de la cellule
	 * 
	 * @param pos_x
	 *            la position X de la cellule
	 */
	public void set_pos_x(int pos_x) {
		this.pos_x = pos_x;
	}

	/**
	 * Méthode permettant de choisir la position Y de la cellule
	 * 
	 * @param pos_y
	 *            la position Y de la cellule
	 */
	public void set_pos_y(int pos_y) {
		this.pos_y = pos_y;
	}

	/**
	 * Méthode permettant de récupérer la position X du curseur
	 * 
	 * @return la position X du curseur
	 */
	public int get_pos_x() {
		return this.pos_x;
	}

	/**
	 * Méthode permettant de récupérer la position Y du curseur
	 * 
	 * @return la position Y du curseur
	 */
	public int get_pos_y() {
		return this.pos_y;
	}

	/**
	 * Méthode permettant de récupérer la taille des curseurs
	 * 
	 * @return la taille des curseurs
	 */
	public int get_size_per_selector() {
		return this.size_per_selector;
	}

	@Override
	public void paintComponent(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setColor(java.awt.Color.WHITE);
		g.setStroke(new BasicStroke(3));
		for (int i = 0; i < size_selector; i++) {
			g.drawRect(pos_x + (i * size_per_selector), pos_y, size_per_selector, size_per_selector);
		}
		g.setStroke(new BasicStroke(1));
	}

}
