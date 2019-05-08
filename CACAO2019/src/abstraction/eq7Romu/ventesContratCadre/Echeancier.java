package abstraction.eq7Romu.ventesContratCadre;

import java.util.ArrayList;
import java.util.List;

import abstraction.fourni.Monde;

public class Echeancier {
	private int stepDebut;
	private List<Double>  quantites;

	// Initialise l'echeancier avec stepDebut pour date de premiere echeance 
	// et une liste vide de quantites desirees
	public Echeancier(int stepDebut) {
		this.stepDebut = stepDebut;
		this.quantites = new ArrayList<Double>();
	}

	// Initialise l'echeancier avec une premiere echeance fixee au step suivant
	// et une liste vide de quantites desirees
	public Echeancier() {
		this(Monde.LE_MONDE==null ? 1 : Monde.LE_MONDE.getStep()+1);
	}

	/**
	 * 
	 * @param stepDebut
	 * @param nbStep, nbStep>0
	 * @param quantiteParStep, quantiteParStep>=0.0
	 */
	public Echeancier(int stepDebut, int nbStep, double quantiteParStep) {
		this(stepDebut);
		if (nbStep<1) {
			throw new IllegalArgumentException("Le constructeur d'Echeancier est appele avec nbStep<=0");
		}
		if (quantiteParStep<0.0) {
			throw new IllegalArgumentException("Le constructeur d'Echeancier est appele avec quantiteParStep<0.0");
		}
		for (int i=0; i<nbStep; i++) {
			this.ajouter(quantiteParStep);
		}
	}

	public Echeancier(int stepDebut, List<Double> quantites) {
		this(stepDebut);
		for (Double d : quantites) {
			if (d<0.0) {
				throw new IllegalArgumentException("Le constructeur(Echeancier((stepDebut, quantites) est appele avec une liste comportant une/des valeurs negatives : "+quantites);
			}
		}
		this.quantites = quantites;
	}

	public Echeancier(Echeancier e) {
		this(e.getStepDebut());
		for (int step=e.getStepDebut(); step<=e.getStepFin(); step++) {
			this.set(step, e.getQuantite(step));
		}
	}

	public int getNbEcheances() {
		return this.quantites.size();
	}

	public int getStepDebut() {
		return this.stepDebut;
	}

	public int getStepFin() {
		return this.getStepDebut() + this.getNbEcheances() - 1;
	}

	public double getQuantite(int step) {
		if (step<this.getStepDebut() || step>this.getStepFin()) {
			return 0.0;
		} else {
			return this.quantites.get(step-this.getStepDebut());
		}
	}

	public double getQuantiteJusquA(int step) {
		double res=0;
		for (int s= this.stepDebut; s<=step; s++) {
			res += this.getQuantite(s);
		}
		return res;
	}
	public void vider() {
		for (int step=this.getStepDebut() ; step<=this.getStepFin() ; step++) {
			this.set(step, 0.0);
		}
	}

	/**
	 * @return Retourne la quantite globale de l'echeancier, c'est-a-dire la somme des
	 * quantites prevues aux differentes echeances.
	 */
	public double getQuantiteTotale() {
		return getQuantiteAPartirDe(this.getStepDebut());
	}

	/**
	 * 
	 * @param step
	 * @return Retourne la somme des quantites a partir du step step (les echeances avant step ne 
	 * sont pas consideree). Informellement, la quantite totale "qu'il reste" a partir de step.
	 */
	public double getQuantiteAPartirDe(int step) {
		double somme=0.0;
		for (int s = step; s<=this.getStepFin(); s++) {
			somme+=this.getQuantite(s);
		}
		return somme;

	}
	/**
	 * Si quantite<0.0, leve une IllegalArgumentException.
	 * Sinon, ajoute en fin d'echeancier une echeance supplementaire correspondant 
	 * a la quantite passee en parametre
	 * @param quantite, quantite>=0.0
	 */
	public void ajouter(double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("La methode ajouter(quantite) d'Echeancier est appelee avec quantite<0.0");
		}
		this.quantites.add(quantite);
	}

	/**
	 * Si step<stepDebut ou quantite<0.0, leve une IllegalArgumentException.
	 * Sinon, fixe la quantite a quantite pour le step step (la methode peut etre amenee
	 * a ajouter des echeances de quantite 0.0 si l'echeancier s'achevait avant step).
	 * @param step, step>=stepDebut
	 * @param quantite, quantite>=0.0
	 */
	public void set(int step, double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("La methode set(step,quantite) d'Echeancier est appelee avec quantite<0.0");
		}
		if (step<this.getStepDebut()) {
			throw new IllegalArgumentException("La methode set(step,quantite) d'Echeancier est appelee avec step="+step+" alors que l'echeancier debute au step "+this.getStepDebut());
		}
		if (step<=this.getStepFin()) {
			int index = step-this.getStepDebut();
			this.quantites.remove(index);
			this.quantites.add(index,quantite);
		} else {
			while (this.getStepFin()+1<step) {
				this.ajouter(0.0);
			}
			this.ajouter(quantite);
		}
	}

	public boolean equivalent(Echeancier e) {
		return this.getStepDebut() == e.getStepDebut() 
				&& this.getStepFin()==e.getStepFin()
				&& this.getQuantiteTotale()==e.getQuantiteTotale();
	}

	public String toString() {
		String res="[";
		for (int step=this.getStepDebut(); step<this.getStepFin(); step++) {
			res=res+step+":"+String.format("%.3f",this.getQuantite(step))+", ";
		}
		if (this.getStepFin()>=this.getStepDebut()) {
			res=res+this.getStepFin()+":"+String.format("%.3f",this.getQuantite(this.getStepFin()));
		}
		return res+"]";
	}


	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((quantites == null) ? 0 : quantites.hashCode());
		result = prime * result + stepDebut;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof Echeancier)) {
			return false;
		} else {
			Echeancier other = (Echeancier) obj;

			if (quantites == null) {
				if (other.quantites != null) {
					return false;
				}
			} else if (!quantites.equals(other.quantites)) {
				return false;
			} else if (stepDebut != other.stepDebut) {
				return false;
			}
			return true;
		}
	}

	public static void main(String[] args) {
		Echeancier e = new Echeancier();
		System.out.println(e);
		e.ajouter(12);
		System.out.println(e);
		e.ajouter(5);
		System.out.println(e);
		e.set(8,  3.5);
		System.out.println(e);
		e.set(5,  1.2);
		System.out.println(e);
		Echeancier f = new Echeancier(e);
		System.out.println(f);
		e.vider();
		System.out.println(e);
		System.out.println(f);
		e = new Echeancier(12, 10, 200.0);
		System.out.println(e);
		System.out.println(e.getQuantiteTotale());
		System.out.println("a partir de 15 = "+e.getQuantiteAPartirDe(15));
		f = new Echeancier(e);
		System.out.println("f.equals(e) ? "+f.equals(e));
	}
}
