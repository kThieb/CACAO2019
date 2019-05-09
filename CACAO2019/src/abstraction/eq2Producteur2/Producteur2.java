package abstraction.eq2Producteur2;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IVendeurCacaoAleatoire;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur2 implements IActeur, IVendeurCacaoAleatoire, IVendeurContratCadre<Feve> {
	
	private Indicateur stockFeves;
	private Indicateur soldeBancaire;

	public Producteur2() {
		this.stockFeves=new Indicateur("EQ2 stock feves", this, 1000);
		this.soldeBancaire=new Indicateur("EQ2 solde bancaire", this, 50000);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Journal journal=new Journal("jEq2");
		Monde.LE_MONDE.ajouterJournal(journal);
		
	}
	
	public String getNom() {
		return "EQ2";
	}

	public void initialiser() {
	}

	public void next() {
		// production
		double nouveauStock = this.stockFeves.getValeur() + Math.random()*300;
		this.stockFeves.setValeur(this, nouveauStock);
	}

	public double quantiteEnVente(double prix) {
		return this.stockFeves.getValeur();
	}
	public void notificationVente(double quantite, double prix) {
		this.stockFeves.retirer(this, quantite);
		this.soldeBancaire.ajouter(this, quantite*prix);
	}

	@Override
	public StockEnVente getStockEnVente() {
		double stockrestant = this.stockFeves.getValeur();
		for (ContratCadre<Feve> cc : this.contratsEnCours) {
			if (Monde.LE_MONDE != null) {
				stockRestant = stockRestant - cc.getQuantiteRestantALivrer();
			}
		}
		StockEnVente<Feve> res = new StockEnVente<Feve>();
		res.ajouter(this.fevesProduites, Math.max(0.0, stockRestant));
		return res;
	}



	@Override
	public void proposerEcheancierVendeur(ContratCadre cc) {
		if (Math.random()<0.5) { // une chance sur deux d'accepter l'echeancier
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la proposition de l'acheteur car on a la quantite en stock 
		} else { // une chance sur deux de proposer un echeancier etalant sur un step de plus
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()+1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()+1)));
		}
	}
		

	@Override
	public void proposerPrixVendeur(ContratCadre cc) {
		if (cc.getListePrixAuKilo().size()==0) {
			cc.ajouterPrixAuKilo(getPrix(cc.getProduit(), cc.getQuantite()));
		} else {
			double prixVendeur = cc.getListePrixAuKilo().get(0);
			double prixAcheteur = cc.getPrixAuKilo();
			if (prixAcheteur>=0.75*prixVendeur) { // on ne fait une proposition que si l'acheteur ne demande pas un prix trop bas.
				if (Math.random()<0.25) { // probabilite de
					cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				} else {
					cc.ajouterPrixAuKilo((prixVendeur*(0.9+Math.random()*0.1))); // rabais de 10% max
				}
			}
		}
		
	}

	@Override
	public void notifierVendeur(ContratCadre cc) {
		this.contratsEnCours.add(cc);
	}



	@Override
	public void encaisser(double montant, ContratCadre cc) {
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode encaisser de ProducteurRomu avec un montant negatif");
		}
		this.soldeBancaire.ajouter(this,  montant);
	}

	@Override
	public double getPrix(Feve produit, Double quantite) {
		if (produit==null || quantite<=0.0 || this.getStockEnVente().get(produit)<quantite) {
			return Double.NaN;
		}
		return this.prixVente;
	}

	@Override
	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if (produit==null || !produit.equals(this.fevesProduites)) {
			throw new IllegalArgumentException("Appel de la methode livrer de ProducteurRomu avec un produit ne correspondant pas aux feves produites");
		}
		double livraison = Math.min(quantite, this.stock.getValeur());
		this.stock.retirer(this, livraison);
		return livraison;
	}

}
