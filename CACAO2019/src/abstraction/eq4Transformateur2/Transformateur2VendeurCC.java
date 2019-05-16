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
		if (cc.getListePrixAuKilo().size()==0) {
			cc.ajouterPrixAuKilo(getPrix(cc.getProduit(), cc.getQuantite()));
		} else {
			double prixVendeur = cc.getListePrixAuKilo().get(0);
			double prixAcheteur = cc.getPrixAuKilo();
			if (prixAcheteur>=0.75*prixVendeur) { // on ne fait une proposition que si l'acheteur ne demande pas un prix trop bas.
				if (Math.random()<0.25) { // probabilite de 25% d'accepter
					cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				} if (prixAcheteur>=0.9*prixVendeur) { // si l'acheteur propose 90% de notre prix on accepte
					cc.ajouterPrixAuKilo(cc.getPrixAuKilo());
				} else {
					final double RABAIS_MAX = 0.1;
					cc.ajouterPrixAuKilo((prixVendeur-prixVendeur*Math.random()*RABAIS_MAX)); // rabais de 10% max
				}
			}
		}
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
