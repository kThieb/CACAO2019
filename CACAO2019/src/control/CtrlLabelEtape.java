package control;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import abstraction.fourni.Monde;

public class CtrlLabelEtape implements Observer{

	private JLabel labelStep;
	
	public CtrlLabelEtape(JLabel labelStep) {
		this.labelStep = labelStep;
	}
	public void update(Observable arg0, Object arg1) {
		this.labelStep.setText("Step : "+((Monde)arg0).getStep());
	}
}
