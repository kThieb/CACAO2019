package abstraction.eq3Transformateur1;

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

public class Transformateur1 implements IActeur, IAcheteurContratCadre<Feve>, IVendeurContratCadre<Chocolat>  {
	
//	private Indicateur iStockFeves;
//	private Indicateur iStockChocolat;
    private Indicateur soldeBancaire;
	private int nbNextAvantEchange;
	private Journal journal;
	//Begin Kevin
	private static final double PRIX_VENTE_PAR_DEFAUT = 40.0;
	//End Kevin
	
	//begin sacha
	private List<ContratCadre<Chocolat>> contratsChocolatEnCours;
	private List<ContratCadre<Feve>> contratsFeveEnCours;
	private Feve fevesAchetees;
	//end sacha
	//begin Raphael
	private Indicateur prixAchats;
	//end Raphael
	private HashMap<Chocolat,Stock> stockChocolat;
	private HashMap<Feve,Stock> stockFeves;
	
	public Transformateur1() {
		
		// begin eve
		this.stockFeves = new HashMap<Feve,Stock>();
		this.stockChocolat = new HashMap<Chocolat,Stock>();
//		int sommeFeves = 0;
//		this.iStockFeves = new Indicateur("EQ3 stock feves", this, sommeFeves);
//		int sommeChocolat = 0;
//		this.iStockChocolat = new Indicateur("EQ3 stock chocolat", this, sommeChocolat);
		// end eve
		this.soldeBancaire=new Indicateur("EQ3 solde bancaire", this, 100000);
		this.journal = new Journal ("Vente aléatoire de cacao");
		Monde.LE_MONDE.ajouterJournal(this.journal);
		System.out.println("ajout du journal jEq3");
//		Monde.LE_MONDE.ajouterIndicateur(this.iStockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
//		Monde.LE_MONDE.ajouterIndicateur(this.iStockChocolat);
		
		//begin sacha
		this.contratsChocolatEnCours = new ArrayList<ContratCadre<Chocolat>>();
		this.contratsFeveEnCours = new ArrayList<ContratCadre<Feve>>();
		this.fevesAchetees = fevesAchetees;
		//end sacha
		
		//begin Raphael
		this.prixAchats=new Indicateur("EQ3 prix achats", this);
		//end Raphael
		
		this.nbNextAvantEchange = 0;

	}
	
	// -------------------------------------------------------------------------------------------
	// 			GETTERS & SETTERS
	// -------------------------------------------------------------------------------------------
	
	public String getNom() {
		return "EQ3";
	}
	
	
	// -------------------------------------------------------------------------------------------
	// 			STEPS
	// -------------------------------------------------------------------------------------------
		
	public void initialiser() {
	}

	public void next() {
		// transformation
//		double quantiteTransformee = Math.random()*Math.min(100, this.iStockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
//		this.iStockFeves.retirer(this, quantiteTransformee);
//		this.iStockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
//		this.soldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}
	
	// -------------------------------------------------------------------------------------------
	// 			ACHETEUR
	// -------------------------------------------------------------------------------------------

	// A MODIFIER
	public double quantiteDesiree(double quantiteEnVente, double prix) {
		double possible = Math.max(0.0, soldeBancaire.getValeur()/prix);
		
		double desiree= Math.min(possible,  quantiteEnVente); // achete le plus possible
//		this.iStockFeves.ajouter(this, desiree);
		this.soldeBancaire.retirer(this, desiree*prix);
		return desiree;
	}
	
	@Override
	public ContratCadre<Feve> getNouveauContrat() {
		// TODO
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixAcheteur(ContratCadre<Feve> cc) {
		//begin raphael
		double prixVendeur = cc.getListePrixAuKilo().get(0);
		int nbAchatsMoyenne=Math.min(10,this.prixAchats.getHistorique().getTaille());//Nombre d'achats pris en compte pour le calcul de la moyenne (au plus 10)
		double moyenneDerniersAchats=0;
		for(int i=0;i<nbAchatsMoyenne;i++) {//Calcul de la moyenne des derniers prix d'achat
			moyenneDerniersAchats+=this.prixAchats.getHistorique().get(i).getValeur();
		}
		moyenneDerniersAchats=moyenneDerniersAchats/nbAchatsMoyenne;
		if (prixVendeur<moyenneDerniersAchats*1.1) { // On accepte les prix inférieurs à 110% du prix moyen des derniers achats
			cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
		} else {
			cc.ajouterPrixAuKilo(moyenneDerniersAchats); // Sinon on propose un achat au prix moyen d'achat des dernièrs achats
		}
		//end raphael
	}

	@Override
	public void notifierAcheteur(ContratCadre<Feve> cc) {
		// begin sacha
		this.contratsFeveEnCours.add(cc);
		//end sachaaa
	}

	@Override
	public void receptionner(Feve produit, double quantite, ContratCadre<Feve> cc) {
		// begin sacha
		if (produit==null || !produit.equals(this.fevesAchetees)) {
			throw new IllegalArgumentException("Appel de la methode receptionner de Transformateur1 avec un produit ne correspondant pas aux feves achetees par le transformateur");
		}
		if (quantite<=0.0) {
			throw new IllegalArgumentException("Appel de la methode receptionner de Transformateur1 avec une quantite egale a "+quantite);
		}
		this.stockFeves.ajou;
		
	}

	@Override
	public double payer(double montant, ContratCadre<Feve> cc) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// -------------------------------------------------------------------------------------------
	// 			VENDEUR
	// -------------------------------------------------------------------------------------------

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public double getPrix(Chocolat produit, Double quantite) {
		//Begin Kevin
		if (produit==null || quantite<=0.0 || this.getStockEnVente().get(produit)<quantite) {
			return Double.NaN;
		}
		if (this.contratsFeveEnCours.size()==0) {
			return PRIX_VENTE_PAR_DEFAUT;
		}
		else {
			double prixMoyen = 0;
			for (ContratCadre<Feve> cc : this.contratsFevesEnCours) {
				prixMoyen+=cc.getPrixAuKilo();
			}
			prixMoyen = prixMoyen/ this.contratsFevesEnCours.size();
			double prixProposé = 0 ;
			prixProposé = prixMoyen + prixMoyen*0.05
		}
		
		//End Kevin
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
