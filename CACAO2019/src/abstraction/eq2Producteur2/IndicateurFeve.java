package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;

public class IndicateurFeve {
	private HashMap<String,Indicateur> feves=new HashMap<String,Indicateur>();
	
	public IndicateurFeve(IActeur acteur) {
		feves.put("Stock",new Indicateur("Strock",acteur,0));
		feves.put("Prix", new Indicateur("Prix",acteur,0));
		feves.put("ProductionParStep", new Indicateur("ProductionParStep",acteur,0));
	}
}
