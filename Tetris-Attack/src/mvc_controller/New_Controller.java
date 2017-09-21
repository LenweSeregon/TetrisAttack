package mvc_controller;

import java.awt.Dimension;

import mvc_global_enums.Direction;
import mvc_global_enums.Grid_position;
import mvc_model.Grid;

public class New_Controller implements Runnable {

	private New_Controller controller_versus;
	private Grid model;
	volatile private int y_adder_accumulator;
	private int y_adder;
	private int timer_growing;
	volatile private boolean is_running;
	volatile private boolean is_running_checking;
	volatile private boolean is_listening;

	volatile private boolean in_pause;
	private static final int BASE_GROWING = 5000;

	/**
	 * Constructeur de la classe qui représente notre controleur sur la partie.
	 * Celui-ci représente le controleur du pattern MVC et c'est par lui que
	 * transite toutes les informations de la vue
	 * 
	 * @param model
	 *            le modele en référence
	 * @param name
	 *            le nom du controleur
	 */
	public New_Controller(Grid model, String name) {
		this.model = model;
		this.y_adder_accumulator = 0;
		this.y_adder = 1;
		this.is_listening = true;
		this.timer_growing = BASE_GROWING / model.get_size_tile();
		this.in_pause = false;
	}

	public void set_pause(boolean pause) {
		this.in_pause = pause;
	}

	/**
	 * Méthode permettant de lier le deuxième controleur lorsque l'on se trouve
	 * en mode 1 v 1
	 * 
	 * @param controller
	 *            le controleur que l'on veut lier
	 */
	public void bind_controller(New_Controller controller) {
		controller_versus = controller;
		model.bind_versus_controler(controller_versus);
	}

	/**
	 * Méthode permettant d'arreter le controle du jeu par le controleur via sa
	 * boucle de controle.
	 */
	public void desactive_controlling() {
		is_running = false;
		is_running_checking = false;
		is_listening = false;
	}

	/**
	 * Méthode étant appelé pour initialiser la grille et réaliser les
	 * différents tests via un thread pour supprimer les combinaisons
	 */
	public void init_grid() {
		this.model.generate_starting_grid();
		this.model.notify_creation(this.model);

		this.is_running_checking = true;
		new Thread(new Runnable() {
			@Override
			public void run() {

				while (is_running_checking) {
					if (!in_pause) {
						model.routine();
						model.notify_score_change(model.get_position(), model.get_score());
						model.notify_grid_tiles_move(model, model.get_grid());

						if (model.has_lose()) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(2000);
										if (model.has_lose()) {
											if (model.get_position() == Grid_position.LEFT) {
												model.notify_grid_winner(Grid_position.RIGHT);
											} else {
												model.notify_grid_winner(Grid_position.LEFT);
											}
										}
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							});
						}
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Méthode permettant de bouger le selecteur de notre modele en vérifiant
	 * que ceci est possible
	 * 
	 * @param dir
	 *            la direction que l'on souhaite donner à notre curseur via une
	 *            énumération
	 */
	public void move_selector(Direction dir) {
		if (is_listening) {
			if (this.model.can_move_selector(dir)) {
				this.model.move_selector(dir);
				this.model.notify_grid_selector_move(this.model, dir, this.model.get_size_tile());
			}
		}
	}

	/**
	 * Méthode étant appelé pour recevoir une brique sur sa grille
	 * 
	 * @param width
	 *            la largeur des briques
	 * @param nb
	 *            le nombre de brique
	 * @param strong_brick
	 *            est ce que c'est une brique forte
	 */
	public void receive_brick(int width, int nb, boolean strong_brick) {
		if (is_listening) {
			for (int i = 0; i < nb; i++) {
				Dimension dim = this.model.get_position_possible_insertion(width);
				if (dim != null) {
					model.insert_brick_to(dim.width, dim.height, width, strong_brick);
					model.notify_grid_tiles_move(model, model.get_grid());
				}
			}
		}
	}

	/**
	 * Méthode étant appelé pour changer les positions via la position du
	 * curseur
	 */
	public void swap_position() {
		if (is_listening) {
			this.model.swap_position_from_selector();
			this.model.routine();
			this.model.notify_grid_tiles_move(this.model, this.model.get_grid());
		}
	}

	@Override
	public void run() {
		this.is_running = true;

		while (is_running) {
			if (!in_pause) {
				this.y_adder_accumulator += y_adder;
				this.model.all_up_tile(y_adder, y_adder_accumulator);
				this.model.up_selector(y_adder, y_adder_accumulator);

				if (this.y_adder_accumulator >= this.model.get_size_tile()) {
					y_adder_accumulator = 0;
				}

				if (this.model.get_selection().get_index_x() == 0) {
					this.model.move_selector(Direction.DOWN);
					this.model.notify_grid_selector_move(this.model, Direction.DOWN, this.model.get_size_tile());

				}

				this.model.set_y_acc_ref(y_adder_accumulator);

				if (this.model.has_lose()) {
					is_running = false;
					if (this.model.get_position() == Grid_position.LEFT) {
						this.model.notify_grid_winner(Grid_position.RIGHT);
					} else {
						this.model.notify_grid_winner(Grid_position.LEFT);
					}

				}

				this.model.notify_grid_tiles_move(this.model, this.model.get_grid());
				this.model.notify_grid_selector_move(this.model, Direction.UP, y_adder);
			}
			try {
				Thread.sleep(timer_growing);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
