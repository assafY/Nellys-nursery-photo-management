package Data;

import Core.Library;
import Core.Taggable;

public class Area extends Taggable{

    public Area(String name) {
        super(name);
        Library.addArea(this);

    }
}
