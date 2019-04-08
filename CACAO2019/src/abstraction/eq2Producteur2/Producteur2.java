package abstraction.eq2Producteur2;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IVendeurCacaoAleatoire;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur2 implements IActeur, IVendeurCacaoAleatoire {
	
	private Indicateur stockFeves;
	private Indicateur soldeBancaire;

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

}
