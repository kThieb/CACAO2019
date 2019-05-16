package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import abstraction.eq3Transformateur1.Stock;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
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
	private double marge;
	private static final double stockLim = 10000.0;
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
		
		// --------------------------------- begin eve
		
		// stock de feves
		this.stockFeves = new HashMap<Feve,Stock>();
		this.stockFeves.put(Feve.CRIOLLO_HG_EQ, new Stock(0));
		this.stockFeves.put(Feve.FORASTERO_MG_EQ, new Stock(0));
		this.stockFeves.put(Feve.FORASTERO_MG_NEQ, new Stock(0));
		this.stockFeves.put(Feve.MERCEDES_MG_EQ, new Stock(0));
		this.stockFeves.put(Feve.MERCEDES_MG_NEQ, new Stock(0));
		this.stockFeves.put(Feve.TRINITARIO_MG_EQ, new Stock(0));
		this.stockFeves.put(Feve.TRINITARIO_MG_NEQ, new Stock(0));
		
		// stock de chocolat
		this.stockChocolat = new HashMap<Chocolat,Stock>();
		this.stockChocolat.put(Chocolat.MG_NE_HP, new Stock(0));
		this.stockChocolat.put(Chocolat.MG_NE_SHP, new Stock(0));
		this.stockChocolat.put(Chocolat.MG_E_SHP, new Stock(0));
		
//		int sommeFeves = 0;
//		this.iStockFeves = new Indicateur("EQ3 stock feves", this, sommeFeves);
//		int sommeChocolat = 0;
//		this.iStockChocolat = new Indicateur("EQ3 stock chocolat", this, sommeChocolat);
		// --------------------------------- end eve
		
		
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

	// -------------------------- begin eve
	public double quantiteDesiree(double quantiteEnVente, double prix) {
		double possible = Math.max(0.0, soldeBancaire.getValeur()/prix);
		double desiree= Math.min(possible,  quantiteEnVente); // achete le plus possible
		return desiree;
	}
	// -------------------------- end eve
	
	@Override
	public ContratCadre<Feve> getNouveauContrat() {
		// begin sacha
		ContratCadre<Feve> res=null;
        // on determine combien il resterait sur le compte si on soldait tous les contrats en cours.
		double solde = this.soldeBancaire.getValeur();
		this.journal.ajouter("Determination du solde une fois tous les contrats en cours payes");
		this.journal.ajouter("- solde="+solde);
		for (ContratCadre<Feve> cc : this.contratsFeveEnCours) {
			this.journal.ajouter("- contrat #"+cc.getNumero()+" restant a regler ="+cc.getMontantRestantARegler());
			solde = solde - cc.getMontantRestantARegler();
		}
		this.journal.ajouter("--> solde="+solde);

		if (solde>10000.0) { // On ne cherche pas a etablir d'autres contrats d'achat si le compte bancaire est trop bas
			List<IVendeurContratCadre<Feve>> vendeurs = new ArrayList<IVendeurContratCadre<Feve>>();
            this.journal.ajouter("  recherche vendeur de "+this.fevesAchetees);
			for (IActeur acteur : Monde.LE_MONDE.getActeurs()) {
				if (acteur instanceof IVendeurContratCadre) {
					IVendeurContratCadre vacteur = (IVendeurContratCadre)acteur;
					StockEnVente stock = vacteur.getStockEnVente();
					if (stock.get(this.fevesAchetees)>=100.0) {// on souhaite faire des contrats d'au moins 100kg
						this.journal.ajouter("   "+(acteur.getNom())+" vend "+stock.get(this.fevesAchetees)+" de "+this.fevesAchetees);
						vendeurs.add((IVendeurContratCadre<Feve>)vacteur);
					} else {
						this.journal.ajouter("   "+(acteur.getNom())+" ne vend que "+stock.toHtml());
					}
				}
			}
			if (vendeurs.size()>=1) {
				IVendeurContratCadre<Feve> vendeur = vendeurs.get( (int)( Math.random()*vendeurs.size())); // ici tire au hasard plutot que de tenir compte des stocks en vente et des prix
				// On determine la quantite qu'on peut esperer avec le tiers du reste de notre solde bancaire
                this.journal.ajouter(" Determination de la quantite achetable avec une somme de "+String.format("%.3f",solde/3.0));
				double quantite = 100.0; // On ne cherche pas a faire de contrat pour moins de 100 kg
				double prix = vendeur.getPrix(this.fevesAchetees, quantite);
				while (!Double.isNaN(prix) && prix*quantite<solde/3.0 ) {
					quantite=quantite*1.5;
					prix = vendeur.getPrix(this.fevesAchetees,  quantite);
					this.journal.ajouter(" quantite "+String.format("%.3f",quantite)+" --> "+String.format("%.3f",quantite*prix));
				}
				quantite = quantite/1.5;
				res = new ContratCadre<Feve>(this, vendeur, this.fevesAchetees, quantite);
			} else {
				this.journal.ajouter("   Aucun vendeur trouve --> pas de nouveau contrat a ce step");
			}
		} else {
			this.journal.ajouter("   Il ne reste que "+solde+" une fois tous les contrats payes donc nous ne souhaitons pas en creer d'autres pour l'instant");
		}
		return res;
	}
	//end sacha

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {
		//Begin Kevin
		if (cc.getEcheancier()==null) { // il n'y a pas encore eu de contre-proposition de la part du vendeur
			cc.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), 12, cc.getQuantite()/12));
		} else {
			if ((this.contratsFeveEnCours.isEmpty())&&(this.stockFeves.get(cc.getProduit()).getQuantiteEnStock() < stockLim)) { // On accepte forcément la propostion si on a pas de contrat cadre en cours et que le stock est inférieur à une quantité arbitraire
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier()));
			} 
			if (Math.random() < 0.33) {
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); //1 chance sur 3 d'accepter l'échéancier (si la première condition n'est pas remplie)
			}      
			
			else { // 2 chance sur 3 de proposer un echeancier etalant sur un step de plus
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()+1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()+1)));
			}
		}
		//End Kevin
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
		this.stockFeves.put(produit, new Stock(quantite));
		
	}
//end sacha
	@Override
	public double payer(double montant, ContratCadre<Feve> cc) {
		// begin sacha
		if (montant<=0.0) {
			throw new IllegalArgumentException("Appel de la methode payer de Transformateur1 avec un montant negatif = "+montant);
		}
		double paiement = Math.min(montant,  this.soldeBancaire.getValeur());
		this.soldeBancaire.retirer(this,  paiement);
		return paiement;
	}
	
	// -------------------------------------------------------------------------------------------
	// 			VENDEUR
	// -------------------------------------------------------------------------------------------


	public StockEnVente<Chocolat> getStockEnVente() {
		StockEnVente<Chocolat> stock = new StockEnVente<Chocolat>();
		for (Entry<Chocolat, Stock> choco : this.stockChocolat.entrySet()) {
			stock.ajouter(choco.getKey(), choco.getValue().getQuantiteEnStock());
		};
		return stock;
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
			double prixMoyen = 0.0;
			for (ContratCadre<Feve> cc : this.contratsFeveEnCours) {
				prixMoyen+=cc.getPrixAuKilo();
			}

			prixMoyen = prixMoyen/ this.contratsFeveEnCours.size();
			return prixMoyen *(1.0+this.marge);
		}
		
		//End Kevin
	}

	@Override
	public void proposerEcheancierVendeur(ContratCadre<Chocolat> cc) {
		//Begin Kevin
		if (Math.random()<0.5) { // une chance sur deux d'accepter l'echeancier
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la proposition de l'acheteur car on a la quantite en stock 
		} else {
			if ((Math.random() < 0.5) && (cc.getEcheancier().getNbEcheances() > 1)) {
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()-1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()-1)));
			    // une chance sur deux de proposer  un echeancier etalant sur un step de moins quand c'est possible
			}
			else {
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()+1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()+1)));
				// une chance sur deux de proposer un echeancier etalant sur un step de plus
			}
		}
		//End Kevin
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
