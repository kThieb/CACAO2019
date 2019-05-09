package abstraction.eq4Transformateur2;

public class Stock {
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
	}
}
