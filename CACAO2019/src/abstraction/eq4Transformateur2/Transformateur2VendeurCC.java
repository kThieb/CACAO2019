package abstraction.eq4Transformateur2;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IVendeurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;

public class Transformateur2VendeurCC implements IVendeurContratCadre<Chocolat> {
	private Transformateur2 t2;
	
	public Transformateur2VendeurCC(Transformateur2 trans2) {
		this.t2 = trans2;
	}
	
	public List<Chocolat> getProduitsEnVente() {
		ArrayList<Chocolat> chocolat = new ArrayList<Chocolat>();
		chocolat.addAll(t2.stockEnVente.keySet());
		return chocolat;
	}

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		StockEnVente<Chocolat> sev = new StockEnVente<Chocolat>();
		sev.ajouter(Chocolat.HG_E_SHP, 12000.0);
		return sev;
	}


	@Override
	public double getPrix(Chocolat produit, Double quantite) {
		return 0;
	}

	@Override
	public void proposerEcheancierVendeur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixVendeur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierVendeur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double livrer(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void encaisser(double montant, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

}
