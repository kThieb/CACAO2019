package abstraction.eq4Transformateur2;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Feve;

public class Recette {
	private Feve inputFeve;
	private double inputQte; // kg de feve par kg de chocolat
	private double coutTransformation;
	private Chocolat output;
	
	
	public Recette(Feve inputFeve, double inputQte, double coutTransformation, Chocolat output) {
		this.inputFeve = inputFeve;
		this.inputQte = inputQte;
		this.coutTransformation = coutTransformation;
		this.output = output;
	}
}
