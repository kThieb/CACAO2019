package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IVendeurCacaoAleatoire;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.produits.Gamme;
import abstraction.eq7Romu.produits.Variete;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur2 implements IActeur, IVendeurContratCadre<Feve> {
	  
	private static int NB_PROD = 2;
	private static final double PRIX_INIT = 1.500;
	private static final double PRIX_MIN = 0.800;
	private static final double PRIX_MAX = 2.500;
	



	private Indicateur soldeBancaire;
	private Journal journal;

	
	private Feve fevesProduites;
	private int numero;
	private List<ContratCadre<Feve>> contratsEnCours;
	private int numStep;
	private GestionnaireFeve gestionnaireFeve;
	
	
	
	public Producteur2(Feve fevesProduites, int productionParStep, double stockInitial, double soldeInitial) {
		NB_PROD++;


		this.numero = NB_PROD;
		this.fevesProduites=fevesProduites;

		this.soldeBancaire = new Indicateur(this.getNom()+" Solde", this, soldeInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		this.contratsEnCours = new ArrayList<ContratCadre<Feve>>();
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.numStep = 1;
	}
	
	public Producteur2() {
		this(Feve.FORASTERO_MG_NEQ, 75000000, 220000000, 100000000);
	}
	
	public String getNom() {
		return "EQ2";
		
	}

	public void initialiser() {
		
	}

	public void next() {

		if (this.numStep <= 6 || this.numStep >= 21 || (this.numStep >= 9 && this.numStep <= 14)) {
			double qualiteProduction = (Math.random() - 0.5)/2.5 + 1; //entre 0.8 et 1.2
			double nouveauStock = this.gestionnaireFeve.getStock(Feve.FORASTERO_MG_NEQ)+ this.gestionnaireFeve.getProductionParStep(Feve.FORASTERO_MG_NEQ)* qualiteProduction;
			this.gestionnaireFeve.setStock(this, Feve.FORASTERO_MG_NEQ, nouveauStock); }
		if (this.numStep == 24) {
			this.numStep = 1;
		} else {
		this.numStep++; }
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : prix de vente = "+this.gestionnaireFeve.getPrixVente(Feve.FORASTERO_MG_NEQ));

	}
	
	


	@Override
	public StockEnVente<Feve> getStockEnVente() {
		double stockRestant = this.gestionnaireFeve.getStock(Feve.FORASTERO_MG_NEQ);
		for (ContratCadre<Feve> cc : this.contratsEnCours) {
			if (Monde.LE_MONDE != null) {
				stockRestant = stockRestant - cc.getQuantiteRestantALivrer();
			}
		}
		StockEnVente<Feve> res = new StockEnVente<Feve>();
		res.ajouter(this.fevesProduites, Math.max(0.0, stockRestant));
		return res;
	}



	/** a modifier*/
	public void proposerEcheancierVendeur(ContratCadre<Feve> cc) {
		cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la proposition de l'acheteur car on a la quantite en stock 
	}
		

	@Override
	public void proposerPrixVendeur(ContratCadre<Feve> cc) {
		if (cc.getListePrixAuKilo().size()==0) { //On vérifie qu'on a un prix à proposer
			cc.ajouterPrixAuKilo(getPrix(cc.getProduit(), cc.getQuantite()));
		} else {
			double prixVendeur = cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size()-1);
			double prixAcheteur = cc.getPrixAuKilo();
			cc.ajouterPrixAuKilo(prixVendeur); //Le premier prix proposé est la prix au kilo initial
			
			if ((prixVendeur - prixAcheteur) < 0.05 * prixVendeur) { //On arrête la négociation si la différence de prix est suffisamment faible (5% du prixVendeur)
				prixVendeur = prixAcheteur;
				cc.getListePrixAuKilo().add(prixVendeur);
			} else {
				
				if (prixAcheteur>=0.75*prixVendeur) { // on ne fait une proposition que si l'acheteur ne demande pas un prix trop bas.
				prixVendeur = prixAcheteur * 1.1; // on augmente le prix proposé par l'acheteur de 10%
				cc.getListePrixAuKilo().add(prixVendeur);
				
				} else {
				prixVendeur *= 0.90; //On diminue le prix proposé de 10%
				cc.getListePrixAuKilo().add(prixVendeur);
				}
			}
		}
	}

	@Override
	public void notifierVendeur(ContratCadre<Feve> cc) {
		this.contratsEnCours.add(cc);
	}


	@Override
	public void encaisser(double montant, ContratCadre<Feve> cc) {
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode encaisser de Producteur2 avec un montant negatif");
		}
		this.soldeBancaire.ajouter(this,  montant);
	}

	@Override
	public double getPrix(Feve produit, Double quantite) {
		if (produit==null || quantite<=0.0 || this.getStockEnVente().get(produit)<quantite) {

		
		return this.gestionnaireFeve.getPrixVente(fevesProduites);
		} 
	
		if (quantite > 10000000 && quantite < 20000000) {
			return this.gestionnaireFeve.getPrixVente(fevesProduites) * 0.95;
		}
		if (quantite > 20000000) {
			return this.gestionnaireFeve.getPrixVente(fevesProduites) * 0.9;
		}
		if (this.contratsEnCours.size() >= 1) {
			ContratCadre<Feve> cc = this.contratsEnCours.get(this.contratsEnCours.size()-1);
			double dernierPrix = cc.getPrixAuKilo();
			if (dernierPrix > this.gestionnaireFeve.getPrixVente(fevesProduites) * 0.9 && this.gestionnaireFeve.getPrixVente(fevesProduites) * 1.05 < PRIX_MAX) {
				this.gestionnaireFeve.get(fevesProduites).setPrix(this, this.gestionnaireFeve.getPrixVente(fevesProduites)*1.05);
			}
			else if (dernierPrix < this.gestionnaireFeve.getPrixVente(fevesProduites) * 0.8 && this.gestionnaireFeve.getPrixVente(fevesProduites) * 0.95 > PRIX_MIN) {
				this.gestionnaireFeve.get(fevesProduites).setPrix(this, this.gestionnaireFeve.getPrixVente(fevesProduites)*0.95);
			}
			
		}
	}

	@Override
	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if (produit==null || !produit.equals(this.fevesProduites)) {
			throw new IllegalArgumentException("Appel de la methode livrer de Producteur2 avec un produit ne correspondant pas aux feves produites");
		}
		double livraison = Math.min(quantite, this.gestionnaireFeve.getStock(fevesProduites));
		this.gestionnaireFeve.setStock(this, fevesProduites, this.gestionnaireFeve.getStock(fevesProduites)-livraison);  
		return livraison;
	}
}
