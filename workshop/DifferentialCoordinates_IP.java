package workshop;

import jv.geom.PgElementSet;
import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jv.objectGui.PsList;
import jv.project.PgGeometryIf;
import jv.project.PvGeometryIf;
import jv.vecmath.PdMatrix;
import jv.viewer.PvDisplay;
import jvx.project.PjWorkshop_IP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;


/**
 * Info Panel of Workshop for surface registration
 *
 */
public class DifferentialCoordinates_IP extends PjWorkshop_IP implements ActionListener{
	protected	DifferentialCoordinates	m_diffCoords;
	protected   Button			m_brushButton;
	protected  Button 			m_resetButton;

	protected   TextField[] 	matrixA;

	/** Constructor */
	public DifferentialCoordinates_IP() {
		super();
		if (getClass() == DifferentialCoordinates_IP.class)
			init();
	}

	/**
	 * Informational text on the usage of the dialog.
	 * This notice will be displayed if this info panel is shown in a dialog.
	 * The text is split at line breaks into individual lines on the dialog.
	 */
	public String getNotice() {
		return "This text should explain what the workshop is about and how to use it.";
	}
	
	/** Assign a parent object. */
	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_diffCoords = (DifferentialCoordinates) parent;

		addSubTitle("Differential Coordinates");

		Panel mainPanel = new Panel();
		mainPanel.setLayout(new GridLayout(3, 1));

		Panel matrixPanel = new Panel();
		matrixPanel.setLayout(new GridLayout(3, 3));
		matrixA = new TextField[9];
		for(int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrixA[3 * i + j] = new TextField();
				matrixPanel.add(matrixA[3 * i + j]);
			}
		}
		mainPanel.add(matrixPanel);

		m_brushButton = new Button("Apply Brush");
		m_brushButton.addActionListener(this);
		mainPanel.add(m_brushButton);

		add(mainPanel);

		validate();
	}
		
	/** Initialisation */
	public void init() {
		super.init();
		setTitle("Surface Registration");
		
	}

	/**
	 * Handle action events fired by buttons etc.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == m_brushButton) {
			 PdMatrix A = new PdMatrix(3,3);

			 for(int i = 0; i < 3; i++) {
			 	for (int j = 0; j < 3; j++) {
			 		A.addEntry(i, j, Double.parseDouble(matrixA[3 * i + j].getText()));
			 	}
			 }

			try {
				m_diffCoords.applyBrush(A, false);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			m_diffCoords.m_geom.update(m_diffCoords.m_geom);
			return;
		}
	}
	/**
	 * Get information which bottom buttons a dialog should create
	 * when showing this info panel.
	 */
	protected int getDialogButtons(){
		return PsDialog.BUTTON_OK;
	}
}
