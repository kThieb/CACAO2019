package abstraction.eq4Transformateur2;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Feve;

public enum Recette {
	HauteQ(Feve.CRIOLLO_HG_EQ, 1, 5,Chocolat.MG_NE_HP),
	MG_E_F(Feve.FORASTERO_MG_EQ, 1, 3, Chocolat.MG_E_SHP),
	MG_NE_F(Feve.FORASTERO_MG_NEQ, 1, 2, Chocolat.MG_NE_SHP),
	MG_E_M(Feve.MERCEDES_MG_EQ, 1, 3, Chocolat.MG_E_SHP),
	MG_NE_M(Feve.MERCEDES_MG_NEQ, 1, 2, Chocolat.MG_NE_SHP),
	MG_E_T(Feve.TRINITARIO_MG_EQ, 1, 3, Chocolat.MG_E_SHP),
	MG_NE_T(Feve.TRINITARIO_MG_NEQ, 1, 2, Chocolat.MG_NE_SHP);
	
	private Feve inputFeve;
	private double inputQte; // kg de feve par kg de chocolat
	private double coutTransformation;
	private Chocolat output;
	
	Recette(Feve inputFeve, double inputQte, double coutTransformation, Chocolat output) {
		this.inputFeve = inputFeve;
		this.inputQte = inputQte;
		this.coutTransformation = coutTransformation;
		this.output = output;
	}
	
	public Feve getInputFeve() {
		return inputFeve;
	}


	public double getInputQte() {
		return inputQte;
	}

	public double getCoutTransformation() {
		return coutTransformation;
	}

	public Chocolat getOutput() {
		return output;
	}

	
}