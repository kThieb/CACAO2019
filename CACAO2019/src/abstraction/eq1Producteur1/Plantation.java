package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.IActeur;
import abstraction.fourni.Indicateur;
import static abstraction.eq1Producteur1.Producteur1Interne.*;

public class Plantation {
	private Indicateur ind;
	private HashMap<Integer, Double> plantation;//clé=step de stockage, objet=quantité
	private IActeur act;
	private int stepBorneInf=-(40-1)*unAnEnSteps;
	
	public Plantation(Feve feve,IActeur act,int plantationDepart) {
		this.act=act;
		ind=new Indicateur("EQ1 stock "+feve.getVariete(), act, plantationDepart);
		plantation=new HashMap<Integer, Double>();
		for (int an=0;an<40;an++) {
			plantation.put(-an*unAnEnSteps, (double)plantationDepart/40);//plantation initial avant le step de départ
		}
		
		
		
	}
	
	public Plantation(Feve feve,IActeur act) {
		this(feve,act,1000);
		
	}
	
	
	
	public Indicateur getInd() {
		return ind;
	}

	public HashMap<Integer, Double> getPlantation() {
		return plantation;
	}

	public IActeur getAct() {
		return act;
	}

	public int getStepBorneInf() {
		return stepBorneInf;
	}

	public void planter(int step,double quantite){
		getPlantation().put(step, quantite); //maj de la plantation
		getInd().ajouter(getAct(), quantite); //maj de l'indicateur
	}
	
	public void retraitArbresAges(int stepCourant) {
		int stepArbresAges=stepCourant-dureeDeVieCacaoyer;
		if (getPlantation().get(stepArbresAges)!=null) {
			double ArbresAges=getPlantation().get(stepArbresAges);
			getPlantation().put(stepArbresAges, (double)0);
			getInd().retirer(getAct(), ArbresAges);
		} 
	}
}
