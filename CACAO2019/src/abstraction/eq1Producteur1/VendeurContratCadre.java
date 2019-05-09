package abstraction.eq1Producteur1;

import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;

public class VendeurContratCadre implements IVendeurContratCadre {
	//ANTI 

	@Override
	public StockEnVente getStockEnVente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPrix(Object produit, Double quantite) {
		// BEGIN Pauline
		if (produit== null || quantite<=0.0) {
			return Double.NaN;
		} else if (quantite > this.getStockEnVente().get(produit)) {
			return Double.NaN;
		} else {
			return 5.0;
		}
	}

	@Override
	public void proposerEcheancierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierVendeur(ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double livrer(Object produit, double quantite, ContratCadre cc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void encaisser(double montant, ContratCadre cc) {
		// TODO Auto-generated method stub
		
	}

}
