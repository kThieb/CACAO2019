package abstraction.eq7Romu.acteurs;

import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

import java.util.ArrayList;
import java.util.List;

public class ProducteurRomu implements IActeur, IVendeurContratCadre<Feve> {
	private static int NB_PROD = 0;
	private static final double PRIX_INIT = 1.500;
	private static final double PRIX_MIN = 0.800;
	private static final double PRIX_MAX = 2.500;

	private Indicateur stock;
	private Indicateur soldeBancaire;
	private Journal journal;

	private int productionParStep; // kg
	private Feve fevesProduites;
	private int numero;
	private List<ContratCadre<Feve>> contratsEnCours;
	private double prixVente;

	public ProducteurRomu(Feve fevesProduites, int productionParStep, double stockInitial, double soldeInitial) {
		NB_PROD++;
		this.numero = NB_PROD;
		this.prixVente = PRIX_INIT;
		this.fevesProduites = fevesProduites;
		this.productionParStep = productionParStep;
		this.stock = new Indicateur(this.getNom()+" Stock", this, stockInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.stock);
		this.soldeBancaire = new Indicateur(this.getNom()+" Solde", this, soldeInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		this.contratsEnCours = new ArrayList<ContratCadre<Feve>>();
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}

	public String getNom() {
		return "PR"+this.numero+"Romu";
	}

	public void initialiser() {
	}

	public void next() {
		this.stock.ajouter(this, productionParStep);
		retireVieuxContrats();
		if (this.contratsEnCours.size()==0) {
			this.prixVente = 0.99*this.prixVente;
		} else {
			this.prixVente = 1.01*this.prixVente;
		}
		this.prixVente = Math.min(PRIX_MAX, Math.max(PRIX_MIN, this.prixVente));
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : prix de vente = "+this.prixVente);
	}

	/** 
	 * Retire de la liste des contrats en cours les contrats pour lesquels la quantite a livrer 
	 * est nulle et le montant a regler est egalement nul (toutes les livraisons et tous les paiements
	 * ont ete effectues).
	 */
	public void retireVieuxContrats() {
		List<ContratCadre<Feve>> aEnlever = new ArrayList<ContratCadre<Feve>>();
		for (ContratCadre<Feve> c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()<=0.0 && c.getMontantRestantARegler()<=0.0) {
				aEnlever.add(c);
			}
		}
		for (ContratCadre<Feve> c : aEnlever) {
			this.contratsEnCours.remove(c);
		}
	}

	public StockEnVente<Feve> getStockEnVente() {
		Double stockRestant = this.stock.getValeur();
		for (ContratCadre<Feve> cc : this.contratsEnCours) {
			if (Monde.LE_MONDE!=null) {
				stockRestant = stockRestant - cc.getQuantiteRestantALivrer();
			}
		}
		StockEnVente<Feve> res = new StockEnVente<Feve>();
		res.ajouter(this.fevesProduites, Math.max(0.0, stockRestant));
		return res;
	}

	public double getPrix(Feve produit, Double quantite) {
		if (produit==null || quantite<=0.0 || this.getStockEnVente().get(produit)<quantite) {
			return Double.NaN;
		}
		return this.prixVente; // non degressif... ne tient pas compte de la quantite...
	}

	public void proposerEcheancierVendeur(ContratCadre<Feve> cc) {
		if (Math.random()<0.5) { // une chance sur deux d'accepter l'echeancier
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la proposition de l'acheteur car on a la quantite en stock 
		} else { // une chance sur deux de proposer un echeancier etalant sur un step de plus
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()+1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()+1)));
		}
	}

	public void proposerPrixVendeur(ContratCadre<Feve> cc) {
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

	public void notifierVendeur(ContratCadre<Feve> cc) {
		this.contratsEnCours.add(cc);
	}

	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if (produit==null || !produit.equals(this.fevesProduites)) {
			throw new IllegalArgumentException("Appel de la methode livrer de ProducteurRomu avec un produit ne correspondant pas aux feves produites");
		}
		double livraison = Math.min(quantite, this.stock.getValeur());
		this.stock.retirer(this, livraison);
		return livraison;
	}

	public void encaisser(double montant, ContratCadre<Feve> cc) {
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode encaisser de ProducteurRomu avec un montant negatif");
		}
		this.soldeBancaire.ajouter(this,  montant);
	}

}
