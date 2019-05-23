package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;

public class Stock<T> {

	private HashMap<T, HashMap<String, Double>> stock;
	
	public Stock(ArrayList<T> produits) {
		for (T p: produits) { 
			this.stock.put(p, new HashMap<String, Double>()); 
			this.stock.get(p).put("quantite", 0.);
			this.stock.get(p).put("cout en feves", 0.);
		}
	}
	public Stock() { }
	
	// -----------------------------------------------------------
	//          GETTERS & SETTERS
	// -----------------------------------------------------------
	
	public double getQuantiteEnStock(T produit) {
		return this.stock.get(produit).get("quantite");
	}
	
	public boolean estEnStock(T produit) {
		return this.stock.containsKey(produit) && (this.stock.get(produit).get("quantite") > 0.);
	}
	
	public ArrayList<T> getProduitsEnStock() {
		ArrayList<T> resultat = new ArrayList<T>();
		for (T p: this.stock.keySet()) {
			resultat.add(p);
		}
		return resultat;
	}
	public double getCoutEnFeves(T produit) throws IllegalArgumentException {
		if (produit instanceof Feve) {
			if (estEnStock(produit)) {
				double cout = this.stock.get(produit).get("cout en feves");
				return cout;
			}
			else { throw new IllegalArgumentException("La feve demandee n'est pas en stock"); }
		}
		else { throw new IllegalArgumentException("Le produit passe en argument n'est pas une feve"); }
	}
	
	public void setQuantiteEnStock(T produit, double quantite) {
		if (quantite >= 0.) { this.stock.get(produit).put("quantite", quantite); }
		else { this.stock.get(produit).put("quantite", 0.); }
	}
	public void setCoutEnFeves(T produit, double cout) {
		if (cout >= 0.) { this.stock.get(produit).put("cout en feves", cout); }
		else { this.stock.get(produit).put("cout en feves", 0.); }
	}
	
	
	
}
