package pattern_observer;

import mvc_global_enums.Direction;
import mvc_global_enums.Grid_position;
import mvc_model.Grid;
import mvc_model.Tile;

public interface Observer {

	/**
	 * Méthode permettant d'etre notifié de la création du modele de grille
	 * 
	 * @param grid
	 *            la grille nouvellement crée
	 */
	public void update_creation(Grid grid);

	/**
	 * Méthode permettant d'etre notifié d'un changement de position des
	 * cellules
	 * 
	 * @param grid
	 *            la grille qui contient les cellules
	 * @param tiles
	 *            les différentes cellules pour etre mise a jour dans la vue
	 */
	public void update_grid_tiles_move(Grid grid, Tile[][] tiles);

	/**
	 * Méthode permettant d'etre notifié d'un changement de position du curseur
	 * 
	 * @param grid
	 *            la grille qui contient le curseur
	 * @param dir
	 *            la direction du curseur
	 * @param nb
	 *            le nombre du curseur
	 */
	public void update_grid_selector_move(Grid grid, Direction dir, int nb);

	/**
	 * Méthode permettant de notifier de la victoire d'un joueur
	 * 
	 * @param p
	 *            la position de la grille qui a gagné
	 */
	public void update_grid_winner(Grid_position p);

	/**
	 * Méthode permettant d'etre notifié un sond via une combinaison
	 */
	public void update_sound_combi();

	/**
	 * Méthode permettant d'etre notifié de l'image de mort
	 * 
	 * @param i
	 *            la position en I
	 * @param j
	 *            la position en J
	 */
	public void update_image_death(int i, int j);

	/**
	 * Méthode permettant d'etre notifié de l'arrivé dans 3 secondes d'une
	 * brique dans sa grille
	 * 
	 * @param p
	 *            la position de la grille qui va recevoir la brique
	 * @param width
	 *            la largeur de la brique
	 */
	public void update_blocs_coming_from(Grid_position p, int width);

	/**
	 * Méthode permettant d'etre notifié d'un changement de score dans une
	 * grille
	 * 
	 * @param p
	 *            la position de la grille qui subit un changement de score
	 * @param score
	 *            la nouvelle valeure du score
	 */
	public void update_score_change(Grid_position p, int score);
}
