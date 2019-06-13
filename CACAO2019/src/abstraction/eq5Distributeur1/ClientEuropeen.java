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

public class ClientEuropeen implements IActeur {
	private static int NB_CLIENT = 0;
	
	private int numero;
	private Journal journal;
	private int quantiteParStep;
	
	/** @author Erwann DEFOY */
	public String getNom() {
		return "Client Europeen"+this.numero;
	}
	
	/** @author Erwann DEFOY */
	public void initialiser() {
	}

	/** @author Erwann DEFOY */
	public ClientEuropeen(int quantiteParStep) {
		NB_CLIENT++;
		this.numero = NB_CLIENT;
		this.quantiteParStep = quantiteParStep;
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}

	/** @author Erwann DEFOY */
	public void next() {
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : Tentative d'achat de "+quantiteParStep+" du meilleur choclat ____________");
		double quantiteAchetee = 0.0;
		Chocolat produitQ = null;
		IDistributeurChocolat distributeurDeQualite = null;
		double meilleureQualite = 0.0;
		double meilleurPrix = Double.MAX_VALUE;
		double quantiteEnVente = 0.0;
		double quantiteEnVenteMeilleur = 0.0;
		do {
			distributeurDeQualite = null;
			quantiteEnVenteMeilleur = 0.0;
			for (IActeur acteur : Monde.LE_MONDE.getActeurs()) { // recherche des distributeurs avec la meilleur qualité de chocolat
				if (acteur instanceof IDistributeurChocolat) { // recherche des distributeurs
					IDistributeurChocolat dist = (IDistributeurChocolat)acteur;
					StockEnVente<Chocolat> s = dist.getStockEnVente();
					for (Chocolat c : s.getProduitsEnVente()) {
						quantiteEnVente = s.get(c);
						this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : "+((IActeur)dist).getNom()+" vend la quantite de "+quantiteEnVente+" a "+dist.getPrix(c)+" avec une qualite de "+getNoteQualite(dist, c));
						if (quantiteEnVente>0.0) { // dist vend le chocolat recherche
							if ((distributeurDeQualite==null || getNoteQualite(dist, c)>meilleureQualite) && dist.getPrix(c) < 10 ) { // recherche si le produit est de meilleur qualité
								distributeurDeQualite = dist;
								produitQ = c;
								quantiteEnVenteMeilleur = quantiteEnVente;
								meilleureQualite = getNoteQualite(dist, c);
								meilleurPrix = dist.getPrix(c);
							} else if ((distributeurDeQualite==null || (getNoteQualite(dist, c) == meilleureQualite 
									&& dist.getPrix(c) < meilleurPrix)) && dist.getPrix(c) < 10) { // prend le meilleur prix si qualité identique
								distributeurDeQualite = dist;
								produitQ = c;
								quantiteEnVenteMeilleur = quantiteEnVente;
								meilleureQualite = getNoteQualite(dist, c);
								meilleurPrix = dist.getPrix(c);
							}
						}
					}
				}
			}
			if (quantiteAchetee<this.quantiteParStep && distributeurDeQualite!=null) {
				double quantiteCommandee = Math.min(this.quantiteParStep-quantiteAchetee, quantiteEnVenteMeilleur);
				double quantiteVendue = distributeurDeQualite.vendre(produitQ, quantiteCommandee);
				quantiteAchetee+=quantiteVendue;
				this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : Achat de "+ produitQ +" de "+quantiteVendue+" chez "+((IActeur)distributeurDeQualite).getNom()+" au prix de "+meilleurPrix);
			}
		} while (quantiteAchetee<this.quantiteParStep && distributeurDeQualite!=null);
	}
	
	/** @author Erwann DEFOY */
	public double NoteQualite(Chocolat c) {
		int N = 0;
		if (c.isEquitable()) {
			N = N+1 ;
		}
		if (c.isSansHuileDePalme()) {
			N = N+1 ;
		}
		if (c.getGamme() == Gamme.HAUTE) {
			N= N+2 ;
		} else if (c.getGamme() == Gamme.MOYENNE) {
			N = N+1 ;
		}
		return N ;
	}
	
	/** @author Erwann DEFOY */
	public double getNoteQualite (IDistributeurChocolat dist, Chocolat c) {
		return NoteQualite (c);
	}

}
