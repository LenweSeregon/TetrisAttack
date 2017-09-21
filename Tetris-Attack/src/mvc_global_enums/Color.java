package mvc_global_enums;

import java.util.Random;

/**
 * Enumeration représentant les différents couleurs possibles dans le jeu
 * 
 * @author nicolas
 *
 */
public enum Color {
	RED, BLUE, YELLOW, GREEN, GRAY, MAUVE;

	static public Color get_random_color_without(Color c) {
		Color[] colors = new Color[Color.values().length - 1];
		int i = 0;
		for (Color color : Color.values()) {
			if (color != c) {
				colors[i] = color;
			}
			i++;
		}

		Random random = new Random();
		return colors[random.nextInt(Color.values().length)];
	}
}
