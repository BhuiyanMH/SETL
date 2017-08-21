package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import controller.OperationsController;
import model.DatabaseConnection;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainFrame {

	static CardLayout appLayout;
	JPanel panelMainContainer;
	
	public static JFrame frmMain;
	JTextArea textAreaRDF;
	JLabel lblCSVFileName;
	OperationsController  operationsController;
	
	
	//Keys for the panels
	final static String DB_PANEL_KEY = "DBPanel";
	final static String CSV_PANEL_KEY = "CSVPanel";
	final static String DB_PROPERTIES_PANEL_KEY = "DBPropertiesPanel";
	final static String DIRECT_MAPPING_PANEL_KEY = "DirectRDFPANEL";
	final static String SPARQL_QUERY_PANEL_KEY = "SPARQLPanel";
	final static String LINKING_PANEL_KEY = "LinkingPanel";
	final static String RML_PROCESSING_PANEL_KEY = "RMLProcessingPanel";
	final static String DB_PARAMS_PANEL_KEY = "DBParamsPanel";
	
	public static String dbURL;
	public static String dbUserName;
	public static String dbPassword;
	private JPanel panelTopButtons;
	//All views
	private PanelDatabase dbParamsPanel;
	private PanelCSV csvPanel;
	private PanelDirectRDF directRDFPanel;
	private PanelSparqlQuery sparqlQueryPanel;
	private LinkingPanel linkingPanel;
	private PanelRMLProcessing rmlProcessingPanel;
	
	
	private JButton btnDirectMapping;
	private JButton btnSparql;
	private JButton btnLinking;
	private JButton btnRMLTransform;
	private JPanel panelMapping;
	private JLabel lblMapping;
	private JPanel panelTransform;
	private JLabel lblTransform;
	private JPanel panelRetreive;
	private JLabel lblQuery;
	private JPanel panelLinking;
	private JLabel lblLinking;
	
	static boolean dbConnected = false;
	
	private JLabel lblExtract;
	private JButton btnPostgresDb;
	//PanelDBProperties dbPropertiesPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					MainFrame window = new MainFrame();
					window.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try {
			
			for(LookAndFeelInfo lF : UIManager.getInstalledLookAndFeels())
			{
				if(lF.getName().equals("Nimbus"))
				{
					UIManager.setLookAndFeel(lF.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Look and feel not changed.");
		}
		
		
		frmMain = new JFrame();
		frmMain.setTitle("SETL");
		frmMain.setBounds(100, 100, 1200, 800);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmMain.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panelTopButtons = new JPanel();
		JScrollPane scrollPaneTopButtons = new JScrollPane(panelTopButtons);
		frmMain.getContentPane().add(scrollPaneTopButtons, BorderLayout.NORTH);
		
		
		
		FlowLayout flowLayout = (FlowLayout) panelTopButtons.getLayout();
		flowLayout.setVgap(15);
		flowLayout.setHgap(10);
		panelTopButtons.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		//frmMain.getContentPane().add(panelTopButtons, BorderLayout.NORTH);
		
		panelMapping = new JPanel();
		panelMapping.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelMapping);
		
		lblMapping = new JLabel("MAPPING: ");
		lblMapping.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelMapping.add(lblMapping);
		
		btnDirectMapping = new JButton("Direct Mapping");
		panelMapping.add(btnDirectMapping);
		btnDirectMapping.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnDatabaseR2RML = new JButton("R2RML");
		panelMapping.add(btnDatabaseR2RML);
		btnDatabaseR2RML.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDatabaseR2RML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(DB_PANEL_KEY);
				
			}
		});
		btnDirectMapping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(DIRECT_MAPPING_PANEL_KEY);
			}
		});
		
		panelTransform = new JPanel();
		panelTransform.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelTransform);
		
		lblTransform = new JLabel("TRANSFORM: ");
		lblTransform.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTransform.add(lblTransform);
		
		JButton btnCSV = new JButton("CSV");
		panelTransform.add(btnCSV);
		btnCSV.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			showPanel(CSV_PANEL_KEY);
				
			}
		});
		
		btnRMLTransform = new JButton("RML");
		btnRMLTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(RML_PROCESSING_PANEL_KEY);
			}
		});
		panelTransform.add(btnRMLTransform);
		btnRMLTransform.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		panelRetreive = new JPanel();
		panelRetreive.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelRetreive);
		
		lblQuery = new JLabel("RETREIVE: ");
		panelRetreive.add(lblQuery);
		lblQuery.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnSparql = new JButton("SPARQL Endpoint");
		panelRetreive.add(btnSparql);
		btnSparql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(SPARQL_QUERY_PANEL_KEY);
			}
		});
		btnSparql.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		panelLinking = new JPanel();
		panelLinking.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelLinking);
		
		lblLinking = new JLabel("LINKING: ");
		lblLinking.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelLinking.add(lblLinking);
		
		btnLinking = new JButton("DBPedia");
		panelLinking.add(btnLinking);
		btnLinking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(LINKING_PANEL_KEY);
				
			}
		});
		btnLinking.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		
		panelMainContainer = new JPanel();
		frmMain.getContentPane().add(panelMainContainer, BorderLayout.CENTER);
		panelMainContainer.setLayout(new CardLayout(0, 0));
		
		
		//Initilaize the panels
		
		dbParamsPanel = new PanelDatabase(panelMainContainer);
		csvPanel = new PanelCSV(panelMainContainer);
		//dbPropertiesPanel = new PanelDBProperties(panelMainContainer);
		directRDFPanel = new PanelDirectRDF(panelMainContainer);
		sparqlQueryPanel = new PanelSparqlQuery(panelMainContainer);
		linkingPanel = new LinkingPanel();
		rmlProcessingPanel = new PanelRMLProcessing(panelMainContainer);
	
	
		//Adding the panels to the frame container
	
		
		panelMainContainer.add(sparqlQueryPanel, SPARQL_QUERY_PANEL_KEY);
		panelMainContainer.add(dbParamsPanel, DB_PANEL_KEY);
		//panelMainContainer.add(dbPropertiesPanel, DB_PROPERTIES_PANEL_KEY);
		panelMainContainer.add(csvPanel, CSV_PANEL_KEY);	
		panelMainContainer.add(directRDFPanel, DIRECT_MAPPING_PANEL_KEY);
		panelMainContainer.add(linkingPanel, LINKING_PANEL_KEY);
		panelMainContainer.add(rmlProcessingPanel, RML_PROCESSING_PANEL_KEY);
		
		//Initializing the controller for the buttons
		
		operationsController = new OperationsController();
		
		appLayout = (CardLayout)panelMainContainer.getLayout();
			
		//System.out.println("System Initialized");
		
		//showPanel(DB_PROPERTIES_PANEL_KEY);

	}
	
	void showPanel(String key)
	{
		appLayout.show(panelMainContainer, key);
	}
	
	

}
