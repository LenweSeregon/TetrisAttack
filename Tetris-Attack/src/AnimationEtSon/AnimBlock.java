package AnimationEtSon;

import javax.swing.JPanel;

public class AnimBlock extends Anim {

	private String type;

	private String[] strNormal;
	private String[] strDanger;
	private String[] strMort;
	
	private boolean estNormal = true, estDanger = false, estMort = false;
	
	public AnimBlock(String[] strNormal, String[]strDanger, String[] strMort, int posX, int posY, int largeur,
			int hauteur, int freq, JPanel p, String type) {
		super(strNormal, posX, posY, hauteur, largeur, freq, p);
		this.strNormal = strNormal;
		this.strDanger = strDanger;
		this.strMort = strMort;
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void etatNormal(){
		if (!estNormal){
			this.estNormal = true;
			this.estDanger = false;
			this.estMort = false;
			super.changerImages(strNormal);
		}
	}
	
	public void enDanger(){
		if (!estDanger){
			this.estNormal = false;
			this.estDanger = true;
			this.estMort = false;
			super.changerImages(strDanger);
		}
	}
	
	public void Mort(){
		if (!estMort){
			this.estNormal = false;
			this.estDanger = false;
			this.estMort = true;
			super.changerImages(strMort);
		}
	}
}
