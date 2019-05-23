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

}
