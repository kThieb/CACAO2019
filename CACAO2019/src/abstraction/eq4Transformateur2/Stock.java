package abstraction.eq4Transformateur2;

public class Stock {
	// Quantité de fèves, en kg
	private int feves;
	
	public Stock() {
		
	}
	
	public int getFeves() {
		return feves;
	}
	

	public void addFeves(int n) {
		this.feves += n;
	}
	
	public void removeFeves(int n) {
		this.feves -= n;
		if(this.feves <= 0) {
			System.err.println("Nombre de fèves négatif !");
			// Throw exception ?
		}
	}
}
