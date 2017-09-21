package mvc_model;

import mvc_global_enums.Color;
import mvc_global_enums.Form;

public class Tile {

	private int index_x;
	private int index_y;
	private int size_tile;
	private int pos_x;
	private int pos_y;
	private Color color;
	private Form form;
	private boolean combinaison_detected;
	private int combinaison_number;

	private boolean strong_brick;
	private boolean is_brick;
	private int brick_number;
	private int width_brick;

	/**
	 * Constructeur de la classe qui représente une cellule dans notre grille
	 * 
	 * @param idx_x
	 *            l'index X de la cellule
	 * @param idx_y
	 *            l'index Y de la cellule
	 * @param size_tile
	 *            la taille de la cellule
	 * @param color
	 *            la couleur de la cellule
	 * @param form
	 *            la forme de la cellule
	 * @param is_brick
	 *            la cellule est-elle une brique ?
	 * @param strong_brick
	 *            la brique est elle une super brique
	 */
	public Tile(int idx_x, int idx_y, int size_tile, Color color, Form form, boolean is_brick, boolean strong_brick) {
		this.index_x = idx_x;
		this.index_y = idx_y;
		this.size_tile = size_tile;
		this.pos_x = index_y * size_tile;
		this.pos_y = index_x * size_tile;
		this.color = color;
		this.form = form;
		this.combinaison_detected = false;
		this.combinaison_number = -1;
		this.is_brick = is_brick;
		this.brick_number = -1;
		this.width_brick = -1;
		this.strong_brick = strong_brick;
	}

	/**
	 * Méthode permettant de choisir le numéro de brique et sa largeur
	 * 
	 * @param number
	 *            le numéro de brique
	 * @param width_brick
	 *            la largeur de brique
	 */
	public void set_brick_number(int number, int width_brick) {
		this.brick_number = number;
		this.width_brick = width_brick;
	}

	/**
	 * Méthod permettant de choisir si la cellule est une brique
	 * 
	 * @param brick
	 *            la cellule est une brique ?
	 */
	public void set_is_brick(boolean brick) {
		this.is_brick = brick;

		if (!brick) {
			brick_number = -1;
			width_brick = -1;
			strong_brick = false;
		}
	}

	/**
	 * Méthode permettant de choisir si la cellule est une brique forte
	 * 
	 * @param value
	 *            la cellule est telle une brique forte
	 */
	public void set_strong_brick(boolean value) {
		this.strong_brick = value;
	}

	/**
	 * Méthode permettant de savoir si la cellule est une brique forte
	 * 
	 * @return vrai si cellule brique forte, faux sinon
	 */
	public boolean get_strong_brick() {
		return this.strong_brick;
	}

	/**
	 * Méthode permettant de choisir l'index X en indice de la cellule
	 * 
	 * @param index_x
	 *            l'index X
	 */
	public void set_index_x(int index_x) {
		this.index_x = index_x;
	}

	/**
	 * Méthode permettant de choisir l'index Y en indice de la cellule
	 * 
	 * @param index_x
	 *            l'index Y
	 */
	public void set_index_y(int index_y) {
		this.index_y = index_y;
	}

	/**
	 * Méthode permettant de choisir la position X en pixel de la cellule
	 * 
	 * @param index_x
	 *            la position X
	 */
	public void set_pos_x(int pos_x) {
		this.pos_x = pos_x;
	}

	/**
	 * Méthode permettant de choisir la position Y en pixel de la cellule
	 * 
	 * @param index_x
	 *            la position Y
	 */
	public void set_pos_y(int pos_y) {
		this.pos_y = pos_y;
	}

	/**
	 * Méthode permettant de choisir si une combinaison est detecté via cette
	 * cellule
	 * 
	 * @param b
	 *            une combinaison est telle détecté ?
	 */
	public void set_combinaison_detected(boolean b) {
		this.combinaison_detected = b;
	}

	/**
	 * Méthode permettant de choisir si le numéro de combinaison detecté
	 * 
	 * @param number
	 *            le numéro de combinaison detecté
	 */
	public void set_combinaison_number(int number) {
		this.combinaison_number = number;
	}

	/**
	 * Méthode permettant de récupérer l'index X de la cellule
	 * 
	 * @return l'index X de la cellule
	 */
	public int get_index_x() {
		return this.index_x;
	}

	/**
	 * Méthode permettant de récupérer l'index Y de la cellule
	 * 
	 * @return l'index Y de la cellule
	 */
	public int get_index_y() {
		return this.index_y;
	}

	/**
	 * Méthode permettant de récupérer la position X de la cellule
	 * 
	 * @return la position X de la cellule
	 */
	public int get_pos_x() {
		return this.pos_x;
	}

	/**
	 * Méthode permettant de récupérer la position Y de la cellule
	 * 
	 * @return la position Y de la cellule
	 */
	public int get_pos_y() {
		return this.pos_y;
	}

	/**
	 * Méthode permettant de récupérer la taille de la cellule
	 * 
	 * @return la taille de la cellule
	 */
	public int get_size_cell() {
		return size_tile;
	}

	/**
	 * Méthode permettant de récupérer le taille de la brique accroché à la
	 * cellule
	 * 
	 * @return la taille de la brique
	 */
	public int get_width_brick() {
		return this.width_brick;
	}

	/**
	 * Méthode permettant de récupérer la combinaison detecté
	 * 
	 * @return la combinaison detecté
	 */
	public boolean get_combinaison_detected() {
		return this.combinaison_detected;
	}

	/**
	 * Méthode permettant de récupérer la couleur de la cellule
	 * 
	 * @return la couleur de la cellule sous forme d'enumeration
	 */
	public Color get_color() {
		return this.color;
	}

	/**
	 * Méthode permettant de récupérer la forme de la cellule
	 * 
	 * @return la forme de la cellule sous forme d'enumeration
	 */
	public Form get_form() {
		return this.form;
	}

	/**
	 * Méthode permettant de savoir si la cellule est une brique
	 * 
	 * @return
	 */
	public boolean get_is_brick() {
		return this.is_brick;
	}

	/**
	 * Méthode permettant de savoir le nombre de brique associé
	 * 
	 * @return le nombre de brique associé à la cellule
	 */
	public int get_brick_number() {
		return this.brick_number;
	}

	/**
	 * Méthode permettant de connaitre le numéro de combinaison de la cellule
	 * 
	 * @return le numéro de combinaison
	 */
	public int get_combinaison_number() {
		return this.combinaison_number;
	}

	/**
	 * Méthode permettant de savoir si deux cellules ont la meme représentation
	 * 
	 * @param c
	 *            la cellule a tester
	 * @return vrai si ils ont la meme couleur et la meme forme et que ce ne
	 *         sont pas des briques
	 */
	public boolean same_representation(Tile c) {
		if (this.is_brick || c.is_brick) {
			return false;
		} else {
			return (this.form == c.form && this.color == c.color);
		}
	}

}
