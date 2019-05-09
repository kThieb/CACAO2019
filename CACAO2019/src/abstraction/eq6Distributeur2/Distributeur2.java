package abstraction.eq6Distributeur2;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
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

	private Indicateur soldeBancaire;
	private Chocolat mG_E_SHP;
	private Chocolat mG_NE_SHP;
	private Chocolat mG_NE_HP;;
	private Chocolat hG_E_SHP;
	private Indicateur stockMG_E_SHP;
	private Indicateur stockMG_NE_SHP;
	private Indicateur stockMG_NE_HP;;
	private Indicateur stockHG_E_SHP;
	private Journal journal;
	private HashMap prixParProduit;
	private double marge;
	private StockEnVente<Chocolat> stockEnVente; 

	public Distributeur2() {
	}
	
	public List<ContratCadre<Chocolat>> getContratsEnCours() {
		return this.contratsEnCours;
	}
	
	public HashMap getPrixParProduit () {
		return this.prixParProduit;
	}
	
	public Distributeur2(Chocolat mG_E_SHP, Chocolat mG_NE_SHP, Chocolat mG_NE_HP,
			Chocolat hG_E_SHP, double stockMG_E_SHP, double stockMG_NE_SHP, double stockMG_NE_HP,
			double stockHG_E_SHP, Indicateur soldeinitial, Journal journal, List<ContratCadre<Chocolat>> contratsEnCours, Double marge ) {
		//NORDIN
		this.soldeBancaire = soldeBancaire;
		mG_E_SHP = mG_E_SHP;
		mG_NE_SHP = mG_NE_SHP;
		mG_NE_HP = mG_NE_HP;
		hG_E_SHP = hG_E_SHP;
		this.stockMG_E_SHP = new Indicateur(this.getNom()+" Stock de moyenne-gamme, équitable et sans huile de palme ", this, stockMG_E_SHP);
		Monde.LE_MONDE.ajouterIndicateur(this.stockMG_E_SHP);
		this.stockMG_NE_SHP = new Indicateur(this.getNom()+" Stock de moyenne-gamme et sans huile de palme ", this, stockMG_NE_SHP);
		Monde.LE_MONDE.ajouterIndicateur(this.stockMG_NE_SHP);
		this.stockMG_NE_HP = new Indicateur(this.getNom()+" Stock de moyenne-gamme  ", this, stockMG_NE_HP);
		Monde.LE_MONDE.ajouterIndicateur(this.stockMG_NE_HP);
		this.stockHG_E_SHP = new Indicateur(this.getNom()+" Stock de haute-gamme", this, stockHG_E_SHP);
		Monde.LE_MONDE.ajouterIndicateur(this.stockHG_E_SHP);
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.contratsEnCours = new ArrayList<ContratCadre<Chocolat>>();
		this.marge = marge; 

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

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		//NORDIN 
		StockEnVente<Chocolat> stockEnVente = new StockEnVente<Chocolat>();
		stockEnVente.ajouter(mG_E_SHP, this.stockMG_E_SHP.getValeur());
		stockEnVente.ajouter(mG_NE_SHP, this.stockMG_NE_SHP.getValeur());
		stockEnVente.ajouter(mG_NE_HP, this.stockMG_NE_HP.getValeur());
		stockEnVente.ajouter(mG_E_SHP, this.stockMG_NE_SHP.getValeur());
		return stockEnVente;		
	}

	@Override
	public double getPrix(Chocolat c) {
		//NORDIN
		/*for (Chocolat chocolat : this.stockEnVente.getProduitsEnVente() ) {
			
		}
		if (this.contratsEnCours.size()==0) {
			return 40;
		} else {
			double prixMoyen = 0;
			for (ContratCadre<Chocolat> cc : this.contratsEnCours) {
				prixMoyen+=cc.getPrixAuKilo();
			}
			prixMoyen = prixMoyen/ this.contratsEnCours.size();
			return prixMoyen *(1.0+this.marge);
		}*/
		return 0;
	}

	@Override
	public double vendre(Chocolat c, double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ContratCadre<Chocolat> getNouveauContrat() { //ILIAS
		ContratCadre<Chocolat> res=null;
		double solde = this.soldeBancaire.getValeur();
		for (ContratCadre<Chocolat> cc : this.getContratsEnCours()) {
			solde = solde - cc.getMontantRestantARegler();
		}
		List<IVendeurContratCadre<Chocolat>> vendeurs = new ArrayList<IVendeurContratCadre<Chocolat>>();
		for (IActeur acteur : Monde.LE_MONDE.getActeurs()) {
			if (acteur instanceof IVendeurContratCadre) {
				IVendeurContratCadre vacteur = (IVendeurContratCadre)acteur;
				StockEnVente stock = vacteur.getStockEnVente();
				vendeurs.add((IVendeurContratCadre<Chocolat>)vacteur);
			}
		}
		if (vendeurs.size()>=1) {
			double quantite = 50;
			IVendeurContratCadre<Chocolat> vendeur = vendeurs.get( (int)( Math.random()*vendeurs.size()));
			double prix = vendeur.getPrix(this.produit, quantite);
			while (!Double.isNaN(prix)) {
				quantite=quantite*1.1;
				prix = vendeur.getPrix(this.produit,  quantite);
			}
			quantite = quantite/1.1;
			res = new ContratCadre<Chocolat>(this, vendeur, this.produit, quantite);
		}
		return res;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
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
		
		if (satisfaitParPrixContratCadre (cc)) {
			cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
		} else {
			if (cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size() -1)==cc.getListePrixAuKilo().get(cc.getListePrixAuKilo().size() -3)) {
				cc.ajouterPrixAuKilo(null);
			} else {
				cc.ajouterPrixAuKilo(cc.getPrixAuKilo()*0.95);
			}
		}
		
	}

	@Override
	public void notifierAcheteur(ContratCadre<Chocolat> cc) {
		if (cc!=null) {
			this.getContratsEnCours().add(cc);
		}
	}

	@Override
	public void receptionner(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double payer(double montant, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		return 0;
	}
}
