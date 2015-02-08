package Data;

import Core.Settings;

import java.util.ArrayList;

public class Tag {

    private static String[] ROOMS = {"Main Room", "Baby Room", "Garden"};

    private String room;
    private ArrayList<Child> children;
    private String date;

    public Tag() {
        room = null;
        children = new ArrayList<Child>();
        date = null;
    }

    public void setRoom(int roomCode) {
        this.room = ROOMS[roomCode];
    }

    public void addChild(Child child) {
        children.add(child);
    }

    public void removeChild(Child child) {
        children.remove(child);
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public String getDate() {
        return date;
    }

    public boolean isFullyTagged() {
        return(room != null && children.size() > 0 && date != null);
    }

    public boolean isPartiallyTagged() {
        return(room != null || children.size() > 0 || date != null);
    }
}
