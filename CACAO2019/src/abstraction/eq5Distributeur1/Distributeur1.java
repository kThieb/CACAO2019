package abstraction.eq5Distributeur1;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Distributeur1 implements IActeur, IAcheteurContratCadre, IDistributeurChocolat {
	private Journal journal;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
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