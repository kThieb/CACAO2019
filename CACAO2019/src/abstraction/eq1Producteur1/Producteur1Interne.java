package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import abstraction.eq1Producteur1.ventesCacaoAleatoires.SuperviseurVentesCacaoAleatoires;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.produits.Variete;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Producteur1Interne implements IActeur /* , IVendeurCacaoAleatoire */ {

	public static int COUT_FIXE = 1000;
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

	//BEGIN ANTI 
	protected Indicateur plantationCriolloI;
	protected Indicateur plantationForasteroI;
	protected Indicateur plantationTrinitarioI;
	protected HashMap<Integer, Integer> plantationCriollo;
	protected HashMap<Integer, Integer> plantationForastero;
	protected HashMap<Integer, Integer> plantationTrinitario; 
	protected int compteurSteps = 0 ;
	public static int dureeDeVieCacaoyer = 1040 ; 
	protected int criolloPlante = 33 ; 
	protected int forasteroPlante = 33 ; 
	protected int trinitarioPlante = 33 ;
	public static int unAnEnSteps = 26 ; 
	public static int troisAnsEnSteps = 78 ; 
//END ANTI

	protected int compteur_recolte = 0;
	protected int alea;


	protected Indicateur soldeBancaire;
	// BEGIN ANTI
	// private StockEnVente<Feve> stockEnVente;
	// END ANTI
	// BEGIN Manon
	protected Journal journal1;
	// END MANON
	// BEGIN Pauline
	protected HashMap<Feve, Double> prixAuKilo;
	// END Pauline
	// BEGIN ANTI
	protected HashMap<Integer, ContratCadre<Feve>> historiqueContrats=new HashMap<Integer, ContratCadre<Feve>>();
	// END ANTI

	public Producteur1Interne() {
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
		Random r=new Random();
		alea=r.nextInt(unAnEnSteps);
		// END Nas
		//BEGIN ANTI 
		this.plantationCriolloI = new Indicateur("EQ1 plantation criollo", this, 80);
		this.plantationForasteroI = new Indicateur("EQ1 plantation forastero", this, 80);
		this.plantationTrinitarioI = new Indicateur("EQ1 plantation trinitario", this, 80);
		this.plantationCriollo = new HashMap<Integer, Integer>();
		this.plantationForastero = new HashMap<Integer, Integer>(); 
		this.plantationTrinitario = new HashMap<Integer, Integer>(); 
		
		for (int next = 0; next < dureeDeVieCacaoyer - 1; next++) {
			if ( next%unAnEnSteps == 0 ) {
				plantationCriollo.put(next, 2);
				plantationForastero.put(next, 2);
				plantationTrinitario.put(next, 2);
			} else {
				plantationCriollo.put(next, 0);
				plantationForastero.put(next, 0);
				plantationTrinitario.put(next, 0);
			}
		}
		//END ANTI 
		this.soldeBancaire = new Indicateur("EQ1 solde bancaire", this, 100000);
		Monde.LE_MONDE.ajouterIndicateur(this.stockFeves);
		Monde.LE_MONDE.ajouterIndicateur(this.stockCriolloI);
		Monde.LE_MONDE.ajouterIndicateur(this.stockForasteroI);
		Monde.LE_MONDE.ajouterIndicateur(this.stockTrinitarioI);
		Monde.LE_MONDE.ajouterIndicateur(this.soldeBancaire);
		//Monde.LE_MONDE.ajouterActeur(new SuperviseurVentesCacaoAleatoires());
		// BEGIN Manon
		this.journal1 = new Journal("Journal EQ1");
		Monde.LE_MONDE.ajouterJournal(this.journal1);
		System.out.println(" ajout du journal...");
		// END Manon

	}
	// BEGIN Anti
	public HashMap<Integer, ContratCadre<Feve>> getHistoriqueContrats() {
		return this.historiqueContrats;
	}
	// END Anti

	// BEGIN Nas
	public double getRecolte(Feve feve) {
		
		
		if (feve.getVariete() == Variete.CRIOLLO) {
			return alea==compteur_recolte ? recolteCriollo*Math.random() :recolteCriollo;
		} else if (feve.getVariete() == Variete.FORASTERO) {
			return alea==compteur_recolte ? recolteForastero*Math.random() :recolteForastero;
		} else if (feve.getVariete() == Variete.TRINITARIO) {
			return alea==compteur_recolte ? recolteTrinitario*Math.random() :recolteTrinitario;
		}
		
		return Double.NaN;
	}

	
	public void modifierCompteurRecolte() {
		if (compteur_recolte<unAnEnSteps) {
			compteur_recolte++;
		} else {
			compteur_recolte=0;
			Random r=new Random();
			alea=r.nextInt(unAnEnSteps);
		}
	}
	
	// END Nas

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
		updateStock();
		this.soldeBancaire.retirer(this, COUT_FIXE + COUT_VARIABLE_STOCK * stockFeves.getValeur());
		// END Nas
		//BEGIN ANTI 
		updatePlantation();
		//END ANTI

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
	//BEGIN ANTI 
	
	public void updatePlantation() {
		
	
		HashMap<Integer, Integer> plantationCriolloOld = new HashMap<Integer,Integer>(plantationCriollo);
		HashMap<Integer, Integer> plantationForasteroOld = new HashMap<Integer, Integer>(plantationForastero);
		HashMap<Integer, Integer> plantationTrinitarioOld = new HashMap<Integer, Integer>(plantationTrinitario);
		
		for (int next = 0; next < dureeDeVieCacaoyer - 1; next++) {
			plantationCriollo.put(next + 1 , plantationCriolloOld.get(next));
			plantationForastero.put(next + 1 , plantationForasteroOld.get(next));
			plantationTrinitario.put( next + 1 , plantationTrinitarioOld.get(next));
			if (next > troisAnsEnSteps - 1) {
			recolteCriollo += plantationCriollo.get(next);
			recolteForastero += plantationForastero.get(next);
			recolteTrinitario += plantationTrinitario.get(next);
			}
			
					
		}
		if (compteurSteps%unAnEnSteps == 0  ) {
			plantationCriollo.put(0, criolloPlante);
			plantationForastero.put(0, forasteroPlante);
			plantationTrinitario.put(0, trinitarioPlante);
			
		}
		plantationCriolloI.setValeur(this,  0);
		plantationForasteroI.setValeur(this,  0);
		plantationTrinitarioI.setValeur(this, 0);
		
		for (int next = 0 ; next < dureeDeVieCacaoyer; next++) {
			plantationCriolloI.ajouter(this, plantationCriollo.get(next));
			plantationForasteroI.ajouter(this, plantationForastero.get(next));
			plantationTrinitarioI.ajouter(this, plantationTrinitario.get(next));
			
		}
	}
	// END ANTI 

	// BEGIN Nas
	private void updateStock() {
		HashMap<Integer, Double> stockCriolloOld = new HashMap<Integer, Double>( stockCriollo);
		HashMap<Integer, Double> stockForasteroOld = new HashMap<Integer, Double>(stockForastero);
		HashMap<Integer, Double> stockTrinitarioOld = new HashMap<Integer, Double>(stockTrinitario);
	
		for (int next = 0; next < DUREE_DE_VIE_FEVE - 1; next++) {
			stockCriollo.put(next + 1, stockCriolloOld.get(next));
			stockForastero.put(next + 1, stockForasteroOld.get(next));
			stockTrinitario.put(next + 1, stockTrinitarioOld.get(next));
		}
		
		modifierCompteurRecolte();
		
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
	
	//Begin Manon
	public List<Feve> getFeve(){
		ArrayList<Feve> typeFeve=new ArrayList<Feve>();
		typeFeve.add(Feve.CRIOLLO_HG_EQ);
		typeFeve.add(Feve.FORASTERO_MG_NEQ);
		typeFeve.add(Feve.TRINITARIO_MG_NEQ);
		return typeFeve;
		
	}
	//END MANON

}
