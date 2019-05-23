package abstraction.eq4Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StockProduit<T> {
	/** Stocke une pile de tas de fèves (ou de chocolat) par type de fèves (ou de chocolat)*/
	private HashMap<T , LinkedList<TasProduit<T>>> stocks;
	
	
	public StockProduit(List<T> stockables) {
		stocks = new HashMap<T, LinkedList<TasProduit<T>>>();
		for(int i = 0; i < stockables.size(); i++)
			stocks.put(stockables.get(i), new LinkedList<TasProduit<T>>());
	}
	
	public void ajouterTas(T type, TasProduit<T> t) {
		stocks.get(type).add(t);
	}
	
	public double getQuantiteTotale(T type) {
		// On récupère les tas de fèves correspondant à ce type
		LinkedList<TasProduit<T>> tas = stocks.get(type);
		double qty = 0;
		for(int i = 0; i < tas.size(); i++)
			qty += tas.get(i).getQuantité();
		return qty;
	}
	
	/** Renvoie le prix que l'on a payé pour acheter la quantité demandée (ou 0 si l'on n'a pas cette quantité) */
	public double getPrixAchat(T sousType, double qty) {
		// On récupère les tas correspondant à ce sous-type
		LinkedList<TasProduit<T>> tas = stocks.get(sousType);
		
		if(tas.isEmpty() || qty > getQuantiteTotale(sousType))
			return 0;
		else {
			double prix = 0;
			int i = 0;
			while(qty > 0) {
				TasProduit<T> t = tas.get(i); // prochain tas à vider
				double qteAPrendre = Math.min(qty, t.getQuantité());
				qty -= qteAPrendre;
				prix += qteAPrendre * t.getPrixUnitaire();
				i++;
			}
			return prix;
		}
	}
	
	/** Récupère les fèves demandées dans la file. Renvoie le prix total (ou 0 s'il n'y a pas assez de fèves) */
	public double prendreProduits(T sousType, double qty) {
		// On récupère les tas correspondant à ce sous-type
		LinkedList<TasProduit<T>> tas = stocks.get(sousType);
		
		if(tas.isEmpty() || qty > getQuantiteTotale(sousType))
			return 0;
		else {
			double prix = 0;
			while(qty > 0) {
				TasProduit<T> t = tas.peek(); // prochain tas à vider
				double qteAPrendre = Math.min(qty, t.getQuantité());
				t.prendre(qty);
				qty -= qteAPrendre;
				prix += qteAPrendre * t.getPrixUnitaire();
				// On supprime le tas s'il est vide
				if(t.getQuantité() == 0)
					tas.pop();
			}
			return prix;
		}
	}
}
