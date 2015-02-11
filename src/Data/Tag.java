package Data;

import java.util.ArrayList;
import java.util.Date;

public class Tag {

    private static String[] ROOMS = {"Main Room", "Baby Room", "Garden"};

    private String room;
    private ArrayList<Child> children;
    private Date date;

    public Tag() {
        room = null;
        children = new ArrayList<Child>();
        date = null;
    }

    public void setRoom(int roomCode) {
        this.room = ROOMS[roomCode];
    }

    public void addChild(Child child, Picture picture) {
        children.add(child);
        child.addTaggedPicture(picture);
    }

    public void removeChild(Child child, Picture picture) {
        children.remove(child);
        child.removeTaggedPicture(picture);
    }

    public void setDate(Date date){
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
        return(room != null && children.size() > 0 && date != null);
    }

    public boolean isPartiallyTagged() {
        return(room != null || children.size() > 0 || date != null);
    }
}
