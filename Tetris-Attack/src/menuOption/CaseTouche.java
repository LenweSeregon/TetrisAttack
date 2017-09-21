package menuOption;
import java.awt.Color;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;


public class CaseTouche extends JFormattedTextField{

	String touche;
	
	public CaseTouche(String t){
		this.touche=t;
		try {
            MaskFormatter dateMask = new MaskFormatter("U");
            dateMask.install(this);
        } catch (ParseException ex) {
            System.out.println("Problème lors de la création du MaskFormatter");
        }
		this.setText(t);
	}

	public String getTouche() {
		return touche;
	}

	public void setTouche(String t) {
		this.touche = t;
		this.setText(touche);
	}
	
}
