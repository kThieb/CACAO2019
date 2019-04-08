package abstraction.fourni;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Classe modelisant un historique, vu comme un ensemble 
 * d'entrees (modification de la valeur d'un indicateur 
 * par un acteur a une etape donnee)
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 *
 * @author Romuald Debruyne
 */
public class Historique extends Observable {
	private ArrayList<Entree>liste;

	public Historique() {
		this.liste=new ArrayList<Entree>();
	}
	public int getTaille() {
		return this.liste.size();
	}
	public void ajouter(IActeur auteur, int etape, double valeur) {
		this.liste.add(new Entree( auteur,  etape,  valeur));
		this.setChanged();
		this.notifyObservers("add");
	}
	public Entree get(int i) {
		if (i>=0 && i<this.getTaille()) {
			return this.liste.get(i);
		}
		else {
			System.out.println("Appel de get("+i+") sur un historique de taille "+this.getTaille());
			return null;
		}
	}
	/**
	 * Retourne la valeur actuelle (donc la derniere)
	 * @return la valeur actuelle (donc la derniere)
	 */
	public double getValeur() {
		if (this.getTaille()<1) {
			return 0.0;// par convention
		}
		else {
			return this.get(this.getTaille()-1).getValeur();
		}
	}
	public String toString() {
		String s="";
		for (int i=0; i<this.getTaille(); i++) {
			s+=this.get(i).toString()+"\n";
		}
		return s;
	}
	public String toHtml() {
		String s="<html>";
		for (int i=0; i<this.getTaille(); i++) {
			s+=this.get(i).toString()+"<br>";
		}
		return s+"</html>";
	}
}
