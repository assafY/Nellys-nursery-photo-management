package Data;

import Core.Library;

import java.util.ArrayList;

/**
 * Created by assafyossifoff on 23/02/2015.
 */
public class Area {

    private String name;
    private ArrayList<Picture> taggedPictures;

    public Area(String name) {

        this.name = name;
        taggedPictures = new ArrayList<Picture>();
        Library.addArea(this);

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
