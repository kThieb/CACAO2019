package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.Indicateur;

public class Stock{
	private Indicateur ind;
	private HashMap<Integer, Integer> stock;
	private int alea;
	
	
	public Stock(Feve feve) {
		ind=new Indicateur("EQ1 stock "+feve.getVariete(), new Producteur1(), 1000);
		stock=new HashMap<Integer, Integer>();
		//for ()
		
	}
	
	public void depot(int next,int quantite) {
		stock.put(next, quantite);
	}
	
	public int retrait(int nextCourant,int quantite) {
		int quantiteAEnlever=quantite;
		int nextAExplorer=0;
		while (quantiteAEnlever>0 && nextAExplorer <=nextCourant) {
			if (stock.get(nextAExplorer)<quantiteAEnlever) {
				quantiteAEnlever=quantiteAEnlever-stock.get(nextAExplorer);
				stock.put(nextAExplorer, 0);
			} else {
			
				stock.put(nextAExplorer, stock.get(nextAExplorer)-quantiteAEnlever);
				quantiteAEnlever=0;
			}
			nextAExplorer++;
			
		}
		return quantite-quantiteAEnlever;
		
	}
	
	public void updateStock(int nextCourant,int recolte) {
		stock.put(nextCourant,recolte);
		int nextPerime=nextCourant-Producteur1Interne.dureeDeVieFeve;
		int stockPerime=stock.get(nextPerime);
		stock.put(nextPerime, 0);
		ind.retirer(new Producteur1(), stockPerime);
		ind.ajouter(new Producteur1(), recolte);
	}
	
	public static void main(String[] args) {
		System.out.println("EQ1 stock "+Feve.TRINITARIO_MG_NEQ.getVariete());
		System.out.println(new Integer(-1));
		HashMap<Integer, Integer> toast = new HashMap<Integer, Integer>();
		System.out.println(toast.get(0));
	}

}
