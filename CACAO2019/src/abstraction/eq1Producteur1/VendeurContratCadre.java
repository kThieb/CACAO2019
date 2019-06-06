package abstraction.eq1Producteur1;

import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.produits.Variete;
//ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.Echeancier;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.Journal;

public class VendeurContratCadre extends Producteur1Interne implements IVendeurContratCadre<Feve> {
	// ANTI
	private StockEnVente<Feve> stockEnVente;


	public StockEnVente<Feve> getStockEnVente() {
        journal1.ajouter("stock en vente " +stockEnVente); // ROMU
		return new StockEnVente<Feve>();// ROMU. Prealablement stockEnVente; mais jamais initialisee...
	}

	public double getPrix(Feve produit, Double quantite) {
		// BEGIN Pauline
		if (produit == null || quantite <= 0.0) {
			return Double.NaN;
		} else if (quantite > this.getStockEnVente().get(produit)) {
			return Double.NaN;
		} else {
			// utiliser Producteur1.getPrixAuKilo() pour savoir prix en fct du produit
			Producteur1Interne prod = new Producteur1Interne();
			return prod.getPrixAuKilo().get(produit);
		}
	}
//Begin MANON ET PAULINE
	public void proposerEcheancierVendeur(ContratCadre<Feve> cc) {
		Echeancier e= cc.getEcheancier();
		Feve feve= cc.getProduit();
		Echeancier newEcheancier= new Echeancier();
		int reste= 0; //quantite qu'on ne pourrait pas livrer à un mois et qu'on livrerai plus tard si possible//
		double stock= this.getStockEnVente().get(feve);
		for (int i=e.getStepDebut(); i<=e.getStepFin();i++) {
			double qtt= e.getQuantite(i);
			stock+= this.getRecolte(feve);
			if (qtt>=stock) {
				newEcheancier.ajouter(stock);
				stock= 0;
				reste+=qtt-stock;}
			else if(qtt+reste>=stock){
				newEcheancier.ajouter(stock);
			stock=0;
					reste-=stock-qtt;
			}
			else {
				newEcheancier.ajouter(qtt+reste);
				reste= 0;
				stock-= reste+qtt;}
		}
		if(reste!=0) { newEcheancier.ajouter(reste);}
				
				cc.ajouterEcheancier(newEcheancier);
			}
		
		
		

	

//Manon

	public void proposerPrixVendeur(ContratCadre<Feve> cc) {

		/* Si la liste est nulle on ajoute le prix initialement proposé */
		if (cc.getListePrixAuKilo() == null) {
			cc.ajouterPrixAuKilo(this.getPrix(cc.getProduit(), cc.getQuantite()));
		} else {
			double prixVendeur = (double) cc.getListePrixAuKilo().get(-2);
			double prixAcheteur = cc.getPrixAuKilo();
			/*
			 * Si la différence de prix est inférieur à celle de 5% proposé on accepte le
			 * prix de l'acheteur
			 */
			if (prixVendeur - prixAcheteur < 0.05 * prixVendeur) {
				cc.ajouterPrixAuKilo(prixAcheteur);
			}
			/*
			 * Sinon on propose in prix inferieur mais superieur à la moyenne des deux prix
			 */
			else {
				double nouveauPrix;
				nouveauPrix = prixVendeur - (prixVendeur - prixAcheteur) * 0.2;
				cc.ajouterPrixAuKilo(nouveauPrix);

			}
		}
	}

//BEGIN ANTI



	public void notifierVendeur(ContratCadre<Feve> cc) {

		super.getHistoriqueContrats().put(cc.getNumero(), cc);
//END ANTI


	}

	/**
	 * Methode invoquee par le superviseur afin que l'acheteur encaisse le montant indique. Le montant est du
	 * au contrat cc mais peut etre inferieur a la somme qui devrait etre encaissee au step courant (l'acheteur
	 * peut avoir un probleme de tresorerie l'empechant de payer l'integralite de la somme due). Si le montant 
	 * est inferieur au montant qui devrait etre paye au step courant d'apres le contrat cc, l'acheteur aura une
	 * penalite de ContratCadre.PENALITE_PAIEMENT% sur la somme qui n'a pas ete percue qu'il devra regler
	 * lors des prochains steps.
	 * @param montant
	 * @param cc
	 */


	public void encaisser(double montant, ContratCadre<Feve> cc) {
		super.soldeBancaire.ajouter(this ,  montant);
		cc.payer(montant);
		journal1.ajouter("solde bancaire +" + Double.toString(montant));
	}
//

	public double livrer(Feve produit, double quantite, ContratCadre<Feve> cc) {
		//Manon
		   
		      if (quantite<0.0) {
		         throw new IllegalArgumentException("Appel de la methode livrer(produit,quantite,ContratCadre) de VendeurContratCadre avec quantite<0.0 ( "+quantite+" )");
		      }
		      if (produit==null) {throw new IllegalArgumentException("Appel de la methode livrer(produit,quantite,ContratCadre) de VendeurContratCadre avec produit null ");
		      }
		      if (cc==null) {throw new IllegalArgumentException("Appel de la methode livrer(produit,quantite,ContratCadre) de VendeurContratCadre avec ContratCadre null ");
		      }
		      /*if (quantite>this.getStockEnVente().get(produit)) {
		    	  super.stockFeves.retirer(this, this.getStockEnVente().get(produit));
		    	  cc.livrer(this.getStockEnVente().get(produit));
		         return this.getStockEnVente().get(produit);
		      }*/
		      //BEGIN Nas
		      if (quantite>getStockI(produit).getValeur()) {
		    	  double valeur_livre=getStockI(produit).getValeur();
		    	  retirer(produit,valeur_livre);
		    	  cc.livrer(valeur_livre);
		         return valeur_livre;
		      }
		      
		      //END Nas
		      else {
		    	  cc.livrer(quantite);
		    	  //super.stockFeves.retirer(this, quantite);
		    	  retirer(produit,quantite);
		         return quantite;
		      }
		   
	}
	
	//BEGIN NAS
	public void retirer(Feve feve, double quantite) {
		super.stockFeves.retirer(this, quantite);
		getStockI(feve).retirer(this, quantite);
		double quantite_a_enlever=quantite;
		int next=DUREE_DE_VIE_FEVE-1;
		while (quantite_a_enlever>0 && next >0) {
			if (getStock(feve).get(next)<quantite_a_enlever) {
				quantite_a_enlever=quantite_a_enlever-getStock(feve).get(next);
				getStock(feve).put(next, (double) 0);
			} else {
			
				getStock(feve).put(next, getStock(feve).get(next)-quantite_a_enlever);
				quantite_a_enlever=0;
			}
			next--;
			
		}
		
		
	}
	//END NAS
	

}
