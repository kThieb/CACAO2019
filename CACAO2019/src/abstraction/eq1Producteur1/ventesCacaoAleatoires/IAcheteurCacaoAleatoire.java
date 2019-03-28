package abstraction.eq1Producteur1.ventesCacaoAleatoires;

public interface IAcheteurCacaoAleatoire {
	/**
	 * 
	 * @param quantiteEnVente>=0
	 * @param prix>0
	 * @return Retourne la quantite que l'acheteur souhaite acheter compte tenu de la quantite 
	 * en vente et du prix
	 * L'acheteur doit disposer sur son compte bancaire du montant correspondant a la quantite souhaitee.
	 * La valeur retournee est positive ou nulle et forcement inferieure ou egale a quantiteEnVente.
	 * Le vendeur ne peut pas refuser la vente : la methode quantiteeDesiree actualise l'etat de l'acheteur
	 * pour tenir compte de la vente (sur le stock de feves, le compte bancaire, ...)
	 */
	public double quantiteDesiree(double quantiteEnVente, double prix);
}
