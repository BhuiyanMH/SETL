package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.jena.rdf.model.RDFNode;

import model.FileOperations;
import model.ParseController;
import model.WebOperations;
import view.MainFrame;

public class OperationController {
	String filePath = "";
	String fileName = "";
	String webFileName = "dbpediaData.json";
	
	public String chooseFile(JFrame frame ){
		
		//Create a file chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);
		
		
		int returnVal = fileChooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			
			File csvFile = fileChooser.getSelectedFile();
			filePath = csvFile.getPath();
			fileName = csvFile.getName();
		} else {
			errorMessage("Open command cancelled by user.");
		}
		return filePath;
	}

	public void errorMessage(String string) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, string, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public ArrayList<RDFNode> extractObjects(String filePath) {
		// TODO Auto-generated method stub
		ArrayList<RDFNode> rdfNodes = new ArrayList<>();
		
		if (filePath == null) {
			errorMessage("Select the file first.");
		} else {
			File file = new File(filePath);
			if (file.exists()) {
				FileOperations operations = new FileOperations();
				rdfNodes = operations.getAllObjects(filePath);
				
				if (rdfNodes.size() == 0) {
					errorMessage("The file doesn't contain any valid object.");
				} else {
					return rdfNodes;
				}
			} else {
				errorMessage("File doesn't exists");
			}
		}
		
		return rdfNodes;
	}
	

	public LinkedHashMap<Object, Object> extractProperties(String filePath, Object object) {
		// TODO Auto-generated method stub
		LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
		
		if (filePath == null) {
			errorMessage("Select the file first.");
		} else {
			File file = new File(filePath);
			if (file.exists()) {
				FileOperations operations = new FileOperations();
				hashMap = operations.getAllProperties(filePath, object);
				
				if (hashMap.size() == 0) {
					errorMessage("The file doesn't contain any valid object.");
				} else {
					return hashMap;
				}
			} else {
				errorMessage("Sorry! File not found.");
			}
		}
		return hashMap;
	}

	/*public ArrayList<Object> getAllProperties(LinkedHashMap<Object, Object> hashMap) {
		// TODO Auto-generated method stub
		ArrayList<Object> list = new ArrayList<>();
		
		if (hashMap.isEmpty()) {
			errorMessage("Hashmap is empty");
		} else {
			for (Entry<Object, Object> m : hashMap.entrySet()) {
				list.add(m.getKey()+ " - " +m.getValue());
			}
			
			if (list.isEmpty()) {
				errorMessage("There is no valid property.");
			} else {
				return list;
			}
		}
		return list;
	}*/
	
	public ArrayList<Object> getOnlyProperties(LinkedHashMap<Object, Object> hashMap) {
		// TODO Auto-generated method stub
		ArrayList<Object> list = new ArrayList<>();
		
		if (hashMap.isEmpty()) {
			errorMessage("Hashmap is empty");
		} else {
			for (Entry<Object, Object> m : hashMap.entrySet()) {
				list.add(m.getKey());
			}
			
			if (list.isEmpty()) {
				errorMessage("There is no valid property.");
			} else {
				return list;
			}
		}
		return list;
	}

	public LinkedHashMap<Object, Object> updateHashMap(LinkedHashMap<Object, Object> hashMap, int index) {
		// TODO Auto-generated method stub
		if (hashMap.isEmpty()) {
			errorMessage("HashMap is Empty.");
		} else {
			Object key = (hashMap.keySet().toArray())[ index ];
			Object value = hashMap.get( (hashMap.keySet().toArray())[ index ] );
			
			Object newKey = key;
			
			newKey = JOptionPane.showInputDialog("Original KEY: " + key);
			
			if (!newKey.equals("") && !value.equals("")) {
				hashMap.remove(key);
				hashMap.put(newKey, value);
			}
		}
		
		return hashMap;
	}

	public LinkedHashMap<Object, Object> getDataFromWeb(LinkedHashMap<Object, Object> hashMap, String string, Object value) {
		// TODO Auto-generated method stub
		LinkedHashMap<Object, Object> webHashMap = new LinkedHashMap<>();
		try {
			int hits = Integer.parseInt(string);
			if (value == null) {
				errorMessage("Please choose a property for fetching data.");
			} else if (hashMap.isEmpty()) {
				errorMessage("There is no valid property.");
			} else if (hits < 1) {
				errorMessage("Enter a valid Max. Hit Number.");
			} else {
				WebOperations operations = new WebOperations();
				operations.retriveData(webFileName, hits, value.toString());
				
				File file = new File(webFileName);
				if(file.exists() && !file.isDirectory()) { 
					BufferedReader br = new BufferedReader(new FileReader(webFileName));
					
					if (br.readLine() != null) {
						ParseController controller = new ParseController();
						webHashMap = controller.parseData(webFileName);
						
						if (webHashMap.isEmpty()) {
							errorMessage("Problem in parsing DBpedia File");
							return null;
						}
					} else {
						errorMessage("Problem in parsing DBpedia File");
					}
				} else {
					errorMessage("Problem in parsing DBpedia File");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			errorMessage("Enter a valid Max. Hit Number.");
		}
		return webHashMap;
	}
	
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, message);
	}

	protected void showIt(Object object) {
		// TODO Auto-generated method stub
		System.out.println(object);
	}

	public LinkedHashMap<Object, Object> removeItems(LinkedHashMap<Object, Object> fileHashMap, int[] arrays) {
		// TODO Auto-generated method stub
		LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
		if (fileHashMap.isEmpty()) {
			errorMessage("HashMap is empty");
		} else {
			for (int i = 0; i < arrays.length; i++) {
				int a = arrays[i];
				Object key = (fileHashMap.keySet().toArray())[ a ];
				Object value = fileHashMap.get( (fileHashMap.keySet().toArray())[ a ] );
				hashMap.put(key, value);
			}
			if (hashMap.isEmpty()) {
				errorMessage("New HashMap is Empty");
			} else {
				showMessage("Properties Selected.");
				return hashMap;
			}
		}
		return hashMap;
	}

	public Object getSearchValue(int index, LinkedHashMap<Object, Object> fileHashMap) {
		// TODO Auto-generated method stub
		Object object = null;
		if (index != -1) {
			object = fileHashMap.get( (fileHashMap.keySet().toArray())[ index ] );
			showMessage("Property selected");
		} else {
			errorMessage("Select an item from list.");
		}
		return object;
	}

	
	public void addProperty(Object propertyValue, Object resource, String filePath) {
		// TODO Auto-generated method stub
		FileOperations operations = new FileOperations();
		operations.addProperty(propertyValue, resource, filePath);
	}
}
