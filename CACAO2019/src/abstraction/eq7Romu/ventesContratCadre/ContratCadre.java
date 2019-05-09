package abstraction.eq7Romu.ventesContratCadre;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq7Romu.acteurs.ProducteurRomu;
import abstraction.eq7Romu.acteurs.TransformateurRomu;
import abstraction.eq7Romu.produits.Chocolat;
import abstraction.eq7Romu.produits.Feve;
import abstraction.fourni.IActeur;
import abstraction.fourni.Monde;

public class ContratCadre<I> {
	public static int NB_CONTRATS = 0;
	public static final int NB_MAX_ECHEANCIERS = 20; // Limite aux negociations sur les echeanciers. 
	//Si aucun accord n'a pu etre trouve alors qu'il y a deja eu NB_MAX_ECHEANCIERS propositions 
	// d'echeanciers alors le superviseur estime qu'aucun accord ne peut etre trouve.
	public static final Double EPSILON = 0.1;
	public static final Double PENALITE_LIVRAISON = 0.03;
	public static final Double PENALITE_PAIEMENT = 0.03;

	private int numero; // numero unique identifiant le contrat
	private IAcheteurContratCadre<I> acheteur;
	private IVendeurContratCadre<I> vendeur;
	private I produit;
	private Double quantite;
	private boolean signe;
	// true lorsque la phase de negociation s'est terminee avec succes (et que vendeur et acheteur ont ete notifies du succes de la negociation)
	// lorsqu'un contrat est signe il n'est plus possible de le modifier.

	private List<Echeancier> echeanciers ; // la liste des echeanciers 
	// les echeanciers sont alternativement proposes par l'acheteur et le vendeur. Formellement : 
	// -- Pour tout k de [0, (echeanciers.size()-1)/2], echeanciers.get(2k) est un echeancier propose par l'acheteur. 
	// -- Pour tout k de [0, (echeanciers.size()-1)/2], echeanciers.get(2k+1) est un echeancier propose par le vendeur. 
	// Tous les echeanciers sont equivalents (portent sur les memes steps et ont une meme quantite totale). Formellement : 
	// -- Pour tout k de [1, echeanciers.size()[, echeanciers.get(k).equivalent(echeanciers.get(0))
	// Quel que soit l'echeancier, la quantite totale correspond a la quantite du contrat. Formellement :
	// Pour tout k de [0, echeanciers.size()[, echeanciers.get(k).getQuantiteTotale()==quantite 

	private List<Double> prixAuKilo; // la liste des propositions de prix au kilo.

	private Echeancier previsionnelPaiements; // Un previsionnel des paiements a effectuer par 
	// l'acheteur, etabli a la signature du contrat. 

	private Echeancier quantitesLivrees; // La quantite effectivement livree. 
	// Seul le superviseur pourra completer cet echeancier au fur et a mesure des livraisons. 

	private Echeancier paiements; // Les paiments effectues par l'acheteur. 
	// Seul le superviseur pourra completer cet echeancier au fur et a mesure des paiements. 

	private double montantRestantARegler;
	private double quantiteRestantALivrer;


	public ContratCadre(IAcheteurContratCadre<I> acheteur,
			IVendeurContratCadre<I> vendeur, 
			I produit, 
			Double quantite) {
		NB_CONTRATS++;
		this.numero = NB_CONTRATS;
		if (acheteur==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec acheteur==null");
		}
		if (vendeur==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec vendeur==null");
		}
		if (produit==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec produit==null");
		}
		this.acheteur = acheteur;
		this.vendeur = vendeur; 
		this.produit = produit;
		this.quantite = quantite;
		this.echeanciers = new ArrayList<Echeancier>();
		this.prixAuKilo = new ArrayList<Double>();
		this.signe = false;
		this.quantitesLivrees=null;// ce n'est qu'une fois le contrat signe qu'un echeancier memorisant les livraisons sera cree.
		this.paiements = null;
		this.previsionnelPaiements = null;
		this.quantiteRestantALivrer = 0.0;
		this.montantRestantARegler = 0.0;
	}

	public int getNumero() {
		return this.numero;
	}

	public IAcheteurContratCadre<I> getAcheteur() {
		return this.acheteur;
	}

	public IVendeurContratCadre<I> getVendeur() {
		return this.vendeur;
	}

	public I getProduit() {
		return this.produit;
	}

	public Double getQuantite() {
		return this.quantite;
	}

	public boolean estSigne() {
		return this.signe;
	}

	public void signer() {
		if (this.estSigne()) {
			throw new IllegalArgumentException("Appel de signer() sur un contrat deja signe");
		}
		this.signe = true;
		this.quantitesLivrees = new Echeancier(this.getEcheancier().getStepDebut()); // les livraisons debuteront en debut de contrat.
		this.paiements = new Echeancier(this.getEcheancier().getStepDebut()); 
		this.previsionnelPaiements = new Echeancier(this.getEcheancier().getStepDebut()); 
		Echeancier e = this.getEcheancier();
		this.montantRestantARegler = 0.0;
		for (int step = e.getStepDebut(); step<=e.getStepFin(); step++) {
			if (e.getQuantite(step)>0.0) {
				double quantiteStep =  e.getQuantite(step);
				double montantStep = quantiteStep*this.getPrixAuKilo();
				this.previsionnelPaiements.set(step, montantStep);
				this.montantRestantARegler += montantStep;
				this.quantiteRestantALivrer += quantiteStep;
			}
		}
	}

	public double getMontantRestantARegler() {
		return this.montantRestantARegler;
	}

	public double getQuantiteRestantALivrer() {
		return this.quantiteRestantALivrer;
	}

	public void ajouterEcheancier(Echeancier e) {
		if (e==null) {
			throw new IllegalArgumentException("Appel de la methode ajouterEcheancier(e) avec e==null");
		}
		if (Math.abs(e.getQuantiteTotale()-this.getQuantite())>EPSILON) {
			throw new IllegalArgumentException("Appel de la methode ajouterEcheancier(e) avec e.getQuantiteTotale() differente de la quantite du contrat");
		}
		if (this.estSigne()) {
			throw new IllegalArgumentException("Appel de la methode ajouterEcheancier(e) sur un contrat deja signe");
		}
		this.echeanciers.add(e);
	}

	public void ajouterPrixAuKilo(Double p) {
		if (this.estSigne()) {
			throw new IllegalArgumentException("Appel de la methode ajouterPrixAuKilo sur un contrat qui est deja signe");
		}
		if (!Double.isNaN(p)) {
			this.prixAuKilo.add(p);
		}
	}

	public List<Echeancier> getEcheanciers() {
		List<Echeancier> res = new ArrayList<Echeancier>();
		for (Echeancier e : this.echeanciers) {
			res.add(new Echeancier(e));
		}
		return res;
	}

	public Echeancier getEcheancier() {
		if (this.echeanciers==null || this.echeanciers.size()<1) {
			return null;
		} else {
			return new Echeancier(this.echeanciers.get(this.echeanciers.size()-1));
		}
	}

	public boolean accordSurEcheancier() {
		return this.echeanciers!=null && this.echeanciers.size()>=2 && this.echeanciers.get(this.echeanciers.size()-1).equals(this.echeanciers.get(this.echeanciers.size()-2));
	}

	public Echeancier getQuantiteLivree() {
		return new Echeancier(this.quantitesLivrees);
	}

	public Echeancier getPaiementsEffectues() {
		return new Echeancier(this.paiements);
	}


	public double getQuantiteALivrerAuStep() {
		if (Monde.LE_MONDE==null) {
			return 0.0;
		}
		int step = Monde.LE_MONDE.getStep();
		if (step<=this.getEcheancier().getStepFin()) {
			return this.getEcheancier().getQuantiteJusquA(step)- (this.getQuantiteLivree()==null ? 0.0 : this.getQuantiteLivree().getQuantiteJusquA(step));
		} else {
			return this.quantiteRestantALivrer;
		}
	}

	public double getPaiementAEffectuerAuStep() {
		if (Monde.LE_MONDE==null) {
			return 0.0;
		}
		int step = Monde.LE_MONDE.getStep();
		if (step<=this.getEcheancier().getStepFin()) {
			return this.previsionnelPaiements.getQuantiteJusquA(step) -(this.paiements==null ? 0.0 : this.paiements.getQuantiteJusquA(step));
		} else {
			return this.montantRestantARegler;
		}
	}


	public List<Double> getListePrixAuKilo() {
		List<Double> res = new ArrayList<Double>();
		for (Double p : this.prixAuKilo) {
			res.add(p);
		}
		return res;
	}
	public Double getPrixAuKilo() {
		return this.prixAuKilo.size()>0 ? this.prixAuKilo.get(this.prixAuKilo.size()-1) : Double.NaN;
	}

	public boolean accordSurPrix() {
		return this.prixAuKilo!=null && this.prixAuKilo.size()>=2 && this.prixAuKilo.get(this.prixAuKilo.size()-1).equals(this.prixAuKilo.get(this.prixAuKilo.size()-2));
	}

	public String toString() {
		String res = "Contrat #"+this.getNumero()+"\nAcheteur:"+((IActeur)(this.getAcheteur())).getNom()+"\n"
				+"Vendeur:"+((IActeur)(this.getVendeur())).getNom()+"\n"
				+"Produit:"+this.getProduit()+"\n"
				+"Quantite:"+String.format("%.3f",this.getQuantite())+"\n"
				+"Echeanciers:\n";
		for (Echeancier e : this.echeanciers) {
			res = res+e.toString()+"\n";
		}
		res=res+"Prix au Kg:\n";
		for (Double p : this.prixAuKilo) {
			res=res+String.format("%.3f",p)+"\n";
		}
		return res;
	}

	public String toHtml() {
		return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+this.toString().replaceAll("\n",  "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}

	public String oneLineHtml() {
		return "#"+this.getNumero()
		+" Ac="+((IActeur)(this.getAcheteur())).getNom()
		+" Ve="+((IActeur)(this.getVendeur())).getNom()
		+" Pr="+this.getProduit()
		+" Qu="+String.format("%.3f",this.getQuantite())
		+" ["+this.getEcheancier().getStepDebut()+"->"+this.getEcheancier().getStepFin()+"]"
		+" Px="+String.format("%.3f",this.getPrixAuKilo())
		+" RL="+String.format("%.3f",this.getQuantiteRestantALivrer())
		+" RP="+String.format("%.3f",this.getMontantRestantARegler())		     ;
	}

	//=================================================================================================
	//=================================================================================================
	//=== METHODES QUE NE PEUVENT ETRE INVOQUEES QUE PAR UN SUPERVISEUR DE VENTES PAR CONTRAT CADRE ===
	//=================================================================================================
	//=================================================================================================

	public void livrer(Double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("Appel de la methode livrer(quantite) de ContratCadre avec quantite<0.0 ( "+quantite+" )");
		}
		if (!this.estSigne()) {
			throw new IllegalArgumentException("Appel de la methode livrer de ContratCadre sur un contrat qui n'est pas signe");
		}
		Class classeAppelante = null;
		try { 
			Exception e = new Exception();
			String name = ((e.getStackTrace())[1]).getClassName();
			classeAppelante = Class.forName( name );
		} catch(Exception e2) {
			classeAppelante = null; 
		}
		if (classeAppelante == null) {
			System.out.println("Aie... je n'identifie pas la classe appelant la methode livrer de ContratCadre");
		} else if (!classeAppelante.equals(SuperviseurVentesContratCadre.class)) {
			throw new IllegalArgumentException("Appel de la methode livrer de ContratCadre par la classe "+classeAppelante.getName()+" : seuls les superviseurs de ventes par contrats cadres sont habilites a appeler cette methode");
		} else {
			this.quantitesLivrees.set(Monde.LE_MONDE.getStep(), quantite);
			this.quantiteRestantALivrer-=quantite;
		}
	}


	public void payer(Double montant) {
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode payer(montant) de ContratCadre avec montant<0.0 ( "+montant+" )");
		}
		if (!this.estSigne()) {
			throw new IllegalArgumentException("Appel de la methode payer de ContratCadre sur un contrat qui n'est pas signe");
		}
		Class classeAppelante = null;
		try { 
			Exception e = new Exception();
			String name = ((e.getStackTrace())[1]).getClassName();
			classeAppelante = Class.forName( name );
		} catch(Exception e2) {
			classeAppelante = null; 
		}
		if (classeAppelante == null) {
			System.out.println("Aie... je n'identifie pas la classe appelant la methode payer de ContratCadre");
		} else if (!classeAppelante.equals(SuperviseurVentesContratCadre.class)) {
			throw new IllegalArgumentException("Appel de la methode livrer de ContratCadre par la classe "+classeAppelante.getName()+" : seuls les superviseurs de ventes par contrats cadres sont habilites a appeler cette methode");
		} else {
			this.paiements.set(Monde.LE_MONDE.getStep(), montant);
			this.montantRestantARegler-=montant;
		}
	}

	public void penaliteLivraison() {
		if (!this.estSigne()) {
			throw new IllegalArgumentException("Appel de la methode penaliteLivraison de ContratCadre sur un contrat qui n'est pas signe");
		}
		Class classeAppelante = null;
		try { 
			Exception e = new Exception();
			String name = ((e.getStackTrace())[1]).getClassName();
			classeAppelante = Class.forName( name );
		} catch(Exception e2) {
			classeAppelante = null; 
		}
		if (classeAppelante == null) {
			System.out.println("Aie... je n'identifie pas la classe appelant la methode penaliteLivraison de ContratCadre");
		} else if (!classeAppelante.equals(SuperviseurVentesContratCadre.class)) {
			throw new IllegalArgumentException("Appel de la methode penaliteLivraison de ContratCadre par la classe "+classeAppelante.getName()+" : seuls les superviseurs de ventes par contrats cadres sont habilites a appeler cette methode");
		} else {
			int step = Monde.LE_MONDE.getStep();
			if (step<=this.getEcheancier().getStepFin()) {
				double nonLivre = this.getEcheancier().getQuantiteJusquA(step)- (this.getQuantiteLivree()==null ? 0.0 : this.getQuantiteLivree().getQuantiteJusquA(step));
				this.quantiteRestantALivrer += nonLivre*PENALITE_LIVRAISON;
			} else {
				this.quantiteRestantALivrer *= (1.0+PENALITE_LIVRAISON);
			}
		}
	}

	public void penalitePaiement() {
		if (!this.estSigne()) {
			throw new IllegalArgumentException("Appel de la methode penalitePaiement de ContratCadre sur un contrat qui n'est pas signe");
		}
		Class classeAppelante = null;
		try { 
			Exception e = new Exception();
			String name = ((e.getStackTrace())[1]).getClassName();
			classeAppelante = Class.forName( name );
		} catch(Exception e2) {
			classeAppelante = null; 
		}
		if (classeAppelante == null) {
			System.out.println("Aie... je n'identifie pas la classe appelant la methode penalitePaiement de ContratCadre");
		} else if (!classeAppelante.equals(SuperviseurVentesContratCadre.class)) {
			throw new IllegalArgumentException("Appel de la methode penalitePaiement de ContratCadre par la classe "+classeAppelante.getName()+" : seuls les superviseurs de ventes par contrats cadres sont habilites a appeler cette methode");
		} else {
			int step = Monde.LE_MONDE.getStep();
			if (step<=this.getEcheancier().getStepFin()) {
				double nonPaye = this.previsionnelPaiements.getQuantiteJusquA(step)- (this.paiements==null ? 0.0 : this.paiements.getQuantiteJusquA(step));
				this.montantRestantARegler += nonPaye*PENALITE_PAIEMENT;
			} else {
				this.montantRestantARegler *= (1.0+PENALITE_PAIEMENT);
			}
		}
	}


	public static void main(String[] args) {
		Monde.LE_MONDE = new Monde(); 
		//Monde.LE_MONDE.peupler();

		IVendeurContratCadre<Feve> p1 = new ProducteurRomu(Feve.CRIOLLO_HG_EQ, 20000, 100000.0, 100000.0);
		IAcheteurContratCadre<Feve> tr1 = new TransformateurRomu(Feve.CRIOLLO_HG_EQ, 
				Chocolat.HG_E_SHP, 2000, 1.1, 2000.0, 2000.0, 100000.0, 0.20);
		ContratCadre<Feve> cc1 = new ContratCadre<Feve>(tr1, p1, Feve.CRIOLLO_HG_EQ, 10000.0);
		Echeancier ec = new Echeancier(1);
		ec.ajouter(5000.0);
		ec.ajouter(5000.0);
		cc1.ajouterEcheancier(ec);
		cc1.signer();
		//cc1.livrer(5000.0);
	}
}
