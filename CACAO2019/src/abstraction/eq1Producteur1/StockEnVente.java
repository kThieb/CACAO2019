package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import abstraction.eq7Romu.produits.Feve;

public class StockEnVente {
	private HashMap<Feve, Double> stockEnVente;
	
	public StockEnVente() {
		this.stockEnVente = new HashMap<Feve, Double>();
	}
	
	public void ajouter(Feve produit, Double stock) {
		if (stock<0.0) {
			throw new IllegalArgumentException("Appel de ajouter(produit, stock) de StockEnVente avec stock<0.0 (=="+stock+")");
		} else {
			this.stockEnVente.put(produit, stock);
		}
	}

	public List<Feve> getProduitsEnVente() {
		ArrayList<Feve> produits=new ArrayList<Feve>();
		produits.addAll(this.stockEnVente.keySet());
		return produits;
	}

	public Double get(Feve produit) {
		return (this.stockEnVente.containsKey(produit)? this.stockEnVente.get(produit) : 0.0) ;
	}

	public String toString() {
		return this.stockEnVente.toString();
	}
	
	public String toHtml() {
		String res = "";
		for (Feve produit : this.stockEnVente.keySet()) {
			res+=produit+":"+String.format("%.3f",this.stockEnVente.get(produit))+"<br>";
		}
		return res;
	}
	
}
