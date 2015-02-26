package Data;

import Core.Taggable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tag implements Serializable {

	// there may be more than 3 rooms - let them decide the names
	// private static String[] ROOMS = {"Main Room", "Baby Room", "Garden"};

	private Taggable area;
	private ArrayList<Taggable> children;
	private Date date;

	/* comment section */{
		// public Tag(ArrayList<Child> children) {
		// this();
		// this.children = children;
		// }
		// public Tag(Date date) {
		// this();
		// this.date = date;
		// }
		// public Tag(String room) {
		// this();
		// this.room = room;
		// }
	}

	public Tag() {
		area = null;
		children = new ArrayList<Taggable>();
		date = null;
	}

	public void setArea(Taggable area) {
		this.area = area;
	}

	public void addChild(Taggable child) {
		// , Picture picture
		children.add(child);
		// child.addTaggedPicture(picture);
	}

	public void removeChild(Taggable child) {
		// , Picture picture
		children.remove(child);
		// child.removeTaggedPicture(picture);
	}
	
	public void removeAllChildren() {
		children.clear();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void removeDate() {
		this.date = null;
	}

	public Taggable getArea() {
		return area;
	}
	
	public void removeArea() {
		this.area = null;
	}

	public ArrayList<Taggable> getChildren() {
		return children;
	}

	public Date getDate() {
		return date;
	}

	public boolean isFullyTagged() {
		return (area != null && children.size() > 0 && date != null);
	}

	public boolean isPartiallyTagged() {
		return (area != null || children.size() > 0 || date != null);
	}

	@Override
	public String toString() {
		String tagString = "";
		tagString += "Date: " + date + "\n";
		tagString += "Area: " + area.getName() + "\n";
		tagString += "Children: ";
		if (children.isEmpty()) {
			tagString += "none";
		} else {
			for (Taggable child : children) {
				tagString += "" + child.getName() + ", ";
			}
			tagString = tagString.substring(0, tagString.length() - 2);
		}

		return tagString + ".";
	}
}
