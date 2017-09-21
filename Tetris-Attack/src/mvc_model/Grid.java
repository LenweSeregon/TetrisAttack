package mvc_model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import mvc_controller.New_Controller;
import mvc_global_enums.Color;
import mvc_global_enums.Direction;
import mvc_global_enums.Form;
import mvc_global_enums.Grid_position;
import pattern_observer.Observable;
import pattern_observer.Observer;

public class Grid implements Observable {

	private int nb_line;
	private int nb_column;
	private int size_tile;
	private Grid_position grid_position;

	private Tile[][] tiles;
	private Selector selector;
	private int y_acc_ref;
	private int score;

	private boolean gray_detected;

	private ArrayList<Observer> observers;
	private int current_combinaison_detected;
	private int current_brick_pack;

	private New_Controller ref_versus_ctrl;

	private int width_brick;
	private int nb_brick;

	/**
	 * Constructeur de la classe qui représente le modele du jeu. C'est ce
	 * modéle qui va procéder aux différents calculs et suppression d'element
	 * 
	 * @param nb_line
	 *            le nombre de ligne dans la grille
	 * @param nb_column
	 *            le nombre de colonne dans la grille
	 * @param size_tile
	 *            la taille des cellules
	 * @param position
	 *            la position de la grille par rapport au plateau
	 */
	public Grid(int nb_line, int nb_column, int size_tile, Grid_position position) {
		this.nb_line = nb_line;
		this.nb_column = nb_column;
		this.size_tile = size_tile;
		this.grid_position = position;
		this.current_combinaison_detected = 0;
		this.ref_versus_ctrl = null;
		y_acc_ref = 0;
		this.score = 0;
		this.gray_detected = false;

		this.width_brick = -1;
		this.nb_brick = -1;

		this.tiles = new Tile[nb_line][nb_column];
		this.selector = new Selector(2, 7, 2, size_tile);

		this.observers = new ArrayList<Observer>();
	}

	/**
	 * Permet de récupérer le score associé à la grille
	 * 
	 * @return le score associé à la grille
	 */
	public int get_score() {
		return this.score;
	}

	/**
	 * Permet de connaitre la position ou l'on peut insérer une brique
	 * 
	 * @param width
	 *            la largeur de la brique
	 * @return une position si elle existe dans un objet Dimension, null sinon
	 */
	public Dimension get_position_possible_insertion(int width) {

		for (int i = nb_line - 2; i >= 0; i--) {
			for (int j = 0; j < nb_column; j++) {
				if (j + width <= nb_column) {
					boolean done = true;
					for (int k = j; k < j + width; k++) {
						if (this.tiles[i][k] != null) {
							done = false;
							break;
						}
					}
					if (done) {
						return new Dimension(i, j);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Méthode permettant d'insrer une brique via une position
	 * 
	 * @param i
	 *            la position en I de la brique
	 * @param j
	 *            la position en J de la brique
	 * @param width
	 *            la largeur de la brique
	 * @param strong_brick
	 *            est ce que la brique est forte
	 */
	public void insert_brick_to(int i, int j, int width, boolean strong_brick) {
		for (int z = j; z < j + width; z++) {
			this.tiles[i][z] = get_random_tile_at_pos(i, z, true, strong_brick);
			this.tiles[i][z].set_brick_number(current_brick_pack, width);
			this.tiles[i][z].set_pos_y(this.tiles[i][z].get_pos_y() - this.y_acc_ref);
			this.tiles[i][z].set_strong_brick(true);
		}
		current_brick_pack++;
	}

	/**
	 * Méthode permettant d'initialiser la grille avec des lignes déjà en place.
	 * Le nombre de lignes à construire est fournis en paramètre
	 * 
	 * @param x_line
	 *            le nombre de ligne qui compose la grille une fois qu'elle est
	 *            initialisé
	 */
	public void init_tiles(int x_line) {
		if (x_line >= this.nb_line - 1) {
			System.err.println("Erreur, nombre de ligne trop haut");
		} else {
			for (int i = this.nb_line - 1; i >= this.nb_line - x_line; i--) {
				for (int j = 0; j < nb_column; j++) {
					tiles[i][j] = get_random_tile_at_pos(i, j, false, false);
				}
			}
		}
	}

	/**
	 * Méthode permettant de générer la grille de départ en s'assurer que celle
	 * ci ne contient pas de combinaison
	 */
	public void generate_starting_grid() {
		boolean nice_generation = false;
		this.init_tiles(7);
		while (!nice_generation) {

			boolean has_combinaison = this.check_all_combinaison();

			if (!has_combinaison) {
				nice_generation = true;
			} else {
				for (int i = 6; i < this.nb_line - 1; i++) {
					for (int j = 0; j < this.nb_column; j++) {
						if (this.tiles[i][j].get_combinaison_detected()) {
							this.tiles[i][j] = this.get_random_tile_at_pos(i, j, false, false);
						}
					}
				}
			}
		}
		this.erase_combinaison();
		untick_combinaison_detected_on_all();
		this.width_brick = -1;
		this.nb_brick = -1;
	}

	/**
	 * Cette méthode générale une ligne qui va apparaitre à la position la plus
	 * basse possible. C'est à dire que cette ligne sera hors d'indice jusqu'à
	 * ce que ça position Y soit égale à 0
	 */
	public void generate_line() {
		/* Line generation */

		for (int i = 0; i < this.nb_column; i++) {
			tiles[nb_line - 1][i] = get_random_tile_at_pos(nb_line - 1, i, false, false);
		}
	}

	/**
	 * Méthode permettant de créer une cellule aléatoirement
	 * 
	 * @param index_x
	 *            la position I de la tile
	 * @param index_y
	 *            la position J de la tile
	 * @param is_brick
	 *            est ce que cette cellule doit etre une brique
	 * @return la cellule générée
	 */
	private Tile get_random_tile_at_pos(int index_x, int index_y, boolean is_brick, boolean strong_brick) {

		Random rand = new Random();
		int rand_value = rand.nextInt((24) - 0) + 0;

		if (rand_value == 0 || rand_value == 1 || rand_value == 2) {
			return new Tile(index_x, index_y, size_tile, Color.RED, Form.HEART, is_brick, strong_brick);
		} else if (rand_value == 3 || rand_value == 4 || rand_value == 5) {
			return new Tile(index_x, index_y, size_tile, Color.BLUE, Form.CIRCLE, is_brick, strong_brick);
		} else if (rand_value == 6 || rand_value == 7 || rand_value == 8) {
			return new Tile(index_x, index_y, size_tile, Color.MAUVE, Form.DIAMOND, is_brick, strong_brick);
		} else if (rand_value == 9 || rand_value == 10 || rand_value == 11) {
			return new Tile(index_x, index_y, size_tile, Color.GREEN, Form.TRIANGLE, is_brick, strong_brick);
		} else if (rand_value == 12 || rand_value == 13 || rand_value == 14) {
			return new Tile(index_x, index_y, size_tile, Color.YELLOW, Form.CIRCLE, is_brick, strong_brick);
		} else {
			return new Tile(index_x, index_y, size_tile, Color.GRAY, Form.EXCLAMATION, is_brick, strong_brick);
		}
	}

	/**
	 * Méthode permettant de savoir si l'on peut bouger le cursur dans une
	 * direction
	 * 
	 * @param direction
	 *            la direction ou l'on veut bouger le curseur
	 * @return vrai si on peut bouger le curseur, faut sinon
	 */
	public boolean can_move_selector(Direction direction) {

		boolean can_move = true;
		switch (direction) {
		case UP:
			if (this.selector.get_index_x() == 1) {
				can_move = false;
			}
			break;
		case DOWN:
			if (this.selector.get_index_x() >= this.nb_line - 2)
				can_move = false;
			break;
		case EAST:
			if (this.selector.get_index_y() + (this.selector.get_size_selector() - 1) >= this.nb_column - 1)
				can_move = false;
			break;
		case WEST:
			if (this.selector.get_index_y() == 0)
				can_move = false;
			break;
		}
		return can_move;
	}

	/**
	 * Méthode permettant de bouger le curseur vers une direction
	 * 
	 * @param direction
	 *            la direction ou l'on bouge le curseur
	 */
	public void move_selector(Direction direction) {

		if (can_move_selector(direction)) {
			switch (direction) {
			case UP:
				this.selector.set_index_x(this.selector.get_index_x() - 1);
				this.selector.set_pos_y(this.selector.get_pos_y() - this.selector.get_size_per_selector());
				break;
			case DOWN:
				this.selector.set_index_x(this.selector.get_index_x() + 1);
				this.selector.set_pos_y(this.selector.get_pos_y() + this.selector.get_size_per_selector());
				break;
			case EAST:
				this.selector.set_index_y(this.selector.get_index_y() + 1);
				this.selector.set_pos_x(this.selector.get_pos_x() + this.selector.get_size_per_selector());
				break;
			case WEST:
				this.selector.set_index_y(this.selector.get_index_y() - 1);
				this.selector.set_pos_x(this.selector.get_pos_x() - this.selector.get_size_per_selector());
				break;
			}
		}
	}

	/**
	 * Méthode permettant de savoir si la grille est en étant de défaite
	 * 
	 * @return vrai si la grille est en état de défaite, faux sinon
	 */
	public boolean has_lose() {
		for (int i = 0; i < this.nb_column; i++) {
			if (this.tiles[0][i] != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 16 Permet de récupérer le nombre de ligne dans la grille
	 * 
	 * @return le nombre de ligne
	 */
	public int get_nb_line() {
		return this.nb_line;
	}

	/**
	 * Permet de récupérer le nombre de colonne dans la grille
	 * 
	 * @return le nombre de colonne
	 */
	public int get_nb_column() {
		return this.nb_column;
	}

	/**
	 * Permet de récupérer la taille des cellules dans la grille
	 * 
	 * @return la taille des cellules
	 */
	public int get_size_tile() {
		return this.size_tile;
	}

	/**
	 * Permet de récupérer la position de la grille par rapport au plateau
	 * 
	 * @return la position de la grille
	 */
	public Grid_position get_position() {
		return this.grid_position;
	}

	/**
	 * Permet de récupérer la grille entiére
	 * 
	 * @return la grille
	 */
	public Tile[][] get_grid() {
		return this.tiles;
	}

	/**
	 * Permet de récupérer le curseur
	 * 
	 * @return le curseur
	 */
	public Selector get_selection() {
		return this.selector;
	}

	/**
	 * Permet de choisir la valeur de position d'accélération en y
	 * 
	 * @param y
	 *            la valeur y
	 */
	public void set_y_acc_ref(int y) {
		this.y_acc_ref = y;
	}

	/**
	 * Permet de faire monter toutes les cellules de 1 indice vers le haut
	 * 
	 * @param upper
	 *            la position Y du up
	 * @param actual_accumulator
	 *            l'accumulateur actuel en Y
	 */
	public void all_up_tile(int upper, int actual_accumulator) {
		for (int i = 0; i < nb_line; i++) {
			for (int j = 0; j < nb_column; j++) {
				if (tiles[i][j] != null) {
					tiles[i][j].set_pos_y(tiles[i][j].get_pos_y() - upper);
				}
			}
		}

		if (actual_accumulator >= size_tile) {
			for (int i = 1; i < nb_line; i++) {
				for (int j = 0; j < nb_column; j++) {
					if (tiles[i][j] != null) {
						tiles[i - 1][j] = tiles[i][j];
						tiles[i - 1][j].set_index_y(i - 1);
						tiles[i - 1][j].set_pos_y(tiles[i - 1][j].get_pos_y());
					}
				}
			}
			for (int j = 0; j < nb_column; j++) {
				tiles[nb_line - 1][j] = null;
			}

			this.generate_line();
		}
	}

	/**
	 * Méthode permettant de faire monter le curseur de 1 index vers le haut
	 * 
	 * @param upper
	 *            la position Y du up
	 * @param actual_accumulator
	 *            l'accumulateur actuel en Y
	 */
	public void up_selector(int upper, int actual_accumulator) {
		this.selector.set_pos_y(this.selector.get_pos_y() - upper);

		if (actual_accumulator >= size_tile) {
			this.selector.set_index_x(this.selector.get_index_x() - 1);
		}
	}

	/**
	 * Méthode permettant de swap les positions depuis la position du curseur
	 */
	public void swap_position_from_selector() {
		int line = this.selector.get_index_x();
		int y_1 = this.selector.get_index_y();
		int y_2 = this.selector.get_index_y() + 1;
		if (tiles[line][y_1] != null && tiles[line][y_1].get_is_brick()) {
			return;
		}
		if (tiles[line][y_2] != null && tiles[line][y_2].get_is_brick()) {
			return;
		}

		swap_position(line, y_1, line, y_2);
	}

	/**
	 * Méthode permettant de swap 2 cellules via leurs identique
	 * 
	 * @param idx_x_1
	 *            I premiére cellule
	 * @param idx_y_1
	 *            J premiére cellule
	 * @param idx_x_2
	 *            I deuxiéme cellule
	 * @param idx_y_2
	 *            J deuxiéme cellule
	 */
	public void swap_position(int idx_x_1, int idx_y_1, int idx_x_2, int idx_y_2) {

		Tile first_tile = this.tiles[idx_x_1][idx_y_1];
		Tile second_tile = this.tiles[idx_x_2][idx_y_2];

		Tile new_first = null;
		Tile new_second = null;

		if (first_tile == null && second_tile == null) {
			// Nothing to do
		} else if (first_tile == null && second_tile != null) {

			new_first = new Tile(idx_x_1, idx_y_1, size_tile, second_tile.get_color(), second_tile.get_form(),
					second_tile.get_is_brick(), second_tile.get_strong_brick());
			new_first.set_pos_x(idx_y_1 * size_tile);
			new_first.set_pos_y(idx_x_1 * size_tile - y_acc_ref);
			new_first.set_brick_number(second_tile.get_brick_number(), second_tile.get_width_brick());
			this.tiles[idx_x_1][idx_y_1] = new_first;
			this.tiles[idx_x_2][idx_y_2] = null;
		} else if (first_tile != null && second_tile == null) {
			new_second = new Tile(idx_x_2, idx_y_2, size_tile, first_tile.get_color(), first_tile.get_form(),
					first_tile.get_is_brick(), first_tile.get_strong_brick());
			new_second.set_pos_x(idx_y_2 * size_tile);
			new_second.set_pos_y(idx_x_2 * size_tile - y_acc_ref);
			new_second.set_brick_number(first_tile.get_brick_number(), first_tile.get_width_brick());
			this.tiles[idx_x_1][idx_y_1] = null;
			this.tiles[idx_x_2][idx_y_2] = new_second;
		} else {
			new_second = new Tile(second_tile.get_index_x(), second_tile.get_index_y(), size_tile,
					first_tile.get_color(), first_tile.get_form(), false, false);

			new_first = new Tile(first_tile.get_index_x(), first_tile.get_index_y(), size_tile, second_tile.get_color(),
					second_tile.get_form(), false, false);

			this.tiles[idx_x_1][idx_y_1] = new_first;
			this.tiles[idx_x_2][idx_y_2] = new_second;
			this.tiles[idx_x_1][idx_y_1].set_pos_x(first_tile.get_pos_x());
			this.tiles[idx_x_1][idx_y_1].set_pos_y(first_tile.get_pos_y());
			this.tiles[idx_x_1][idx_y_1].set_is_brick(first_tile.get_is_brick());
			this.tiles[idx_x_1][idx_y_1].set_strong_brick(first_tile.get_strong_brick());
			this.tiles[idx_x_2][idx_y_2].set_pos_x(second_tile.get_pos_x());
			this.tiles[idx_x_2][idx_y_2].set_pos_y(second_tile.get_pos_y());
			this.tiles[idx_x_2][idx_y_2].set_is_brick(second_tile.get_is_brick());
			this.tiles[idx_x_2][idx_y_2].set_strong_brick(second_tile.get_strong_brick());
		}
	}

	/**
	 * Une routine qu iva tester les combinaisons, les effacer et créer les
	 * combos si besoin
	 */
	public void routine() {
		gray_detected = false;
		apply_physic_on_all();
		int it = 0;
		int sum = 0;
		while (check_all_combinaison()) {
			it++;
			sum += erase_combinaison();
		}

		if (gray_detected) {
			if (it > 1) {
				score += sum * it;
				width_brick = 6;
				nb_brick = it;

				new Thread(new Runnable() {
					@Override
					public void run() {
						int nb = nb_brick;
						int wid = width_brick;
						notify_blocs_coming_from(get_position(), get_width_brick());
						nb_brick = -1;
						width_brick = -1;
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (ref_versus_ctrl != null) {
							ref_versus_ctrl.receive_brick(wid, nb, true);
						}
					}
				}).start();
			} else if (it == 1) {
				score += sum;
			}
		} else {
			if (it > 1) {
				score += sum * it;
				width_brick = 6;
				nb_brick = it - 1;

				new Thread(new Runnable() {

					@Override
					public void run() {
						int nb = nb_brick;
						int wid = width_brick;
						notify_blocs_coming_from(get_position(), get_width_brick());
						nb_brick = -1;
						width_brick = -1;
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (ref_versus_ctrl != null) {
							ref_versus_ctrl.receive_brick(wid, nb, false);
						}
					}

				}).start();
			} else if (it == 1) {
				score += sum;
			}
		}

	}

	/**
	 * Méthode permettant d'appliquer la physique a tout les elements
	 */
	public void apply_physics() {
		this.apply_physic_on_all();
		this.check_all_combinaison();
		this.erase_combinaison();
		this.check_all_combinaison();
		this.erase_combinaison();
	}

	/**
	 * Méthode permettant d'appliquer la physique a tout les elements
	 */
	public void apply_physic_on_all() {
		for (int i = this.nb_line - 2; i >= 0; i--) {
			for (int j = 0; j < this.nb_column; j++) {
				if (tiles[i][j] != null) {
					apply_physic(i, j);
				}
			}
		}
	}

	/**
	 * Méthode permettant d'appliquer la physique à une cellule donnée
	 * 
	 * @param i
	 *            la position I de la cellule
	 * @param j
	 *            la position J de la cellule
	 */
	private void apply_physic(int i, int j) {

		if (i >= 0 && i < this.nb_line - 1 && this.tiles[i][j] != null && this.tiles[i + 1][j] == null) {

			if (this.tiles[i][j].get_is_brick()) {
				boolean done = true;
				boolean total_denied = true;
				int brick_number = tiles[i][j].get_brick_number();

				int it = 0;
				for (int z = j;; z++) {
					if (z == nb_column || tiles[i][z] == null || !tiles[i][z].get_is_brick()
							|| tiles[i][z].get_brick_number() != tiles[i][j].get_brick_number()) {

						if (it == tiles[i][j].get_width_brick()) {
							done = true;
						} else {
							done = false;
						}
						break;
					} else {
						if (tiles[i + 1][z] != null) {
							total_denied = false;
						}
					}
					it++;
				}

				if (done && total_denied) {
					for (int z = j;; z++) {
						if (z == nb_column || tiles[i][z] == null || !tiles[i][z].get_is_brick()
								|| tiles[i][z].get_brick_number() != brick_number) {
							break;
						} else {
							swap_position(i, z, i + 1, z);
						}
					}
					apply_physics();
				}
			} else {

				swap_position(i, j, i + 1, j);
				apply_physic(i + 1, j);
			}
		}
	}

	private int get_width_new(int i, int j, int brick_number, int increment) {
		int width = 0;
		for (int k = j + increment;; k += increment) {
			if (k == -1 || k == nb_column || tiles[i][k] == null || !tiles[i][k].get_is_brick()
					|| tiles[i][k].get_brick_number() != brick_number) {
				break;
			} else {
				width++;
			}
		}
		return width;
	}

	/**
	 * Méthode permettant de supprimer les combinaisons qui ont été trouvé
	 * 
	 * @return donne le score réalisé via les combinaisons
	 */
	public int erase_combinaison() {

		int sum_return = 0;
		for (int i = 0; i < current_combinaison_detected; i++) {
			Vector<Dimension> vec = new Vector<Dimension>();
			for (int j = this.nb_line - 2; j >= 0; j--) {
				for (int k = 0; k < this.nb_column; k++) {
					if (this.tiles[j][k] != null && this.tiles[j][k].get_combinaison_detected()
							&& this.tiles[j][k].get_combinaison_number() == i) {
						vec.add(new Dimension(j, k));
					}
				}
			}

			// Gestion des gros blocs
			for (Dimension t : vec) {
				int idx_i = t.width;
				int idx_j = t.height;

				// Test au dessus
				if (idx_i - 1 >= 0) {
					int index_i = idx_i - 1;
					Tile t_up = tiles[index_i][idx_j];
					if (t_up != null && t_up.get_is_brick()) {
						if (tiles[index_i][idx_j].get_strong_brick()) {
							int number_ref = tiles[index_i][idx_j].get_brick_number();
							tiles[index_i][idx_j].set_is_brick(false);
							int size_left = get_width_new(index_i, idx_j, number_ref, -1);
							int size_right = get_width_new(index_i, idx_j, number_ref, 1);

							for (int k = idx_j - 1;; k--) {
								if (k == -1 || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_ref) {
									break;
								} else {
									tiles[index_i][k].set_brick_number(current_brick_pack, size_left);
								}
							}
							current_brick_pack++;

							for (int k = idx_j + 1;; k++) {
								if (k == nb_column || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_ref) {
									break;
								} else {
									tiles[index_i][k].set_brick_number(current_brick_pack, size_right);
								}
							}
							current_brick_pack++;

						} else {
							for (int k = idx_j + 1;; k++) {
								if (k == nb_column || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != tiles[index_i][idx_j]
												.get_brick_number()) {
									break;
								} else {
									tiles[index_i][k].set_is_brick(false);
								}
							}

							for (int k = idx_j - 1;; k--) {
								if (k == -1 || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != tiles[index_i][idx_j]
												.get_brick_number()) {
									break;
								} else {
									tiles[index_i][k].set_is_brick(false);
								}
							}
							tiles[index_i][idx_j].set_is_brick(false);
						}
					}
				}

				// Test en bas
				if (idx_i + 1 < nb_line - 1) {
					int index_i = idx_i + 1;
					Tile t_down = tiles[index_i][idx_j];
					if (t_down != null && t_down.get_is_brick()) {
						if (tiles[index_i][idx_j].get_strong_brick()) {
							int number_ref = tiles[index_i][idx_j].get_brick_number();
							tiles[index_i][idx_j].set_is_brick(false);
							int size_left = get_width_new(index_i, idx_j, number_ref, -1);
							int size_right = get_width_new(index_i, idx_j, number_ref, 1);

							for (int k = idx_j - 1;; k--) {
								if (k == -1 || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_ref) {
									break;
								} else {
									tiles[index_i][k].set_brick_number(current_brick_pack, size_left);
								}
							}
							current_brick_pack++;

							for (int k = idx_j + 1;; k++) {
								if (k == nb_column || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_ref) {
									break;
								} else {
									tiles[index_i][k].set_brick_number(current_brick_pack, size_right);
								}
							}
							current_brick_pack++;
						} else {
							for (int k = idx_j + 1;; k++) {
								if (k == nb_column || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != tiles[index_i][idx_j]
												.get_brick_number()) {
									break;
								} else {
									tiles[index_i][k].set_is_brick(false);
								}
							}

							for (int k = idx_j - 1;; k--) {
								if (k == -1 || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != tiles[index_i][idx_j]
												.get_brick_number()) {
									break;
								} else {
									tiles[index_i][k].set_is_brick(false);
								}
							}
							tiles[index_i][idx_j].set_is_brick(false);
						}
					}
				}
				// Test a gauche
				if (idx_j - 1 >= 0) {
					int index_i = idx_i;
					Tile t_left = tiles[index_i][idx_j - 1];
					if (t_left != null && t_left.get_is_brick()) {
						System.out.println("BRIQUE LEFT");
						if (tiles[index_i][idx_j - 1].get_strong_brick()) {
							int number_ref = tiles[index_i][idx_j - 1].get_brick_number();
							tiles[index_i][idx_j - 1].set_is_brick(false);
							int size_left = get_width_new(index_i, idx_j - 1, number_ref, -1);

							for (int k = idx_j - 1;; k--) {
								if (k == -1 || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_ref) {
									break;
								} else {
									tiles[index_i][k].set_brick_number(current_brick_pack, size_left);
								}
							}
							current_brick_pack++;

						} else {
							int number_brick = tiles[index_i][idx_j - 1].get_brick_number();
							for (int k = idx_j - 1;; k--) {

								if (k == -1 || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_brick) {
									break;
								} else {
									tiles[index_i][k].set_is_brick(false);
								}
							}

						}
					}
				}

				// Test a droite
				if (idx_j + 1 < nb_column) {
					int index_i = idx_i;
					Tile t_right = tiles[index_i][idx_j + 1];
					if (t_right != null && t_right.get_is_brick()) {

						if (tiles[index_i][idx_j + 1].get_strong_brick()) {
							int number_ref = tiles[index_i][idx_j + 1].get_brick_number();
							tiles[index_i][idx_j + 1].set_is_brick(false);
							int size_right = get_width_new(index_i, idx_j + 1, number_ref, 1);

							for (int k = idx_j + 1;; k++) {
								if (k == nb_column || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_ref) {
									break;
								} else {
									tiles[index_i][k].set_brick_number(current_brick_pack, size_right);
								}
							}
							current_brick_pack++;
						} else {
							int number_brick = tiles[index_i][idx_j + 1].get_brick_number();
							for (int k = idx_j + 1;; k++) {

								if (k == nb_column || tiles[index_i][k] == null || !tiles[index_i][k].get_is_brick()
										|| tiles[index_i][k].get_brick_number() != number_brick) {
									break;
								} else {
									tiles[index_i][k].set_is_brick(false);
								}
							}

						}
					}
				}

			}

			if (vec.size() >= 3) {
				Dimension test = vec.get(0);
				if (tiles[test.width][test.height] != null
						&& tiles[test.width][test.height].get_form() == Form.EXCLAMATION) {
					gray_detected = true;
					if (vec.size() == 3) {
						this.width_brick = 5;
						this.nb_brick = 1;
					} else if (vec.size() == 4) {
						this.width_brick = 6;
						this.nb_brick = 1;
					} else if (vec.size() == 5) {
						this.width_brick = 6;
						this.nb_brick = 2;
					}

					new Thread(new Runnable() {
						@Override
						public void run() {
							int nb = nb_brick;
							int wid = width_brick;
							notify_blocs_coming_from(get_position(), get_width_brick());
							nb_brick = -1;
							width_brick = -1;
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (ref_versus_ctrl != null) {
								ref_versus_ctrl.receive_brick(wid, nb, false);
							}
						}
					}).start();

				} else {
					if (vec.size() == 3) {
						// Gestion combo et combinaison
					} else if (vec.size() == 4) {
						this.width_brick = 3;
						this.nb_brick = 1;
					} else if (vec.size() == 5) {
						this.width_brick = 5;
						this.nb_brick = 1;
					}

					if (vec.size() >= 4) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								int nb = nb_brick;
								int wid = width_brick;
								notify_blocs_coming_from(get_position(), get_width_brick());
								nb_brick = -1;
								width_brick = -1;
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (ref_versus_ctrl != null) {
									ref_versus_ctrl.receive_brick(wid, nb, false);
								}
							}
						}).start();
					}
				}

				sum_return += 10 * (vec.size() - 2);
			}

			// Suppression
			for (

			Dimension t : vec) {
				this.tiles[t.width][t.height] = null;
			}

		}
		this.apply_physic_on_all();
		return sum_return;
	}

	public void bind_versus_controler(New_Controller ctrl) {
		ref_versus_ctrl = ctrl;
	}

	/**
	 * Méthode permettant d'enlever toutes les combinaisons trouver sur les
	 * cellules
	 */
	private void untick_combinaison_detected_on_all() {
		current_combinaison_detected = 0;
		for (int i = 0; i < this.nb_line; i++) {
			for (int j = 0; j < this.nb_column; j++) {
				if (this.tiles[i][j] != null) {
					this.tiles[i][j].set_combinaison_detected(false);
					this.tiles[i][j].set_combinaison_number(-1);
				}
			}
		}
	}

	/**
	 * Méthode permettant de lancer le test de combinaison de la grille
	 * 
	 * @return vrai si une combinaison a été trouvée, faux sinon
	 */
	public boolean check_all_combinaison() {
		boolean combinaison = false;
		this.untick_combinaison_detected_on_all();
		for (int i = 0; i < this.nb_line - 1; i++) {
			for (int j = 0; j < this.nb_column; j++) {
				if (this.tiles[i][j] != null && !this.tiles[i][j].get_combinaison_detected()) {
					if (check_all_combinaison_from_index(i, j)) {
						combinaison = true;
					}
				}
			}
		}

		return combinaison;
	}

	/**
	 * Test des combinaisons via l'index
	 * 
	 * @param i
	 *            l'index I de la cellule que l'on test
	 * @param j
	 *            l'index J de la cellule que l'on test
	 * @return vrai si une combinaison est trouvée, faux sinon
	 */
	public boolean check_all_combinaison_from_index(int i, int j) {

		Vector<Tile> result_line = check_line_combinaison_from_index(i, j);
		if (result_line == null) {
			Vector<Tile> result_column = check_column_combinaison_from_index(i, j);
			if (result_column == null) {
				return false;
			} else {
				for (int k = 0; k < result_column.size(); k++) {
					result_column.get(k).set_combinaison_detected(true);
					result_column.get(k).set_combinaison_number(this.current_combinaison_detected);
				}
				current_combinaison_detected++;
				return true;
			}

		} else {
			for (int k = 0; k < result_line.size(); k++) {
				result_line.get(k).set_combinaison_detected(true);
				result_line.get(k).set_combinaison_number(current_combinaison_detected);
			}
			current_combinaison_detected++;
			return true;
		}
	}

	/**
	 * Méthode permettant de regarder toutes les combinaisons de colonnes via un
	 * indice
	 * 
	 * @param i
	 *            l'index I de la cellule a tester
	 * @param j
	 *            l'index J de la cellule a tester
	 * @return le tableau des cellules qui constitue la combinaison si elle
	 *         existe, null sinon
	 */
	public Vector<Tile> check_column_combinaison_from_index(int i, int j) {
		/* Init */
		Vector<Tile> cells = new Vector<Tile>();
		Tile origin = this.tiles[i][j];
		cells.add(origin);

		/* Line checking */
		int up_searcher = i - 1;
		int down_searcher = i + 1;
		boolean valid_up = true;
		boolean valid_down = true;
		while ((up_searcher >= 0 || down_searcher <= this.nb_line - 2) && (valid_up || valid_down)) {
			Tile tgt_up = null;
			Tile tgt_down = null;
			if (up_searcher >= 0) {
				tgt_up = this.tiles[up_searcher][j];
			} else {
				valid_up = false;
			}
			if (down_searcher <= this.nb_line - 2) {
				tgt_down = this.tiles[down_searcher][j];
			} else {
				valid_down = false;
			}

			if (tgt_up == null || !valid_up || !origin.same_representation(tgt_up)) {
				valid_up = false;
			} else {
				cells.add(tgt_up);
			}
			if (tgt_down == null || !valid_down || !origin.same_representation(tgt_down)) {
				valid_down = false;
			} else {
				cells.add(tgt_down);
			}
			up_searcher--;
			down_searcher++;
		}

		/* Reajust for column searching */
		if (up_searcher < 0) {
			up_searcher = 0;
		}
		if (down_searcher >= this.nb_line - 2) {
			down_searcher = this.nb_line - 2;
		}

		if (cells.size() >= 3) {
			/* Column checking */
			int iterator_line = up_searcher;
			while (iterator_line <= down_searcher) {
				Vector<Tile> tmp_line_cells = new Vector<Tile>();
				tmp_line_cells.add(this.tiles[iterator_line][j]);
				int left_searcher = j - 1;
				int right_searcher = j + 1;
				boolean valid_left = true;
				boolean valid_right = true;
				while ((left_searcher >= 0 || right_searcher <= this.nb_column - 1) && (valid_left || valid_right)) {

					Tile tgt_left = null;
					Tile tgt_right = null;
					if (left_searcher >= 0) {
						tgt_left = this.tiles[iterator_line][left_searcher];
					} else {
						valid_up = false;
					}
					if (right_searcher <= this.nb_column - 1) {
						tgt_right = this.tiles[iterator_line][right_searcher];
					} else {
						valid_down = false;
					}

					if (tgt_left == null || !valid_left || !origin.same_representation(tgt_left)) {
						valid_left = false;
					} else {
						tmp_line_cells.add(tgt_left);
					}

					if (tgt_right == null || !valid_right || !origin.same_representation(tgt_right)) {
						valid_right = false;
					} else {
						tmp_line_cells.add(tgt_right);
					}
					left_searcher--;
					right_searcher++;

				}
				iterator_line++;
				if (tmp_line_cells.size() >= 3) {
					for (int p = 1; p < tmp_line_cells.size(); p++) {
						cells.addElement(tmp_line_cells.elementAt(p));
					}
				}
			}

		}
		return ((cells.size() >= 3) ? (cells) : (null));
	}

	/**
	 * Méthode permettant de regarder toutes les combinaisons de ligne via un
	 * indice
	 * 
	 * @param i
	 *            l'index I de la cellule a tester
	 * @param j
	 *            l'index J de la cellule a tester
	 * @return le tableau des cellules qui constitue la combinaison si elle
	 *         existe, null sinon
	 */
	public Vector<Tile> check_line_combinaison_from_index(int i, int j) {

		/* Init */
		Vector<Tile> cells = new Vector<Tile>();
		Tile origin = this.tiles[i][j];
		cells.add(origin);

		/* Line checking */
		int left_searcher = j - 1;
		int right_searcher = j + 1;
		boolean valid_left = true;
		boolean valid_right = true;
		while ((left_searcher >= 0 || right_searcher <= this.nb_column - 1) && (valid_left || valid_right)) {
			Tile tgt_left = null;
			Tile tgt_right = null;
			if (left_searcher >= 0) {
				tgt_left = this.tiles[i][left_searcher];
			} else {
				valid_left = false;
			}
			if (right_searcher <= this.nb_column - 1) {
				tgt_right = this.tiles[i][right_searcher];
			} else {
				valid_right = false;
			}

			if (tgt_left == null || !valid_left || !origin.same_representation(tgt_left)) {
				valid_left = false;
			} else {
				cells.add(tgt_left);
			}

			if (tgt_right == null || !valid_right || !origin.same_representation(tgt_right)) {
				valid_right = false;
			} else {
				cells.add(tgt_right);
			}

			left_searcher--;
			right_searcher++;
		}

		/* Reajust for column searching */
		if (left_searcher < 0) {
			left_searcher = 0;
		}
		if (right_searcher >= this.nb_column - 1) {
			right_searcher = this.nb_column - 1;
		}

		if (cells.size() >= 3) {
			/* Column checking */
			int iterator_column = left_searcher;
			while (iterator_column <= right_searcher) {
				Vector<Tile> tmp_column_cells = new Vector<Tile>();
				tmp_column_cells.add(this.tiles[i][iterator_column]);
				int up_searcher = i - 1;
				int down_searcher = i + 1;
				boolean valid_up = true;
				boolean valid_down = true;
				while ((up_searcher >= 0 || down_searcher <= this.nb_line - 1) && (valid_up || valid_down)) {

					Tile tgt_up = null;
					Tile tgt_down = null;
					if (up_searcher >= 0) {
						tgt_up = this.tiles[up_searcher][iterator_column];
					} else {
						valid_up = false;
					}
					if (down_searcher <= this.nb_line - 2) {
						tgt_down = this.tiles[down_searcher][iterator_column];
					} else {
						valid_down = false;
					}

					if (tgt_up == null || !valid_up || !origin.same_representation(tgt_up)) {
						valid_up = false;
					} else {
						tmp_column_cells.add(tgt_up);
					}

					if (tgt_down == null || !valid_down || !origin.same_representation(tgt_down)) {
						valid_down = false;
					} else {
						tmp_column_cells.add(tgt_down);
					}
					up_searcher--;
					down_searcher++;
				}
				iterator_column++;
				if (tmp_column_cells.size() >= 3) {
					for (int p = 1; p < tmp_column_cells.size(); p++) {
						cells.addElement(tmp_column_cells.elementAt(p));
					}
				}
			}

		}

		return ((cells.size() >= 3) ? (cells) : (null));
	}

	/**
	 * Méthode qui permet de récupérer la largeur de brique a envoyer
	 * 
	 * @return la largeur de brique
	 */
	public int get_width_brick() {
		return this.width_brick;
	}

	/**
	 * Méthode qui permet de récupérer le nombre de brique a envoyer
	 * 
	 * @return le nombre de brique
	 */
	public int get_nb_brick() {
		return this.nb_brick;
	}

	/**
	 * Méthode permettant de choisir la largeur de brique
	 * 
	 * @param width
	 *            la largeur de brique
	 */
	public void set_width_brick(int width) {
		this.width_brick = width;
	}

	/**
	 * Méthode permettant de choisir le nombre de brique
	 * 
	 * @param nb
	 *            le nombre de brique
	 */
	public void set_nb_brick(int nb) {
		this.nb_brick = nb;
	}

	@Override
	public void add_observer(Observer obs) {
		observers.add(obs);
	}

	@Override
	public void remove_observer(Observer obs) {
		observers.remove(obs);
	}

	@Override
	public void remove_all_observers() {
		observers.clear();
	}

	@Override
	public void notify_creation(Grid grid) {
		for (Observer ob : observers) {
			ob.update_creation(grid);
		}
	}

	@Override
	public void notify_grid_tiles_move(Grid grid, Tile[][] tiles) {
		for (Observer ob : observers) {
			ob.update_grid_tiles_move(grid, tiles);
		}
	}

	@Override
	public void notify_grid_selector_move(Grid grid, Direction dir, int nb) {
		for (Observer ob : observers) {
			ob.update_grid_selector_move(grid, dir, nb);
		}
	}

	@Override
	public void notify_grid_winner(Grid_position p) {
		for (Observer ob : observers) {
			ob.update_grid_winner(p);
		}
	}

	@Override
	public void notify_blocs_coming_from(Grid_position p, int width) {
		for (Observer ob : observers) {
			ob.update_blocs_coming_from(p, width);
		}
	}

	@Override
	public void notify_score_change(Grid_position p, int score) {
		for (Observer ob : observers) {
			ob.update_score_change(p, score);
		}
	}

}
