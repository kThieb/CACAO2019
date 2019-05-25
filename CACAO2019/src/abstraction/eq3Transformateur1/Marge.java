package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.HashMap;

public class Marge<T> {

private HashMap<T, Double> coutProd;
private HashMap<T, Double> margeBrute;
	
	public Marge(ArrayList<T> produits) {
		for (T p: produits) { 
			this.coutProd.put(p, 0.);
			this.margeBrute.put(p, 0.);
		}
	}
	public Marge() { }
	
	// -----------------------------------------------------------
	//          GETTERS & SETTERS
	// -----------------------------------------------------------

	public double getCoutProd(T produit) {
		return this.coutProd.get(produit);
	}
	public double getMargeBrute(T produit) {
		return this.margeBrute.get(produit);
	}
	public void setCoutProd(T produit, double cout) {
		if (cout >= 0.) {
			this.coutProd.put(produit, cout);
		} else {
			this.coutProd.put(produit, 0.);
		}
	}
	public void setMargeBrute(T produit, double marge) {
		if (marge >= 0.) {
			this.coutProd.put(produit, marge);
		} else {
			this.coutProd.put(produit, 0.);
		}
	}
}
