package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.HashMap;

public class Stock<T> {

	private HashMap<T, Double> stock;
	
	public Stock(ArrayList<T> produits) {
		for (T p: produits) { this.stock.put(p, 0.); }
	}
	public Stock() { }
	
	// -----------------------------------------------------------
	//          GETTERS & SETTERS
	// -----------------------------------------------------------
	
	public double getQuantiteEnStock(T produit) {
		return this.stock.get(produit);
	}
	public HashMap<T, Double> getToutStock() {
		return this.stock;
	}
	public void setQuantiteEnStock(T produit, double quantite) {
		if (quantite >= 0.) { this.stock.put(produit, quantite); }
		else { this.stock.put(produit, 0.); }
	}
	
}
