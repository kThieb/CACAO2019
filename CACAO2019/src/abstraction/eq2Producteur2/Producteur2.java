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
	private double productionParStep; // kg
	private int numero;
	private List<ContratCadre<Feve>> contratsEnCours;
	private double prixVente;
	private Indicateur stockFeves;
	private int numStep;
	private GestionnaireFeve gestionnaireFeve;
	
	
	
	public Producteur2(Feve fevesProduites, int productionParStep, double stockInitial, double soldeInitial) {
		NB_PROD++;
		this.productionParStep=gestionnaireFeve.getProductionParStep(Feve.FORASTERO_MG_NEQ);



		this.numero = NB_PROD;
		this.prixVente = gestionnaireFeve.getPrixVente(Feve.FORASTERO_MG_NEQ);
		this.stockFeves=gestionnaireFeve.get(Feve.FORASTERO_MG_NEQ).getStockIndicateur();
		this.fevesProduites=fevesProduites;

		Monde.LE_MONDE.ajouterIndicateur(gestionnaireFeve.getStock());
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
			double nouveauStock = this.stockFeves.getValeur() + productionParStep * qualiteProduction;
			this.stockFeves.setValeur(this, nouveauStock); }
		if (this.numStep == 24) {
			this.numStep = 1;
		} else {
		this.numStep++; }
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : prix de vente = "+this.prixVente);

	}
	
	


	@Override
	public StockEnVente<Feve> getStockEnVente() {
		double stockRestant = this.stockFeves.getValeur();
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
		if (cc.getListePrixAuKilo().size()==0) {
			cc.ajouterPrixAuKilo(getPrix(cc.getProduit(), cc.getQuantite()));
		} else {
			double prixVendeur = cc.getListePrixAuKilo().get(0);
			double prixAcheteur = cc.getPrixAuKilo();
			cc.ajouterPrixAuKilo(prixVendeur);
			
			
			/*if (prixAcheteur>=0.75*prixVendeur) { // on ne fait une proposition que si l'acheteur ne demande pas un prix trop bas.
				if (Math.random()<0.25) { // probabilite de
					cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				} else {
					cc.ajouterPrixAuKilo((prixVendeur*(0.9+Math.random()*0.1))); // rabais de 10% max
				}
			}*/
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
			return Double.NaN;
		} 
		return this.prixVente;
		
	
	}

	@Override
	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if (produit==null || !produit.equals(this.fevesProduites)) {
			throw new IllegalArgumentException("Appel de la methode livrer de Producteur2 avec un produit ne correspondant pas aux feves produites");
		}
		double livraison = Math.min(quantite, this.stockFeves.getValeur());
		this.stockFeves.retirer(this, livraison);
		return livraison;
	}

}
