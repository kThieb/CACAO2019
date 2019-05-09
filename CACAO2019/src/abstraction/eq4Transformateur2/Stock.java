package abstraction.eq4Transformateur2;

public class Stock {
	private int quantité;
	
	public Stock() {
		
	}
	
	public int getQuantité() {
		return quantité;
	}
	

	public void add(int n) {
		this.quantité += n;
	}
	
	public void take(int n) {
		this.quantité -= n;
		if(this.quantité <= 0) {
			System.err.println("Stock négatif !");
			// Throw exception ?
		}
	}
}
