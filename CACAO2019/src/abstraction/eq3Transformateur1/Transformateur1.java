package abstraction.eq3Transformateur1;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IAcheteurCacaoAleatoire;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Transformateur1 implements IActeur, IAcheteurCacaoAleatoire {
	
	private Indicateur stockFeves;
    private Indicateur soldeBancaire;
	private Indicateur stockChocolat;
	//begin Sacha
	private int nbNextAvantEchange;
	//End Sacha
	//begin Kevin
	private Journal journal;
	//End Kevin
	
	
	public Transformateur1() {
		this.stockFeves=new Indicateur("EQ3 stock feves", this, 50);
		this.soldeBancaire=new Indicateur("EQ3 solde bancaire", this, 100000);
		this.stockChocolat=new Indicateur("EQ3 stock chocolat", this, 100);
		this.journal = new Journal ("Vente aléatoire de cacao");
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Monde.LE_MONDE.ajouterIndicateur(this.stockChocolat);
		Monde.LE_MONDE.ajouterJournal(this.journal);
		//begin sacha
		System.out.println("ajout du journal jEq3");
		this.nbNextAvantEchange = 0;
		//end Sacha
	}
	
	public String getNom() {
		return "EQ3";
	}

	public void initialiser() {
	}

	public void next() {
		// transformation
		double quantiteTransformee = Math.random()*Math.min(100, this.stockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
		this.stockFeves.retirer(this, quantiteTransformee);
		this.stockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
		this.soldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}

	public double quantiteDesiree(double quantiteEnVente, double prix) {
		double possible = Math.max(0.0, soldeBancaire.getValeur()/prix);
		
		double desiree= Math.min(possible,  quantiteEnVente); // achete le plus possible
		this.stockFeves.ajouter(this, desiree);
		this.soldeBancaire.retirer(this, desiree*prix);
		return desiree;
	}
}
