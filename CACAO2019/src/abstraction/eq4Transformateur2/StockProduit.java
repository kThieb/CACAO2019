package abstraction.eq4Transformateur2;

import java.util.LinkedList;

public class StockFeve {
	/** Représente une pile de fèves */
	
	private LinkedList<TasFeve> tas;
	
	public StockFeve() {
		this.tas = new LinkedList<TasFeve>();
	}
	
	public void ajouterTas(TasFeve t) {
		this.tas.add(t);
	}
	
	public double getQuantiteTotale() {
		double qty = 0;
		for(int i = 0; i < tas.size(); i++)
			qty += tas.get(i).getQuantité();
		return qty;
	}
	
	/** Renvoie le prix que l'on a payé pour acheter la quantité de fèves demandée (ou 0 si l'on n'a pas cette quantité) */
	public double getPrixAchat(double qty) {
		if(tas.isEmpty() || qty > getQuantiteTotale())
			return 0;
		else {
			double prix = 0;
			int i = 0;
			while(qty > 0) {
				TasFeve t = tas.get(i); // prochain tas à vider
				double qteAPrendre = Math.min(qty, t.getQuantité());
				qty -= qteAPrendre;
				prix += qteAPrendre * t.getPrixUnitaire();
				i++;
			}
			return prix;
		}
	}
	
	/** Récupère les fèves demandées dans la file. Renvoie le prix total (ou 0 s'il n'y a pas assez de fèves) */
	public double prendreFeves(double qty) {
		if(tas.isEmpty() || qty > getQuantiteTotale())
			return 0;
		else {
			double prix = 0;
			while(qty > 0) {
				TasFeve t = tas.peek(); // prochain tas à vider
				double qteAPrendre = Math.min(qty, t.getQuantité());
				t.prendre(qty);
				qty -= qteAPrendre;
				prix += qteAPrendre * t.getPrixUnitaire();
				// On supprime le tas de feves s'il est vide
				if(t.getQuantité() == 0)
					tas.pop();
			}
			return prix;
		}
	}
}
