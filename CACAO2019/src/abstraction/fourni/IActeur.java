package abstraction.fourni;

/**
 * Tout acteur doit implementer cette interface
 * 
 * Vous aurez donc a creer une/des implementation(s) de cette classe
 * 
 * @author Romuald Debruyne
 *
 */
public interface IActeur {
	/**
	 * @return Le nom de l'acteur
	 */
	public String getNom();
	
	/**
	 * Methode de l'acteur invoquee apres la creation des acteurs et avant le premier next.
	 * Cette methode peut notamment (a vous de decider de ses specifications) permettre de 
	 * determiner les acteurs commerciaux du monde Monde.LE_MONDE
	 * avec lesquels il faudra interagir. 
	 */
	public void initialiser();

	/**
	 * Methode de l'acteur invoquee suite a l'appui sur le bouton NEXT de la fenetre principale
	 */
	public void next();
}
