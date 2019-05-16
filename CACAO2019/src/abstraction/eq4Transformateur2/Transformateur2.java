package abstraction.eq4Transformateur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class Transformateur2 implements IActeur, IAcheteurContratCadre<Feve>, IVendeurContratCadre<Chocolat> {
	private Indicateur iStockFeves;
	private Indicateur iStockChocolat;
	
	private Indicateur soldeBancaire;

	private Journal journal;
	
	private HashMap<Chocolat, Stock> stocksChocolat;
	private HashMap<Feve, Stock> stockFeves;
	
	private List<ContratCadre<Feve>> contratsFevesEnCours;
	private List<ContratCadre<Chocolat>> contratsChocolatEnCours;
	
	private HashMap<Chocolat,Double> stockEnVente;
	
	public Transformateur2() {
		this.iStockFeves=new Indicateur("EQ4 stock feves", this, 50);
		this.soldeBancaire=new Indicateur("EQ4 solde bancaire", this, 100000);
		this.iStockChocolat=new Indicateur("EQ4 stock chocolat", this, 100);
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
		
		contratsFevesEnCours = new ArrayList<ContratCadre<Feve>>();
		contratsChocolatEnCours = new ArrayList<ContratCadre<Chocolat>>();
		
		
		// Initialisation des stocks
		stocksChocolat = new HashMap<Chocolat, Stock>();
		stockFeves = new HashMap<Feve, Stock>();
		for(int i = 0; i < Chocolat.values().length; i++)
			stocksChocolat.put(Chocolat.values()[i], new Stock());
		for(int i = 0; i < Feve.values().length; i++)
			stockFeves.put(Feve.values()[i], new Stock());
	}

	public void next() {
		// transformation
		double quantiteTransformee = Math.random()*Math.min(100, this.iStockFeves.getValeur()); // on suppose qu'on a un stock infini de sucre
		this.iStockFeves.retirer(this, quantiteTransformee);
		this.iStockChocolat.ajouter(this, (2*quantiteTransformee));// 50% cacao, 50% sucre
		this.soldeBancaire.retirer(this, quantiteTransformee*1.0234); // sucre, main d'oeuvre, autres frais
	}

	
	/*****************************************************
	 Fonctions relatives à IAcheteurContratCadre<Feve>
	 *****************************************************/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ContratCadre<Feve> getNouveauContrat() {
		// Calcul du solde que l'on peut dépenser en fonction des contrats sortants et entrants
		double solde = this.soldeBancaire.getValeur();
		for(ContratCadre<Feve> cc : this.contratsFevesEnCours)
			solde -= cc.getMontantRestantARegler();
		for(ContratCadre<Chocolat> cc : this.contratsChocolatEnCours)
			solde += cc.getMontantRestantARegler();


		List<IVendeurContratCadre> vendeurs = new ArrayList<IVendeurContratCadre>();
		
		// Liste des fèves que l'on souhaite acheter 
		List<Feve> fevesAchetees = new ArrayList<Feve>();
		fevesAchetees.add(Feve.CRIOLLO_HG_EQ);
		fevesAchetees.add(Feve.FORASTERO_MG_EQ);
		fevesAchetees.add(Feve.MERCEDES_MG_EQ);
		fevesAchetees.add(Feve.TRINITARIO_MG_EQ);
		
		final double POIDS_MIN_CONTRAT = 150.0; // pas de contrats de moins de 150 kg
		
		// Choix du vendeur : on regroupe d'abord tous les vendeurs vérifiant les conditions souhaitées
		for(IActeur a : Monde.LE_MONDE.getActeurs()) {
			if(a instanceof IVendeurContratCadre) {
				StockEnVente sev = ((IVendeurContratCadre) a).getStockEnVente();
				
				List<Object> produits = sev.getProduitsEnVente();
				for(Object o : produits) {
					if(fevesAchetees.contains(o) && sev.get(o) >= POIDS_MIN_CONTRAT)
						vendeurs.add((IVendeurContratCadre) a);
				}
			}
		}
		
		// On choisit ensuite l'un des vendeurs, si possible
		if(vendeurs.size() > 0) {
			IVendeurContratCadre vendeur = vendeurs.get((int) (Math.random() * vendeurs.size()));
			
			// Construction d'une liste des produits que l'on est susceptibles d'acheter à ce vendeur
			List<Feve> produitsInteressants = new ArrayList<Feve>();
			for(Object o : vendeur.getStockEnVente().getProduitsEnVente()) {
				if(fevesAchetees.contains(o) && vendeur.getStockEnVente().get(o) >= POIDS_MIN_CONTRAT)
					produitsInteressants.add((Feve) o);
			}
			// Choix du produit à acheter (pour l'instant : le produit que l'on a en quantité la plus faible)
			double minStock = 0;
			Feve minProduit = produitsInteressants.get(0);
			for(Feve f : produitsInteressants) {
				double stock = stockFeves.get(f).getQuantité();
				if(stock < minStock) {
					minStock = stock;
					minProduit = f;
				}
			}
			
			double qté = Math.min(20e3, vendeur.getStockEnVente().get(minProduit)); // on achete au maximum 20 tonnes
			double prix = vendeur.getPrix(minProduit, qté);			
			// On réduit la quantité achetée tant que le prix est supérieur à 60% de notre solde
			while(qté * prix > solde * 0.60) {
				qté *= 0.8;
				prix = vendeur.getPrix(minProduit, qté);
			}
			qté /= 0.8;

			return new ContratCadre<Feve>(this, vendeur, minProduit, qté);	
		}
		else
			return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {
		if(cc.getEcheancier() == null) { // il n'y a pas encore eu de contre-proposition de la part du vendeur
			// On répartit la quantité sur 12 steps (3 mois)
			cc.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), 12, cc.getQuantite() / 12));
		} 
		else {
			Echeancier ech = cc.getEcheancier();
			int duree = ech.getNbEcheances();
			final int DUREE_MAX = 4*6; // on n'accepte pas d'échéancier sur plus de 6 mois : on diminue plutôt la quantité totale
			if(duree > DUREE_MAX)
				cc.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), DUREE_MAX, (cc.getQuantite()*0.75)/DUREE_MAX));
			else {
				// On essaye de se rapprocher d'un échéancier de 12 steps
				if(duree > 12)
					cc.ajouterEcheancier(new Echeancier(ech.getStepDebut(), ech.getNbEcheances()-1, cc.getQuantite()/(ech.getNbEcheances()-1)));
				else if(duree < 12) 
					cc.ajouterEcheancier(new Echeancier(ech.getStepDebut(), ech.getNbEcheances()+1, cc.getQuantite()/(ech.getNbEcheances()+1)));
			}
		}
	}

	@Override
	public void proposerPrixAcheteur(ContratCadre<Feve> cc) {
		// TODO Stocker le prix du dernier achat de ce produit et l'utiliser comme référence 
		// (éviter d'acheter plus de 10% plus haut que l'achat le moins cher de ce produit, par exemple)
		double prixVendeur = cc.getListePrixAuKilo().get(0);
		if (Math.random() < 0.25) // probabilite de 25% d'accepter
			cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
		else {
			// On essaye de diminuer le prix (de 15% max)
			final double REDUCTION_MAX = 0.15;
			cc.ajouterPrixAuKilo((prixVendeur - prixVendeur*Math.random()*REDUCTION_MAX));
		}
	}

	@Override
	public void notifierAcheteur(ContratCadre<Feve> cc) {
		this.contratsFevesEnCours.add(cc);
	}

	@Override
	public void receptionner(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if(quantite <= 0.0)
			throw new IllegalArgumentException("Appel de la méthode réceptionner de Transformateur2 avec une quantité égale à " + quantite);
		
		this.stockFeves.get(produit).add(quantite);		
	}

	@Override
	public double payer(double montant, ContratCadre<Feve> cc) {
		if(montant <= 0.0)
			throw new IllegalArgumentException("Appel de la methode payer de Transformateur2 avec un montant negatif = " + montant);
		
		double paiement = Math.min(montant,  this.soldeBancaire.getValeur());
		this.soldeBancaire.retirer(this,  paiement);
		return paiement;
	}

	/*****************************************************
	 Fonctions relatives à IVendeurContratCadre<Chocolat>
	 *****************************************************/


	public List<Chocolat> getProduitsEnVente() {
		ArrayList<Chocolat> chocolat = new ArrayList<Chocolat>();
		chocolat.addAll(this.stockEnVente.keySet());
		return chocolat;
	}

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		StockEnVente<Chocolat> sev = new StockEnVente<Chocolat>();
		sev.ajouter(Chocolat.HG_E_SHP, 12000.0);
		return sev;
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