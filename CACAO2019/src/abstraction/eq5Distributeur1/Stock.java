package abstraction.eq5Distributeur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.Set;

import abstraction.eq7Romu.produits.Chocolat;

public class Stock {
	private HashMap<Chocolat, Double> stock;

	public Stock() {
		this.stock = new HashMap<Chocolat, Double>();
	}

	/**
	 * Si produit figure deja dans le stock en vente, actualise la quantite du produit a stock.
	 * Sinon, ajoute qu'il y a une quantite stock de produit dans le stock en vente
	 * @param produit
	 * @param quantite mise en vente du produit
	 */
	public void ajouter(Chocolat produit, Double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("Appel de ajouter(produit, quantite) de Stock avec quantite<0.0 (=="+quantite+")");
		} else {
			this.stock.put(produit, this.get(produit) + quantite);
		}
	}
	
	public void enlever(Chocolat produit, Double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("Appel de enlever(produit, quantite) de Stock "
					+ "avec quantite<0.0 (=="+quantite+")");
		} else if (this.get(produit) < quantite) {
			throw new IllegalArgumentException("Appel de enlever(produit, quantite) de Stock "
					+ "avec stock (=="+ this.get(produit) + ") < quantite (==" + quantite + ")");
		} else {
			this.stock.put(produit, this.get(produit)- quantite);
		}
	}
	

	public List<Chocolat> getProduitsEnVente() {
		ArrayList<Chocolat> produits=new ArrayList<Chocolat>();
		produits.addAll(this.stock.keySet());
		return produits;
	}

	public Double get(Chocolat produit) {
		return (this.stock.containsKey(produit)? this.stock.get(produit) : 0.0) ;
	}

	public String toString() {
		return this.stock.toString();
	}
	
	public String toHtml() {
		String res = "";
		for (Chocolat produit : this.stock.keySet()) {
			res+=produit+":"+String.format("%.3f",this.stock.get(produit))+"<br>";
		}
		return res;
	}

	/*
	public static void main(String[] args) {
		StockEnVente<Chocolat> s = new StockEnVente<Chocolat>();
		s.ajouter(Chocolat.MG_E_SHP, 120.0);
		s.ajouter(Chocolat.MG_NE_SHP, 200.0);
		System.out.println(s);
		s.ajouter(Chocolat.MG_E_SHP, 300.0);
		System.out.println(s);
	}
	*/
}