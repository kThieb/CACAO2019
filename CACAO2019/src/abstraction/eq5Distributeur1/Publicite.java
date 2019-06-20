package abstraction.eq5Distributeur1;

import abstraction.eq7Romu.produits.Chocolat;

public class Publicite {
	private double budget;
	private Chocolat produit;
	
	/**
	 * @author Erine DUPONT
	 */
	public Publicite (Chocolat produit, double budget) throws IllegalArgumentException {
		this.produit = produit;
		if (budget < 0) {
			throw new IllegalArgumentException("Le budget est négatif: " + budget);
		} else {
			this.budget = budget;
		}
	}
	
	public Chocolat getProduit() {
		return this.produit;
	}
	
	public double getBudget() {
		return this.budget;
	}
	
	public String toString() {
		return "Produit : " + this.getProduit() + " Budget : " + this.getBudget() + " €";
	}
}
