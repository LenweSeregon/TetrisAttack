package mvc_view;

import java.awt.Graphics;

import javax.swing.JPanel;

import AnimationEtSon.JouerSon;
import mvc_global_enums.Direction;
import mvc_model.Grid;
import mvc_model.Selector;
import mvc_model.Tile;

@SuppressWarnings("serial")
public class Grid_view extends JPanel {

	private Tile_view[][] tiles;
	private Selector_view selector;
	private int nb_line;
	private int nb_column;
	private int p_x;
	private int p_y;
	private JPanel ref_board;

	private JouerSon musique;
	private Thread threadMusique;

	private int indJ;
	
	/**
	 * Constructeur de la classe qui représente une vue de grille dans le
	 * plateau
	 * 
	 * @param grid
	 *            la référence sur la grille
	 * @param pos_x
	 *            la position X de début de grille
	 * @param pos_y
	 *            la position Y de début de grille
	 * @param ref_board
	 *            la référence sur le plateau de jeu
	 */
	public Grid_view(Grid grid, int pos_x, int pos_y, JPanel ref_board, int indJ) {
		this.nb_line = grid.get_nb_line();
		this.nb_column = grid.get_nb_column();
		this.p_x = pos_x;
		this.p_y = pos_y;
		this.ref_board = ref_board;
		this.tiles = new Tile_view[nb_line][nb_column];
		
		this.indJ = indJ;
		
		Selector s = grid.get_selection();
		this.selector = new Selector_view(p_x + s.get_pos_x(), p_y + s.get_pos_y(), s.get_size_per_selector(),
				s.get_size_selector());

		this.musique = new JouerSon("/ressources/Audio/audioIceWorld.wav", true);
		threadMusique = new Thread(new Runnable() {
			public void run() {
				try {
					Grid_view.this.musique.play();
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		this.threadMusique.start();

		Tile[][] rpn = grid.get_grid();
		for (int i = 0; i < nb_line; i++) {
			for (int j = 0; j < nb_column; j++) {
				if (rpn[i][j] == null) {
					tiles[i][j] = null;
				} else {
					if (i == nb_line - 1) {
						tiles[i][j] = new Tile_view(p_x + rpn[i][j].get_pos_x(), p_y + rpn[i][j].get_pos_y(),
								rpn[i][j].get_size_cell(), true, rpn[i][j].get_color(), rpn[i][j].get_form(), ref_board,
								null, rpn[i][j].get_is_brick(), rpn[i][j].get_strong_brick(), this.indJ);
					} else {
						tiles[i][j] = new Tile_view(p_x + rpn[i][j].get_pos_x(), p_y + rpn[i][j].get_pos_y(),
								rpn[i][j].get_size_cell(), false, rpn[i][j].get_color(), rpn[i][j].get_form(),
								ref_board, null, rpn[i][j].get_is_brick(), rpn[i][j].get_strong_brick(), this.indJ);
					}
				}
			}
		}
	}

	/**
	 * Méthode permettant de mettre a jour les représentations des différentes
	 * cellules
	 * 
	 * @param rpn
	 *            les différentes cellules
	 */
	public void set_representation(Tile[][] rpn) {

		for (int i = 0; i < nb_line; i++) {
			for (int j = 0; j < nb_column; j++) {
				if (rpn[i][j] == null) {
					tiles[i][j] = null;
				} else {
					if (tiles[i][j] == null) {
						if (i == nb_line - 1) {
							tiles[i][j] = new Tile_view(p_x + rpn[i][j].get_pos_x(), p_y + rpn[i][j].get_pos_y(),
									rpn[i][j].get_size_cell(), true, rpn[i][j].get_color(), rpn[i][j].get_form(),
									ref_board, null, rpn[i][j].get_is_brick(), rpn[i][j].get_strong_brick(), this.indJ);
						} else {
							tiles[i][j] = new Tile_view(p_x + rpn[i][j].get_pos_x(), p_y + rpn[i][j].get_pos_y(),
									rpn[i][j].get_size_cell(), false, rpn[i][j].get_color(), rpn[i][j].get_form(),
									ref_board, null, rpn[i][j].get_is_brick(), rpn[i][j].get_strong_brick(), this.indJ);
						}
					} else {
						if (i == nb_line - 1) {
							tiles[i][j] = new Tile_view(p_x + rpn[i][j].get_pos_x(), p_y + rpn[i][j].get_pos_y(),
									rpn[i][j].get_size_cell(), true, rpn[i][j].get_color(), rpn[i][j].get_form(),
									ref_board, tiles[i][j].get_anim(), rpn[i][j].get_is_brick(),
									rpn[i][j].get_strong_brick(), this.indJ);
						} else {
							tiles[i][j] = new Tile_view(p_x + rpn[i][j].get_pos_x(), p_y + rpn[i][j].get_pos_y(),
									rpn[i][j].get_size_cell(), false, rpn[i][j].get_color(), rpn[i][j].get_form(),
									ref_board, tiles[i][j].get_anim(), rpn[i][j].get_is_brick(),
									rpn[i][j].get_strong_brick(), this.indJ);
						}
					}
				}
			}
		}

		this.repaint();

	}

	/**
	 * Méthode permettant de choisir la position du curseur au niveau de la vue
	 * 
	 * @param dir
	 *            la direction qu'a pris le couleur
	 * @param nb
	 *            l'incrementation de position
	 */
	public void set_selector_view(Direction dir, int nb) {
		switch (dir) {
		case UP:
			this.selector.set_pos_y(this.selector.get_pos_y() - nb);
			break;
		case DOWN:
			this.selector.set_pos_y(this.selector.get_pos_y() + nb);
			break;
		case WEST:
			this.selector.set_pos_x(this.selector.get_pos_x() - nb);
			break;
		case EAST:
			this.selector.set_pos_x(this.selector.get_pos_x() + nb);
			break;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		for (int i = 0; i < nb_line; i++) {
			for (int j = 0; j < nb_column; j++) {
				if (tiles[i][j] != null) {
					tiles[i][j].paintComponent(g);
				}
			}
		}

		this.selector.paintComponent(g);
	}
}
