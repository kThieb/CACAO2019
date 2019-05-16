package abstraction.eq4Transformateur2;

import java.util.LinkedList;

import abstraction.eq7Romu.produits.Feve;

public class StockFeve {
	/** Représente une pile de fèves */
	
	private Feve type;
	private LinkedList<TasFeve> tas;
	
	public StockFeve(Feve type) {
		this.type = type;
		this.tas = new LinkedList<TasFeve>();
	}
	
	public void ajouterTas(TasFeve t) {
		this.tas.add(t);
	}
	
	public int getQuantiteTotale() {
		int qty = 0;
		for(int i = 0; i < tas.size(); i++)
			qty += tas.get(i).getQuantité();
		return qty;
	}
	
	public boolean prendreFeves(int qty) {
		// TODO Renvoyer un boolean et le prix
		if(tas.isEmpty() || qty > getQuantiteTotale())
			return false;
		else {
			double prix = 0;
			while(qty > 0) {
				TasFeve t = tas.peek(); // prochain tas à vider
				int qteAPrendre = Math.min(qty, t.getQuantité());
				t.prendre(qty);
				qty -= qteAPrendre;
				prix += qteAPrendre * t.getPrixUnitaire();
				// On supprime le tas de feves s'il est vide
				if(t.getQuantité() == 0)
					tas.pop();
			}
			return true;
		}
	}
}
