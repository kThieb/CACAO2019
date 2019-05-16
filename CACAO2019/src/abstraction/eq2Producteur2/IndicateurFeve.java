package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;

public class IndicateurFeve extends HashMap<String,Indicateur>{
	
	public IndicateurFeve(IActeur acteur) {
		this.put("Stock",new Indicateur("Strock",acteur,0));
		this.put("PrixVente", new Indicateur("PrixVente",acteur,0));
		this.put("ProductionParStep", new Indicateur("ProductionParStep",acteur,0));
	}
}
