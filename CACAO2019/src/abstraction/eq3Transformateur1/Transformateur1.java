package abstraction.eq3Transformateur1;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Transformateur1 implements IActeur, IAcheteurContratCadre<Feve>, IVendeurContratCadre<Chocolat>  {
	
	private Indicateur iStockFeves;
    private Indicateur soldeBancaire;
	private Indicateur iStockChocolat;
	private int nbNextAvantEchange;
	private Journal journal;
	//begin Raphael
	private Indicateur prixAchats;
	//end Raphael
	
	
	public Transformateur1() {
		this.iStockFeves=new Indicateur("EQ3 stock feves", this, 50);
		this.soldeBancaire=new Indicateur("EQ3 solde bancaire", this, 100000);
		this.iStockChocolat=new Indicateur("EQ3 stock chocolat", this, 100);
		//Begin Kevin
		this.journal = new Journal ("Vente aléatoire de cacao");
		Monde.LE_MONDE.ajouterJournal(this.journal);
		//End Kevin
		Monde.LE_MONDE.ajouterIndicateur(this.iStockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Monde.LE_MONDE.ajouterIndicateur(this.iStockChocolat);

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
		double quantiteTransformee = Math.random()*Math.min(100, this.iStockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
		this.iStockFeves.retirer(this, quantiteTransformee);
		this.iStockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
		this.soldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}

	public double quantiteDesiree(double quantiteEnVente, double prix) {
		double possible = Math.max(0.0, soldeBancaire.getValeur()/prix);
		
		double desiree= Math.min(possible,  quantiteEnVente); // achete le plus possible
		this.iStockFeves.ajouter(this, desiree);
		this.soldeBancaire.retirer(this, desiree*prix);
		return desiree;
	}

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPrix(Chocolat produit, Double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void proposerEcheancierVendeur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixVendeur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierVendeur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double livrer(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void encaisser(double montant, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContratCadre<Feve> getNouveauContrat() {
		// Choix du vendeur
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixAcheteur(ContratCadre<Feve> cc) {
		//begin Raphael
		//Code de Romu, a modifier
		double prixVendeur = cc.getListePrixAuKilo().get(0);
		if (Math.random()<0.25) { // probabilite de 25% d'accepter
			cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
		} else {
			cc.ajouterPrixAuKilo((prixVendeur*(0.9+Math.random()*0.1))); // rabais de 10% max
		}
		//End Raphael
		
	}

	@Override
	public void notifierAcheteur(ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receptionner(Feve produit, double quantite, ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double payer(double montant, ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
