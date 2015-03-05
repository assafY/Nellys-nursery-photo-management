package Data;

import Core.Library;
import Core.Settings;
import Core.Taggable;

public class Area extends Taggable{

    public Area(String name) {
        super(name);
        Library.addTaggableComponent(this);

    }

    @Override
    public int getType() {
        return Settings.AREA_TAG;
    }
}
