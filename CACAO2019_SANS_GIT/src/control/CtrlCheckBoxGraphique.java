package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;

import presentation.FenetreGraphique;

public class CtrlCheckBoxGraphique extends WindowAdapter implements ActionListener, Observer {

	private FenetreGraphique graphique;
	private JCheckBox checkBox;

	public CtrlCheckBoxGraphique( FenetreGraphique graphique, JCheckBox checkBox) {
		this.graphique = graphique;
		this.checkBox = checkBox;
	}

	public void windowClosing(WindowEvent arg0) {
		this.graphique.setVisible(true);
		this.checkBox.setSelected(false);
	}

	public void actionPerformed(ActionEvent e) {
		this.graphique.setVisible(this.checkBox.isSelected());
	}


	public void update(Observable o, Object arg) {
		if (this.graphique.isVisible()) {
			this.graphique.repaint();
		}

	}
}
