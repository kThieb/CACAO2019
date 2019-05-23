package abstraction.eq6Distributeur2;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Gamme;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;


public class Distributeur2 implements IActeur, IAcheteurContratCadre<Chocolat>, IDistributeurChocolat {


	private List<ContratCadre<Chocolat>> contratsEnCours;
	private Indicateur stock;

	private int numero;

	
	private HashMap<Chocolat,Double> margeParProduit;
	private double marge;
	private Indicateur soldeBancaire;
	
	private Indicateur stockMG_E_SHP;
	private Indicateur stockMG_NE_SHP;
	private Indicateur stockMG_NE_HP;
	private Indicateur stockHG_E_SHP;
	
	private Indicateur prixMG_E_SHP;
	private Indicateur prixMG_NE_SHP;
	private Indicateur prixMG_NE_HP;;
	private Indicateur prixHG_E_SHP;
	
	private Journal journal;
	
	private HashMap<Chocolat,Double> prixParProduit;
	private StockEnVente<Chocolat> stockEnVente; 

	
	public Distributeur2() {
		
		//NORDIN et Caroline

		
		this.soldeBancaire = new Indicateur("SoldeBancaire", this, 1000);

		// Partie se référant au journal
		this.journal = new Journal ("Marché du Chocolat");
		this.soldeBancaire = new Indicateur("EQ6 Solde Bancaire", this, 10000);

		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		
		//Chnager par nom du chocolat pour que le getNom de indcateur renvoie le type chocolat
		this.stockMG_E_SHP = new Indicateur("EQ6 " + Chocolat.MG_E_SHP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.stockMG_E_SHP);
		this.stockMG_NE_SHP = new Indicateur("EQ6 " + Chocolat.MG_NE_SHP.toString(), this,10);
		Monde.LE_MONDE.ajouterIndicateur(this.stockMG_NE_SHP);
		this.stockMG_NE_HP = new Indicateur("EQ6 " + Chocolat.MG_NE_HP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.stockMG_NE_HP);
		this.stockHG_E_SHP = new Indicateur("EQ6 "+ Chocolat.HG_E_SHP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.stockHG_E_SHP);
		
		this.prixMG_E_SHP = new Indicateur("EQ6 " + Chocolat.MG_E_SHP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.prixMG_E_SHP);
		this.prixMG_NE_SHP = new Indicateur("EQ6 " + Chocolat.MG_NE_SHP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.prixMG_NE_SHP);
		this.prixMG_NE_HP = new Indicateur("EQ6 "+ Chocolat.MG_NE_HP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.prixMG_NE_HP);
		this.prixHG_E_SHP = new Indicateur("EQ6 " + Chocolat.HG_E_SHP.toString(), this, 10);
		Monde.LE_MONDE.ajouterIndicateur(this.prixHG_E_SHP);
		
		this.journal = new Journal("Journal EQ6");
		Monde.LE_MONDE.ajouterJournal(this.journal);
	
		this.contratsEnCours = new ArrayList<ContratCadre<Chocolat>>();
		
		this.marge = 1.5; 

	}


	/**
	 * @return the soldeBancaire
	 */
	public Indicateur getSoldeBancaire() {
		return soldeBancaire;
	}


	/**
	 * @return the prixMG_E_SHP
	 */
	public Indicateur getPrixMG_E_SHP() {
		return prixMG_E_SHP;
	}

	/**
	 * @return the prixMG_NE_SHP
	 */
	public Indicateur getPrixMG_NE_SHP() {
		return prixMG_NE_SHP;
	}

	/**
	 * @return the prixMG_NE_HP
	 */
	public Indicateur getPrixMG_NE_HP() {
		return prixMG_NE_HP;
	}

	/**
	 * @return the prixHG_E_SHP
	 */
	public Indicateur getPrixHG_E_SHP() {
		return prixHG_E_SHP;
	}

	
	/**
	 * @return the stockMG_E_SHP
	 */
	public Indicateur getStockMG_E_SHP() {
		return stockMG_E_SHP;
	}

	/**
	 * @return the stockMG_NE_SHP
	 */
	public Indicateur getStockMG_NE_SHP() {
		return stockMG_NE_SHP;
	}

	/**
	 * @return the stockMG_NE_HP
	 */
	public Indicateur getStockMG_NE_HP() {
		return stockMG_NE_HP;
	}

	/**
	 * @return the stockHG_E_SHP
	 */
	public Indicateur getStockHG_E_SHP() {
		return stockHG_E_SHP;
	}
	//Nordin et Caro
	public Indicateur getIndicateurStock(Chocolat c) {
		if (c.getGamme()==Gamme.MOYENNE && (c.isEquitable()) && (c.isSansHuileDePalme())) {
			return this.stockMG_E_SHP;
		}
		if (c.getGamme()==Gamme.MOYENNE && !(c.isEquitable()) && (c.isSansHuileDePalme())) {
			return this.stockMG_NE_SHP;
		}
		if (c.getGamme()==Gamme.MOYENNE && !(c.isEquitable()) && !(c.isSansHuileDePalme())){ 
			return this.stockMG_NE_HP;
		}
		if (c.getGamme()==Gamme.HAUTE && (c.isEquitable()) && (c.isSansHuileDePalme())){
			return this.stockHG_E_SHP;
		}
		else {
			return null;
		}
	}
	//Nordin et Caro
	public Indicateur getIndicateurPrix (Chocolat c) {
		
		if (c.getGamme()==Gamme.MOYENNE && (c.isEquitable()) && (c.isSansHuileDePalme())) {
			return this.prixMG_E_SHP;
		}
		if (c.getGamme()==Gamme.MOYENNE && !(c.isEquitable()) && (c.isSansHuileDePalme())) {
			return this.prixMG_NE_SHP;
		}
		if (c.getGamme()==Gamme.MOYENNE && !(c.isEquitable()) && !(c.isSansHuileDePalme())){ 
			return this.prixMG_NE_HP;
		}
		if (c.getGamme()==Gamme.HAUTE && (c.isEquitable()) && (c.isSansHuileDePalme())){
			return this.prixHG_E_SHP;
		}
		else {
			return null;
		}
		
	}
	
	public List<ContratCadre<Chocolat>> getContratsEnCours() {
		return this.contratsEnCours;
	}
	
	public HashMap<Chocolat,Double> getPrixParProduit () {	
		//Caroline
		this.prixParProduit =  new HashMap<Chocolat,Double>();
		this.prixParProduit.put(Chocolat.HG_E_SHP, this.getPrixHG_E_SHP().getValeur());
		this.prixParProduit.put(Chocolat.MG_E_SHP, this.getPrixMG_E_SHP().getValeur());
		this.prixParProduit.put(Chocolat.MG_NE_SHP,this.getPrixMG_NE_SHP().getValeur());
		this.prixParProduit.put(Chocolat.MG_NE_HP, this.getPrixMG_NE_HP().getValeur());
		return this.prixParProduit;
	}
	
	public StockEnVente<Chocolat> getStockEnVente() {
		//NORDIN
		this.stockEnVente = new StockEnVente<Chocolat>();
		this.stockEnVente.ajouter(Chocolat.HG_E_SHP, this.getStockHG_E_SHP().getValeur());
		this.stockEnVente.ajouter(Chocolat.MG_E_SHP, this.getStockMG_E_SHP().getValeur());
		this.stockEnVente.ajouter(Chocolat.MG_NE_SHP,this.getStockMG_NE_SHP().getValeur());
		this.stockEnVente.ajouter(Chocolat.MG_NE_HP, this.getStockMG_NE_HP().getValeur());
		return this.stockEnVente;
	}
	
	public double getMarge() {
		return marge;
	}

	public void setMarge(double marge) {
		this.marge = marge;
	}

	public String getNom() {
		return "Walmart";

	}

	public void initialiser() {
	}

	public void next() {
		
	}
	
	//Nordin
	public double getPrix(Chocolat c) {
		return (this.getPrixParProduit().containsKey(c)? this.prixParProduit.get(c) : 0.0);
	}

	//Nordin
	public double vendre(Chocolat c, double quantite) {
		for (Chocolat chocolat : this.getStockEnVente().getProduitsEnVente()) {
		if (!c.equals(chocolat)) {
			this.journal.ajouter("vente de 0.0 (produit demande = "+c+ " vs produit dispo = "+chocolat+")");
			return 0.0;
		}
		}
		Double q = Math.min(this.getStockEnVente().get(c), quantite);
		this.getIndicateurStock(c).retirer(this, q);
		this.soldeBancaire.ajouter(this, this.getPrix(c));
		this.journal.ajouter("Vente de "+q+" a "+this.getPrix(c));
		return q;
		
	}


	/** 
	 * Retire de la liste des contrats en cours les contrats pour lesquels la quantite a livrer 
	 * est nulle et le montant a regler est egalement nul (toutes les livraisons et tous les paiements
	 * ont ete effectues).
	 */
	public void retireVieuxContrats() {
		List<ContratCadre<Chocolat>> aEnlever = new ArrayList<ContratCadre<Chocolat>>();
		for (ContratCadre<Chocolat> c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()<=0.0 && c.getMontantRestantARegler()<=0.0) {
				aEnlever.add(c);
			}
		}
		for (ContratCadre<Chocolat> c : aEnlever) {
			this.contratsEnCours.remove(c);
		}
	}

	public HashMap<Chocolat, Double> prevision_variation_stock () {
		HashMap<Chocolat, Double> variations_produit= new HashMap<Chocolat, Double>();
		
		if ( stockMG_E_SHP.getHistorique().getTaille() -2 > 0 ) {
			double variation_stockMG_E_SHP = stockMG_E_SHP.getHistorique().get(stockMG_E_SHP.getHistorique().getTaille() -2).getValeur() - stockMG_E_SHP.getValeur();
		    variations_produit.put(Chocolat.MG_E_SHP, -1*variation_stockMG_E_SHP);
		} else {
			variations_produit.put(Chocolat.MG_E_SHP, 0.0);
			} 
		
		if ( stockMG_NE_SHP.getHistorique().getTaille() -2 > 0 ) {
			double variation_stockMG_NE_SHP = stockMG_NE_SHP.getHistorique().get(stockMG_NE_SHP.getHistorique().getTaille() -2).getValeur() - stockMG_NE_SHP.getValeur();
			variations_produit.put(Chocolat.MG_NE_SHP, -1*variation_stockMG_NE_SHP);
		} else {
			variations_produit.put(Chocolat.MG_NE_SHP, 0.0);
		}
		
		if ( stockMG_NE_HP.getHistorique().getTaille() -2 > 0 ) {
			double variation_stockMG_NE_HP = stockMG_NE_HP.getHistorique().get(stockMG_NE_HP.getHistorique().getTaille() -2).getValeur() - stockMG_NE_HP.getValeur();
			variations_produit.put(Chocolat.MG_NE_HP, -1*variation_stockMG_NE_HP);
		} else {
			variations_produit.put(Chocolat.MG_NE_HP,0.0);
		}
		if ( stockHG_E_SHP.getHistorique().getTaille() -2 > 0 ) {
			double variation_stockHG_E_SHP = stockHG_E_SHP.getHistorique().get(stockHG_E_SHP.getHistorique().getTaille() -2).getValeur() - stockHG_E_SHP.getValeur();
			variations_produit.put(Chocolat.HG_E_SHP, -1*variation_stockHG_E_SHP);	
		} else {
			variations_produit.put(Chocolat.HG_E_SHP,0.0);
		}
	    
		for (ContratCadre c  : this.getContratsEnCours()) {
			Chocolat ch = (Chocolat) c.getProduit();
			//10 steps pour le contrat 
			double d = c.getEcheancier().getQuantiteTotale()/10;
			variations_produit.put(ch, d);
		}
		
		return variations_produit;
	}
	

	@Override
	public ContratCadre<Chocolat> getNouveauContrat() { //ILIAS
		ContratCadre<Chocolat> res=null;

		double solde = this.getSoldeBancaire().getValeur();
		for (ContratCadre<Chocolat> cc : this.getContratsEnCours()) {
			solde = solde - cc.getMontantRestantARegler();
		}
		
		//Choix du produit 
		HashMap<Chocolat, Double> variations_produit=this.prevision_variation_stock ();
		
		double min = 5000000;
		Chocolat produit = null;
		for (Chocolat c : variations_produit.keySet()) {
			if (variations_produit.get(c) < min) {
				min = variations_produit.get(c);
				produit = c;
			}
		}
		if (variations_produit.get(produit) + this.getStockEnVente().get(produit) > 500) {
			return null;
		}
		
		double quantite = 500 - this.getStockEnVente().get(produit) - variations_produit.get(produit);
		
		if (solde >1000) {
			List<IVendeurContratCadre<Chocolat>> vendeurs = new ArrayList<IVendeurContratCadre<Chocolat>>();
			for (IActeur acteur : Monde.LE_MONDE.getActeurs()) {
				if (acteur instanceof IVendeurContratCadre) {
					IVendeurContratCadre vacteur = (IVendeurContratCadre)acteur;
					if (vacteur.getStockEnVente().get(produit) >= quantite - 50) {
						vendeurs.add((IVendeurContratCadre<Chocolat>)vacteur);
					}
					}
				}
 
			double meilleurprix = 500000;
			IVendeurContratCadre<Chocolat> vendeur = null;
			for (IVendeurContratCadre<Chocolat> v : vendeurs) {
				if (v.getPrix(produit, quantite) < meilleurprix) {
					vendeur = v;
				}
			}
            if (vendeur != null & produit != null && quantite != 0) {
            	res = new ContratCadre<Chocolat>(this, vendeur, produit, vendeur.getStockEnVente().get(produit));
            }
            else { res = null;}
		
		}
		return res; 
	}

	@Override
	//Caroline 
	public void proposerEcheancierAcheteur(ContratCadre<Chocolat> cc) {
		if (cc!=null) {
			if (cc.getEcheancier()==null) { // il n'y a pas encore eu de contre-proposition de la part du vendeur
				cc.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), 10, cc.getQuantite()/10));
		}   else {
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la contre-proposition du vendeur 
				}
		}
	}
	
	//Caroline
	public boolean satisfaitParPrixContratCadre (ContratCadre<Chocolat> cc) {
		boolean satisfait = true;
		Chocolat produit = cc.getProduit();
		
		double dernierprixpropose = cc.getPrixAuKilo();
		double notreprix = this.getPrix(produit);
		
		if (notreprix/dernierprixpropose >= this.getMarge()) {
			satisfait = true;
		}else {
			satisfait = false;
		}
		return satisfait;
	}


	
	@Override
	//Caroline
	public void proposerPrixAcheteur(ContratCadre<Chocolat> cc) {
		//Si le dernier prix de la liste nous satisfait => proposer le même prix
		//Sinon, le dernier prix nous satisfait pas :
			//Si le vendeur propose 2 fois le même prix et pas satisfait => ne pas ajouter de prix
			// Sinon proposer un nouveau prix 
		
		if (cc!=null && 25 > cc.getListePrixAuKilo().size()) {
			if (satisfaitParPrixContratCadre (cc)) {
				cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				this.getIndicateurPrix(cc.getProduit()).ajouter(this,cc.getPrixAuKilo()*this.getMarge());
				this.journal.ajouter("Accord sur Prix sur contrat" + cc.toString());
			} else {
					if (cc.getListePrixAuKilo().size() >= 3 ) {
						cc.ajouterPrixAuKilo(cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size() -2)*1.05);
					}else {
						cc.ajouterPrixAuKilo(this.getIndicateurPrix(cc.getProduit()).getValeur());
				}	}}}

	@Override//Caroline
	public void notifierAcheteur(ContratCadre<Chocolat> cc) {
		cc.signer();
		this.journal.ajouter("Nouveau Contrat" + cc.toString());
		if (cc!=null) {
			this.getContratsEnCours().add(cc);
		}
	}

	@Override//Caroline
	public void receptionner(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		boolean penality = false;
		this.journal.ajouter("Réception du produit" + produit.toString() + "en quantité" + quantite + "au sujet du contrat " + cc.toString());
		if (cc != null && quantite >0 && cc.getProduit().equals(produit)) {
			
			if (quantite != cc.getEcheancier().getQuantite(Monde.LE_MONDE.getStep())) {
				penality = true;
			}
		
			this.getIndicateurStock(cc.getProduit()).ajouter(this, quantite);
			
			cc.livrer(quantite);
			
			if (penality) {
				cc.penaliteLivraison();
			} 	
		}
	}

	@Override//Caroline
	public double payer(double montant, ContratCadre<Chocolat> cc) {
		double montantpaye = 0;
		
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode payer avec un montant negatif");
		}
		
		if (cc!=null && this.soldeBancaire.getValeur() >montant + 100 ) {
			this.soldeBancaire.retirer(this,montant);
			cc.payer(montant);
			montantpaye = montant;
		} 
		else {
			if (montant-100>0) {
				this.soldeBancaire.retirer(this,montant-100);
				cc.payer(  montant-100);
				montantpaye = montant-100;
			} else {
				cc.payer(0.0);
			}
			
			cc.penalitePaiement();
		}
		this.journal.ajouter(montantpaye + "sur le contrat" + cc.toString());
		return montantpaye;
	}
	
	
	public ArrayList<Double> evaluation_produit (Chocolat c) {
		ArrayList<Double> L = new ArrayList<Double>(); 
		L.add(this.getPrix(c));
		L.add(this.getStockEnVente().get(c));
		L.add(c.getQualite());
		return L;
		
	}
	
	
}
