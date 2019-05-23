package abstraction.eq4Transformateur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	protected Indicateur iStockFeves;
	protected Indicateur iStockChocolat;
	
	protected Indicateur soldeBancaire;

	private Journal journal;
	
	private List<Chocolat> CHOCOLATS_VENTE;
	private List<Feve> FEVES_ACHAT;
	
	protected StockProduit<Chocolat> stocksChocolat;
	protected StockProduit<Feve> stockFeves;
	
	private Transformateur2AcheteurCC acheteurCC;
	private Transformateur2VendeurCC vendeurCC;
	protected List<ContratCadre<Feve>> contratsFevesEnCours;
	protected List<ContratCadre<Chocolat>> contratsChocolatEnCours;
	protected HashMap<Chocolat,Double> stockEnVente;
	
	public Transformateur2() {
		this.iStockFeves = new Indicateur("EQ4 stock feves", this, 50);
		this.soldeBancaire = new Indicateur("EQ4 solde bancaire", this, 100000);
		this.iStockChocolat = new Indicateur("EQ4 stock chocolat", this, 100);
		Monde.LE_MONDE.ajouterIndicateur(this.iStockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
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
		
		CHOCOLATS_VENTE = new ArrayList<Chocolat>();
		CHOCOLATS_VENTE.add(Chocolat.HG_E_SHP);
		CHOCOLATS_VENTE.add(Chocolat.MG_E_SHP);
		CHOCOLATS_VENTE.add(Chocolat.MG_NE_SHP);
		
		FEVES_ACHAT = new ArrayList<Feve>();
		FEVES_ACHAT.add(Feve.CRIOLLO_HG_EQ);
		FEVES_ACHAT.add(Feve.FORASTERO_MG_EQ);
		FEVES_ACHAT.add(Feve.FORASTERO_MG_NEQ);
		FEVES_ACHAT.add(Feve.MERCEDES_MG_EQ);
		FEVES_ACHAT.add(Feve.MERCEDES_MG_NEQ);
		FEVES_ACHAT.add(Feve.TRINITARIO_MG_EQ);
		FEVES_ACHAT.add(Feve.TRINITARIO_MG_NEQ);
		
		contratsFevesEnCours = new ArrayList<ContratCadre<Feve>>();
		contratsChocolatEnCours = new ArrayList<ContratCadre<Chocolat>>();
		
		acheteurCC = new Transformateur2AcheteurCC(this);
		vendeurCC = new Transformateur2VendeurCC(this);
		
		// Initialisation des stocks
		stocksChocolat = new StockProduit<Chocolat>(CHOCOLATS_VENTE);
		stockFeves = new StockProduit<Feve>(FEVES_ACHAT);
	}

	public void next() {
		// transformation
		double quantiteTransformee = Math.random()*Math.min(100, this.iStockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
		this.iStockFeves.retirer(this, quantiteTransformee);
		this.iStockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
		this.soldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}

	public double calculerPrixProdReel(Chocolat c, double qte) {
		// TODO
		// Trouver la recette produisant ce chocolat
		// Obtenir le prix des matieres premieres en remontant au bon stock de feves (renvoyer infini si pas assez)
		
		return 0;
	}
	
	/** Fonctions relatives à IAcheteurContratCadre<Feve> */
	public ContratCadre<Feve> getNouveauContrat() {return acheteurCC.getNouveauContrat();}
	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {acheteurCC.proposerEcheancierAcheteur(cc);}
	public void proposerPrixAcheteur(ContratCadre<Feve> cc) {acheteurCC.proposerPrixAcheteur(cc);}
	public void notifierAcheteur(ContratCadre<Feve> cc) {acheteurCC.notifierAcheteur(cc);}
	public void receptionner(Feve p, double qte, ContratCadre<Feve> cc) {acheteurCC.receptionner(p, qte, cc);}
	public double payer(double montant, ContratCadre<Feve> cc) {return acheteurCC.payer(montant, cc);}
	
	/** Fonctions relatives à IVendeurContratCadre<Chocolat> */
	public List<Chocolat> getProduitsEnVente() {return vendeurCC.getProduitsEnVente();}
	public StockEnVente<Chocolat> getStockEnVente() {return vendeurCC.getStockEnVente();}
	public double getPrix(Chocolat produit, Double quantite) {return vendeurCC.getPrix(produit, quantite);}
	public void proposerEcheancierVendeur(ContratCadre<Chocolat> cc) {vendeurCC.proposerEcheancierVendeur(cc);}
	public void proposerPrixVendeur(ContratCadre<Chocolat> cc) {vendeurCC.proposerPrixVendeur(cc);}
	public void notifierVendeur(ContratCadre<Chocolat> cc) {vendeurCC.notifierVendeur(cc);}
	public double livrer(Chocolat p, double qte, ContratCadre<Chocolat> cc) {return vendeurCC.livrer(p, qte, cc);}
	public void encaisser(double montant, ContratCadre<Chocolat> cc) {vendeurCC.encaisser(montant, cc);}
	
	

}