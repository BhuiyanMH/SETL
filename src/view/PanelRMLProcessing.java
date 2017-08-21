package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.function.library.substring;

import model.DBTable;
import model.ForeignKey;
import model.RMLProcessor;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class PanelRMLProcessing extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private JPanel mainContainer;
	private CardLayout appLayout;
	ArrayList<DBTable> allDBTableStructure;
	String baseURL;
	private RMLProcessor rmlProcessor;
	private String rmlFileString;
	private JTextArea textAreaRMLRDF;
	private JTextField textFieldDBUrl;
	private JTextField textFieldDBUserName;
	private JPasswordField passwordFieldDBPassword;
	private String rmlFilePath;
	
	public PanelRMLProcessing(JPanel mainContainerPanel) {
		
		this.mainContainer = mainContainerPanel;
		this.appLayout = (CardLayout) mainContainer.getLayout();
		this.baseURL = baseURL;
		
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelButtons = new JPanel();
		add(panelButtons, BorderLayout.SOUTH);
		
		JButton buttonRMLoad = new JButton("Load");
		buttonRMLoad.setFont(new Font("Tahoma", Font.BOLD, 16));
		buttonRMLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			
				
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);
				
				rmlFilePath = "";
				int returnVal = fileChooser.showOpenDialog(mainContainer);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					
					File rmlFile = fileChooser.getSelectedFile();
					rmlFilePath = rmlFile.getPath();
					//String fileName = rmlFile.getName();
					//System.out.println(rmlFilePath);
					
					if(rmlProcessor == null){
						JOptionPane.showMessageDialog(null, "Please connect database!");
					}else{
						
						ArrayList<String> lines = rmlProcessor.readRML(rmlFilePath);
						
						ArrayList<String> rdfTriples = rmlProcessor.getRDFTriples(lines);
						
						String rdfString = "";
						
						for(String rdfTriplesString: rdfTriples){
							rdfString+=rdfTriplesString+"\n";							
						}
						
						textAreaRMLRDF.setText(rdfString);
					}
					
				}	
					
			}
			
			
		});
		panelButtons.add(buttonRMLoad);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				textAreaRMLRDF.setEditable(false);
				
				PrintWriter out;
				try {
					// Create a file chooser
					final JFileChooser fileChooser = new JFileChooser();
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);

					String rmlFilePath = "";

					int returnVal = fileChooser.showOpenDialog(mainContainer);
					if (returnVal == JFileChooser.APPROVE_OPTION) {

						File rmlFile = fileChooser.getSelectedFile();
						rmlFilePath = rmlFile.getPath();
						// fileName = csvFile.getName();
					}

					out = new PrintWriter(rmlFilePath + "/RML_FILE_RDF.n3");
					out.println(textAreaRMLRDF.getText().toString());
					out.close();

				} catch (FileNotFoundException error) {

					JOptionPane.showMessageDialog(null, "File Saving Failed");
				}
				
			}
		});
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaRMLRDF.setEditable(true);
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnEdit);
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnSave);
		
//		JLabel lblRmlMappingOf = new JLabel("RML Mapping of The Database");
//		lblRmlMappingOf.setFont(new Font("Tahoma", Font.BOLD, 20));
//		lblRmlMappingOf.setHorizontalAlignment(SwingConstants.CENTER);
//		add(lblRmlMappingOf, BorderLayout.NORTH);
		
		JScrollPane scrollPaneRMLRDF = new JScrollPane();
		add(scrollPaneRMLRDF, BorderLayout.CENTER);
		
		textAreaRMLRDF = new JTextArea();
		textAreaRMLRDF.setEditable(false);
		textAreaRMLRDF.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPaneRMLRDF.setViewportView(textAreaRMLRDF);
	
		JPanel panelDBParams = new JPanel();
		add(panelDBParams, BorderLayout.NORTH);
		GridBagLayout gbl_panelDBParams = new GridBagLayout();
		gbl_panelDBParams.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelDBParams.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelDBParams.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0};
		gbl_panelDBParams.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0 ,0.0, Double.MIN_VALUE};
		panelDBParams.setLayout(gbl_panelDBParams);
		
		JLabel label = new JLabel("DB URL:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(5, 5, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panelDBParams.add(label, gbc_label);
		
		textFieldDBUrl = new JTextField();
		textFieldDBUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBUrl.setToolTipText("DB URL");
		textFieldDBUrl.setColumns(20);
		GridBagConstraints gbc_textFieldDBUrl = new GridBagConstraints();
		gbc_textFieldDBUrl.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBUrl.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldDBUrl.gridx = 1;
		gbc_textFieldDBUrl.gridy = 0;
		panelDBParams.add(textFieldDBUrl, gbc_textFieldDBUrl);
		
		JLabel labelDBName = new JLabel("DB Name:");
		labelDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_labelDBName = new GridBagConstraints();
		gbc_labelDBName.anchor = GridBagConstraints.EAST;
		gbc_labelDBName.insets = new Insets(5, 0, 5, 5);
		gbc_labelDBName.gridx = 2;
		gbc_labelDBName.gridy = 0;
		panelDBParams.add(labelDBName, gbc_labelDBName);
		
		textFieldDBUserName = new JTextField();
		textFieldDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBUserName.setToolTipText("DB User Name");
		textFieldDBUserName.setColumns(20);
		GridBagConstraints gbc_textFieldDBUserName = new GridBagConstraints();
		gbc_textFieldDBUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBUserName.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldDBUserName.gridx = 3;
		gbc_textFieldDBUserName.gridy = 0;
		panelDBParams.add(textFieldDBUserName, gbc_textFieldDBUserName);
		
		JLabel labelDBPassword = new JLabel("DB Password:");
		labelDBPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_labelDBPassword = new GridBagConstraints();
		gbc_labelDBPassword.anchor = GridBagConstraints.EAST;
		gbc_labelDBPassword.insets = new Insets(5, 0, 5, 5);
		gbc_labelDBPassword.gridx = 4;
		gbc_labelDBPassword.gridy = 0;
		panelDBParams.add(labelDBPassword, gbc_labelDBPassword);
		
		passwordFieldDBPassword = new JPasswordField();
		passwordFieldDBPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldDBPassword.setToolTipText("DB Password");
		passwordFieldDBPassword.setColumns(20);
		GridBagConstraints gbc_passwordFieldDBPassword = new GridBagConstraints();
		gbc_passwordFieldDBPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldDBPassword.insets = new Insets(5, 0, 5, 5);
		gbc_passwordFieldDBPassword.gridx = 5;
		gbc_passwordFieldDBPassword.gridy = 0;
		panelDBParams.add(passwordFieldDBPassword, gbc_passwordFieldDBPassword);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.insets = new Insets(5, 5, 5, 20);
		gbc_btnConnect.gridx = 6;
		gbc_btnConnect.gridy = 0;
		panelDBParams.add(btnConnect, gbc_btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				String url = textFieldDBUrl.getText().toString();
				String userName = textFieldDBUserName.getText().toString();
				String userPass = passwordFieldDBPassword.getText().toString();
				
				if(url.equals("")){
					JOptionPane.showMessageDialog(null, "Please provide URL of database.");
					return;
				}
				
				rmlProcessor = new RMLProcessor(url, userName, userPass);
				
//				textFieldDBUrl.setText("");
//				textFieldDBUserName.setText("");
//				passwordFieldDBPassword.setText("");
			}
		});
		
	}

}
