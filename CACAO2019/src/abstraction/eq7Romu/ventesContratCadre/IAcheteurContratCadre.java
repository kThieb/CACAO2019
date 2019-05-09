package abstraction.eq7Romu.ventesContratCadre;

public interface IAcheteurContratCadre<I> {

	/**
	 * @return Retourne null si l'acheteur ne souhaite pas etablir de contrat cadre a ce step.
	 * Sinon, retourne un contrat partiellement rempli qui servira de base aux negociations.
	 * Ce contrat devra preciser l'acheteur, le vendeur, le produit et la quantite, mais ne comportera 
	 * ni echeancier ni prix 
	 */
	public ContratCadre<I> getNouveauContrat();

	/**
	 * Methode appelee par le superviseur durant la phase de negociation de l'echeancier afin que l'acheteur
	 * ajoute une proposition d'echeancier.
	 * Si l'acheteur n'ajoute pas d'echeanier a cc, alors le superviseur arretera les negociations en supposant
	 * qu'aucun accord ne peut etre trouve. 
	 * Si l'acheteur ajoute un echeancier a cc egal a la derniere proposition d'echeancier du vendeur alors
	 * le superviseur estime qu'un accord est trouve sur l'echeancier et poursuivra par la negociation du prix.
	 * Si l'acheteur ajoute un echeancier a cc different de la derniere proposition d'echeancier du vendeur alors
	 * les negociations sur l'echeancier se poursuivent (sauf si il y a deja eu ContratCadre.NB_MAX_ECHEANCIERS 
	 * ajoutes au contrat.
	 * @param cc, cc!=null, le contrat pour lequel la phase de negociation des echeanciers est en cours.
	 *        Si cc.getEcheancier()!=null alors cc.getEcheancier() correspond a la derniere proposition faite
	 *        par le vendeur.
	 */
	public void proposerEcheancierAcheteur(ContratCadre<I> cc);
	
	/**
	 * Methode appelee par le superviseur durant la phase de negociation du prix afin que l'acheteur ajoute 
	 * une proposition de prix.
	 * Si l'acheteur n'ajoute pas de prix alors le superviseur arrete les negociations, supposant qu'aucun 
	 * accord ne peut etre trouve.
	 * Si le vendeur ajoute le meme prix que la derniere proposition de prix, alors le superviseur estime qu'un
	 * accord est trouve sur le prix et passera a la signature du contrat puis la notification des parties prenantes.
	 * Si le vendeur ajoute un prix different de la derniere proposition, la negociation sur le prix se 
	 * poursuit (sauf si on a atteint le nombre maximum de propositions de prix)
	 * @param cc, cc!=null, le contrat pour lequel la phase de negociation des prix est en cours.
	 *    cc.getPrixAuKilo() est le prix propose precedemment par l'acheteur.
	 */
	public void proposerPrixAcheteur(ContratCadre<I> cc);

	/**
	 * Methode appelee par le superviseur apres succes des negociations afin de notifier l'acheteur que le
	 * contrat est signe.
	 * @param cc, cc!=null, un contrat venant d'etre signe impliquant l'acheteur
	 */
	public void notifierAcheteur(ContratCadre<I> cc);

	/**
	 * Methode invoquee par le superviseur afin que l'acheteur receptionne la quantite de produit precisee
	 * en parametre. Cette quantite correspond a la quantite que le vendeur livre effectivement et 
	 * peut etre inferieure a la quantite qui devrait etre livre d'apres le contrat cc, notamment si le 
	 * vendeur n'a pas la quantite suffisante en stock. Si cette quantite est inferieure, le vendeur aura une
	 * penalite de ContratCadre.PENALITE_LIVRAISON% sur la quantite non livree qu'il devra fournir lors des prochains step.
	 * @param produit, produit!=null, le produit indique sur le contrat cc
	 * @param quantite, quantite>0.0, la quantite de produit effectivement livree par le vendeur (a ajouter au stock de l'acheteur) au step courant
	 * @param cc, cc!=null, le contrat en cours stipulant la necessite de livrer cette quantite de produit a ce step
	 */
	public void receptionner(I produit, double quantite, ContratCadre<I> cc);
	
	/**
	 * Methode invoquee par le superviseur afin que l'acheteur paye le montant prevue par le contrat cc 
	 * au step courant. 
	 * @param montant, montant>0.0, le montant a payer (en enlever du compte de l'acheteur)
	 * @param cc, cc!=null, le contrat pour lequel le montant est a regler
	 * @return Retourne le montant reellement paye. Si ce montant est inferieur a celui precise en parametre
	 * (par exemple parce que l'acheteur n'a pas suffisament de tresorerie pour payer l'integralite) une penalite
	 * de Contratcadre.PENALITE_PAIEMENT% sur le montant du sera exige lors des prochains steps.
	 */
	public double payer(double montant, ContratCadre<I> cc);

}
