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

    /**
     * constructor initialises private fields.
     */
	public Tag() {
		taggedComponents = new ArrayList<Taggable>();
		date = null;
        areaSet = false;
	}

	public void addTag(Taggable taggedComponent) {
		// if child tag does not exist, add to picture metadata
        if (taggedComponent.getType() == Settings.CHILD_TAG) {
            if (!taggedComponents.contains(taggedComponent)) {
                taggedComponents.add(taggedComponent);
            }
        }
        else {
            // if area tag is not already
            if (!areaSet) {
                areaSet = true;
                if (!taggedComponents.contains(taggedComponent)) {
                    taggedComponents.add(taggedComponent);
                }
            }
            else {
                taggedComponents.remove(getArea());
                taggedComponents.add(taggedComponent);
            }
        }
	}

	public void removeTag(Taggable taggedComponent) {
		taggedComponents.remove(taggedComponent);
        if (taggedComponent.getType() == Settings.AREA_TAG) {
            areaSet = false;
        }
	}

    public Taggable getArea() {

        if (areaSet) {
            for (Taggable t : taggedComponents) {
                if (t.getType() == Settings.AREA_TAG) {
                    return t;
                }
            }
        }

        return null;
    }

	public void setDate(Date date) {
		this.date = date;
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
		return (isAreaSet() && taggedComponents.size() > 1 && date != null);
	}

    public boolean isPartiallyTagged() {
        return ((isAreaSet() || taggedComponents.size() > 0) &&
                (!isAreaSet() || taggedComponents.size() <= 1));
    }

	public boolean isUntagged() {
		return (taggedComponents.size() == 0);
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
