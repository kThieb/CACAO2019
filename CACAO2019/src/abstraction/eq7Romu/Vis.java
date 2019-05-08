package abstraction.eq7Romu;

public enum Vis {
	V3540(3.5, 40), 
	V3530(3.5, 30), 
	V5060(5.0, 60);
	
	private double diametre;
	private int longueur;
	
	Vis(double diametre, int longueur) {
		this.diametre = diametre; 
		this.longueur = longueur;
	}
	public double getDiametre() {
		return this.diametre;
	}
	public int getLongueur() {
		return this.longueur;
	}
	public String toString() {
		return this.getDiametre()+"x"+this.getLongueur();
	}

	public static void main(String[] args) {
		for (Vis v : Vis.values()) {
			System.out.println(v);
		}
		System.out.println("le vis V5060 corresponde a "+Vis.V5060);
	}
}
