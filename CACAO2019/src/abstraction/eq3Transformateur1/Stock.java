package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import abstraction.eq7Romu.produits.Chocolat;

/** 
 * 
 * @author eve
 *
 */
public class Stock<T> {

	// T est le chocolat ou la feve ; lui est associe une quantite en stock, en kg
	HashMap<T, Collection<Lot>> stock;
	int numLot;
	int peremptionFeve;
	int peremptionChocolat;
	
	public Stock(ArrayList<T> produits) {
		this.stock = new HashMap<T, Collection<Lot>>();
		for (T p: produits) { 
			this.stock.put(p, new ArrayList<Lot>());
		}
		this.peremptionFeve = 90;
		this.peremptionChocolat = 30;
	}
	public Stock() { }
	
	// -----------------------------------------------------------
	//          GETTERS
	// -----------------------------------------------------------
	
	public double getQuantiteEnStock(T produit) {
		try { 
			double result = 0.;
			for (Lot l: this.stock.get(produit)) {
				result += l.getQuantite();
			}
			return result;
		}
		catch (NullPointerException e) { return 0.; }
	}
	
	public boolean estEnStock(T produit) {
		return this.stock.containsKey(produit) && (this.getQuantiteEnStock(produit) > 1.);
	}
	
	public ArrayList<T> getProduitsEnStock() {
		ArrayList<T> resultat = new ArrayList<T>();
		for (T p: this.stock.keySet()) {
				if (this.getQuantiteEnStock(p) > 1.) {
					resultat.add(p);
				}
		}
		return resultat;
	}
	
	
	// -----------------------------------------------------------
	//          SETTERS
	// -----------------------------------------------------------
	
	public void addQuantiteEnStock(T produit, double quantite)
			throws IllegalArgumentException {
		if (quantite>=0.) {
			
			// chocolat ou feve = pas meme date de peremption
			if (produit instanceof Chocolat) {
				try { this.stock.get(produit).add(new Lot(quantite, peremptionChocolat)); }
				catch (IllegalArgumentException e) { this.stock.put(produit, new ArrayList<Lot>(new Lot(quantite, peremptionChocolat))) }
			}
		}
		else {
			throw new IllegalArgumentException("Appel de addQuantiteEnStock avec quantite negative. Utiliser plutot removeQuantiteEnStock");
		}
	}
	
	public void removeQuantiteEnStock(T produit, double quantite)
			throws IllegalArgumentException {
		if (quantite>=0.) {
			double newQuantiteEnStock = getQuantiteEnStock(produit) - quantite;
			if (newQuantiteEnStock<0.) {
				throw new IllegalArgumentException("Quantite retiree trop grande !");
			}
			else {
				this.stock.put(produit, newQuantiteEnStock);
			}
		}
		else {
			throw new IllegalArgumentException("Appel de removeQuantiteEnStock avec quantite positive. Utiliser plutot addQuantiteEnStock");
		}
	}
	
	// -----------------------------------------------------------
	//          METHODS
	// -----------------------------------------------------------
	
	private void incrLot() {
		this.numLot ++;
	}

}
