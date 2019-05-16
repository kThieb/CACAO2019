package abstraction.eq5Distributeur1;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Distributeur1 implements IActeur, IAcheteurContratCadre, IDistributeurChocolat {
	private Journal journal;
	private ArrayList<Indicateur> stock;
	


	public Distributeur1() {
		this.journal = new Journal("jEq5");
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}
	
	public String getNom() {
		return "EQ5";
	}

	public void initialiser() {
	}

	public void next() {
	}


	
// ------------------------------------------------------------------------------------------------------
// ACHETEUR
// ------------------------------------------------------------------------------------------------------ 
	
	@Override
	public ContratCadre getNouveauContrat() {
		// On va créer un nouveau contrat cadre 
		// Au préalable, il faut identifier produit, quantité, vendeur, acheteur
		
		//Choix du produit
		ArrayList<Chocolat> produits = new ArrayList<Chocolat>();
		produits.add(Chocolat.HG_E_SHP);
		produits.add(Chocolat.MG_E_SHP);
		produits.add(Chocolat.MG_NE_HP);
		produits.add(Chocolat.MG_NE_SHP);
		
		//Choix quantité
		
		//Création Contrat
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre C) {
		// TODO Auto-generated method stub
		if (C.getEcheancier()==null) {//pas de contre-proposition
			C.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), 20, C.getQuantite()/20));
		} else {
			C.ajouterEcheancier(new Echeancier(C.getEcheancier())); // accepter la contre-proposition
		}
	}
		


	@Override
	public void proposerPrixAcheteur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierAcheteur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receptionner(Object produit, double quantite, ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double payer(double montant, ContratCadre cc) {
		// TODO Auto-generated method stub
		return 0;
	}

// ---------------------------------------------------------------------------------------------------------
// VENDEUR CLIENT
// ---------------------------------------------------------------------------------------------------------
	
	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		StockEnVente<Chocolat> res = new StockEnVente<Chocolat>();
		return res;
	}

	@Override
	public double getPrix(Chocolat c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double vendre(Chocolat c, double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}
}