package Data;

import java.io.Serializable;

import Core.Library;
import Core.Settings;
import Core.Taggable;

public class Area extends Taggable implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4164023735887524588L;

	public Area(String name) {
        super(name);
        Library.addTaggableComponent(this);

    }

    @Override
    public int getType() {
        return Settings.AREA_TAG;
    }
}
