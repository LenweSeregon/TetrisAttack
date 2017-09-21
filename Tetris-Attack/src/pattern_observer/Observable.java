package pattern_observer;

import mvc_global_enums.Direction;
import mvc_global_enums.Grid_position;
import mvc_model.Grid;
import mvc_model.Tile;

public interface Observable {

	/**
	 * Méthode permettant d'ajouter un observeur à un observable
	 * 
	 * @param obs
	 *            l'observeur à ajouter
	 */
	public void add_observer(Observer obs);

	/**
	 * Méthode permettant de supprimer un observeur à un observable
	 * 
	 * @param obs
	 *            l'observer à supprimer
	 */
	public void remove_observer(Observer obs);

	/**
	 * Méthode permettatn de supprimer tout les observeurs
	 */
	public void remove_all_observers();

	/**
	 * Méthode permettant de notifier de la création du modele de grille
	 * 
	 * @param grid
	 *            la grille nouvellement crée
	 */
	public void notify_creation(Grid grid);

	/**
	 * Méthode permettant de notifier d'un changement de position des cellules
	 * 
	 * @param grid
	 *            la grille qui contient les cellules
	 * @param tiles
	 *            les différentes cellules pour etre mise a jour dans la vue
	 */
	public void notify_grid_tiles_move(Grid grid, Tile[][] tiles);

	/**
	 * Méthode permettant de notifier d'un changement de position du curseur
	 * 
	 * @param grid
	 *            la grille qui contient le curseur
	 * @param dir
	 *            la direction du curseur
	 * @param nb
	 *            le nombre du curseur
	 */
	public void notify_grid_selector_move(Grid grid, Direction dir, int nb);

	/**
	 * Méthode permettant de notifier de la victoire d'un joueur
	 * 
	 * @param p
	 *            la position de la grille qui a gagné
	 */
	public void notify_grid_winner(Grid_position p);

	/**
	 * Méthode permettant de notifier de l'arrivé dans 3 secondes d'une brique
	 * dans sa grille
	 * 
	 * @param p
	 *            la position de la grille qui va recevoir la brique
	 * @param width
	 *            la largeur de la brique
	 */
	public void notify_blocs_coming_from(Grid_position p, int width);

	/**
	 * Méthode permettant de notifier d'un changement de score dans une grille
	 * 
	 * @param p
	 *            la position de la grille qui subit un changement de score
	 * @param score
	 *            la nouvelle valeure du score
	 */
	public void notify_score_change(Grid_position p, int score);

}
