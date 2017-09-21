package menu;

public class MenuChoices {

	/*
	 * Classe pour les images du menu principal
	 * Pour pouvoir facilement récupérer les indices des images
	 */
	
	private String name;
	private int indX;
	private int indY;
	
	public MenuChoices() {
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setIndX(int x) {
		this.indX = x;
	}
	public void setIndY(int y) {
		this.indY = y;
	}
	
	public String getName() {
		return this.name;
	}
	public int getIndX() {
		return this.indX;
	}
	public int getIndY() {
		return this.indY;
	}
	
}