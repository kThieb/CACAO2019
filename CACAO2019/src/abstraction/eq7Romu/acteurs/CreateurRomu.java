package abstraction.eq7Romu.acteurs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Feve;
import abstraction.eq7Romu.ventesContratCadre.SuperviseurVentesContratCadre;
import abstraction.fourni.IActeur;
import abstraction.fourni.Monde;

/**
 * Acteur creant et ajoutant au monde les acteurs qui ne sont pas le fruit des 6 equipes.
 * @author R. Debruyne
 *
 */
public class CreateurRomu implements IActeur {

	public CreateurRomu() {
		Monde.LE_MONDE.ajouterActeur(new SuperviseurVentesContratCadre());
		Monde.LE_MONDE.ajouterActeur(new ProducteurRomu(Feve.CRIOLLO_HG_EQ,
				10000, 100000.0, 100000.0));
		Monde.LE_MONDE.ajouterActeur(new ProducteurRomu(Feve.CRIOLLO_HG_EQ,
				10000, 100000.0, 100000.0));
//		Monde.LE_MONDE.ajouterActeur(new ProducteurRomu(Feve.TRINITARIO_MG_NEQ,
//				15000, 5000.0, 100000.0));
//		Monde.LE_MONDE.ajouterActeur(new TransformateurRomu(Feve.TRINITARIO_MG_NEQ, 
//				Chocolat.MG_NE_HP, 10000, 1.5, 5000.0, 5000.0, 100000.0, 0.15));
//		Monde.LE_MONDE.ajouterActeur(new TransformateurRomu(Feve.TRINITARIO_MG_NEQ, 
//				Chocolat.MG_NE_HP, 5000, 1.8, 5000.0, 5000.0, 100000.0, 0.20));
		Monde.LE_MONDE.ajouterActeur(new TransformateurRomu(Feve.CRIOLLO_HG_EQ, 
				Chocolat.HG_E_SHP, 2000, 1.1, 2000.0, 2000.0, 100000.0, 0.25));
//		Monde.LE_MONDE.ajouterActeur(new DistributeurRomu(Chocolat.MG_NE_HP, 
//				0.25, 200.0, 100000.0));
		Monde.LE_MONDE.ajouterActeur(new DistributeurRomu(Chocolat.HG_E_SHP, 
				0.35, 200.0, 100000.0));
	//	Monde.LE_MONDE.ajouterActeur(new DistributeurRomu(Chocolat.MG_NE_HP, 
	//			0.20, 200.0, 100000.0));
//		Monde.LE_MONDE.ajouterActeur(new DistributeurRomu(Chocolat.HG_E_SHP, 
//				0.30, 200.0, 100000.0));
		List<Double> list = new ArrayList<Double>();
		for(int i = 1; i <= 100; i++)
		{
			Double index =i+0.0;
			list.add(index);	
			}
		
		/*for(int i = 1; i <= 10; i++)
		{
			Collections.shuffle(list);
			Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.HG_E_SHP, 
					list.get(50)));
			Collections.shuffle(list);
			Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.MG_E_SHP, 
					list.get(25)));
			Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.MG_NE_SHP, 
					list.get(10)));
			Collections.shuffle(list);
			Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.MG_NE_HP, 
					list.get(50)));
		}
		*/
		Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.HG_E_SHP, 
				7500.0));
		Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.MG_E_SHP, 
				7500.0));
		Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.MG_NE_SHP, 
				7500.0));
		Monde.LE_MONDE.ajouterActeur(new ClientFinalRomu(Chocolat.MG_NE_HP, 
				7500.0));
		
	}
	public String getNom() {
		return "CreateurROMU";
	}

	public void initialiser() {
	}

	public void next() {
	}
}
