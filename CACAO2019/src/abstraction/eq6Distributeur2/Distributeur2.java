package abstraction.eq6Distributeur2;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Distributeur2 implements IActeur, IAcheteurContratCadre<Chocolat>, IDistributeurChocolat {

	
	private Journal journal;
	
	public Distributeur2() {
		this.journal = new Journal("jEq6");
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}
	
	public String getNom() {
		return "EQ6";
	}

	public void initialiser() {
	}

	public void next() {
	}

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

	@Override
	public ContratCadre<Chocolat> getNouveauContrat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}
	
	//Caroline
	public boolean satisfaitParPrixContratCadre (ContratCadre<Chocolat> cc) {
		boolean satisfait = true;
		Chocolat produit = cc.getProduit();
		
		double marge = 1.05;
		
		double dernierprixpropose = cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size() -1);
		double notreprix = this.getStockEnVente().get(produit);
		
		if (notreprix/dernierprixpropose > 1.05) {
			satisfait = true;
		}else {
			satisfait = false;
		}
		
		return satisfait;
	}

	@Override
	//Caroline
	public void proposerPrixAcheteur(ContratCadre<Chocolat> cc) {
		double prixVendeur = cc.getListePrixAuKilo().get(0);
		//Si le dernier prix de la liste nous satisfait => proposer le même prix
		//Sinon, le dernier prix nous satisfait pas :
			//Si le vendeur propose 2 fois le même prix et pas satisfait => ne pas ajouter de prix
			// Sinon proposer un nouveau prix 
		
		if (satisfaitParPrixContratCadre (cc)) {
			cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
		} else {
			if (cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size() -1)==cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size() -1)) {
				cc.ajouterPrixAuKilo(null);
			} else {
				cc.ajouterPrixAuKilo(cc.getPrixAuKilo()*0.95);
			}
		}
		
	}

	@Override
	public void notifierAcheteur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receptionner(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double payer(double montant, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		return 0;
	}
}
