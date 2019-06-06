package abstraction.eq4Transformateur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.fourni.Monde;
//Guillaume
public class HistoriqueDemande {
	private HashMap<Integer, HashMap<Chocolat,TasProduit<Chocolat>>> historique;
	
	public HistoriqueDemande() {
		int i=1;
		while (i<24) {
			historique.put(i, new HashMap());
			i=i+1;
		}
	}
//Guillaume
	
	public void ajouterDemande(int step,TasProduit<Chocolat> tas,Chocolat c) {
		HashMap<Chocolat, TasProduit<Chocolat>> DemandeParProduit = new HashMap();
		if (!historique.containsKey(step)) {
			historique.put(step, new HashMap());
		}
		else {
			DemandeParProduit.put(c, tas);
			historique.put(step,DemandeParProduit);	
		}
	}
//Adrien
	private  TasProduit<Chocolat> getDemande (int yearsBack, int stepInYear, Chocolat c) {
		//renvoie la quantité demandée pour un chocolat donné à une step précise
		int actualStep = Monde.LE_MONDE.getStep();
		if (actualStep-yearsBack*24+stepInYear<=0) {
			return null ;
		}
		HashMap<Chocolat,TasProduit<Chocolat>> demandeAncienne = historique.get(actualStep-yearsBack*24+stepInYear);
		if (demandeAncienne.containsKey(c)){
			TasProduit<Chocolat> tas = demandeAncienne.get(c);
			return tas;
			}
		else {
			return null;
		}
	}	
}
