package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq7Romu.produits.Feve;
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

	
	private int numero;
	private List<ContratCadre<Feve>> contratsEnCours;
	private int numStep;
	private GestionnaireFeve gestionnaireFeve; 


	
	public Producteur2( List<Integer> productionParStep, List<Double> stockInitial, double soldeInitial) {
		this.gestionnaireFeve = new GestionnaireFeve(this);
		
		List<Feve> feves=this.gestionnaireFeve.getFeves();
		for(Feve f:feves) {
			Monde.LE_MONDE.ajouterIndicateur(gestionnaireFeve.get(f).getStockIndicateur());
		}
		this.soldeBancaire = new Indicateur(this.getNom()+" Solde", this, soldeInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		this.contratsEnCours = new ArrayList<ContratCadre<Feve>>();
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.numStep = 1;
	}
	
	public Producteur2() {
		this.gestionnaireFeve.setProduction(this, Feve.FORASTERO_MG_NEQ, 75000000);
		this.gestionnaireFeve.setStock(this, Feve.FORASTERO_MG_NEQ, 220000000);
		
		this.gestionnaireFeve.setProduction(this, Feve.FORASTERO_MG_EQ, 75000000); //TODO rectifier les productions des autres feves
		this.gestionnaireFeve.setStock(this, Feve.FORASTERO_MG_EQ, 220000000);
		
		this.gestionnaireFeve.setProduction(this, Feve.MERCEDES_MG_NEQ, 75000000);
		this.gestionnaireFeve.setStock(this, Feve.MERCEDES_MG_NEQ, 220000000);
		
		this.gestionnaireFeve.setProduction(this, Feve.MERCEDES_MG_EQ, 75000000);
		this.gestionnaireFeve.setStock(this, Feve.MERCEDES_MG_EQ, 220000000);
		
	}
	
	public String getNom() {
		return "EQ2";
	}

	public void initialiser() {
		
	}

	public void next() {
		for (Feve f:gestionnaireFeve.getFeves()) {
			if (this.numStep <= 6 || this.numStep >= 21 || (this.numStep >= 9 && this.numStep <= 14)) {
				double qualiteProduction = (Math.random() - 0.5)/2.5 + 1; //entre 0.8 et 1.2
				double nouveauStock = this.gestionnaireFeve.getStock(f)+ this.gestionnaireFeve.getProductionParStep(f) * qualiteProduction;  //fait varier la production entre 80% et 120% de la production "normale"
				this.gestionnaireFeve.setStock(this, f, nouveauStock);}
			if (this.numStep == 24) {
				this.numStep = 1;
			} else {
			this.numStep++; }
			this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : prix de vente = "+this.gestionnaireFeve.getPrixVente(f));
		}
	}


	@Override
	public StockEnVente<Feve> getStockEnVente() {
		StockEnVente<Feve> res = new StockEnVente<Feve>();
		List<Feve>feves = gestionnaireFeve.getFeves();
		for(Feve feve : feves) {
			double stockRestant = this.gestionnaireFeve.getStock(feve);
			for (ContratCadre<Feve> cc : this.contratsEnCours) {
				if (Monde.LE_MONDE != null) {
					if (cc.getProduit()==feve) {
					stockRestant = stockRestant - cc.getQuantiteRestantALivrer();
					res.ajouter(feve, Math.max(0.0, stockRestant));
				}}}}
	return res;
	}


	/** 
	 * Propose un nouvel echeancier au producteur
	 * */
	public void proposerEcheancierVendeur(ContratCadre<Feve> cc) {
		if (contratsEnCours.contains(cc)) {
			Echeancier e = cc.getEcheancier();
		} else {
			contratsEnCours.add(cc);
			Echeancier e = cc.getEcheancier();
			if (e.getQuantiteTotale() > this.getStockEnVente().get(cc.getProduit())) { //On s assure que la quantitée demandée est en stock
				throw new IllegalArgumentException("La quantité demandée n est pas disponible");
			} else {
				cc.ajouterEcheancier(new Echeancier(e)); // on accepte la proposition de l'acheteur car on a la quantite en stock 
			}
		}
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



	public double getPrix(Feve produit, Double quantite) {
		// si tu peux voir ce message, c'est que ca a marche :)
		double prixAPayer = 0;
		
		
		if (produit==null || quantite<=0.0 || this.getStockEnVente().get(produit)<quantite) {
			return Double.NaN;
		}
		
		
		else {
		if (quantite > 10000000 && quantite < 20000000) {
			prixAPayer = this.gestionnaireFeve.getPrixVente(produit) * 0.95;  // on réduit le prix de 5% si l'on commande plus de 10 000 T
		}
		else if (quantite > 20000000) {
			prixAPayer = this.gestionnaireFeve.getPrixVente(produit )* 0.9;  // on réduit le prix de 10% si l'on commande plus de 20 000 T
		}
		
		else { prixAPayer = this.gestionnaireFeve.getPrixVente(produit);}

		if (this.contratsEnCours.size() >= 1) {
			ContratCadre<Feve> cc = this.contratsEnCours.get(this.contratsEnCours.size()-1);
			double dernierPrix = cc.getPrixAuKilo();  //  on recherche le prix auquel on a vendu la dernière fois
			if (dernierPrix > this.gestionnaireFeve.getPrixVente(produit) * 0.9 && this.gestionnaireFeve.getPrixVente(produit) * 1.05 < PRIX_MAX) {
				this.gestionnaireFeve.get(produit).setPrix(this, this.gestionnaireFeve.getPrixVente(produit)*1.05);
			}  // si l'on a vendu à plus de 90% du prix maximal, on augmente le prix initial de 5%
			else if (dernierPrix < this.gestionnaireFeve.getPrixVente(produit) * 0.8 && this.gestionnaireFeve.getPrixVente(produit) * 0.95 > PRIX_MIN) {
				this.gestionnaireFeve.get(produit).setPrix(this, this.gestionnaireFeve.getPrixVente(produit)*0.95);
			}  // si l'on a vendu à moins de 80% du prix maximal, on diminue le prix initial de 5%
		}
		return prixAPayer ; }
		

	}
	
	@Override
	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if (produit==null || !produit.equals(produit)) {
			throw new IllegalArgumentException("Appel de la methode livrer de Producteur2 avec un produit ne correspondant pas aux feves produites");
		}
		double livraison = Math.min(quantite,this.gestionnaireFeve.getStock(produit));
		this.gestionnaireFeve.get(produit).getStockIndicateur().retirer(this, livraison);
		return livraison;
	}

	
	
}
