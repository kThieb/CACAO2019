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
	
	private Indicateur stockFeves;
	private Indicateur soldeBancaire;
	private Journal journal;

	private int productionParStep; // kg
	private Feve fevesProduites;
	private int numero;
	private List<ContratCadre<Feve>> contratsEnCours;
	private double prixVente;
	
	
	public Producteur2() {
		this.stockFeves=new Indicateur("EQ2 stock feves", this, 1000);
		this.soldeBancaire=new Indicateur("EQ2 solde bancaire", this, 50000);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Journal journal=new Journal("jEq2");
		Monde.LE_MONDE.ajouterJournal(journal);
		
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
