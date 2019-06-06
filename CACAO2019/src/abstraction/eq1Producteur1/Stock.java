package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import static abstraction.eq1Producteur1.Producteur1Interne.*;

public class Stock{
	private Indicateur ind;
	private HashMap<Integer, Integer> stock;
	private int alea;
	private IActeur act;
	private int nextBorneInf=-(40-1)*unAnEnSteps;
	
	
	public Stock(Feve feve,IActeur act) {
		this.act=act;
		ind=new Indicateur("EQ1 stock "+feve.getVariete(), act, 1000);
		stock=new HashMap<Integer, Integer>();
		for (int an=0;an<40;an++) {
			stock.put(-an*unAnEnSteps, 1000/40);
		}
		
	}
	
	public void depot(int next,int quantite) {
		stock.put(next, quantite);
		ind.ajouter(act, quantite);
	}
	
	public int retrait(int nextCourant,int quantite) {
		int quantiteAEnlever=quantite;
		int nextAExplorer=nextBorneInf;
		while (quantiteAEnlever>0 && nextAExplorer <=nextCourant) {
			if (stock.get(nextAExplorer)<quantiteAEnlever) {
				quantiteAEnlever=quantiteAEnlever-stock.get(nextAExplorer);
				stock.put(nextAExplorer, 0);
				nextBorneInf=nextAExplorer;
			} else {
			
				stock.put(nextAExplorer, stock.get(nextAExplorer)-quantiteAEnlever);
				quantiteAEnlever=0;
			}
			nextAExplorer++;
			
		}
		int quantiteRetire=quantite-quantiteAEnlever;
		ind.retirer(act, quantiteRetire);
		return quantiteRetire;
		
	}
	
	public void updateStock(int nextCourant,int recolte) {
		stock.put(nextCourant,recolte);
		int nextPerime=nextCourant-dureeDeVieFeve;
		int stockPerime=stock.get(nextPerime);
		stock.put(nextPerime, 0);
		ind.retirer(act, stockPerime);
		ind.ajouter(act, recolte);
	}
	
	public static void main(String[] args) {
		System.out.println("EQ1 stock "+Feve.TRINITARIO_MG_NEQ.getVariete());
		System.out.println(new Integer(-1));
		HashMap<Integer, Integer> toast = new HashMap<Integer, Integer>();
		System.out.println(toast.get(0));
	}

}
