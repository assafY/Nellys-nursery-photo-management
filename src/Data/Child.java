package Data;

import java.util.ArrayList;

public class Child {

    private String name;
    private ArrayList<Picture> taggedPictures;

    public Child(String name) {
        this.name = name;
        taggedPictures = new ArrayList<Picture>();
    }

    public void addTaggedPicture(Picture picture) {
        taggedPictures.add(picture);
    }

    public ArrayList<Picture> getTaggedPictures() {
        return taggedPictures;
    }
}
