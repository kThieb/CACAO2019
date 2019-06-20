package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eq7Romu.produits.Feve;

public class Arbre {
	public static final int QT_ARBRE_AN = 18225;
	private HashMap<Feve, List<Integer>> nbArbres;
	private HashMap<Feve, Integer> nbtot;

	public Arbre() {
		nbArbres = new HashMap<Feve, List<Integer>>();
		nbtot = new HashMap<Feve, Integer>();
	}

	public void initialise() {
		this.nbArbres.put(Feve.FORASTERO_MG_NEQ, new ArrayList<Integer>());
		this.nbArbres.put(Feve.FORASTERO_MG_EQ, new ArrayList<Integer>());
		this.nbArbres.put(Feve.MERCEDES_MG_EQ, new ArrayList<Integer>());
		this.nbArbres.put(Feve.MERCEDES_MG_NEQ, new ArrayList<Integer>());

		this.nbtot.put(Feve.FORASTERO_MG_NEQ, 0);
		this.nbtot.put(Feve.FORASTERO_MG_EQ, 0);
		this.nbtot.put(Feve.MERCEDES_MG_EQ, 0);
		this.nbtot.put(Feve.MERCEDES_MG_NEQ, 0);

		for (int i = 0; i < 37; i++) {
			this.nbArbres.get(Feve.FORASTERO_MG_EQ).add(125);
			this.nbArbres.get(Feve.FORASTERO_MG_NEQ).add(525);
			this.nbArbres.get(Feve.MERCEDES_MG_EQ).add(75);
			this.nbArbres.get(Feve.MERCEDES_MG_NEQ).add(675);

			int surface_F_EQ = this.nbtot.get(Feve.FORASTERO_MG_EQ) + 2025;
			int surface_F_NEQ = this.nbtot.get(Feve.FORASTERO_MG_NEQ) + 18225;
			int surface_M_EQ = this.nbtot.get(Feve.MERCEDES_MG_EQ) + 75;
			int surface_M_NEQ = this.nbtot.get(Feve.MERCEDES_MG_NEQ) + 675;

			this.nbtot.put(Feve.FORASTERO_MG_EQ, surface_F_EQ);// replace(f, t);
			this.nbtot.put(Feve.FORASTERO_MG_NEQ, surface_F_NEQ);// replace(f, t);
			this.nbtot.put(Feve.MERCEDES_MG_EQ, surface_M_EQ);// replace(f, t);
			this.nbtot.put(Feve.MERCEDES_MG_NEQ, surface_M_NEQ);// replace(f, t);

		}
		for (int i = 0; i < 3; i++) {
			this.nbArbres.get(Feve.FORASTERO_MG_EQ).add(2025);
			this.nbArbres.get(Feve.FORASTERO_MG_NEQ).add(18225);
			this.nbArbres.get(Feve.MERCEDES_MG_EQ).add(75);
			this.nbArbres.get(Feve.MERCEDES_MG_NEQ).add(675);			
		}
	}

	public void actualise(HashMap<Feve, Integer> nouveaux) {
		for (Feve f : this.nbArbres.keySet()) {
			int arbreMort = this.nbArbres.get(f).remove(0);
			int nouveauTot = this.nbtot.get(f) - arbreMort + this.nbArbres.get(f).get(36);
			this.nbtot.put(f, nouveauTot); // replace(f, nouveauTot)
			this.nbArbres.get(f).add(nouveaux.get(f));
		}
	}

	public void actualise() {
		for (Feve f : this.nbArbres.keySet()) {
			int arbreMort = this.nbArbres.get(f).remove(0);
			int nouveauTot = this.nbtot.get(f) - arbreMort + this.nbArbres.get(f).get(36);
			this.nbtot.put(f, nouveauTot);

		}
		this.nbArbres.get(Feve.FORASTERO_MG_EQ).add(525);
		this.nbArbres.get(Feve.FORASTERO_MG_NEQ).add(8225);
		this.nbArbres.get(Feve.MERCEDES_MG_EQ).add(75);
		this.nbArbres.get(Feve.MERCEDES_MG_NEQ).add(675);
	}
	
	public Integer getNbArbres(Feve feve) {
		return this.nbtot.get(feve);
	}

	public double getPrixParStep(Feve f) {

		double surfaceTotale = this.nbtot.get(f);
		return surfaceTotale * 132; // en dollar
	}
}
