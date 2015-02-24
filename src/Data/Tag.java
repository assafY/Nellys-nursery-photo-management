package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tag implements Serializable {

	// there may be more than 3 rooms - let them decide the names
	// private static String[] ROOMS = {"Main Room", "Baby Room", "Garden"};

	private Area area;
	private ArrayList<Child> children;
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
		children = new ArrayList<Child>();
		date = null;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void addChild(Child child) {
		// , Picture picture
		children.add(child);
		// child.addTaggedPicture(picture);
	}

	public void removeChild(Child child) {
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

	public Area getArea() {
		return area;
	}
	
	public void removeArea() {
		this.area = null;
	}

	public ArrayList<Child> getChildren() {
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
			for (Child child : children) {
				tagString += "" + child.getName() + ", ";
			}
			tagString = tagString.substring(0, tagString.length() - 2);
		}

		return tagString + ".";
	}
}
