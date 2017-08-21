package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class LinkingPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public LinkingPanel() {
		setLayout(new MigLayout("", "[grow]", ""));
		PanelView panelView = new PanelView();
		
		PanelOperation panelOperation = new PanelOperation(panelView, this);
		add(panelOperation, "pushx, growx, wrap");
		
		add(panelView, "push, grow, wrap");
	}
}
