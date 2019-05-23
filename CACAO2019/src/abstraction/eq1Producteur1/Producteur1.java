package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.IVendeurCacaoAleatoire;
import abstraction.eq1Producteur1.ventesCacaoAleatoires.SuperviseurVentesCacaoAleatoires;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur1 implements IActeur, IVendeurCacaoAleatoire {

	public static int COUT_FIXE_STOCK = 1000;
	public static int COUT_VARIABLE_STOCK = 5;
	public static int DUREE_DE_VIE = 40 * 52; // durée de vie en nexts
	protected Indicateur stockFeves;
	protected HashMap<Integer, Integer> stockCriollo;
	protected HashMap<Integer, Integer> stockForastero;
	protected HashMap<Integer, Integer> stockTrinitario;

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
		this.stockFeves = new Indicateur("EQ1 stock feves", this, 1000);
		this.soldeBancaire = new Indicateur("EQ1 solde bancaire", this, 100000);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
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
		double nouveauStock = this.stockFeves.getValeur() + Math.random() * 200;

		this.stockFeves.setValeur(this, nouveauStock);
		// BEGIN Nas
		this.soldeBancaire.ajouter(this, COUT_FIXE_STOCK + COUT_VARIABLE_STOCK * stockFeves.getValeur());
		// END Nas

	}

	public double quantiteEnVente(double prix) {
		if (prix > 2.0) {
			return this.stockFeves.getValeur();
		} else if (prix < 1.9) {
			return 0;
		} else {
			return this.stockFeves.getValeur() / 2.0;
		}
	}

	public void notificationVente(double quantite, double prix) {
		this.stockFeves.retirer(this, quantite);
		this.soldeBancaire.ajouter(this, quantite * prix);
	}

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
		HashMap<Integer, Integer> stockCriolloOld = stockCriollo;
		HashMap<Integer, Integer> stockForasteroOld = stockForastero;
		HashMap<Integer, Integer> stockTrinitarioOld = stockTrinitario;
		for (int next = 0; next < DUREE_DE_VIE - 1; next++) {
			stockCriollo.put(next + 1, stockCriolloOld.get(next));
			stockForastero.put(next + 1, stockForasteroOld.get(next));
			stockTrinitario.put(next + 1, stockTrinitarioOld.get(next));
		}
		stockCriollo.put(0, 1);
		stockForastero.put(0, 1);
		stockTrinitario.put(0, 1);

	}
	// END Nas

}
