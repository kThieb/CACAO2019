/**
 * @author Erwann DEFOY
 */

package abstraction.eq5Distributeur1;

import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Gamme;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class ClientFidele implements IActeur {
	private static int NB_CLIENT = 0;

	private int numero;
	private Journal journal;
	private int quantiteParStep;
	Chocolat uniqueProduit;
	IDistributeurChocolat dist;

	/** @author Erwann DEFOY */
	public String getNom() {
		return "CL"+this.numero;
	}

	/** @author Erwann DEFOY */
	public void initialiser() {
	}

	/** @author Erwann DEFOY */
	public ClientFidele(IDistributeurChocolat dist, int quantiteParStep) {
		NB_CLIENT++;
		this.numero = NB_CLIENT;
		this.quantiteParStep = quantiteParStep;
		this.dist = dist;
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}
	
	/** @author Erwann DEFOY */
	public void next() {
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : Tentative d'achat de "+quantiteParStep+" de chocolat a "+dist);
		double quantiteAchetee = 0.0;
		Chocolat produitQ = null;
		double quantiteEnVente = 0.0;
	}
}
