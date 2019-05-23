package abstraction.eq7Romu.acteurs;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;
import abstraction.fourni.Clavier;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class ClientFinalRomu implements IActeur {
	private static int NB_CLIENT = 0;

	private int numero;
	private Journal journal;
	private Chocolat uniqueProduit;
	private Double quantiteParStep;

	public ClientFinalRomu(Chocolat uniqueProduit, Double quantiteParStep) {
		NB_CLIENT++;
		this.numero = NB_CLIENT;
		this.uniqueProduit = uniqueProduit;
		this.quantiteParStep = quantiteParStep;
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
	}

	public String getNom() {
		return "CL"+this.numero+"Romu";
	}

	public void initialiser() {
	}

	public void next() {
		System.out.println("dddddd");
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : tentative d'achat de "+quantiteParStep+" de "+this.uniqueProduit+" ____________");
		double quantiteAchetee = 0.0;
		IDistributeurChocolat distributeurLeMoinsCher = null;
		double meilleurPrix = Double.MAX_VALUE;
		double quantiteEnVente = 0.0;
		double quantiteEnVenteMeilleur = 0.0;
		do {
			distributeurLeMoinsCher = null;
			quantiteEnVenteMeilleur = 0.0;
			for (IActeur acteur : Monde.LE_MONDE.getActeurs()) {
				if (acteur instanceof IDistributeurChocolat) {
					IDistributeurChocolat dist = (IDistributeurChocolat)acteur;
					StockEnVente<Chocolat> s = dist.getStockEnVente();
					if (s.getProduitsEnVente().contains(this.uniqueProduit)) {
						quantiteEnVente = s.get(this.uniqueProduit);
						this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : "+((IActeur)dist).getNom()+" vend la quantite de "+quantiteEnVente+" a "+dist.getPrix(this.uniqueProduit));
						if (quantiteEnVente>0.0) { // dist vend le chocolat recherche
							if (distributeurLeMoinsCher==null || dist.getPrix(this.uniqueProduit)<meilleurPrix) {
								distributeurLeMoinsCher = dist;
								quantiteEnVenteMeilleur = quantiteEnVente;
								meilleurPrix = dist.getPrix(this.uniqueProduit);
							}
						} 
					}
				}
			}
			//			if (distributeurLeMoinsCher==null) {
			//				System.out.println("for termine. Meilleur=null");
			//			} else {
			//			    System.out.println("for termine. Meilleur="+((IActeur)distributeurLeMoinsCher).getNom()+" quantite="+quantiteEnVente+" dejaAchetee="+quantiteAchetee);
			//			}
			if (quantiteAchetee<this.quantiteParStep && distributeurLeMoinsCher!=null) {
				double quantiteCommandee = Math.min(this.quantiteParStep-quantiteAchetee, quantiteEnVenteMeilleur);
				double quantiteVendue = distributeurLeMoinsCher.vendre(this.uniqueProduit, quantiteCommandee);
				quantiteAchetee+=quantiteVendue;
				this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : Achat de "+quantiteVendue+" chez "+((IActeur)distributeurLeMoinsCher).getNom()+" au prix de "+meilleurPrix);
			}
			//			Clavier.lireString();
		} while (quantiteAchetee<this.quantiteParStep && distributeurLeMoinsCher!=null);
	}
}
