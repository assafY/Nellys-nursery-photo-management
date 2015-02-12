package Data;

import Core.Library;

import java.util.ArrayList;

public class Child {

    private String name;
    private ArrayList<Picture> taggedPictures;

    public Child(String name) {
        this.name = name;
        taggedPictures = new ArrayList<Picture>();
        Library.addChild(this);
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
}
