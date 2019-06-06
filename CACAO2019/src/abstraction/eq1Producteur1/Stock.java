package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.Indicateur;

public class Stock{
	private Indicateur ind;
	private HashMap<Integer, Integer> stock;
	
	public Stock(Feve feve) {
		ind=new Indicateur("EQ1 stock "+feve.getVariete(), new Producteur1(), 1000);
		stock=new HashMap<Integer, Integer>();
	}
	
	public void depot(int next,int quantite) {
		stock.put(next, quantite);
	}
	
	public int retrait(int quantite,int nextCourant) {
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
	
	public void updateStock() {
		
	}
	
	public static void main(String[] args) {
		System.out.println("EQ1 stock "+Feve.TRINITARIO_MG_NEQ.getVariete());
	}

}
