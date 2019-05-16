 package abstraction.eq7Romu.ventesContratCadre;

public interface IVendeurContratCadre<I> {
	
	/**
	 * @return Retourne une instance de StockEnVente<I> indiquant la quantite en vente pour chacun 
	 * des produits en vente.
	 */
	public StockEnVente<I> getStockEnVente() ;
	
	/**
     * @param produit, produit!=null
	 * @param quantite, quantite>0.0
	 * @return Retourne Double.NaN si produit==null ou quantite<=0.0 ou si la quantite disponible a la vente est inferieure a quantite.
	 * Sinon, retourne le prix d'un kg avant negociation pour une commande d'une quantite quantite de produit produit.
	 * Attention : si un accord est trouve sur un echeancier de la quantite quantite, alors le vendeur
	 * ne pourra pas refuser un prix superieur ou egal au prix retourne par getPrix (mais il pourra bien sur
	 * refuser si l'acheteur propose un prix inferieur).
	 */
	public double getPrix(I produit, Double quantite) ;

	/**
	 * Methode appelee par le superviseur durant la phase de negociation de l'echeancier afin que le vendeur
	 * ajoute une proposition d'echeancier.
	 * Si le vendeur n'ajoute pas d'echeanier a cc, alors le superviseur arretera les negociations en supposant
	 * qu'aucun accord ne peut etre trouve. 
	 * Si le vendeur ajoute un echeancier a cc egal a la derniere proposition d'echeancier de l'acheteur alors
	 * le superviseur estime qu'un accord est trouve sur l'echeancier et poursuivra par la negociation du prix.
	 * Si le vendeur ajoute un echeancier a cc different de la derniere proposition d'echeancier de l'acheteur alors
	 * les negociations sur l'echeancier se poursuivent (sauf si il y a deja eu ContratCadre.NB_MAX_ECHEANCIERS 
	 * ajoutes au contrat).
	 * @param cc, cc!=null, le contrat pour lequel la phase de negociation des echeanciers est en cours.
	 *        cc.getEcheancier() correspond a la derniere proposition faite par l'acheteur.
	 */
	public void proposerEcheancierVendeur(ContratCadre<I> cc);
	
	/**
	 * Methode appelee par le superviseur durant la phase de negociation du prix afin que le vendeur ajoute 
	 * une proposition de prix.
	 * Si le vendeur n'ajoute pas de prix ou si il ajoute un prix superieur a celui retourne par getPrix pour 
	 * la quantite du contrat alors le superviseur arrete les negociations, supposant qu'aucun accord ne 
	 * peut etre trouve.
	 * Si le vendeur ajoute le meme prix que la derniere proposition de prix, alors le superviseur estime qu'un
	 * accord est trouve sur le prix et passera a la signature du contrat puis la notification des parties prenantes.
	 * Si le vendeur ajoute un prix different de la derniere proposition, la negociation sur le prix se 
	 * poursuit (sauf si on a atteint le nombre maximum de propositions de prix)
	 * @param cc, cc!=null, le contrat pour lequel la phase de negociation des prix est en cours.
	 *    Si Double.isNaN(cc.getPrixAuKilo()) (equivalent a cc.getListePrixAuKilo().size()==0) alors il n'y a pas encore eu de proposition, 
	 *    sinon cc.getPrixAuKilo() est le prix propose precedemment par l'acheteur.
	 */
	public void proposerPrixVendeur(ContratCadre<I> cc);
	
	/**
	 * Methode appelee par le superviseur apres succes des negociations afin de notifier le vendeur que le
	 * contrat est signe.
	 * @param cc, cc!=null, un contrat venant d'etre signe impliquant le vendeur
	 */
	public void notifierVendeur(ContratCadre<I> cc);

	/**
	 * Methode invoquee par le superviseur afin que le vendeur livre la quantite de produit due au step courant
	 * a la vue du contrat cc.
	 * @param produit, produit!=null, le produit indique sur le contrat cc
	 * @param quantite, quantite>0.0, la quantite de produit a livrer (a enlever du stock du vendeur) au step courant
	 * @param cc, cc!=null, le contrat en cours stipulant la necessite de livrer cette quantite de produit a ce step
	 * @return La quantite reellement livree (reellement retiree du stock du vendeur). Si la valeur retournee 
	 * est inferieure a la quantite indiquee en parametre, une penalite de ContratCadre.PENALITE_LIVRAISON % de la quantite livree en retard
	 * sera exigee lors des prochains steps
	 */
	public double livrer(I produit, double quantite, ContratCadre<I> cc);
	
	/**
	 * Methode invoquee par le superviseur afin que l'acheteur encaisse le montant indique. Le montant est du
	 * au contrat cc mais peut etre inferieur a la somme qui devrait etre encaissee au step courant (l'acheteur
	 * peut avoir un probleme de tresorerie l'empechant de payer l'integralite de la somme due). Si le montant 
	 * est inferieur au montant qui devrait etre paye au step courant d'apres le contrat cc, l'acheteur aura une
	 * penalite de ContratCadre.PENALITE_PAIEMENT% sur la somme qui n'a pas ete percue qu'il devra regler
	 * lors des prochains steps.
	 * @param montant
	 * @param cc
	 */
	public void encaisser(double montant, ContratCadre<I> cc);
}
