package abstraction.eq5Distributeur1;

public class CompteBancaire {
	private float compte;
	
	//Cette classe sert à gérer notre compte bancaire pour y imposer des règles propres au compte
	
	public CompteBancaire() {
		this.compte = 0;
	}
	
	public CompteBancaire(float compte) {
		this.compte=compte;
	}
	
	public float getCompteBancaire() {
		return this.compte;
	}
	
	
	public float Payer (float compte, float paiement) {
		float nouveausolde = compte-paiement;
		if (paiement<0.0){
			throw new IllegalArgumentException("Appel de Payer(compte, paiement) de CompteBancaire avec paiement<0.0 (=="+paiement+")");
		}
		if (compte-paiement <0.0) {
			throw new IllegalArgumentException("Appel de Payer(compte, paiement) de CompteBancaire avec compte-paiement<0.0 (=="+nouveausolde+")");
		}
		else {
			return nouveausolde;
		}
	}
	
	public float RecevoirPaiement (float compte, float paiement) {
		if (paiement <0.0) {
			throw new IllegalArgumentException("Appel de RecevoirPaiement(compte, paiement) de CompteBancaire avec paiement<0.0 (=="+paiement+")");
		}
		else {
			return compte + paiement;
		}
	}

}
