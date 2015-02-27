package Data;

import Core.Settings;
import Core.Taggable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tag implements Serializable {


	private ArrayList<Taggable> taggedComponents;
	private Date date;
    private boolean areaSet;

	public Tag() {
		taggedComponents = new ArrayList<Taggable>();
		date = null;
        areaSet = false;
	}

	public void addTag(Taggable taggedComponent) {
		// , Picture picture
		taggedComponents.add(taggedComponent);
        if (taggedComponent.getType() == Settings.AREA_TAG) {
            areaSet = true;
        }
		// child.addTaggedPicture(picture);
	}

	public void removeTag(Taggable taggedComponent) {
		// , Picture picture
		taggedComponents.remove(taggedComponent);
        if (taggedComponent.getType() == Settings.AREA_TAG) {
            areaSet = false;
        }
		// child.removeTaggedPicture(picture);
	}
	
	public void removeAllTags() {
		taggedComponents.clear();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void removeDate() {
		this.date = null;
	}

	public ArrayList<Taggable> getTaggedComponents() {
		return taggedComponents;
	}

	public Date getDate() {
		return date;
	}

    public boolean isAreaSet() {
        return areaSet;
    }

	public boolean isFullyTagged() {
		return (taggedComponents.size() > 0 && date != null);
	}

	public boolean isPartiallyTagged() {
		return (taggedComponents.size() > 0 || date != null);
	}

	@Override
	public String toString() {
		String tagString = "";
		tagString += "Date: " + date + "\n";
		tagString += "Children and Areas: ";
		if (taggedComponents.isEmpty()) {
			tagString += "none";
		} else {
			for (Taggable taggedComponent : taggedComponents) {
				tagString += "" + taggedComponent.getName() + ", ";
			}
			tagString = tagString.substring(0, tagString.length() - 2);
		}

		return tagString + ".";
	}
}
