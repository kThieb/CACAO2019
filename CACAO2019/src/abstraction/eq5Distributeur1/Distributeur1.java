package abstraction.eq5Distributeur1;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Distributeur1 implements IActeur, IAcheteurContratCadre, IDistributeurChocolat {
	private Journal journal;
	private ArrayList<Indicateur> stock;
	private int numero;
	private Indicateur soldeBancaire;
	private ArrayList<Chocolat> produits;
	private Double marge;
	private List<ContratCadre<Chocolat>> contratsEnCours;



	public Distributeur1() {
		this.journal = new Journal("jEq5");
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.produits = new ArrayList<Chocolat>();
		produits.add(Chocolat.HG_E_SHP);
		produits.add(Chocolat.MG_E_SHP);
		produits.add(Chocolat.MG_NE_HP);
		produits.add(Chocolat.MG_NE_SHP);
	}

	public Distributeur1(double marge, ArrayList<Double> stockInitial, Double soldeInitial) {
		this.numero =1 ;
		this.produits = new ArrayList<Chocolat>();
		produits.add(Chocolat.HG_E_SHP);
		produits.add(Chocolat.MG_E_SHP);
		produits.add(Chocolat.MG_NE_HP);
		produits.add(Chocolat.MG_NE_SHP);
		this.marge = marge;
		this.stock= new ArrayList<Indicateur>();
		for (int i = 0 ; i <produits.size(); i++) {
			this.stock.add(new Indicateur(this.getNom()+" Stock", this, stockInitial.get(i)));
			Monde.LE_MONDE.ajouterIndicateur(this.stock.get(i));
		}
		this.soldeBancaire = new Indicateur(this.getNom()+" Solde", this, soldeInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.contratsEnCours = new ArrayList<ContratCadre<Chocolat>>();
	}

	public String getNom() {
		return "EQ5";
	}

	public void initialiser() {
	}

	public void next() {
	}



	// ------------------------------------------------------------------------------------------------------
	// ACHETEUR
	// ------------------------------------------------------------------------------------------------------ 

	@Override
	public ContratCadre<Chocolat> getNouveauContrat() {
		// On va créer un nouveau contrat cadre 
		ContratCadre<Chocolat> ncc = null;
		// Au préalable, il faut identifier produit, quantité, vendeur, acheteur

		// On détermine combien il resterait sur le compte si on soldait tous les contrats en cours.
		double solde = this.soldeBancaire.getValeur();
		for (ContratCadre<Chocolat> cc : this.contratsEnCours) {
			solde = solde - cc.getMontantRestantARegler();
		}

		// On ne cherche pas a établir d'autres contrats d'achat si le compte bancaire est trop bas
		if (solde>10000.0) { 


			//Choix du produit : on choisit un produit au hasard parmi tous les produits
			ArrayList<Chocolat> produits = new ArrayList<Chocolat>();
			produits.add(Chocolat.HG_E_SHP);
			produits.add(Chocolat.MG_E_SHP);
			produits.add(Chocolat.MG_NE_HP);
			produits.add(Chocolat.MG_NE_SHP);
			Chocolat produit = produits.get((int) Math.random()*produits.size());


			//Choix quantité : on choisit le vendeur ayant la plus grande quantité du produit
			//Choix acteur
			List<IVendeurContratCadre<Chocolat>> vendeurs = new ArrayList<IVendeurContratCadre<Chocolat>>();
			for (IActeur acteur : Monde.LE_MONDE.getActeurs()) {
				if (acteur instanceof IVendeurContratCadre) {
					IVendeurContratCadre vacteur = (IVendeurContratCadre)acteur;
					StockEnVente stock = vacteur.getStockEnVente();
					if (stock.get(produit)>100.0) { // on souhaite faire des contrats d'au moins 100kg
						vendeurs.add((IVendeurContratCadre<Chocolat>)vacteur);
					}
				}
				if (vendeurs.size()>=1) {
					IVendeurContratCadre<Chocolat> vendeur = vendeurs.get( (int)( Math.random()*vendeurs.size())); // ici tire au hasard plutot que de tenir compte des stocks en vente et des prix
					// On détermine la quantité qu'on peut espérer avec le tiers du reste de notre solde bancaire
					double quantite = 100.0; // on souhaite faire des contrats d'au moins 100 kg
					double prix = vendeur.getPrix(produit, quantite);
					while (!Double.isNaN(prix) && prix<solde/3.0 ) {
						quantite=quantite*1.5;
						prix = vendeur.getPrix(produit,  quantite);
					}
					quantite = quantite/1.5;
					ncc = new ContratCadre<Chocolat>(this, vendeur, produit, quantite);

				} else {
					this.journal.ajouter("   Il ne reste que "+solde+" une fois tous les contrats payes donc nous ne souhaitons pas en creer d'autres pour l'instant");
				}
				
			}
		}
		//Création Contrat
		return ncc;
	}

			@Override
			public void proposerEcheancierAcheteur(ContratCadre C) {
				// TODO Auto-generated method stub
				if (C.getEcheancier()==null) {//pas de contre-proposition
					C.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), 20, C.getQuantite()/20));
				} else {
					C.ajouterEcheancier(new Echeancier(C.getEcheancier())); // accepter la contre-proposition
				}
			}

			@Override
			public void proposerPrixAcheteur(ContratCadre cc) {
				// TODO Auto-generated method stub
				double prixVendeur = cc.getPrixAuKilo();
				if (Math.random()<0.25) { // probabilite de 25% d'accepter
					cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				} else {
					cc.ajouterPrixAuKilo((prixVendeur*(0.9+Math.random()*0.1))); // Rabais de 10% max
				}
			}

			@Override
			public void notifierAcheteur(ContratCadre cc) {
				// TODO Auto-generated method stub

			}

			@Override
			public void receptionner(Object produit, double quantite, ContratCadre cc) {
				// TODO Auto-generated method stub

			}

			@Override
			public double payer(double montant, ContratCadre cc) {
				// TODO Auto-generated method stub
				return 0;
			}

			// ---------------------------------------------------------------------------------------------------------
			// VENDEUR CLIENT
			// ---------------------------------------------------------------------------------------------------------

			@Override
			public StockEnVente<Chocolat> getStockEnVente() {
				StockEnVente<Chocolat> res = new StockEnVente<Chocolat>();
				for (int i = 0 ; i< this.produits.size(); i++) {
					res.ajouter(produits.get(i), stock.get(i).getValeur());
				}
				return res;
			}

			@Override
			public double getPrix(Chocolat c) {
				boolean vendu = false;
				for (int i=0; i<this.produits.size();i++) {
					if (c.equals(this.produits.get(i))) {
						vendu = true;
					}
				}
				if (!vendu) {
					return Double.NaN;
				}

				if (this.contratsEnCours.size()==0) {
					return 50;
				} else {

					double prixMoyen = 0;
					for (ContratCadre<Chocolat> cc : this.contratsEnCours) {
						if (cc.getProduit()==c) {
							prixMoyen+=cc.getPrixAuKilo();
						}
					}
					prixMoyen = prixMoyen/ this.contratsEnCours.size();
					return prixMoyen *(1.0+this.marge);
				}
			}

			@Override
			public double vendre(Chocolat c, double quantite) {
				// TODO Auto-generated method stub
				return 0;
			}
		}