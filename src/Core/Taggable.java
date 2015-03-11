package Core;

import Data.Picture;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Taggable implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -149605517619597727L;
	private String name;
    private ArrayList<Picture> taggedPictures;

    public Taggable(String name) {

        this.name = name;
        taggedPictures = new ArrayList<Picture>();

    }

    public String getName() {
        return name;
    }

    public void addTaggedPicture(Picture picture) {
        taggedPictures.add(picture);
    }

    public void removeTaggedPicture(Picture picture) {
        taggedPictures.remove(picture);
    }

    public ArrayList<Picture> getTaggedPictures() {
        return taggedPictures;
    }

    public abstract int getType();
}