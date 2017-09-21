package mvc_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import AnimationEtSon.Anim;
import AnimationEtSon.JouerSon;
import clock.Clock;
import menuOption.Joueur1;
import menuOption.Joueur2;
import mvc_controller.New_Controller;
import mvc_global_enums.Direction;
import mvc_global_enums.Grid_position;
import mvc_model.Grid;
import mvc_model.Tile;
import pattern_observer.Observer;

@SuppressWarnings("serial")
public class Board_view extends JPanel implements Observer {

	private Grid_view grid_left;
	private Grid_view grid_right;

	private New_Controller controller_left;
	private New_Controller controller_right;
	
	private String[] toucheJ1 = new String[5];
	private String[] toucheJ2 = new String[5];

	private String[] strFond = { "/ressources/Game/background/frame_1.png", "/ressources/Game/background/frame_2.png",
			"/ressources/Game/background/frame_3.png" };
	private String[] strFondSolp = {"/ressources/Game/fond1J.png"};
	private Anim animFond;

	private boolean pause;

	private JouerSon swap;
	private Thread threadSwap;

	private int width;
	private int height;

	private JouerSon erase;
	private Thread threadErase;

	private Image border = (new ImageIcon(this.getClass().getResource("/ressources/Game/fond.png"))).getImage();
	private Image fond1 = (new ImageIcon(this.getClass().getResource("/ressources/Game/wallPlayer1.png"))).getImage();
	private Image fond2 = (new ImageIcon(this.getClass().getResource("/ressources/Game/wallPlayer2.png"))).getImage();

	private Image bottom_left = (new ImageIcon(this.getClass().getResource("/ressources/Game/borderBasPlayer1.png")))
			.getImage();;

	private Image bottom_right = (new ImageIcon(this.getClass().getResource("/ressources/Game/borderBasPlayer2.png")))
			.getImage();;

	private Clock clock;

	/**
	 * Constructeur de la classe qui représente la vue globale de notre jeu
	 * 
	 * @param nb_line
	 *            le nombre de ligne dans les grilles
	 * @param nb_column
	 *            le nombre de colonne dans les grilles
	 * @param size_cell
	 *            la taille des cellules
	 * @param ctrl_l
	 *            la référence sur le controleur de gauche
	 * @param ctrl_r
	 *            la référence sur le controleur de droite
	 */
	public Board_view(int nb_line, int nb_column, int size_cell, New_Controller ctrl_l, New_Controller ctrl_r) {

		this.controller_left = ctrl_l;
		this.controller_right = ctrl_r;

		this.width = 1100;
		this.height = 860;
		this.grid_left = null;
		this.grid_right = null;

		if (controller_right != null) this.animFond = new Anim(strFond, 0, 0, (nb_column * size_cell) * 2 + 40, nb_line * size_cell + 285, 100, this);
		else this.animFond = new Anim(strFondSolp, 0, 0, nb_line * size_cell + 50, (nb_column * size_cell) * 2 + 285, 100, this);
		
		this.pause = false;

		this.animFond.start();

		if (controller_right != null) {
			this.clock = new Clock(4 * size_cell, 135);
			new Thread(this.clock).start();
			this.add(this.clock, BorderLayout.CENTER);
			this.clock.setBounds(6 * size_cell + 55, nb_line * size_cell - 216, 2 * size_cell + 38, 300);
		}

		this.setPreferredSize(new Dimension(((nb_column * size_cell) * 2) + 142, nb_line * size_cell + 50));
		this.setSize(new Dimension(((nb_column * size_cell) * 2) + 108, nb_line * size_cell + 50));

		this.setLayout(null);

		this.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				keyboard_event_manager(e);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		if (pause) {
			g.setColor(new Color(80, 80, 80, 210));
			g2.fillRect(0, 0, width, height);

			FontMetrics f = g2.getFontMetrics(g2.getFont().deriveFont(65.f));
			int width_t = f.stringWidth("Pause");
			g2.setFont(g2.getFont().deriveFont(65.f));
			g2.setColor(Color.YELLOW);
			g2.drawString("Pause", (width - width_t) / 2, (height) / 2);

		}
	}

	/**
	 * Méthode permettant d'associer un event à un joueur
	 * 
	 * @param e
	 *            l'evenement déclencheur
	 */
	public void keyboard_event_manager(KeyEvent e) {

		if (e.getKeyChar() == 'h') {
			pause = !pause;
			if (controller_right != null) {
				controller_right.set_pause(pause);
			}
			if (controller_left != null) {
				controller_left.set_pause(pause);
			}
			repaint();
		}
		
		toucheJ1 = Joueur1.getTableau();
		toucheJ2 = Joueur2.getTableau();

		if (controller_right != null && !pause) {
			if (toucheJ2[2].equals(e.getKeyChar()+"")) {
				this.controller_right.move_selector(Direction.UP);
			} else if (toucheJ2[3].equals(e.getKeyChar()+"")) {
				this.controller_right.move_selector(Direction.DOWN);
			} else if (toucheJ2[0].equals(e.getKeyChar()+"")) {
				this.controller_right.move_selector(Direction.WEST);
			} else if (toucheJ2[1].equals(e.getKeyChar()+"")) {
				this.controller_right.move_selector(Direction.EAST);
			}
		}

		if (!pause) {
			if (toucheJ1[2].equals(e.getKeyChar()+"")) {
				this.controller_left.move_selector(Direction.UP);
			} else if (toucheJ1[3].equals(e.getKeyChar()+"")) {
				this.controller_left.move_selector(Direction.DOWN);
			} else if (toucheJ1[0].equals(e.getKeyChar()+"")) {
				this.controller_left.move_selector(Direction.WEST);
			} else if (toucheJ1[1].equals(e.getKeyChar()+"")) {
				this.controller_left.move_selector(Direction.EAST);
			} else if (toucheJ1[4].equals(e.getKeyChar()+"")) {
				this.controller_left.swap_position();
				try {
					this.threadSwap.interrupt();
				} catch (NullPointerException ee) {
					//
				}

				this.swap = new JouerSon("/ressources/Audio/audioDeplacement.wav", false);
				this.swap = new JouerSon("/ressources/Audio/audioSwitchBlocks2.wav", false);

				threadSwap = new Thread(new Runnable() {
					public void run() {
						try {
							Board_view.this.swap.play();
						} catch (Exception e) {
							Thread.currentThread().interrupt();
						}
					}
				});
				this.threadSwap.start();
			} else if (toucheJ2[4].equals(e.getKeyChar()+"") && controller_right != null) {
				this.controller_right.swap_position();
				try {
					this.threadSwap.interrupt();
				} catch (NullPointerException ee) {
					//
				}
				this.swap = new JouerSon("/ressources/Audio/audioSwitchBlocks2.wav", false);
				threadSwap = new Thread(new Runnable() {
					public void run() {
						try {
							Board_view.this.swap.play();
						} catch (Exception e) {
							Thread.currentThread().interrupt();
						}
					}
				});
				this.threadSwap.start();
			}
		}

		this.setPreferredSize(new Dimension(1100, 860));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.animFond.paintComponent(g);
		if (controller_right != null){
			g.drawImage(this.fond1, 8, 30, (this.getWidth()) + 75, this.getHeight(), 0, 0, this.border.getWidth(this),
					this.border.getHeight(this), this);
			g.drawImage(this.fond2, (this.getWidth() - 108) / 2 + 144, 30, (this.getWidth()) + 665 + 40, this.getHeight(), 0, 0,
					this.border.getWidth(this), this.border.getHeight(this), this);
			g.drawImage(this.border, 8, 30, this.getWidth() - 8, this.getHeight(), 0, 0, this.border.getWidth(this),
					this.border.getHeight(this), this);
	
			if (this.grid_left != null) {
				this.grid_left.paintComponent(g);
			}
	
			if (this.grid_right != null) {
				this.grid_right.paintComponent(g);
			}
	
			g.drawImage(bottom_left, 8, 840, this.border.getWidth(this) + 210, this.bottom_left.getHeight(this) + 20,
					this);
	
			g.drawImage(bottom_right, (this.getWidth() - 108) / 2 + 144, 840, this.border.getWidth(this) + 200,
					this.bottom_right.getHeight(this) + 20, this);
		} else {
			g.drawImage(this.fond1, (this.getWidth()/2) - 187, 30, (this.getWidth()) + 400, this.getHeight(), 0, 0, this.border.getWidth(this)-5,
					this.border.getHeight(this), this);
	
			if (this.grid_left != null) {
				this.grid_left.paintComponent(g);
			}
			g.drawImage(bottom_left, (this.getWidth()/2) - 187 , 840, this.border.getWidth(this) + 200, this.bottom_left.getHeight(this) + 20,
					this);
		}
	}

	@Override
	public void update_creation(Grid grid) {
		if (this.controller_right != null){
			if (grid.get_position() == Grid_position.LEFT) {
				this.grid_left = new Grid_view(grid, 32, 40, this, 1);
			} else {
				this.grid_right = new Grid_view(grid, 661, 40, this, 2);
			}
		} else {
			this.grid_left = new Grid_view(grid, (this.getWidth()/2) - 77, 40, this, 1);
		}
		this.repaint();
	}

	@Override
	public void update_grid_tiles_move(Grid grid, Tile[][] tiles) {
		if (grid.get_position() == Grid_position.LEFT) {
			this.grid_left.set_representation(tiles);
		} else {
			this.grid_right.set_representation(tiles);
		}
		this.repaint();
	}

	@Override
	public void update_grid_selector_move(Grid grid, Direction dir, int nb) {
		if (grid.get_position() == Grid_position.LEFT) {
			this.grid_left.set_selector_view(dir, nb);
		} else {
			this.grid_right.set_selector_view(dir, nb);
		}
		this.repaint();
	}

	@Override
	public void update_grid_winner(Grid_position p) {
		System.out.println("LE VAINQUEUR EST : " + p.toString());
		this.controller_left.desactive_controlling();
		if (controller_right != null) {
			this.controller_right.desactive_controlling();
		}
	}

	@Override
	public void update_sound_combi() {
		// TODO Auto-generated method stub
		try {
			this.threadSwap.interrupt();
		} catch (NullPointerException ee) {
			//
		}
		this.erase = new JouerSon("/ressources/Audio/audioBreakBlocks.wav", false);
		threadErase = new Thread(new Runnable() {
			public void run() {
				try {
					Board_view.this.erase.play();
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		this.threadErase.start();
	}

	@Override
	public void update_image_death(int i, int j) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update_blocs_coming_from(Grid_position p, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update_score_change(Grid_position p, int score) {
		// TODO Auto-generated method stub

	}
}
