package abstraction.eq4Transformateur2;

public class TasProduit<T> {
	/** Représente une livraison de fèves ou de chocolat */
	
	private double quantité; // kg
	private double prixUnitaire;
	private int datePeremption;
	
	public TasProduit(double quantité, double prixUnitaire) {
		this.quantité = quantité;
		this.prixUnitaire = prixUnitaire;
	}
	
	public double getQuantité() {
		return quantité;
	}
	
	public double getPrixUnitaire() {
		return prixUnitaire;
	}
	
	public boolean prendre(double qte) {
		if(qte <= this.quantité) {
			this.quantité -= qte;
			return true;
		}
		return false;
	}
}
