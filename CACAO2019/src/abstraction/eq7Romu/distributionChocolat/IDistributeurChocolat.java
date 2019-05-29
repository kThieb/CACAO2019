package abstraction.eq7Romu.distributionChocolat;

import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.ventesContratCadre.StockEnVente;

public interface IDistributeurChocolat {

	/**
	 * @return Retourne une instance de StockEnVente<Chocolat> indiquant pour chaque chocolat
	 *  en vente la quantite disponible a l'achat
	 */
	public StockEnVente<Chocolat> getStockEnVente() ;
	
	/**
	 * @param c
	 * @return Retourne le prix de vente actuel d'un kilogramme de c
	 */
	public double getPrix(Chocolat c);
	
	/**
	 * @param c
	 * @param quantite, en kg. Quantite > 0
	 * @return Soit qv la quantite disponible a la vente de c et q=Math.min(qv, quantite).
	 * Vend (et donc enleve du stock) la quantite q de c et retourne q.
	 * En particulier :
	 * - si quantite est inferieure ou egale a la quantite disponible a la 
	 * vente de c, vend la quantite quantite de c et retourne quantite.
	 * - si il n'y a aucun stock de c en vente retourne 0.
	 */
	public double vendre(Chocolat c, double quantite);
	
	
}
