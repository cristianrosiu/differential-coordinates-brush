package workshop;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jv.number.PuDouble;
import jv.object.PsConfig;
import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

public class MyWorkshop_IP extends PjWorkshop_IP implements ActionListener {

	protected Button m_bMakeRandomElementColors;
	protected Button m_bMakeRandomVertexColors;
	protected PuDouble m_xOff;
	
	MyWorkshop m_ws;
	
	public MyWorkshop_IP() {
		super();
		if(getClass() == MyWorkshop_IP.class)
			init();
	}
	
	public void init() {
		super.init();
		setTitle("My Workshop");
	}
	
	public String getNotice() {
		return "This text should explain what the workshop is about and how to use it.";
	}
	
	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_ws = (MyWorkshop)parent;
	
		addSubTitle("Example of a subtitle");
		
		m_bMakeRandomElementColors = new Button("Random Element Colors");
		m_bMakeRandomElementColors.addActionListener(this);
		m_bMakeRandomVertexColors = new Button("Random Vertex Colors");
		m_bMakeRandomVertexColors.addActionListener(this);
		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(m_bMakeRandomElementColors);
		panel1.add(m_bMakeRandomVertexColors);
		add(panel1);
		
		m_xOff = new PuDouble("X Offset");
		m_xOff.setDefBounds(-10,10,0.1,1);
		m_xOff.addUpdateListener(this);
		m_xOff.init();
		add(m_xOff.getInfoPanel());
		
		validate();
	}
	
	
	public boolean update(Object event) {
		if (event == m_xOff) {
			m_ws.setXOff(m_xOff.getValue());
			m_ws.m_geom.update(m_ws.m_geom);
			return true;
		} else
			return super.update(event);
	}
	
	/**
	 * Handle action events fired by buttons etc.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == m_bMakeRandomElementColors) {
			m_ws.makeRandomElementColors();
			m_ws.m_geom.update(m_ws.m_geom);
			return;
		}
		else if (source == m_bMakeRandomVertexColors) {
			m_ws.makeRandomVertexColors();
			m_ws.m_geom.update(m_ws.m_geom);
			return;
		}
	}
	/**
	 * Get information which bottom buttons a dialog should create
	 * when showing this info panel.
	 */
	protected int getDialogButtons()		{
		return PsDialog.BUTTON_OK;
	}
}
