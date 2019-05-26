package abstraction.eq7Romu.ventesContratCadre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import abstraction.eq7Romu.produits.Chocolat;

public class StockEnVente<I> {
	private HashMap<I, Double> stockEnVente;

	public StockEnVente() {
		this.stockEnVente = new HashMap<I, Double>();
		
	}

	/**
	 * Si produit figure deja dans le stock en vente, actualise la quantite du produit a stock.
	 * Sinon, ajoute qu'il y a une quantite stock de produit dans le stock en vente
	 * @param produit
	 * @param stock, quantite mise en vente du produit
	 */
	public void ajouter(I produit, Double stock) {
		if (stock<0.0) {
			throw new IllegalArgumentException("Appel de ajouter(produit, stock) de StockEnVente avec stock<0.0 (=="+stock+")");
		} else {
			this.stockEnVente.put(produit, stock);
		}
	}

	public List<I> getProduitsEnVente() {
		ArrayList<I> produits=new ArrayList<I>();
		produits.addAll(this.stockEnVente.keySet());
		return produits;
	}

	public Double get(I produit) {
		return (this.stockEnVente.containsKey(produit)? this.stockEnVente.get(produit) : 0.0) ;
	}

	public String toString() {
		return this.stockEnVente.toString();
	}
	
	public String toHtml() {
		String res = "";
		for (I produit : this.stockEnVente.keySet()) {
			res+=produit+":"+String.format("%.3f",this.stockEnVente.get(produit))+"<br>";
		}
		return res;
	}

	public static void main(String[] args) {
		StockEnVente<Chocolat> s = new StockEnVente<Chocolat>();
		s.ajouter(Chocolat.MG_E_SHP, 120.0);
		s.ajouter(Chocolat.MG_NE_SHP, 200.0);
		System.out.println(s);
		s.ajouter(Chocolat.MG_E_SHP, 300.0);
		System.out.println(s);
	}
}
