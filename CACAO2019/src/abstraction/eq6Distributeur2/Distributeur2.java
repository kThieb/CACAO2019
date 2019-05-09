package abstraction.eq6Distributeur2;

import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.ContratCadre;
import abstraction.eq7Romu.ventesContratCadre.IAcheteurContratCadre;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Distributeur2 implements IActeur, IAcheteurContratCadre<Chocolat>, IDistributeurChocolat {
	
	private Indicateur stock;
	private Indicateur soldeBancaire;
	private Chocolat produit;
	private Journal journal;
	private List<ContratCadre<Chocolat>> contratsEnCours;
	
	public Distributeur2() {
	
	}
	
	public String getNom() {
		return "Walmart";
	}

	public void initialiser() {
	}

	public void next() {
	}

	@Override
	public StockEnVente<Chocolat> getStockEnVente() {
		StockEnVente<Chocolat> stockEnVente = new StockEnVente<Chocolat>();
		stockEnVente.ajouter(produit, this.stock.getValeur());
		return stockEnVente;		
	}

	@Override
	public double getPrix(Chocolat c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double vendre(Chocolat c, double quantite) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ContratCadre<Chocolat> getNouveauContrat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void proposerEcheancierAcheteur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void proposerPrixAcheteur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifierAcheteur(ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receptionner(Chocolat produit, double quantite, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double payer(double montant, ContratCadre<Chocolat> cc) {
		// TODO Auto-generated method stub
		return 0;
	}
}
