package Core;

import javax.swing.tree.TreePath;
import java.io.File;

public class Settings {

    public static final int CHILD_TAG = 1;
    public static final int AREA_TAG = 2;
    public static final int[] THUMBNAIL_SIZES = {377, 320, 288, 233, 196, 169, 148, 132, 119, 109};
    public static final String[] AREA_NAMES = {"Babies Room", "Main Room", "Garden"};
    public static int THUMBNAIL_CACHE_SIZE = 100;
    public static int IMPORT_THREAD_COUNT = 0;
    public static int LOADED_THUMBNAILS_COUNT = 0;
    public static boolean IMPORT_IN_PROGRESS = false;
    public static File LAST_VISITED_DIR = null;
    public static TreePath LAST_VISITED_PATH;
    public static String NURSERY_LOCATION;
    public static String CSV_PATH;
    public static File PICTURE_HOME_DIR;

}
