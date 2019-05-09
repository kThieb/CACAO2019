package abstraction.eq7Romu.ventesContratCadre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public class SuperviseurVentesContratCadre implements IActeur {
	public static final int MAX_PRIX_NEGO = 14 ; // Les negociations sur le prix s'arretent apres au plus MAX_PRIX_NEGO propositions de prix
	public static int NB_SUPRVISEURS_CONTRAT_CADRE = 0;
	private int numero;
	private Journal journal;
	private List<ContratCadre> contratsEnCours;
	private List<ContratCadre> contratsTermines;


	public static final int MAX_MEME_VENDEUR_PAR_STEP = 15; 
	// Au cours d'un meme step un acheteur peut negocier au plus MAX_MEME_VENDEUR_PAR_STEP
	// fois avec le meme vendeur. Si l'acheteur demande a negocier avec le vendeur v alors qu'il a 
	// deja negocie MAX_MEME_VENDEUR_PAR_STEP fois avec v durant le step courant alors l'acheteur
	// ne pourra plus negocier durant le step.

	public SuperviseurVentesContratCadre() {
		NB_SUPRVISEURS_CONTRAT_CADRE++;
		this.numero = NB_SUPRVISEURS_CONTRAT_CADRE;
		this.journal = new Journal("Journal "+this.getNom());
		Monde.LE_MONDE.ajouterJournal(this.journal);
		this.contratsEnCours= new ArrayList<ContratCadre>();
		this.contratsTermines= new ArrayList<ContratCadre>();
	}
	public String getNom() {
		return "sup"+this.numero+"CCadre";
	}

	public void initialiser() {
	}

	public void etablirDeNouveauxContrats() {
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" : ETABLISSEMENT DE NOUVEAUX CONTRATS ==========");
		ArrayList<IAcheteurContratCadre> acheteurs = new ArrayList<IAcheteurContratCadre>();
		ArrayList<IVendeurContratCadre> vendeurs = new ArrayList<IVendeurContratCadre>();

		for (IActeur ac : Monde.LE_MONDE.getActeurs()) {
			if (ac instanceof IVendeurContratCadre) {
				vendeurs.add((IVendeurContratCadre)ac);
			}
			if (ac instanceof IAcheteurContratCadre) {
				acheteurs.add((IAcheteurContratCadre)ac);
			}
		}

		// nbPropositions.get(ac).get(ve) vaut le nombre de fois que l'acheteur ac a negocie avec 
		// le vendeur ve durant le step courant. Si ce nombre atteinte MAX_MEME_VENDEUR_PAR_STEP alors
		// l'acheteur ne peux plus negocier durant le step courant.
		HashMap<IAcheteurContratCadre, HashMap<IVendeurContratCadre, Integer>> nbPropositions= new HashMap<IAcheteurContratCadre, HashMap<IVendeurContratCadre, Integer>>();
		for (IAcheteurContratCadre acheteur : acheteurs) {
			HashMap<IVendeurContratCadre, Integer> h = new HashMap<IVendeurContratCadre, Integer>();
			for (IVendeurContratCadre vendeur : vendeurs) {
				h.put(vendeur, 0);
			}
			nbPropositions.put(acheteur, h);
		}
		Collections.shuffle(acheteurs);// on etablit aleatoirement un ordre des acheteurs
		String nomsAcheteurs="";
		for (IAcheteurContratCadre ach : acheteurs) {
			nomsAcheteurs+=((IActeur)ach).getNom()+" ";
		}
		this.journal.ajouter("- Melange aleatoire des acheteurs --> "+nomsAcheteurs);
		boolean acheteurSouhaiteEtablirUnNouveauContrat;
		IVendeurContratCadre vendeur=null;
		int nbTentatives=0;

		while (acheteurs.size()>0) { // tant qu'il y a des acheteur qui potentiellement peuvent avoir envie d'etablir un contrat cadre durant ce step
			// on prend pour acheteur le premier de la liste (il sera remis a la fin de achteurs si il a montre le souhait d'etablir un contrat et qu'il n'a pas cherche a effectuer plus de MAX_MEME_VENDEUR_PAR_STEP negociations avec le meme vendeur durant le step
			IAcheteurContratCadre acheteur = acheteurs.get(0);
			this.journal.ajouter("- acheteur = "+((IActeur)acheteur).getNom());
			acheteurs.remove(0);
			ContratCadre contrat = acheteur.getNouveauContrat();
			this.journal.ajouter("- contrat initial = "+contrat);
			if (contrat!=null) {
				vendeur = contrat.getVendeur();
				nbTentatives = nbPropositions.get(acheteur).get(vendeur);
			}
			acheteurSouhaiteEtablirUnNouveauContrat = contrat!=null && nbTentatives<MAX_MEME_VENDEUR_PAR_STEP && vendeur!=null;
			int nbEcheanciersRequis=0;
			if (acheteurSouhaiteEtablirUnNouveauContrat) {
				nbPropositions.get(acheteur).put(vendeur, nbTentatives+1);
				this.journal.ajouter("- debut des negociations sur l'echeancier");
				acheteur.proposerEcheancierAcheteur(contrat);
				nbEcheanciersRequis++;
				if (contrat.getEcheanciers().size()!=nbEcheanciersRequis) {// si l'acheteur n'a pas ajoute d'echeancier la negociation s'arrete
					this.journal.ajouter("- - L'acheteur n'a pas ajoute d'echeancier --> arret des negociations");
				} else {
					this.journal.ajouter("- - L'acheteur propose :"+contrat.getEcheancier());
					vendeur.proposerEcheancierVendeur(contrat);
					nbEcheanciersRequis++;
					if (contrat.getEcheanciers().size()!=nbEcheanciersRequis) {// si l'acheteur n'a pas ajoute d'echeancier la negociation s'arrete
						this.journal.ajouter("- - Le vendeur n'a pas ajoute d'echeancier --> arret des negociations");
					} else {
						this.journal.ajouter("- - Le vendeur propose :"+contrat.getEcheancier());
						while (contrat.getEcheanciers().size()<ContratCadre.NB_MAX_ECHEANCIERS 
								&& !contrat.accordSurEcheancier()
								&& contrat.getEcheanciers().size()==nbEcheanciersRequis) {
							acheteur.proposerEcheancierAcheteur(contrat);
							nbEcheanciersRequis++;
							if (contrat.getEcheanciers().size()!=nbEcheanciersRequis) {
								this.journal.ajouter("- - L'acheteur n'ajoute pas d'echeancier --> arret des negociations");
							} else {
								this.journal.ajouter("- - L'acheteur propose :"+contrat.getEcheancier());
								if (!contrat.accordSurEcheancier()) {
									vendeur.proposerEcheancierVendeur(contrat);
									nbEcheanciersRequis++;
									if (contrat.getEcheanciers().size()!=nbEcheanciersRequis) {
										this.journal.ajouter("- - Le vendeur n'a pas ajoute d'echeancier --> arret des negociations");
									} else {
										this.journal.ajouter("- - Le vendeur propose :"+contrat.getEcheancier());
									}
								} 
							}
						}
						if (contrat.getEcheanciers().size()==nbEcheanciersRequis && contrat.accordSurEcheancier()) {
							this.journal.ajouter("- - Accord trouve");
							this.journal.ajouter("- debut des negociations sur le prix");
							// Un accord a ete trouve sur l'echeancier --> ouverture des negociations sur le prix.
							int nbMaxPrix = 4 + (int)(Math.random()*(MAX_PRIX_NEGO-4)); // Les negociations ne pourront pas durer plus de nbMaxPrix propositions de prix.
                            this.journal.ajouter("- - le nombre max de propositions est fixe a "+nbMaxPrix);
							int nbPrixRequis=0;
							vendeur.proposerPrixVendeur(contrat);
							nbPrixRequis++;

							if (contrat.getListePrixAuKilo().size()!=nbPrixRequis) {
								this.journal.ajouter("- - Le vendeur n'a pas propose de prix");
							} else if (contrat.getPrixAuKilo()>vendeur.getPrix(contrat.getProduit(),contrat.getQuantite())) {
								this.journal.ajouter("- - Le vendeur propose un prix ("+contrat.getPrixAuKilo()+") superieur au prix qu'il indique via getPrix pour une telle quantite de produit ("+vendeur.getPrix(contrat.getProduit(),contrat.getQuantite())+")");
							} else {
								this.journal.ajouter("- - Le vendeur propose "+String.format("%.3f",contrat.getPrixAuKilo()));
								acheteur.proposerPrixAcheteur(contrat);
								nbPrixRequis++;
								if (contrat.getListePrixAuKilo().size()!=nbPrixRequis) {
									this.journal.ajouter("- - L'acheteur n'a pas propose de prix");
								} else {
									this.journal.ajouter("- - L'acheteur propose "+String.format("%.3f", contrat.getPrixAuKilo()));
									while (contrat.getListePrixAuKilo().size()==nbPrixRequis &&
											contrat.getListePrixAuKilo().size()<nbMaxPrix &&
											!contrat.accordSurPrix()) {
										vendeur.proposerPrixVendeur(contrat);
										nbPrixRequis++;

										if (contrat.getListePrixAuKilo().size()!=nbPrixRequis) {
											this.journal.ajouter("- - Le vendeur n'a pas propose de prix");
										} else if (contrat.getPrixAuKilo()>vendeur.getPrix(contrat.getProduit(),contrat.getQuantite())) {
											this.journal.ajouter("- - Le vendeur propose un prix ("+contrat.getPrixAuKilo()+") superieur au prix qu'il indique via getPrix pour une telle quantite de produit ("+vendeur.getPrix(contrat.getProduit(),contrat.getQuantite())+")");
										} else {
											this.journal.ajouter("- - Le vendeur propose "+String.format("%.3f",contrat.getPrixAuKilo()));
											acheteur.proposerPrixAcheteur(contrat);
											nbPrixRequis++;
											if (contrat.getListePrixAuKilo().size()!=nbPrixRequis) {
												this.journal.ajouter("- - L'acheteur n'a pas propose de prix");
											} else {
												this.journal.ajouter("- - L'acheteur propose "+String.format("%.3f", contrat.getPrixAuKilo()));
											}
										}
									}
									if (contrat.getListePrixAuKilo().size()==nbMaxPrix && !contrat.accordSurPrix()) {
										this.journal.ajouter("   Aucun accord sur le prix n'a ete trouve apres "+nbMaxPrix+" propositions");
									} else if (contrat.getListePrixAuKilo().size()==nbPrixRequis && contrat.accordSurPrix()){
										this.journal.ajouter("   Signature du contrat :<br>"+contrat.toHtml()+"<br>");
										contrat.signer();
										this.contratsEnCours.add(contrat);
										acheteur.notifierAcheteur(contrat);
										vendeur.notifierVendeur(contrat);
									}
								}
							}							
						}
					}
				}
				acheteurs.add(acheteur);
			} else {
				if (nbTentatives>=MAX_MEME_VENDEUR_PAR_STEP) {
					this.journal.ajouter("- -> nombre maximum de tentatives avec le meme vendeur atteint ("+nbTentatives+")");
				} else {
					this.journal.ajouter("- -> ne souhaite pas etablir de nouveaux contrats");
				}
			}
		}
	}
	public void recapitulerContratsEnCours() {
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" Contrats en cours : ");
		for (ContratCadre cc : this.contratsEnCours) {
			this.journal.ajouter(cc.oneLineHtml());
		}		
	}

	public void gererLesEcheancesDesContratsEnCours() {
		this.journal.ajouter("Step "+Monde.LE_MONDE.getStep()+" GESTION DES ECHEANCES DES CONTRATS EN COURS ========");
		for (ContratCadre cc : this.contratsEnCours) {
			this.journal.ajouter("- contrat :"+cc.oneLineHtml());
			double aLivrer = cc.getQuantiteALivrerAuStep();
			if (aLivrer>0.0) {
				IVendeurContratCadre vendeur = cc.getVendeur();
				double effectivementLivre = vendeur.livrer(cc.getProduit(), aLivrer, cc);
				this.journal.ajouter("  a livrer="+String.format("%.3f",aLivrer)+"  livre="+String.format("%.3f",effectivementLivre));
				if (effectivementLivre>0.0) {
					IAcheteurContratCadre acheteur = cc.getAcheteur();
					acheteur.receptionner(cc.getProduit(), effectivementLivre, cc);
					cc.livrer(effectivementLivre);
				}
			} else {
				this.journal.ajouter("- plus rien a livrer");
			}
			double aPayer = cc.getPaiementAEffectuerAuStep();
			if (aPayer>0.0) {
				IAcheteurContratCadre acheteur = cc.getAcheteur();
				double effectivementPaye = acheteur.payer(aPayer, cc);
				this.journal.ajouter("  a payer="+String.format("%.3f",aPayer)+"  paye="+String.format("%.3f",effectivementPaye));
				if (effectivementPaye>0.0) {
					IVendeurContratCadre vendeur = cc.getVendeur();
					vendeur.encaisser(effectivementPaye, cc);
					cc.payer(effectivementPaye);
				}
				
			} else {
				this.journal.ajouter("- plus rien a payer");
			}
		}		
	}
	
	public void archiverContrats() {
		List<ContratCadre> aArchiver = new ArrayList<ContratCadre>();
		for (ContratCadre cc : this.contratsEnCours) {
			if (cc.getMontantRestantARegler()==0.0 && cc.getQuantiteRestantALivrer()==0.0) {
				aArchiver.add(cc);
			}
		}
		for (ContratCadre cc : aArchiver) {
			this.journal.ajouter("Archivage du contrat :<br>"+cc.toHtml());
			this.contratsEnCours.remove(cc);
			this.contratsTermines.add(cc);
		}
	}
		
	public void next() {
		etablirDeNouveauxContrats();
		recapitulerContratsEnCours();
		gererLesEcheancesDesContratsEnCours();
		archiverContrats();
	}
}
