package abstraction.eq4Transformateur2;

import abstraction.eq7Romu.produits.Feve;

public class TasFeve {
	/** Représente une livraison de fèves */
	
	private Feve type;
	private int quantité; // kg
	private double prixUnitaire;
	private int datePeremption;
	
	public TasFeve(Feve type, int quantité, double prixUnitaire) {
		this.type = type;
		this.quantité = quantité;
		this.prixUnitaire = prixUnitaire;
	}
	
	public Feve getType() {
		return type;
	}
	
	public int getQuantité() {
		return quantité;
	}
	
	public double getPrixUnitaire() {
		return prixUnitaire;
	}
	
	public boolean prendre(int qté) {
		if(qté <= this.quantité) {
			this.quantité -= qté;
			return true;
		}
		return false;
	}
}
