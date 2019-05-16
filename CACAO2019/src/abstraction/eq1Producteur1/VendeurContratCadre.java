package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.Indicateur;
import abstraction.eq1Producteur1.Producteurs1;


public class VendeurContratCadre implements IVendeurContratCadre{
	

	
	public StockEnVente getStockEnVente() {
		
		return stockEnVente;
	}


	public double getPrix(Object produit, Double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void proposerEcheancierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	
	public void proposerPrixVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
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
