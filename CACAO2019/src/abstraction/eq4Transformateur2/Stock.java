package abstraction.eq4Transformateur2;

public class Stock {
	private double quantité;
	
	public Stock() {
		
	}
	
	public double getQuantité() {
		return quantité;
	}
	

	public void add(double n) {
		this.quantité += n;
	}
	
	public void take(double n) {
		this.quantité -= n;
		if(this.quantité < 0) {
			System.err.println("Stock négatif !");
			// Throw exception ?
		}
	}
}
