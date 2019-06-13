package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import static abstraction.eq1Producteur1.Producteur1Interne.*;
//BEGIN Nas
public class Stock{
	private Indicateur ind;
	private HashMap<Integer, Double> stock;
	private IActeur act;
	private int nextBorneInf=-(40-1)*unAnEnSteps;
	
	
	public Stock(Feve feve,IActeur act,int stockDepart) {
		this.act=act;
		ind=new Indicateur("EQ1 stock "+feve.getVariete(), act, stockDepart);
		stock=new HashMap<Integer, Double>();
		for (int an=0;an<40;an++) {
			stock.put(-an*unAnEnSteps, (double)1000/40);
		}
		
	}
	
	public Stock(Feve feve,IActeur act) {
		this(feve,act,1000);
		
	}
	
	public void depot(int next,double quantite) {
		getStock().put(next, quantite);
		getInd().ajouter(getAct(), quantite);
	}
	
	public double retrait(int nextCourant,double quantite) {
		double quantiteAEnlever=quantite;
		int nextAExplorer=getNextBorneInf();
		while (quantiteAEnlever>0 && nextAExplorer <=nextCourant) {
			if (getStock().getOrDefault(nextAExplorer,(double)0)<quantiteAEnlever) {
				if (getStock().get(nextAExplorer)!=null) {
					quantiteAEnlever=quantiteAEnlever-getStock().get(nextAExplorer);
					getStock().put(nextAExplorer,(double) 0);
				}
				setNextBorneInf(nextAExplorer);
			} else {
			
				getStock().put(nextAExplorer, getStock().get(nextAExplorer)-quantiteAEnlever);
				quantiteAEnlever=0;
			}
			nextAExplorer++;
			
		}
		double quantiteRetire=quantite-quantiteAEnlever;
		getInd().retirer(getAct(), quantiteRetire);
		return quantiteRetire;
		
	}
	
	public void retraitPerime(int nextCourant) {
		int nextPerime=nextCourant-dureeDeVieFeve;
		if (getStock().get(nextPerime)!=null) {
			double stockPerime=getStock().get(nextPerime);
			getStock().put(nextPerime, (double)0);
			getInd().retirer(getAct(), stockPerime);
		} 
	}
	
	public void updateStock(int nextCourant,double recolte) {
		depot(nextCourant,recolte);
		retraitPerime(nextCourant);
	}
	
	public Indicateur getInd() {
		return ind;
	}

	public HashMap<Integer, Double> getStock() {
		return stock;
	}

	public int getNextBorneInf() {
		return nextBorneInf;
	}
	
	

	public IActeur getAct() {
		return act;
	}

	public void setNextBorneInf(int nextBorneInf) {
		this.nextBorneInf = nextBorneInf;
	}

	public static void main(String[] args) {
		System.out.println("EQ1 stock "+Feve.TRINITARIO_MG_NEQ.getVariete());
		System.out.println(new Integer(-1));
		HashMap<Integer, Integer> toast = new HashMap<Integer, Integer>();
		System.out.println(toast.get(0));
	}

}
//END Nas
