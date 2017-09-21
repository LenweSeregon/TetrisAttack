package mvc_model;

public class Selector {
	private int index_x;
	private int index_y;

	private int pos_x;
	private int pos_y;

	private int size_selector;
	private int size_per_selector;

	/**
	 * Constructeur de la classe qui représente le selecteur dans la grille
	 * 
	 * @param size_selector
	 *            la taille du selecteur en nombre de celulle
	 * @param idx_x
	 *            la position I de départ du selecteur
	 * @param idx_y
	 *            la position J de départ du selecteur
	 * @param size_per_selector
	 *            la taille en pixel de chaque selecteur
	 */
	public Selector(int size_selector, int idx_x, int idx_y, int size_per_selector) {
		this.index_x = idx_x;
		this.index_y = idx_y;
		this.pos_x = idx_y * size_per_selector;
		this.pos_y = idx_x * size_per_selector;
		this.size_selector = size_selector;
		this.size_per_selector = size_per_selector;
	}

	/**
	 * Méthode permettant de récupérer l'index X du selecteur
	 * 
	 * @return l'index X
	 */
	public int get_index_x() {
		return this.index_x;
	}

	/**
	 * Méthode permettant de récupérer l'index Y du selecteur
	 * 
	 * @return l'index Y
	 */
	public int get_index_y() {
		return this.index_y;
	}

	/**
	 * Méthode permettant de récupérer la position X du selecteur
	 * 
	 * @return la position X
	 */
	public int get_pos_x() {
		return this.pos_x;
	}

	/**
	 * Méthode permettant de récupérer la position Y du selecteur
	 * 
	 * @return la position Y
	 */
	public int get_pos_y() {
		return this.pos_y;
	}

	/**
	 * Méthode permettant de récupérer la taille en nombre de cellule du
	 * selecteur
	 * 
	 * @return la taille en nombre de cellule
	 */
	public int get_size_selector() {
		return this.size_selector;
	}

	/**
	 * Méthod permettant de récupérer le taille en pixel de chaque selecteur
	 * 
	 * @return la taille en pixel de chaque selecteur
	 */
	public int get_size_per_selector() {
		return this.size_per_selector;
	}

	/**
	 * Méthode permettant de choisir la position X en index du selecteur
	 * 
	 * @param index_x
	 *            l'index X
	 */
	public void set_index_x(int index_x) {
		this.index_x = index_x;
	}

	/**
	 * Méthode permettant de choisir la position Y en index du selecteur
	 * 
	 * @param index_y
	 *            l'index Y
	 */
	public void set_index_y(int index_y) {
		this.index_y = index_y;
	}

	/**
	 * Méthode permettant de choisir la position X en pixel du selecteur
	 * 
	 * @param pos_x
	 *            la position X
	 */
	public void set_pos_x(int pos_x) {
		this.pos_x = pos_x;
	}

	/**
	 * Méthode permettant de choisir la position Y en pixel du selecteur
	 * 
	 * @param pos_y
	 *            la position Y
	 */
	public void set_pos_y(int pos_y) {
		this.pos_y = pos_y;
	}
}
