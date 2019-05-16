package abstraction.eq7Romu.ventesContratCadre;

import java.util.List;

import abstraction.eq7Romu.distributionChocolat.IDistributeurChocolat;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Monde;

public abstract class Client1 {
	
	private int numero;
	private int noteprix;
	private int notequalite;
	private int temporalite ;
	private List<IDistributeurChocolat> DistributeurChocolat;
	private Journal journal ;
	
	
	
	
	
	public Client1(int numero, int noteprix, int notequalite, int notequantite) {
		
		this.journal = new Journal("Journal "+this.getNom());
		this.numero = numero;
		this.noteprix = noteprix;
		this.notequalite = notequalite;
	}
	
	
	public String getNom() {
		return "CL"+this.numero;
	}

	public void initialiser() {
	} 
	
	
	
	
}
	
	
	
