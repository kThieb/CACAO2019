package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.SuperviseurVentesCacaoAleatoires;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.produits.Variete;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur1 implements IActeur /* , IVendeurCacaoAleatoire */ {

	public static int COUT_FIXE_STOCK = 1000;
	public static int COUT_VARIABLE_STOCK = 5;
	public static int DUREE_DE_VIE_FEVE = 1 * 52 / 2; // durée de vie en nexts
	protected Indicateur stockFeves;
	protected Indicateur stockCriolloI;
	protected Indicateur stockForasteroI;
	protected Indicateur stockTrinitarioI;
	protected HashMap<Integer, Double> stockCriollo;
	protected HashMap<Integer, Double> stockForastero;
	protected HashMap<Integer, Double> stockTrinitario;
	protected double recolteCriollo = 33;
	protected double recolteForastero = 33;
	protected double recolteTrinitario = 33;

	protected Indicateur soldeBancaire;
	// BEGIN ANTI
	// private StockEnVente<Feve> stockEnVente;
	// END ANTI
	// BEGIN Manon
	private Journal journal1;
	// END MANON
	// BEGIN Pauline
	protected HashMap<Feve, Double> prixAuKilo;
	// END Pauline
	// BEGIN ANTI
	protected HashMap<Integer, ContratCadre<Feve>> historiqueContrats;
	// END ANTI

	public Producteur1() {
		this.stockFeves = new Indicateur("EQ1 stock feves", this, 3000);
		// BEGIN Nas
		this.stockCriolloI = new Indicateur("EQ1 stock criollo", this, 1000);
		this.stockForasteroI = new Indicateur("EQ1 stock forastero", this, 1000);
		this.stockTrinitarioI = new Indicateur("EQ1 stock trinitario", this, 1000);
		this.stockCriollo=new HashMap<Integer, Double>();
		this.stockForastero=new HashMap<Integer, Double>();
		this.stockTrinitario=new HashMap<Integer, Double>();
		for (int next = 0; next < DUREE_DE_VIE_FEVE - 1; next++) {
			stockCriollo.put(next, (double) 0);
			stockForastero.put(next, (double) 0);
			stockTrinitario.put(next , (double) 0);
		}
		// END Nas
		this.soldeBancaire = new Indicateur("EQ1 solde bancaire", this, 100000);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.stockCriolloI);
		Monde.LE_MONDE.ajouterIndicateur(this.stockForasteroI);
		Monde.LE_MONDE.ajouterIndicateur(this.stockTrinitarioI);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		Monde.LE_MONDE.ajouterActeur(new SuperviseurVentesCacaoAleatoires());
		// BEGIN Manon
		this.journal1 = new Journal("Ventes aleatoires de cacao");
		Monde.LE_MONDE.ajouterJournal(this.journal1);
		System.out.println(" ajout du journal...");
		// END Manon

	}

	public HashMap<Integer, ContratCadre<Feve>> getHistoriqueContrats() {
		return this.historiqueContrats;
	}

	public double getRecolte(Feve feve) {
		if (feve.getVariete() == Variete.CRIOLLO) {
			return recolteCriollo;
		} else if (feve.getVariete() == Variete.FORASTERO) {
			return recolteForastero;
		} else if (feve.getVariete() == Variete.TRINITARIO) {
			return recolteTrinitario;
		}
		return Double.NaN;
	}

	public Indicateur getSoldeBancaire() {
		return this.soldeBancaire;

	}

	public String getNom() {
		return "EQ1";
	}

	public void initialiser() {
	}

	public void next() {
		// production
		// double nouveauStock = this.stockFeves.getValeur() + Math.random() * 200;

		// this.stockFeves.setValeur(this, nouveauStock);

		// BEGIN Nas
		update();
		this.soldeBancaire.retirer(this, COUT_FIXE_STOCK + COUT_VARIABLE_STOCK * stockFeves.getValeur());
		// END Nas

	}

	/*
	 * public double quantiteEnVente(double prix) { if (prix > 2.0) { return
	 * this.stockFeves.getValeur(); } else if (prix < 1.9) { return 0; } else {
	 * return this.stockFeves.getValeur() / 2.0; } }
	 * 
	 * public void notificationVente(double quantite, double prix) {
	 * this.stockFeves.retirer(this, quantite); this.soldeBancaire.ajouter(this,
	 * quantite * prix); }
	 */

	public HashMap<Feve, Double> getPrixAuKilo() {
		// BEGIN Pauline
		HashMap<Feve, Double> mapPrix = new HashMap<Feve, Double>();
		mapPrix.put(Feve.CRIOLLO_HG_EQ, 3.5);
		mapPrix.put(Feve.FORASTERO_MG_EQ, 2.5);
		mapPrix.put(Feve.FORASTERO_MG_NEQ, 2.0);
		mapPrix.put(Feve.TRINITARIO_MG_EQ, 2.2);
		mapPrix.put(Feve.TRINITARIO_MG_NEQ, 1.5);
		return mapPrix;
		// END Pauline
	}

	// BEGIN Nas
	private void update() {
		HashMap<Integer, Double> stockCriolloOld = new HashMap<Integer, Double>( stockCriollo);
		HashMap<Integer, Double> stockForasteroOld = new HashMap<Integer, Double>(stockForastero);
		HashMap<Integer, Double> stockTrinitarioOld = new HashMap<Integer, Double>(stockTrinitario);
		for (int next = 0; next < DUREE_DE_VIE_FEVE - 1; next++) {
			stockCriollo.put(next + 1, stockCriolloOld.get(next));
			stockForastero.put(next + 1, stockForasteroOld.get(next));
			stockTrinitario.put(next + 1, stockTrinitarioOld.get(next));
		}
		stockCriollo.put(0,  getRecolte(Feve.CRIOLLO_HG_EQ));
		stockForastero.put(0, getRecolte(Feve.FORASTERO_MG_NEQ));
		stockTrinitario.put(0, getRecolte(Feve.TRINITARIO_MG_NEQ));

		stockCriolloI.setValeur(this, 0);
		stockForasteroI.setValeur(this, 0);
		stockTrinitarioI.setValeur(this, 0);

		for (int next = 0; next < DUREE_DE_VIE_FEVE; next++) {
			stockCriolloI.ajouter(this, stockCriollo.get(next));
			;
			stockForasteroI.ajouter(this, stockForastero.get(next));
			;
			stockTrinitarioI.ajouter(this, stockTrinitario.get(next));
		}

		stockFeves.setValeur(this,
				stockCriolloI.getValeur() + stockForasteroI.getValeur() + stockTrinitarioI.getValeur());

	}
	
	protected Indicateur getStockI(Feve feve) {
		if (feve.getVariete()== Variete.CRIOLLO) {
	    	  
	    	  return stockCriolloI;
	      }
	      if (feve.getVariete()== Variete.FORASTERO) {
	    	  
	    	  return stockForasteroI;
	      }
	      if (feve.getVariete()== Variete.TRINITARIO) {
	    	  
	    	  return stockTrinitarioI;
	      }
		return stockFeves;
		
	}
	
	protected HashMap<Integer, Double> getStock(Feve feve){
		if (feve.getVariete()== Variete.CRIOLLO) {
	    	  
	    	  return stockCriollo;
	      }
	      if (feve.getVariete()== Variete.FORASTERO) {
	    	  
	    	  return stockForastero;
	      }
	      if (feve.getVariete()== Variete.TRINITARIO) {
	    	  
	    	  return stockTrinitario;
	      }
		return stockCriollo;
	}
	// END Nass

}
