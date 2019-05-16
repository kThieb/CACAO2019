package abstraction.eq7Romu.produits;

public enum Feve {

	CRIOLLO_HG_EQ(Variete.CRIOLLO, Gamme.HAUTE, true),
	FORASTERO_MG_NEQ(Variete.FORASTERO, Gamme.MOYENNE, false),
	FORASTERO_MG_EQ(Variete.FORASTERO, Gamme.MOYENNE, true),
	TRINITARIO_MG_NEQ(Variete.TRINITARIO, Gamme.MOYENNE, false),
	TRINITARIO_MG_EQ(Variete.TRINITARIO, Gamme.MOYENNE, true),
	MERCEDES_MG_NEQ(Variete.MERCEDES, Gamme.MOYENNE, false),
	MERCEDES_MG_EQ(Variete.MERCEDES, Gamme.MOYENNE, true);
	
	private boolean equitable;
	private Gamme gamme;
	private Variete variete;
	
	Feve(Variete variete, Gamme gamme, Boolean equitable) {
		this.variete = variete;
		this.gamme = gamme;
		this.equitable = equitable;
	}
	public Variete getVariete() {
		return this.variete;
	}
	public Gamme getGamme() {
		return this.gamme;
	}
	public boolean isEquitable() {
		return this.equitable;
	}

	public static void main(String[] args) {
		for (Feve f : Feve.values()) {
			System.out.println(f);
		}
	}
}







	
	