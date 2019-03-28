package abstraction.eq1Producteur1.ventesCacaoAleatoires;

import java.util.ArrayList;
import java.util.List;

import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;
/**
 *  Le superviseur ne realise pas une transaction a chaque step : il y a un delai de 0 à 2 steps (tire au sort) entre deux echanges
 *  Lorsqu'un echange doit avoir lieu, le superviseur 
 *  - tire au sort un prix entre 1.8 et 2.1
 *  - tire au sort un vendeur parmi les vendeurs 
 *  - tire au sort un acheteur parmi les acheteurs
 *  - il demande au vendeur la quantite qu'il souhaite mettre en vente compte tenu 
 *     du prix via la fonction quantiteEnVente(prix) de IVendeurCacaoAleatoire
 *  - il demande ensuite a l'acheteur la quantite qu'il souhaite acheter connaissant 
 *     le prix de vente et la quantite en vente via la fonction quantiteeDesiree(enVente, prix)
 *     de IAcheteurCacaoAleatoire
 *  - il notifie le vendeur de la quantite qui a ete achetee via la methode 
 *     notificationVente(desiree, prix) de IVenduerCacaoAleatoire
 *  - il tire au sort dans combien de step aura lieu le prochain echange.
 *
 */
public class SuperviseurVentesCacaoAleatoires implements IActeur {

	private List<IVendeurCacaoAleatoire> vendeurs;
	private List<IAcheteurCacaoAleatoire> acheteurs;
	private int nbNextAvantEchange;
	private Journal journal;

	public SuperviseurVentesCacaoAleatoires() {
		this.vendeurs = new ArrayList<IVendeurCacaoAleatoire>();
		this.acheteurs = new ArrayList<IAcheteurCacaoAleatoire>();
		this.journal = new Journal("Ventes aleatoires de cacao");
		Monde.LE_MONDE.ajouterJournal(this.journal);
		System.out.println(" ajout du journal...");
		this.nbNextAvantEchange = 0;
	}

	public String getNom() {
		return "Superviseur des ventes de cacao aleatoires";
	}

	public void initialiser() {
		for (IActeur ac : Monde.LE_MONDE.getActeurs()) {
			if (ac instanceof IVendeurCacaoAleatoire) {
				vendeurs.add((IVendeurCacaoAleatoire)ac);
			}
			if (ac instanceof IAcheteurCacaoAleatoire) {
				acheteurs.add((IAcheteurCacaoAleatoire)ac);
			}
		}
	}

	public void next() {
		if (this.vendeurs.size()>0 && this.acheteurs.size()>0) {
			if (this.nbNextAvantEchange==0) {
				this.journal.ajouter("<font color=\"#ff0000\">Echange au step "+Monde.LE_MONDE.getStep()+"</font>");
				double prix = 1.8 + (Math.random()*(2.1-1.8));
				this.journal.ajouter("  prix="+prix);
				IVendeurCacaoAleatoire vendeur = this.vendeurs.get((int)(Math.random()*this.vendeurs.size()));
				this.journal.ajouter("  vendeur="+((IActeur)vendeur).getNom());
				IAcheteurCacaoAleatoire acheteur = this.acheteurs.get((int)(Math.random()*this.acheteurs.size()));
				this.journal.ajouter("  acheteur="+((IActeur)acheteur).getNom());
				double enVente = vendeur.quantiteEnVente(prix);
				this.journal.ajouter("  quantite en vente="+enVente);
				if (enVente>0.0) {
					double desiree=acheteur.quantiteDesiree(enVente, prix);
					vendeur.notificationVente(desiree, prix);
					this.journal.ajouter("   quantitee desiree="+desiree);
				} 
				this.nbNextAvantEchange=1+(int)(Math.random()*3);
			}
			this.nbNextAvantEchange--;
		}
	}

}
