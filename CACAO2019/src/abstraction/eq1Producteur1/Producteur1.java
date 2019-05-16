package abstraction.eq1Producteur1;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IVendeurCacaoAleatoire;
import abstraction.eq1Producteur1.ventesCacaoAleatoires.SuperviseurVentesCacaoAleatoires;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur1 implements IActeur, IVendeurCacaoAleatoire {
	
	private Indicateur stockFeves;
	private Indicateur soldeBancaire;
	// BEGIN ANTI
	private StockEnVente stockEnVente; 
	//END ANTI
	// BEGIN Manon
	private Journal journal1;
	//END MANON

	public Producteur1() {
		this.stockFeves=new Indicateur("EQ1 stock feves", this, 1000);
		this.soldeBancaire=new Indicateur("EQ1 solde bancaire", this, 100000);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Monde.LE_MONDE.ajouterActeur(new SuperviseurVentesCacaoAleatoires());
		//BEGIN Manon
		this.journal1=new Journal("Ventes aleatoires de cacao");
		Monde.LE_MONDE.ajouterJournal(this.journal1);
		System.out.println(" ajout du journal...");
		// END Manon
		
	}
	
	public String getNom() {
		return "EQ1";
	}

	public void initialiser() {
	}

	public void next() {
		// production
		double nouveauStock = this.stockFeves.getValeur() + Math.random()*200;
		this.stockFeves.setValeur(this, nouveauStock);
	}

	public double quantiteEnVente(double prix) {
		if (prix>2.0) {
			return this.stockFeves.getValeur();
		} else if (prix<1.9) {
			return 0;
		} else {
			return this.stockFeves.getValeur()/2.0;
		}
	}

	public void notificationVente(double quantite, double prix) {
		this.stockFeves.retirer(this, quantite);
		this.soldeBancaire.ajouter(this, quantite*prix);
	}

	
}
