package Data;

import Core.Library;
import Core.Settings;
import Core.Taggable;

import java.io.Serializable;

public class Child extends Taggable implements Serializable {

	private static final long serialVersionUID = 3434858481118705418L;
    private Taggable defaultArea;

    public Child(String name, Taggable defaultArea) {
        super(name);
        this.defaultArea = defaultArea;
        Library.addTaggableComponent(this);
    }

    public Taggable getDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(Area newDefaultArea) {
        defaultArea = newDefaultArea;
    }

    @Override
    public int getType() {
        return Settings.CHILD_TAG;
    }
}
