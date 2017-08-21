package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import model.DBTable;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelDBSummary extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private ArrayList<DBTable> allDBTableStructures;
	private JPanel mainContainer;
	private CardLayout appLayout;
	private JPanel panelDBSummeryContent;
	private String baseURL;
	private PanelRMLFile dbRMLFilePanel;
	
	final static String DB_RML_PANEL_KEY = "PanelRMLFile";
	
	public PanelDBSummary(JPanel panelMainContainer, ArrayList<DBTable> allTableStructures, String baseURL) {
		
		if(panelMainContainer == null){
			JOptionPane.showMessageDialog(null, "Main Containe Object is null");
		}
		this.allDBTableStructures = allTableStructures;
		this.mainContainer = panelMainContainer;
		appLayout = (CardLayout)mainContainer.getLayout();
		this.baseURL = baseURL;
		
	
		setLayout(new BorderLayout(0, 0));
		JLabel lblDbPropertiesSummery = new JLabel("DB Properties Summery");
		lblDbPropertiesSummery.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDbPropertiesSummery.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDbPropertiesSummery, BorderLayout.NORTH);
		
		JPanel Buttons = new JPanel();
		add(Buttons, BorderLayout.SOUTH);
		
		JButton buttonBack = new JButton("< Back");
		buttonBack.setFont(new Font("Tahoma", Font.BOLD, 16));
		buttonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				appLayout.show(mainContainer, MainFrame.DB_PANEL_KEY);
			}
		});
		Buttons.add(buttonBack);
		
		JButton btnConfirm = new JButton("Confirm >");
		btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dbRMLFilePanel = new PanelRMLFile(mainContainer, allTableStructures, baseURL);		
				mainContainer.add(dbRMLFilePanel, DB_RML_PANEL_KEY);
				appLayout.show(mainContainer, DB_RML_PANEL_KEY);	
				
			}
		});
		Buttons.add(btnConfirm);
		
		panelDBSummeryContent = new JPanel();
		
		JScrollPane scrollPaneSummery = new JScrollPane(panelDBSummeryContent);
		GridBagLayout gbl_panelDBSummeryContent = new GridBagLayout();
		gbl_panelDBSummeryContent.columnWidths = new int[]{0, 0, 0};
		gbl_panelDBSummeryContent.rowHeights = new int[]{0, 0, 0};
		gbl_panelDBSummeryContent.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelDBSummeryContent.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelDBSummeryContent.setLayout(gbl_panelDBSummeryContent);
		
		add(scrollPaneSummery, BorderLayout.CENTER);
		
		
		
		JLabel lblBaseURL = new JLabel("Base URL: " + baseURL);
		lblBaseURL.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		GridBagConstraints gbc_lblBaseURL = new GridBagConstraints();
		gbc_lblBaseURL.anchor = GridBagConstraints.WEST;
		gbc_lblBaseURL.insets = new Insets(10, 10, 10, 0);
		//gbc_lblTablename.insets = new Insets(0, 0, 0, 0);
		gbc_lblBaseURL.gridx = 1;
		gbc_lblBaseURL.gridy = 1;
		panelDBSummeryContent.add(lblBaseURL, gbc_lblBaseURL);
		
		
		int yCoordinate = 2;
		
		for(DBTable tableStructure: allDBTableStructures){
			
			yCoordinate++;
			JLabel lblTablename = new JLabel("Table Name: " + tableStructure.getTableName());
			lblTablename.setFont(new Font("Tahoma", Font.BOLD, 18));
			
			GridBagConstraints gbc_lblTablename = new GridBagConstraints();
			gbc_lblTablename.anchor = GridBagConstraints.WEST;
			gbc_lblTablename.insets = new Insets(10, 10, 10, 0);
			//gbc_lblTablename.insets = new Insets(0, 0, 0, 0);
			gbc_lblTablename.gridx = 1;
			gbc_lblTablename.gridy = yCoordinate + 0;
			panelDBSummeryContent.add(lblTablename, gbc_lblTablename);
			//System.out.println("Table: y = " + yCoordinate);
			
			int index = 0;
			for(String dataColName: tableStructure.getDataColumns()){
				
				String value  = tableStructure.getDataColumnValues().get(index);
				if(value.equals("L"))
					value = "Literal";
				JLabel lblColumnname = new JLabel("Column Name: " + dataColName + "    Value: " + value);
				GridBagConstraints gbc_lblColumnname = new GridBagConstraints();
				//gbc_lblColumnname.insets = new Insets(0, 0, 0, 0);
				gbc_lblColumnname.insets = new Insets(10, 10, 10, 0);
				gbc_lblColumnname.anchor = GridBagConstraints.WEST;
				gbc_lblColumnname.gridx = 1;
				gbc_lblColumnname.gridy = yCoordinate + 1;
				panelDBSummeryContent.add(lblColumnname, gbc_lblColumnname);
				//System.out.println("Column: y = " + yCoordinate);
				yCoordinate++;
				index++;
				
			}
			
		}
		
		
		

	}

}
