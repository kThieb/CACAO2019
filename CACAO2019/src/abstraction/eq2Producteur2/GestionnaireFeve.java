package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;

public class GestionnaireFeve extends HashMap<Feve, IndicateurFeve>{
	
	public GestionnaireFeve(IActeur acteur) {
		this.put(Feve.FORASTERO_MG_NEQ, new IndicateurFeve(acteur));
		this.put(Feve.FORASTERO_MG_EQ, new IndicateurFeve(acteur));
		this.put(Feve.MERCEDES_MG_EQ, new IndicateurFeve(acteur));
		this.put(Feve.MERCEDES_MG_NEQ, new IndicateurFeve(acteur));


	
	public double getStock(Feve feve){	return this.get(feve).getStock();	}
	public double getPrixVente(Feve feve) {	return this.get(feve).getPrixVente();	}
	public double getProductionParStep(Feve feve) {	return this.get("feve").getProductionParStep();	}
	
	
	

