package abstraction.eq3Transformateur1;

public class Stock {

	private int quantite;
	
	public Stock(int quantite) {
		this.quantite = quantite;
	}
	public Stock() {
		this.quantite = 0;
	}
	
	public int getQuantiteEnStock() {
		return this.quantite;
	}
	public void setQuantiteEnStock(int quantite) {
		this.quantite = quantite;
	}
	public void addEnStock(int quantite) {
		this.quantite = this.quantite + quantite;
	}
	
}
