package abstraction.eq3Transformateur1;

public class Stock {

	private double quantite;
	
	public Stock(double quantite) {
		this.quantite = quantite;
	}
	public Stock() {
		this.quantite = 0;
	}
	
	public double getQuantiteEnStock() {
		return this.quantite;
	}
	public void setQuantiteEnStock(double quantite) {
		this.quantite = quantite;
	}
	public void addEnStock(double quantite) {
		this.quantite = this.quantite + quantite;
	}
	
}
