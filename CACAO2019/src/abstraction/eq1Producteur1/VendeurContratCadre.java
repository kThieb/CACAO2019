package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.Indicateur;





	// ANTI


public class VendeurContratCadre implements IVendeurContratCadre{
	

	
	public StockEnVente getStockEnVente() {
		
		return null;
	}


	public double getPrix(Object produit, Double quantite) {
		// BEGIN Pauline
		if (produit== null || quantite<=0.0) {
			return Double.NaN;
		} else if (quantite > this.getStockEnVente().get(produit)) {
			return Double.NaN;
		} else {
			// utiliser Producteur1.getPrixAuKilo() pour savoir prix en fct du produit 
			Producteur1 prod = new Producteur1();
			return prod.getPrixAuKilo().get(produit);
		}
	}

	
	public void proposerEcheancierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}


//Manon

	public void proposerPrixVendeur(ContratCadre cc) {

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


	public void notifierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub

	}

	
	public double livrer(Object produit, double quantite, ContratCadre cc) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void encaisser(double montant, ContratCadre cc) {
		// TODO Auto-generated method stub

	}

}
