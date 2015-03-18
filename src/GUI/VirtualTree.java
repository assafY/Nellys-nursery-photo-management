package GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import Core.Library;
import Data.Picture;

import Core.Library;
import Data.Picture;

public class VirtualTree extends JTree {
	
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private ArrayList<String> dates;
	private ArrayList<String> years;
	private ArrayList<String> months;
	private ArrayList<String> days;
	private ArrayList<String> filteredMonthAndYearDates;
	private ArrayList<String> filteredDayMonthYearDates;
	private HashMap<String, DefaultMutableTreeNode> yearDirectory;
	private HashMap<String, DefaultMutableTreeNode> monthDirectory;
	private HashMap<String, DefaultMutableTreeNode> dayDirectory;
	
	public VirtualTree(ArrayList<String> dates) {
		root = new DefaultMutableTreeNode("Pictures");
		treeModel = new DefaultTreeModel(root, true);
		this.dates = dates;
		createArrayLists();
		createHashMaps();
		removeRepeatedValuesFromLists();
		SortArrayLists();
		addYearDirectories();
		addMonthDirectories();
		addDayDirectories();
		this.setModel(treeModel);
	}
	private void createArrayLists() {
		years = new ArrayList<String>();
		months = new ArrayList<String>();
		days = new ArrayList<String>();
		filteredMonthAndYearDates = new ArrayList<String>();
		filteredDayMonthYearDates = new ArrayList<String>();
	}
	
	private void createHashMaps() {
		yearDirectory = new HashMap<String, DefaultMutableTreeNode>();
		monthDirectory = new HashMap<String, DefaultMutableTreeNode>();
		dayDirectory = new HashMap<String, DefaultMutableTreeNode>();
	}
	
	private void removeRepeatedValuesFromLists() {
		for (int i = 0; i < dates.size(); i++) {
			addOnlyNewValuesToList(dates.get(i).substring(6, 10), years);
			addOnlyNewValuesToList(dates.get(i).substring(3, 5), months);
			addOnlyNewValuesToList(dates.get(i).substring(0,2), days);
			addOnlyNewValuesToList(dates.get(i).substring(3, 10), filteredMonthAndYearDates);
			addOnlyNewValuesToList(dates.get(i), filteredDayMonthYearDates);
		}
	}
	
	private void SortArrayLists(){
		sortArrayValues(years);
		sortArrayValues(months);
		sortArrayValues(days);
		sortBySubStrings(filteredMonthAndYearDates);
		sortBySubStrings(filteredDayMonthYearDates);
	}
	
	private void addYearDirectories() {
		for (int i = 0; i < years.size(); i++) {
			String yearKey = years.get(i);
			DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(years.get(i));
			yearDirectory.put(yearKey, yearNode);
			treeModel.insertNodeInto(yearNode, root, i);

		}
	}
	
	private void addMonthDirectories() {
		for (int i = 0;i < filteredMonthAndYearDates.size();i++) {
			for (int j = 0; j < months.size(); j++) {
				for (int a = 0; a < years.size(); a++) {
					if (filteredMonthAndYearDates.get(i).equals(months.get(j) + "/" + years.get(a))) {
						if (yearDirectory.containsKey(years.get(a))) {
							String monthKey = filteredMonthAndYearDates.get(i);
							DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(monthNameCreator(months.get(j)));
							monthDirectory.put(monthKey, monthNode);
							yearDirectory.get(years.get(a)).add(monthNode);
						}
					}
				}
			}
		}
	}
	
	private void addDayDirectories() {
		for(int i = 0;i < filteredDayMonthYearDates.size();i++) {
			for(int j = 0;j< days.size();j++) {
				for(int a = 0;a < months.size();a++) {
					for(int b = 0;b < years.size();b++) {
						if(filteredDayMonthYearDates.get(i).equals(days.get(j) + "/" + months.get(a) + "/" + years.get(b))) {
							if(monthDirectory.containsKey(months.get(a) +"/" + years.get(b))) {
								String dayKey = filteredDayMonthYearDates.get(i);
								DefaultMutableTreeNode dayNode = new DefaultMutableTreeNode(days.get(j));
								dayDirectory.put(dayKey, dayNode);
								monthDirectory.get(months.get(a) +"/" + years.get(b)).add(dayNode);
							}
						}
					}
				}
			}
		}
	}

	private void sortArrayValues(ArrayList<String> list) {
		Collections.sort(list, new Comparator<String>() {
			public int compare(String firstValue, String nextValue) {
				return firstValue.compareTo(nextValue);
			}
		});
	}
	
	private void sortBySubStrings(ArrayList<String> list) {
		Collections.sort(list, new Comparator<String>() {
			public int compare(String firstValue, String nextValue) {
				return firstValue.substring(0, 2).compareTo(nextValue.substring(0, 2));
			}
		});
	}
	
	private String monthNameCreator(String monthDate) {
		String[] months = { "January", "Februrary", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		if (monthDate.equals("01")) {
			return months[0];
		} else if (monthDate.equals("02")) {
			return months[1];
		} else if (monthDate.equals("03")) {
			return months[2];
		} else if (monthDate.equals("04")) {
			return months[3];
		} else if (monthDate.equals("05")) {
			return months[4];
		} else if (monthDate.equals("06")) {
			return months[5];
		} else if (monthDate.equals("07")) {
			return months[6];
		} else if (monthDate.equals("08")) {
			return months[7];
		} else if (monthDate.equals("09")) {
			return months[8];
		} else if (monthDate.equals("10")) {
			return months[9];
		} else if (monthDate.equals("11")) {
			return months[10];
		} else if (monthDate.equals("12")) {
			return months[11];
		}
		return "";
	}
	
	private void addOnlyNewValuesToList(String date, ArrayList<String> list) {
		if (list.contains(date) == false)
			list.add(date);
	}
	
	public void updateTreeModel() {
		treeModel.reload();
	}
}
