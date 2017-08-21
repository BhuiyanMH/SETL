package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import controller.OperationController;
import model.Pair;
import model.Word;

import javax.swing.JComboBox;
import java.awt.Font;

public class PanelOperation extends JPanel {

	private JFileChooser fileChooser = new JFileChooser();
	private JLabel lblFileName;
	private JPanel panel = new JPanel();
	
	private String openImagePath = "images/Open16.gif";
	String filePath = null, newFilePath = "matched.rdf";
	
	private JComboBox comboBoxObjects;
	private JComboBox comboBoxProperties;
	private JComboBox comboBoxResults;
	private JScrollPane scrollPane = new JScrollPane();
	
	private JTextField txtHits;
	
	LinkedHashMap<Object, Object> fileHashMap = new LinkedHashMap<>();
	LinkedHashMap<Object, Object> webHashMap = new LinkedHashMap<>();
	
	PanelView panelView;
	LinkingPanel linkingPanel;
	
	Object searchObject, keyObject;
	
	public PanelOperation(PanelView panelView, LinkingPanel linkingPanel) {
		setLayout(new MigLayout("", "[][][][][grow][]", "[][]"));
		setBackground(Color.WHITE);
		
		this.panelView = panelView;
		this.linkingPanel = linkingPanel;
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnOpenFile.setIcon(createImageIcon(openImagePath));
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OperationController controller = new OperationController();
				filePath = controller.chooseFile(MainFrame.frmMain);
				lblFileName.setText(filePath);
			}
		});
		add(btnOpenFile, "growx");
		
		lblFileName = new JLabel();
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFileName.setForeground(Color.BLUE);
		add(lblFileName);
		
		JButton btnExtractObjectsFrom = new JButton("Extract Objects From File");
		btnExtractObjectsFrom.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnExtractObjectsFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperationController controller = new OperationController();
				ArrayList<RDFNode> rdfNodes = controller.extractObjects(filePath);
				
				comboBoxObjects.setModel(new DefaultComboBoxModel(rdfNodes.toArray()));
			}
		});
		add(btnExtractObjectsFrom, "growx, gapx unrelated");
		
		JLabel lblObjects = new JLabel("Select Object: ");
		lblObjects.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblObjects.setForeground(Color.BLUE);
		add(lblObjects, "gapx unrelated");
		
		comboBoxObjects = new JComboBox();
		comboBoxObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyObject = comboBoxObjects.getSelectedItem();
				
				OperationController controller = new OperationController();
				fileHashMap = controller.extractProperties(filePath, keyObject);
				
				// ArrayList<Object> objectList = controller.getOnlyProperties(fileHashMap);
				// comboBoxObjects.setModel(new DefaultComboBoxModel(objectList.toArray()));
				
				createList(fileHashMap, true);
			}
		});
		comboBoxObjects.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(comboBoxObjects, "growx, gapx unrelated");
		
		JButton btnGraphFile = new JButton("View Graph From File");
		btnGraphFile.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnGraphFile.setPreferredSize(new Dimension(166, 26));
		btnGraphFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					remove(panel);
					linkingPanel.remove(scrollPane);
					linkingPanel.repaint();
					linkingPanel.revalidate();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				Object object = comboBoxObjects.getSelectedItem();
				int index = comboBoxObjects.getSelectedIndex();
				
				if (index == -1) {
					OperationController controller = new OperationController();
					controller.errorMessage("Select Object First.");
				} else {
					if (!fileHashMap.isEmpty()) {
						PanelGraph panelGraph = new PanelGraph(object, fileHashMap);
						panelView.removeAll();
						panelView.add(panelGraph, "push, grow, wrap");
						panelView.repaint();
						panelView.revalidate();
						panelView.setVisible(true);
						linkingPanel.add(panelView, "push, grow, wrap");
					} else {
						OperationController controller = new OperationController();
						controller.errorMessage("HashMap is Empty.");
					}
				}
			}
		});
		add(btnGraphFile, "gapx unrelated, wrap");
		
		/*JLabel lblProperties = new JLabel("Select Property: ");
		lblProperties.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblProperties.setForeground(Color.BLUE);
		add(lblProperties, "gapy unrelated");*/
		
		JButton btnSelectProperties = new JButton("Select Key Property");
		btnSelectProperties.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSelectProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperationController controller = new OperationController();
				if (fileHashMap.isEmpty()) {
					controller.errorMessage("HashMap is empty. Select an object first.");
				} else {
					createList(fileHashMap, false);
				}
			}
		});
		add(btnSelectProperties, "gapy unrelated");
		
		/*comboBoxProperties = new JComboBox();
		comboBoxProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = comboBoxProperties.getSelectedIndex();
				
				int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to Edit the key ?");
				if (dialogResult == JOptionPane.YES_OPTION) {
					OperationController controller = new OperationController();
					fileHashMap = controller.updateHashMap(fileHashMap, index);
					
					ArrayList<String> stringList = controller.getAllProperties(fileHashMap);
					comboBoxProperties.setModel(new DefaultComboBoxModel(stringList.toArray()));
				}
			}
		});
		comboBoxProperties.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(comboBoxProperties, "span 2, growx, gapy unrelated, gapx unrelated");*/
		
		JLabel lblHits = new JLabel("Enter Max. Hits: ");
		lblHits.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblHits.setForeground(Color.BLUE);
		add(lblHits, "span 2, split 2, gapy unrelated, gapx unrelated");
		
		txtHits = new JTextField(20);
		txtHits.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtHits.setPreferredSize(new Dimension(200, 25));
		add(txtHits, "growx, gapy unrelated, gapx unrelated");
		
		JButton btnDbpedia = new JButton("Fetch Data From DBpedia");
		btnDbpedia.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDbpedia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String value = txtHits.getText().trim();
				
				OperationController controller = new OperationController();
				webHashMap = controller.getDataFromWeb(fileHashMap, value, searchObject);
				
				ArrayList<Object> objectList = controller.getOnlyProperties(webHashMap);
				comboBoxResults.setModel(new DefaultComboBoxModel(objectList.toArray()));
				
				txtHits.setText("");
			}
		});
		add(btnDbpedia, "span 3, split 3, gapy unrelated, gapx unrelated");
		
		comboBoxResults = new JComboBox();
		comboBoxResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = comboBoxResults.getSelectedIndex();
				Object value = webHashMap.get( (webHashMap.keySet().toArray())[ index ] );
				
				panelView.removeAll();
				scrollPane.setViewportView(panelView);
				
				OperationController controller = new OperationController();
				if (keyObject == null) {
					controller.errorMessage("Select an Object First.");
				} else if (searchObject == null) {
					controller.errorMessage("Select a key property.");
				} else if (fileHashMap.isEmpty()) {
					controller.errorMessage("HashMap is empty.");
				} else if (value == null) {
					controller.errorMessage("Select a dbpedia object first.");
				} else if (filePath == null) {
					controller.errorMessage("Select a file first.");
				} else {
					matchValues(fileHashMap, value, searchObject, keyObject, filePath);
					viewResult(newFilePath, keyObject);
				}
			}
		});
		comboBoxResults.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(comboBoxResults, "growx, gapx unrelated, gapy unrelated");
		
		JButton btnGraphWeb = new JButton("View Graph From Web");
		btnGraphWeb.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnGraphWeb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					remove(panel);
					linkingPanel.remove(scrollPane);
					linkingPanel.repaint();
					linkingPanel.revalidate();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				Object object = comboBoxResults.getSelectedItem();
				int index = comboBoxResults.getSelectedIndex();
				
				if (index == -1) {
					OperationController controller = new OperationController();
					controller.errorMessage("Select Object First.");
				} else {
					if (!webHashMap.isEmpty()) {
						PanelGraph panelGraph = new PanelGraph(index, webHashMap, true);
						panelView.removeAll();
						panelView.add(panelGraph, "push, grow, wrap");
						panelView.repaint();
						panelView.revalidate();
						panelView.setVisible(true);
						linkingPanel.add(panelView, "push, grow, wrap");
						linkingPanel.repaint();
						linkingPanel.revalidate();
					} else {
						OperationController controller = new OperationController();
						controller.errorMessage("HashMap is Empty.");
					}
				}
			}
		});
		add(btnGraphWeb, "gapx unrelated, gapy unrelated, wrap");
	}

	protected void viewResult(String newFilePath2, Object keyObject2) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(newFilePath2);
		
		String queryString = 
				"SELECT ?p ?o WHERE {?s ?p ?o. "
				+ "FILTER regex(str(?s), '"+ keyObject2.toString() +"')}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		
		showLabel("");
		
		while (results.hasNext()) {
			QuerySolution querySolution = (QuerySolution) results.next();
			
			RDFNode property = querySolution.get("p");
			RDFNode value = querySolution.get("o");
			
			showLabel(property + " - " + value);
		}
		
		qe.close();
	}

	private void matchValues(LinkedHashMap<Object, Object> hashMap, Object value, Object propertyValue, Object resource, String filePath) {
		// TODO Auto-generated method stub
		if (value instanceof ArrayList) {
			for (int i = 0; i < ((ArrayList) value).size(); i++) {
				Pair pair = (Pair) ((ArrayList) value).get(i);
				Object keyObject = pair.getKey();
				Object valueObject = pair.getValue();
				// showIt(keyObject);
				// showIt(valueObject);
				
				if (valueObject instanceof ArrayList) {
					// showIt("ArrayLIst");
					matchValues(hashMap, valueObject, propertyValue, resource, filePath);
				} else if (valueObject instanceof LinkedHashMap) {
					// showIt("HashMap");
					matchValues(hashMap, valueObject, propertyValue, resource, filePath);
				} else {
					int total = 0;
					
					for (Entry<Object, Object> m : hashMap.entrySet()) {
						Object newKey = m.getKey();
						Object newValue = m.getValue();

						if (newValue.toString().trim().toLowerCase().equals(valueObject.toString().trim().toLowerCase())) {
							showLabel("Are " + keyObject + " and " + newKey + " is same ?", true);
							Word word = new Word();
							boolean result = word.wordSearch(keyObject.toString(), newKey.toString());
							if (result) {
								showLabel("They are synonym");
								OperationController controller = new OperationController();
								controller.addProperty(propertyValue, resource, filePath);
							} else {
								showLabel("We are not sure about them");
							}
						} else if (valueObject.toString().trim().toLowerCase().indexOf(newValue.toString().trim().toLowerCase()) != -1) {
							String string = valueObject.toString().toLowerCase();
							Pattern pattern = Pattern.compile("\\b" + value.toString().toLowerCase() + "\\b");
							Matcher matcher = pattern.matcher(string);
							int count = 0;
							while (matcher.find())
								count++;

							if (count > 0) {
								total = total + count;
								showLabel(keyObject + " contains " + newValue + " whose key is " + newKey
										+ " partially. Number of Occurance: " + count + "" + " Total Match of All HashMap Items: "
										+ total);

								showLabel("Are they same?", true);

								Word word = new Word();
								boolean result = word.wordSearch(keyObject.toString(), newKey.toString());
								if (result) {
									showLabel("They are synonym", true);
									
									OperationController controller = new OperationController();
									controller.addProperty(propertyValue, resource, filePath);
								} else {
									showLabel("We are not sure about them");
								}
							}
						}
					}
				}
			}
		} else if (value instanceof LinkedHashMap) {
			for (Entry<Object, Object> m : ((LinkedHashMap<Object, Object>) value).entrySet()) {
				Object v = m.getValue();
				matchValues(hashMap, v, propertyValue, resource, filePath);
			}
		} else {
			showIt(value);
		}
	}

	private void showLabel(Object object) {
		// TODO Auto-generated method stub
		try {
			remove(panel);
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
		linkingPanel.repaint();
		linkingPanel.revalidate();
		
		JLabel label = new JLabel(object.toString());
		
		// PanelGraph panelGraph = new PanelGraph();
		// panelGraph.add(label, "wrap");
		
		// panelView.removeAll();
		panelView.add(label, "wrap");
		panelView.repaint();
		panelView.revalidate();
		panelView.setVisible(true);
		linkingPanel.add(scrollPane, "grow, wrap");
		linkingPanel.repaint();
		linkingPanel.revalidate();
	}

	private void showLabel(Object object, boolean b) {
		// TODO Auto-generated method stub
		try {
			remove(panel);
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
		linkingPanel.repaint();
		linkingPanel.revalidate();
		
		JLabel label = new JLabel(object.toString());
		label.setForeground(Color.BLUE);
		
		// PanelGraph panelGraph = new PanelGraph();
		// panelGraph.add(label, "wrap");
		
		// panelView.removeAll();
		panelView.add(label, "wrap");
		panelView.repaint();
		panelView.revalidate();
		panelView.setVisible(true);
		linkingPanel.add(scrollPane, "grow, wrap");
		linkingPanel.repaint();
		linkingPanel.revalidate();
	}

	public void createList(LinkedHashMap<Object, Object> hashMap, boolean selection) {
		// TODO Auto-generated method stub
		/*try {
			remove(panel);
			linkingPanel.remove(panelView);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		panelView.setVisible(false);*/
		
		panelRemove();
		panel = new JPanel();
		panel.setLayout(new MigLayout());
		OperationController controller = new OperationController();
		
		if (!hashMap.isEmpty()) {
			JLabel lblProperties = new JLabel("Select Property: ");
			lblProperties.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblProperties.setForeground(Color.BLUE);
			panel.add(lblProperties, "wrap");
			
			DefaultListModel<Object> listModel = new DefaultListModel<>();
			for (Object value : hashMap.keySet()) {
				listModel.addElement(value);
			}
			JList list = new JList(listModel);
			if (selection) {
				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			} else {
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
			list.setLayoutOrientation(JList.VERTICAL);
			list.setFont(new Font("Tahoma", Font.PLAIN, 14));
			list.setVisibleRowCount(-1);
			
			list.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if (e.getClickCount() == 2) {
						int index = list.locationToIndex(e.getPoint());
			            OperationController controller = new OperationController();
						fileHashMap = controller.updateHashMap(hashMap, index);
						createList(fileHashMap, selection);
					}
				}
			});
					
			JScrollPane listScroller = new JScrollPane(list);
	        listScroller.setPreferredSize(new Dimension(250, 400));
	        listScroller.setAlignmentX(LEFT_ALIGNMENT);
			
			panel.add(listScroller, "push, grow, wrap");
			
			JButton btnFinish = new JButton("Finish");
			btnFinish.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnFinish.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (selection) {
						int[] arrays = list.getSelectedIndices();
						/*LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
						for (int i = 0; i < arrays.length; i++) {
							int a = arrays[i];
							Object key = (fileHashMap.keySet().toArray())[ a ];
							Object value = fileHashMap.get( (fileHashMap.keySet().toArray())[ a ] );
							hashMap.put(key, value);
						}*/
						
						fileHashMap = controller.removeItems(fileHashMap, arrays);
						/*for(Entry<Object, Object> m:fileHashMap.entrySet()){  
							System.out.println(m.getKey()+" "+m.getValue());
						}*/
						// fileHashMap = hashMap;
					} else {
						int index = list.getSelectedIndex();
						// searchObject = fileHashMap.get( (fileHashMap.keySet().toArray())[ index ] );
						searchObject = controller.getSearchValue(index, fileHashMap);
					}
					// OperationController controller = new OperationController();
					// controller.showMessage("Properties Selected.");
					panelRemove();
				}
			});
			panel.add(btnFinish, "grow, wrap");
		}
		
		repaint();
		revalidate();
		
		add(panel, "growx, wrap, span 2");
	}

	private void panelRemove() {
		// TODO Auto-generated method stub
		try {
			remove(panel);
			repaint();
			revalidate();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

	protected void showIt(Object object) {
		// TODO Auto-generated method stub
		System.out.println(object);
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = PanelOperation.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
