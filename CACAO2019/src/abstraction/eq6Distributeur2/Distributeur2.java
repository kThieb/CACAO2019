package abstraction.eq6Distributeur2;

import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class Distributeur2 implements IActeur {
	private int volume ;
	
	private Journal journal;
	private Journal CARO;
	
	public Distributeur2() {
		this.journal = new Journal("jEq6");
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}
	
	public String getNom() {
		return "EQ6";
	}

	public void initialiser() {
	}

	public void next() {
	}
}
