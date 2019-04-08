package abstraction.eq4Transformateur2;

import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Transformateur2 implements IActeur {
	private Indicateur stockFeves;
    private Indicateur soldeBancaire;
	private Indicateur stockChocolat;
	private Journal journal;

	public Transformateur2() {
		this.stockFeves=new Indicateur("EQ4 stock feves", this, 50);
		this.soldeBancaire=new Indicateur("EQ4 solde bancaire", this, 100000);
		this.stockChocolat=new Indicateur("EQ4 stock chocolat", this, 100);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Monde.LE_MONDE.ajouterIndicateur(this.stockChocolat);
	}
	
	public String getNom() {
		return "EQ4";
	}

	public void initialiser() {
		this.journal = new Journal("Ventes de cacao");
		Monde.LE_MONDE.ajouterJournal(this.journal);
		System.out.println("Ajout du journal...");
	}

	public void next() {
		// transformation
		double quantiteTransformee = Math.random()*Math.min(100, this.stockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
		this.stockFeves.retirer(this, quantiteTransformee);
		this.stockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
		this.soldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}
}