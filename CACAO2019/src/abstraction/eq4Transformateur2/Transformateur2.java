package abstraction.eq4Transformateur2;

import java.util.HashMap;

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

public class Transformateur2 implements IActeur, IAcheteurContratCadre<Feve>, IVendeurContratCadre<Chocolat> {
	private Indicateur iStockFeves;
    private Indicateur iSoldeBancaire;
	private Indicateur iStockChocolat;
	private Journal journal;
	
	
	private HashMap<Chocolat, Stock> stocksChocolat;
	private HashMap<Feve, Stock> stockFeves;
	
	public Transformateur2() {
		this.iStockFeves=new Indicateur("EQ4 stock feves", this, 50);
		this.iSoldeBancaire=new Indicateur("EQ4 solde bancaire", this, 100000);
		this.iStockChocolat=new Indicateur("EQ4 stock chocolat", this, 100);
		Monde.LE_MONDE.ajouterIndicateur(this.iStockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.iSoldeBancaire);
		Monde.LE_MONDE.ajouterIndicateur(this.iStockChocolat);
	}
	
	public String getNom() {
		return "EQ4";	
	}

	public void initialiser() {
		// Initialisation du journal
		this.journal = new Journal("jEq4");
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.journal.ajouter("Initialisation du transformateur 2 (Eq4).");
		System.out.println("Ajout du journal...");
	}

	public void next() {
		// transformation
		double quantiteTransformee = Math.random()*Math.min(100, this.iStockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
		this.iStockFeves.retirer(this, quantiteTransformee);
		this.iStockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
		this.iSoldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}

	
	/*****************************************************
	 Fonctions relatives à IAcheteurContratCadre<Feve>
	 *****************************************************/

	@Override
	public ContratCadre<Feve> getNouveauContrat() {
		// Choix du vendeur
		for(IActeur a : Monde.LE_MONDE.getActeurs()) {
			if(a instanceof IVendeurContratCadre) {
				StockEnVente sev = ((IVendeurContratCadre) a).getStockEnVente();
				
			}
		}
		
		
		//ContratCadre<Feve> cc = new ContratCadre<Feve>(this, , , quantite);
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {

		
	}

	@Override
	public void proposerPrixAcheteur(ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		
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
		return 0;
	}

	/*****************************************************
	 Fonctions relatives à IVendeurContratCadre<Chocolat>
	 *****************************************************/

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPrix(Chocolat produit, Double quantite) {
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

}