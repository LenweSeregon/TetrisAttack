package mvc_global_enums;

import java.util.Random;

/**
 * Enumeration représentant les différents formes des cellules
 * 
 * @author nicolas
 *
 */
public enum Form {
	HEART, TRIANGLE, STAR, CIRCLE, DIAMOND, EXCLAMATION;

	static public Form get_random_form_without(Form f) {
		Form[] forms = new Form[Form.values().length - 1];
		int i = 0;
		for (Form form : Form.values()) {
			if (form != f) {
				forms[i] = form;
			}
			i++;
		}

		Random random = new Random();
		return forms[random.nextInt(Form.values().length)];
	}
}
