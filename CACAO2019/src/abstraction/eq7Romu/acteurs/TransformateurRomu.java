package abstraction.eq7Romu.acteurs;

import java.util.ArrayList;
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

public class TransformateurRomu implements IActeur, IAcheteurContratCadre<Feve>, IVendeurContratCadre<Chocolat> {
	private static int NB_TRANSF = 0;
	private static final double PRIX_VENTE_PAR_DEFAUT = 40.0;

	private Indicateur stockFeves;
	private Indicateur stockChocolat;
	private Indicateur soldeBancaire;
	private Journal journal;

	private Double marge;
	private int transformationParStep; // kg
	private double facteurTransformation;
	private Feve fevesAchetees;
	private Chocolat chocolatProduit;
	private int numero;
	private List<ContratCadre<Feve>> contratsFevesEnCours;
	private List<ContratCadre<Chocolat>> contratsChocolatEnCours;

	public TransformateurRomu(Feve fevesAchetees, 
			Chocolat chocolatProduit, 
			int transformationParStep,
			double facteurTransformation, //combien de kg de chocolat est produit avec 1kg de feves
			double stockFevesInitial, 
			double stockChocolatInitial, 
			double soldeInitial, double marge) {
		NB_TRANSF++;
		this.numero = NB_TRANSF;
		this.fevesAchetees = fevesAchetees;
		this.chocolatProduit = chocolatProduit;
		this.transformationParStep = transformationParStep;
		this.facteurTransformation = facteurTransformation;
		this.marge = marge;
		this.stockFeves = new Indicateur(this.getNom()+" StockFeves", this, stockFevesInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		this.stockChocolat = new Indicateur(this.getNom()+" StockChocolat", this, stockChocolatInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.stockChocolat);
		this.soldeBancaire = new Indicateur(this.getNom()+" Solde", this, soldeInitial);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		this.contratsFevesEnCours = new ArrayList<ContratCadre<Feve>>();
		this.contratsChocolatEnCours = new ArrayList<ContratCadre<Chocolat>>();
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}

	public String getNom() {
		return "TR"+this.numero+"Romu";
	}

	public void initialiser() {
	}

	public void next() {
		double quantiteTransformee = Math.min(this.stockFeves.getValeur(), this.transformationParStep);
		this.stockFeves.retirer(this, quantiteTransformee);
		this.stockChocolat.ajouter(this, (quantiteTransformee*this.facteurTransformation));
		retireVieuxContrats();
	}

	/** 
	 * Retire de la liste des contrats en cours les contrats pour lesquels la quantite a livrer 
	 * est nulle et le montant a regler est egalement nul (toutes les livraisons et tous les paiements
	 * ont ete effectues).
	 */
	public void retireVieuxContrats() {
		List<ContratCadre<Feve>> aEnlever = new ArrayList<ContratCadre<Feve>>();
		for (ContratCadre<Feve> c : this.contratsFevesEnCours) {
			if (c.getQuantiteRestantALivrer()<=0.0 && c.getMontantRestantARegler()<=0.0) {
				aEnlever.add(c);
			}
		}
		for (ContratCadre<Feve> c : aEnlever) {
			this.contratsFevesEnCours.remove(c);
		}
		List<ContratCadre<Chocolat>> aEnleverC = new ArrayList<ContratCadre<Chocolat>>();
		for (ContratCadre<Chocolat> c : this.contratsChocolatEnCours) {
			if (c.getQuantiteRestantALivrer()<=0.0 && c.getMontantRestantARegler()<=0.0) {
				aEnleverC.add(c);
			}
		}
		for (ContratCadre<Chocolat> c : aEnleverC) {
			this.contratsChocolatEnCours.remove(c);
		}
	}

	
	public StockEnVente<Chocolat> getStockEnVente() {
		Double stockRestant = this.stockChocolat.getValeur();
		for (ContratCadre<Chocolat> cc : this.contratsChocolatEnCours) {
				stockRestant = stockRestant - cc.getQuantiteRestantALivrer();
		}
		StockEnVente<Chocolat> res = new StockEnVente<Chocolat>();
		res.ajouter(this.chocolatProduit, Math.max(0.0, stockRestant));
		return res;
	}

	@Override
	public double getPrix(Chocolat produit, Double quantite) {
		if (produit==null || quantite<=0.0 || this.getStockEnVente().get(produit)<quantite) {
			return Double.NaN;
		}
		if (this.contratsFevesEnCours.size()==0) {
			return PRIX_VENTE_PAR_DEFAUT;
		} else {
			double prixMoyen = 0;
			for (ContratCadre<Feve> cc : this.contratsFevesEnCours) {
				prixMoyen+=cc.getPrixAuKilo();
			}
			prixMoyen = prixMoyen/ this.contratsFevesEnCours.size();
			return (prixMoyen/this.facteurTransformation) *(1.0+this.marge); // non degressif...
		}
	}


	public ContratCadre<Feve> getNouveauContrat() {
		ContratCadre<Feve> res=null;
        // on determine combien il resterait sur le compte si on soldait tous les contrats en cours.
		double solde = this.soldeBancaire.getValeur();
		this.journal.ajouter("Determination du solde une fois tous les contrats en cours payes");
		this.journal.ajouter("- solde="+solde);
		for (ContratCadre<Feve> cc : this.contratsFevesEnCours) {
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
					if (stock!=null && stock.get(this.fevesAchetees)>=100.0) {// on souhaite faire des contrats d'au moins 100kg
						this.journal.ajouter("   "+(acteur.getNom())+" vend "+stock.get(this.fevesAchetees)+" de "+this.fevesAchetees);
						vendeurs.add((IVendeurContratCadre<Feve>)vacteur);
					} else if (stock==null) {
						this.journal.ajouter("   "+(acteur.getNom())+" retourne null pour stock mis en vente");
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

	public void proposerEcheancierAcheteur(ContratCadre<Feve> cc) {
		if (cc.getEcheancier()==null) { // il n'y a pas encore eu de contre-proposition de la part du vendeur
			cc.ajouterEcheancier(new Echeancier(Monde.LE_MONDE.getStep(), 10, cc.getQuantite()/10));
		} else {
			if (Math.random()<0.5) { // une chance sur deux d'accepter l'echeancier
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la proposition de l'acheteur car on a la quantite en stock 
			} else { // une chance sur deux de proposer un echeancier etalant sur un step de plus
				cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()+1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()+1)));
			}
		}
	}

	public void proposerEcheancierVendeur(ContratCadre<Chocolat> cc) {
		if (Math.random()<0.5) { // une chance sur deux d'accepter l'echeancier
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier())); // on accepte la proposition de l'acheteur car on a la quantite en stock 
		} else { // une chance sur deux de proposer un echeancier etalant sur un step de plus
			cc.ajouterEcheancier(new Echeancier(cc.getEcheancier().getStepDebut(), cc.getEcheancier().getNbEcheances()+1, cc.getQuantite()/(cc.getEcheancier().getNbEcheances()+1)));
		}
	}

	public void proposerPrixAcheteur(ContratCadre<Feve> cc) {
		double prixVendeur = cc.getListePrixAuKilo().get(0);
		if (Math.random()<0.25) { // probabilite de 25% d'accepter
			cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
		} else {
			cc.ajouterPrixAuKilo((prixVendeur*(0.9+Math.random()*0.1))); // rabais de 10% max
		}
	}


	public void proposerPrixVendeur(ContratCadre<Chocolat> cc) {
		if (cc.getListePrixAuKilo().size()==0) {
			cc.ajouterPrixAuKilo(getPrix(cc.getProduit(), cc.getQuantite()));
		} else {
			double prixVendeur = cc.getListePrixAuKilo().get(0);
			double prixAcheteur = cc.getPrixAuKilo();
			if (prixAcheteur>=0.75*prixVendeur) { // on ne fait une proposition que si l'acheteur ne demande pas un prix trop bas.
				if (Math.random()<0.25) { // probabilite de 25% d'accepter
					cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				} else {
					cc.ajouterPrixAuKilo((prixVendeur*(0.9+Math.random()*0.1))); // rabais de 10% max
				}
			}
		}
	}

	public void notifierVendeur(ContratCadre<Chocolat> cc) {
		this.contratsChocolatEnCours.add(cc);
	}

	public void notifierAcheteur(ContratCadre<Feve> cc) {
		this.contratsFevesEnCours.add(cc);
	}

	public double livrer(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		if (produit==null || !produit.equals(this.chocolatProduit)) {
			throw new IllegalArgumentException("Appel de la methode livrer de TransformateurRomu avec un produit ne correspondant pas au chocolat produit");
		}
		double livraison = Math.min(quantite, this.stockChocolat.getValeur());
		this.stockChocolat.retirer(this, livraison);
		return livraison;

	}

	public void receptionner(Feve produit, double quantite, ContratCadre<Feve> cc) {
		if (produit==null || !produit.equals(this.fevesAchetees)) {
			throw new IllegalArgumentException("Appel de la methode receptionner de TransformateurRomu avec un produit ne correspondant pas aux feves achetees par le transformateur");
		}
		if (quantite<=0.0) {
			throw new IllegalArgumentException("Appel de la methode receptionner de TransformateurRomu avec une quantite egale a "+quantite);
		}
		this.stockFeves.ajouter(this, quantite);
	
	}

	public double payer(double montant, ContratCadre<Feve> cc) {
		if (montant<=0.0) {
			throw new IllegalArgumentException("Appel de la methode payer de TransformateurRomu avec un montant negatif = "+montant);
		}
		double paiement = Math.min(montant,  this.soldeBancaire.getValeur());
		this.soldeBancaire.retirer(this,  paiement);
		return paiement;
	}

	public void encaisser(double montant, ContratCadre<Chocolat> cc) {
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode encaisser de TransformateurRomu avec un montant negatif");
		}
		this.soldeBancaire.ajouter(this,  montant);
	}

}
