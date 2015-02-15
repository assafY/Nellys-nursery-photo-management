package Data;

import java.util.ArrayList;
import java.util.Date;

public class Tag {

	// there may be more than 3 rooms - let them decide the names
	// private static String[] ROOMS = {"Main Room", "Baby Room", "Garden"};

	private String room;
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
		room = null;
		children = new ArrayList<Child>();
		date = null;
	}

	public void setRoom(String room) {
		this.room = room;
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

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRoom() {
		return room;
	}

	public ArrayList<Child> getChildren() {
		return children;
	}

	public Date getDate() {
		return date;
	}

	public boolean isFullyTagged() {
		return (room != null && children.size() > 0 && date != null);
	}

	public boolean isPartiallyTagged() {
		return (room != null || children.size() > 0 || date != null);
	}

	@Override
	public String toString() {
		String tagString = "";
		tagString += "Date: " + date + "\n";
		tagString += "Area: " + room + "\n";
		tagString += "Children: ";
		if (children.isEmpty()) {
			tagString += "none";
		} else {
			for (Child child : children) {
				tagString += "" + child.getName() + ", ";
			}
			tagString = tagString.substring(0, tagString.length() - 2);
		}
		return tagString;
	}
}
