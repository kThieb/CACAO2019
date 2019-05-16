package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;

public class GestionnaireFeve {
	private HashMap<Feve, Indicateur> feves=new HashMap<Feve,Indicateur>();
	
	public GestionnaireFeve(IActeur acteur) {
		feves.put(Feve.FORASTERO_MG_NEQ, new Indicateur(Feve.FORASTERO_MG_NEQ.name(), acteur,0));
		feves.put(Feve.FORASTERO_MG_EQ, new Indicateur(Feve.FORASTERO_MG_EQ.name(),acteur,0));
		feves.put(Feve.MERCEDES_MG_EQ, new Indicateur(Feve.MERCEDES_MG_EQ.name(),acteur,0));
		feves.put(Feve.MERCEDES_MG_NEQ, new Indicateur(Feve.MERCEDES_MG_NEQ.name(),acteur,0));
	}
	
	
}
