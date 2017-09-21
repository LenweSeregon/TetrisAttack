package mvc_view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JPanel;

import AnimationEtSon.AnimBlock;
import mvc_global_enums.Color;
import mvc_global_enums.Form;

@SuppressWarnings("serial")
public class Tile_view extends JComponent {

	private int pos_x;
	private int pos_y;
	private int size_tile;
	private boolean is_generating;
	private Color color;
	private Form form;
	private AnimBlock block;
	private JPanel ref_board;
	private boolean is_brick;
	private boolean strong_brick;

	private static final int FREQ_BLOCK = 110;

	private String[] strImgNormalYellow = new String[1];
	private String[] strImgNormalBlue = new String[1];
	private String[] strImgNormalRed = new String[1];
	private String[] strImgNormalGreen = new String[1];
	private String[] strImgNormalGray = new String[1];
	private String[] strImgNormalMauve = new String[1];

	private String[] strImgYellow = new String[4];
	private String[] strImgBlue = new String[4];
	private String[] strImgRed = new String[4];
	private String[] strImgGreen = new String[4];
	private String[] strImgGray = new String[4];
	private String[] strImgMauve = new String[4];

	private String[] strImgDeathYellow = new String[3];
	private String[] strImgDeathBlue = new String[3];
	private String[] strImgDeathRed = new String[3];
	private String[] strImgDeathGreen = new String[3];
	private String[] strImgDeathGray = new String[3];
	private String[] strImgDeathMauve = new String[3];

	private String garbageBleu, garbageRouge;

	private Garbage garbage;

	private int indJ;

	/**
	 * Constructeur de la classe qui représente la vue d'une cellule
	 * 
	 * @param pos_x
	 *            la position X de la cellule
	 * @param pos_y
	 *            la position Y de la cellule
	 * @param size_tile
	 *            la taille de la cellule
	 * @param is_generating
	 *            est ce que la cellule est a sa premiére génération
	 * @param color
	 *            la couleur de la cellule
	 * @param form
	 *            la forme de la cellule
	 * @param ref_board
	 *            la référence sur le plateau
	 * @param block
	 *            l'animation de block
	 * @param is_brick
	 *            est ce que la cellule est une brique
	 * @param strong_brick
	 *            est ce que la cellule est une brique forte
	 */
	public Tile_view(int pos_x, int pos_y, int size_tile, boolean is_generating, Color color, Form form,
			JPanel ref_board, AnimBlock block, boolean is_brick, boolean strong_brick, int indJ) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.size_tile = size_tile;
		this.is_generating = is_generating;
		this.color = color;
		this.form = form;
		this.block = block;
		this.ref_board = ref_board;
		this.is_brick = is_brick;
		this.strong_brick = strong_brick;
		this.indJ = indJ;

		strImgNormalYellow[0] = "/ressources/Game/Blocks/jaune1.png";
		strImgNormalBlue[0] = "/ressources/Game/Blocks/bleu1.png";
		strImgNormalGreen[0] = "/ressources/Game/Blocks/vert1.png";
		strImgNormalGray[0] = "/ressources/Game/Blocks/gris1.png";
		strImgNormalRed[0] = "/ressources/Game/Blocks/rouge1.png";
		strImgNormalMauve[0] = "/ressources/Game/Blocks/violet1.png";

		for (int i = 1; i <= 4; i++) {
			strImgYellow[i - 1] = "/ressources/Game/Blocks/jaune" + i + ".png";
			strImgBlue[i - 1] = "/ressources/Game/Blocks/bleu" + i + ".png";
			strImgGreen[i - 1] = "/ressources/Game/Blocks/vert" + i + ".png";
			strImgGray[i - 1] = "/ressources/Game/Blocks/gris" + i + ".png";
			strImgRed[i - 1] = "/ressources/Game/Blocks/rouge" + i + ".png";
			strImgMauve[i - 1] = "/ressources/Game/Blocks/violet" + i + ".png";
		}

		for (int i = 0; i < 3; i++) {
			strImgDeathYellow[i] = "/ressources/Game/Blocks/jaune" + (i + 5) + ".png";
			strImgDeathBlue[i] = "/ressources/Game/Blocks/bleu" + (i + 5) + ".png";
			strImgDeathRed[i] = "/ressources/Game/Blocks/vert" + (i + 5) + ".png";
			strImgDeathGreen[i] = "/ressources/Game/Blocks/gris" + (i + 5) + ".png";
			strImgDeathGray[i] = "/ressources/Game/Blocks/rouge" + (i + 5) + ".png";
			strImgDeathMauve[i] = "/ressources/Game/Blocks/violet" + (i + 5) + ".png";
		}
		garbageBleu = "/ressources/Game/briqueB0.png";
		garbageRouge = "/ressources/Game/briqueR0.png";

	}

	/**
	 * Méthode permettant de choisir si la cellule est une brique
	 * 
	 * @param b
	 *            est ce que la cellule est une brique
	 */
	public void set_is_brick(boolean b) {
		this.is_brick = b;
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
	 * Méthode permettant de choisir le type de l'animation
	 * 
	 * @param type
	 *            le type de l'animation
	 */
	public void set_anim_type(String type) {
		this.block.setType(type);
	}

	/**
	 * Méthode permettant de récupérer le block d'animation
	 * 
	 * @return le block d'animation
	 */
	public AnimBlock get_anim() {
		return this.block;
	}

	/**
	 * Méthode permettant de récupérer le type du block
	 * 
	 * @return le type du block
	 */
	public String get_type() {
		return block.getType();
	}

	/**
	 * Méthode permettant de récupérer la position X de la cellule
	 * 
	 * @return la position X
	 */
	public int get_pos_x() {
		return this.pos_x;
	}

	/**
	 * Méthode permettant de récupérer la position Y de la cellule
	 * 
	 * @return la position Y
	 */
	public int get_pos_y() {
		return this.pos_y;
	}

	/**
	 * Méthode permettant de récupérer la couleur de la cellule
	 * 
	 * @return la couleur
	 */
	public Color get_color() {
		return this.color;
	}

	/**
	 * Méthode permettant de récupérer la forme de la cellule
	 * 
	 * @return la forme
	 */
	public Form get_form() {
		return this.form;
	}

	/**
	 * Méthode permettant de savoir si la cellule est une brique forte
	 * 
	 * @return vrai si brique forte, faux sinon
	 */
	public boolean get_strong_brick() {
		return strong_brick;
	}

	/**
	 * Méthode permettant de définir si la cellule est en génération
	 * 
	 * @param b
	 *            la valeur booléenne pour la génération
	 */
	public void set_is_generating(boolean b) {
		this.is_generating = b;
	}

	@Override
	public void paintComponent(Graphics g2) {

		Graphics2D g = (Graphics2D) g2;

		Toolkit.getDefaultToolkit().sync();
		if (is_brick) {
			if (this.block != null) {
				this.block.stopper();
			}
			g.setColor(java.awt.Color.BLUE);
			g.fillRect(pos_x, pos_y, size_tile, size_tile);
			if (indJ == 1)
				this.garbage = new Garbage(garbageBleu, pos_x, pos_y, size_tile, size_tile, ref_board);
			else
				this.garbage = new Garbage(garbageRouge, pos_x, pos_y, size_tile, size_tile, ref_board);
			this.garbage.paintComponent(g);
			/*
			 * if (indJ == 1) this.garbage = new Garbage(garbageBleu, pos_x,
			 * pos_y, size_tile, size_tile, ref_board); else this.garbage = new
			 * Garbage(garbageRouge, pos_x, pos_y, size_tile, size_tile,
			 * ref_board); this.garbage.paintComponent(g);
			 */
		} else {
			switch (color) {
			case YELLOW:
				if (this.block == null || !"YELLOW".equals(this.block.getType())) {

					if (this.block != null)
						this.block.stopper();

					this.block = new AnimBlock(strImgNormalYellow, strImgYellow, strImgDeathYellow, pos_x, pos_y,
							size_tile, size_tile, FREQ_BLOCK, ref_board, this.color.toString());

					this.block.start();
				} else {
					this.block.setPosX(pos_x);
					this.block.setPosY(pos_y);
				}
				this.block.paintComponent(g);
				break;
			case BLUE:
				if (this.block == null || !"BLUE".equals(this.block.getType())) {

					if (this.block != null) {
						this.block.setPosX(pos_x);
						this.block.setPosY(pos_y);
						this.block.stopper();
					}
					this.block = new AnimBlock(strImgNormalBlue, strImgBlue, strImgDeathBlue, pos_x, pos_y, size_tile,
							size_tile, FREQ_BLOCK, ref_board, this.color.toString());
					this.block.start();
				} else {
					this.block.setPosX(pos_x);
					this.block.setPosY(pos_y);
				}
				this.block.paintComponent(g);
				break;
			case GREEN:
				if (this.block == null || !"GREEN".equals(this.block.getType())) {

					if (this.block != null) {
						this.block.setPosX(pos_x);
						this.block.setPosY(pos_y);
						this.block.stopper();
					}
					this.block = new AnimBlock(strImgNormalGreen, strImgGreen, strImgDeathGreen, pos_x, pos_y,
							size_tile, size_tile, FREQ_BLOCK, ref_board, this.color.toString());

					this.block.start();
				} else {
					this.block.setPosX(pos_x);
					this.block.setPosY(pos_y);
				}
				this.block.paintComponent(g);
				break;
			case GRAY:
				if (this.block == null || !"GRAY".equals(this.block.getType())) {

					if (this.block != null) {
						this.block.setPosX(pos_x);
						this.block.setPosY(pos_y);
						this.block.stopper();
					}
					this.block = new AnimBlock(strImgNormalGray, strImgGray, strImgDeathGray, pos_x, pos_y, size_tile,
							size_tile, FREQ_BLOCK, ref_board, this.color.toString());

					this.block.start();
				} else {
					this.block.setPosX(pos_x);
					this.block.setPosY(pos_y);
				}
				this.block.paintComponent(g);
				break;
			case RED:
				if (this.block == null || !"RED".equals(this.block.getType())) {

					if (this.block != null) {
						this.block.setPosX(pos_x);
						this.block.setPosY(pos_y);
						this.block.stopper();
					}
					this.block = new AnimBlock(strImgNormalRed, strImgRed, strImgDeathRed, pos_x, pos_y, size_tile,
							size_tile, FREQ_BLOCK, ref_board, this.color.toString());
					this.block.start();
				} else {
					this.block.setPosX(pos_x);
					this.block.setPosY(pos_y);
				}
				this.block.paintComponent(g);
				break;
			case MAUVE:
				if (this.block == null || !"MAUVE".equals(this.block.getType())) {

					if (this.block != null) {
						this.block.setPosX(pos_x);
						this.block.setPosY(pos_y);
						this.block.stopper();
					}
					this.block = new AnimBlock(strImgNormalMauve, strImgMauve, strImgDeathMauve, pos_x, pos_y,
							size_tile, size_tile, FREQ_BLOCK, ref_board, this.color.toString());

					this.block.start();
				} else {
					this.block.setPosX(pos_x);
					this.block.setPosY(pos_y);
				}
				this.block.paintComponent(g);
				break;
			}

			if (pos_y < 150) {
				this.block.enDanger();
			} else if (pos_x < 9) {
				this.block.etatNormal();
			}
		}

		if (this.is_generating) {
			g.setColor(new java.awt.Color(0, 0, 0, 150));
			g.fillRect(pos_x, pos_y, size_tile, size_tile);
		}
	}
}
