package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IVendeurCacaoAleatoire;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur2 implements IActeur, IVendeurCacaoAleatoire, IVendeurContratCadre<Feve> {
	
	private static int NB_PROD = 2;
	private static final double PRIX_INIT = 1.500;
	private static final double PRIX_MIN = 0.800;
	private static final double PRIX_MAX = 2.500;
	
	
	private Indicateur stockFeves;
	private Indicateur soldeBancaire;
	private Journal journal;

	private int productionParStep; // kg
	private Feve fevesProduites;
	private int numero;
	private List<ContratCadre<Feve>> contratsEnCours;
	private double prixVente;
	
	
	public Producteur2(Feve fevesProduites, int productionParStep, double stockInitial, double soldeInitial) {
		NB_PROD++;
		this.numero = NB_PROD;
		this.prixVente = PRIX_INIT;
		this.fevesProduites = fevesProduites;
		this.productionParStep = productionParStep;
		this.stockFeves = new Indicateur(this.getNom()+" Stock", this, stockInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		this.soldeBancaire = new Indicateur(this.getNom()+" Solde", this, soldeInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		this.contratsEnCours = new ArrayList<ContratCadre<Feve>>();
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}
	
	public String getNom() {
		return "EQ2";
	}

	public void initialiser() {
	}

	public void next() {
		// production
		double nouveauStock = this.stockFeves.getValeur() + Math.random()*300;
		this.stockFeves.setValeur(this, nouveauStock);
	}

	public double quantiteEnVente(double prix) {
		return this.stockFeves.getValeur();
	}
	public void notificationVente(double quantite, double prix) {
		this.stockFeves.retirer(this, quantite);
		this.soldeBancaire.ajouter(this, quantite*prix);
	}

	@Override
	public StockEnVente getStockEnVente() {
		return null;
	}

	@Override
	public double getPrix(Object produit, Double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void proposerEcheancierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double livrer(Object produit, double quantite, ContratCadre cc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void encaisser(double montant, ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getPrix(Feve produit, Double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		return 0;
	}

}
