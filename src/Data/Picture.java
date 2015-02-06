package Data;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ivaylo on 06/02/15.
 */
public class Picture {
    private ArrayList<String> taggedChildren = new ArrayList<String>();
    private ImageIcon pictureIcon;
    private boolean tagged = false;
    private String roomTag;
    private Date date;

    public Picture(String filePath){
        pictureIcon = new ImageIcon(filePath);

    }

    public Boolean isTagged(){
        return tagged;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public ImageIcon getPictureIcon(){
        return pictureIcon;
    }

    public Date getDate(){
        return date;
    }

    public String getRoomTag(){
        return roomTag;
    }

    public ArrayList<String> getTaggedChildren(){
        return taggedChildren;
    }


}
