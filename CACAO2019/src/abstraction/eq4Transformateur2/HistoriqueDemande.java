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
		historique = new HashMap<Integer, HashMap<Chocolat, TasProduit<Chocolat>>>();
	}
	
	// Guillaume
	public void ajouterDemande(int step, TasProduit<Chocolat> tas, Chocolat type) {
		if(!historique.containsKey(step))
			historique.put(step, new HashMap<Chocolat, TasProduit<Chocolat>>());
		
		
		HashMap<Chocolat, TasProduit<Chocolat>> demandeParProduit = historique.get(step);
		if(demandeParProduit.containsKey(type)) {
			// TODO Merger le nouveau tas et le tas demandeParProduit.get(type)

		}
		else
			demandeParProduit.put(type, tas);
	}
	
	// Adrien
	public TasProduit<Chocolat> getDemande (int yearsBack, int stepInYear, Chocolat type) {
		// Renvoie la quantité demandée (et le prix associé) pour un chocolat donné à une step précise
		int currentStep = Monde.LE_MONDE.getStep();
		if(currentStep - yearsBack * Transformateur2.STEPS_PAR_ANNEE + stepInYear < 0)
			return null;

		HashMap<Chocolat,TasProduit<Chocolat>> demandeAncienne = historique.get(currentStep - yearsBack * Transformateur2.STEPS_PAR_ANNEE + stepInYear);
		if(demandeAncienne.containsKey(type))
			return demandeAncienne.get(type);
		else
			return null;
	}	
	
	//Adrien et Guillaume
	private TasProduit<Chocolat> fusionTasProduit(TasProduit<Chocolat> tas1, TasProduit<Chocolat> tas2){
		double quantiteFusionnee= tas1.getQuantité()+tas2.getQuantité();
		double prixPondere= (tas1.getPrixAuKilo()*tas1.getQuantité()) + (tas2.getPrixAuKilo()*tas2.getQuantité())/quantiteFusionnee;
		TasProduit<Chocolat> nouveauTas= new TasProduit<Chocolat>(quantiteFusionnee, prixPondere);
		return nouveauTas;
	}
	
}




