package Data;

import Core.Library;
import GUI.MainFrame;
import GUI.PictureLabel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.util.HashSet;

public class ThumbnailClickListener implements KeyListener {

    //private final HashSet<Character> pressedKeys = new HashSet<Character>();
    public static boolean shiftIsPressed = false;
    public static boolean controlIsPressed = false;
    public static PictureLabel mostRecentSelection = null;

    private int currentRow = 0;
    private int currentColumn = 0;


    public void refresh() {
        if (mostRecentSelection != null) {
            for (int i = 0; i < MainFrame.thumbsOnDisplayArray.length; ++i) {
                for (int j = 0; j < MainFrame.thumbsOnDisplayArray[i].length; ++j) {
                    if (MainFrame.thumbsOnDisplayArray[i][j] != null && MainFrame.thumbsOnDisplayArray[i][j].equals(mostRecentSelection)) {
                        currentRow = i;
                        currentColumn = j;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_SHIFT) {
            shiftIsPressed = true;
        }
        else if (k == KeyEvent.VK_CONTROL) {
            controlIsPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftIsPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            controlIsPressed = false;
        } else {

            if (Library.getPictureLibrary().size() > 0) {
                switch (Library.getSelectedThumbs().size()) {
                    case 0:
                        mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][currentColumn];
                        mostRecentSelection.toggleSelection();
                        Library.addSelectedThumb(mostRecentSelection);
                        break;
                    case 1:
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                                if (currentColumn > 0) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow][currentColumn - 1] != null) {
                                        if (!shiftIsPressed) {
                                            mostRecentSelection.toggleSelection();
                                            Library.removeSelectedThumb(mostRecentSelection);
                                        }
                                        mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][--currentColumn];
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;
                            case KeyEvent.VK_RIGHT:
                                if (currentColumn < MainFrame.thumbsOnDisplayArray[currentRow].length - 1) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow][currentColumn + 1] != null) {
                                        if (!shiftIsPressed) {
                                            mostRecentSelection.toggleSelection();
                                            Library.removeSelectedThumb(mostRecentSelection);
                                        }
                                        mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][++currentColumn];
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;
                            case KeyEvent.VK_UP:
                                if (currentRow > 0) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow - 1][currentColumn] != null) {
                                        if (!shiftIsPressed) {
                                            mostRecentSelection.toggleSelection();
                                            Library.removeSelectedThumb(mostRecentSelection);
                                        }
                                        mostRecentSelection = MainFrame.thumbsOnDisplayArray[--currentRow][currentColumn];
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;
                            case KeyEvent.VK_DOWN:
                                if (currentRow < MainFrame.thumbsOnDisplayArray.length - 1) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow + 1][currentColumn] != null) {
                                        if (!shiftIsPressed) {
                                            mostRecentSelection.toggleSelection();
                                            Library.removeSelectedThumb(mostRecentSelection);
                                        }
                                        mostRecentSelection = MainFrame.thumbsOnDisplayArray[++currentRow][currentColumn];
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;

                        }
                        break;
                    default:
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                                if (currentColumn > 0) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow][currentColumn - 1] != null) {
                                        if (!shiftIsPressed) {
                                            Library.removeAllSelectedThumbs();
                                            mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][--currentColumn];
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                        else {
                                            if (MainFrame.thumbsOnDisplayArray[currentRow][currentColumn - 1].isSelected()) {
                                                mostRecentSelection.toggleSelection();
                                                Library.removeSelectedThumb(mostRecentSelection);
                                                mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][--currentColumn];
                                            }
                                            else {
                                                mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][--currentColumn];
                                                mostRecentSelection.toggleSelection();
                                                Library.addSelectedThumb(mostRecentSelection);
                                            }
                                        }
                                    }
                                }
                                else {
                                    if (!shiftIsPressed) {
                                        Library.removeAllSelectedThumbs();
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;
                            case KeyEvent.VK_RIGHT:
                                if (currentColumn < MainFrame.thumbsOnDisplayArray[currentRow].length - 1) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow][currentColumn + 1] != null) {
                                        if (!shiftIsPressed) {
                                            Library.removeAllSelectedThumbs();
                                            mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][++currentColumn];
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                        else {
                                            if (MainFrame.thumbsOnDisplayArray[currentRow][currentColumn + 1].isSelected()) {
                                                mostRecentSelection.toggleSelection();
                                                Library.removeSelectedThumb(mostRecentSelection);
                                                mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][++currentColumn];
                                            }
                                            else {
                                                mostRecentSelection = MainFrame.thumbsOnDisplayArray[currentRow][++currentColumn];
                                                mostRecentSelection.toggleSelection();
                                                Library.addSelectedThumb(mostRecentSelection);
                                            }
                                        }
                                    }
                                    else {
                                        if (!shiftIsPressed) {
                                            Library.removeAllSelectedThumbs();
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                    }
                                }
                                else {
                                    if (!shiftIsPressed) {
                                        Library.removeAllSelectedThumbs();
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;
                            case KeyEvent.VK_UP:
                                if (currentRow > 0) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow - 1][currentColumn] != null) {
                                        if (!shiftIsPressed) {
                                            Library.removeAllSelectedThumbs();
                                            mostRecentSelection = MainFrame.thumbsOnDisplayArray[--currentRow][currentColumn];
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                        else {
                                            mostRecentSelection = MainFrame.thumbsOnDisplayArray[--currentRow][currentColumn];
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                    }
                                }
                                else {
                                    if (!shiftIsPressed) {
                                        Library.removeAllSelectedThumbs();
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;
                            case KeyEvent.VK_DOWN:
                                if (currentRow < MainFrame.thumbsOnDisplayArray.length - 1) {
                                    if (MainFrame.thumbsOnDisplayArray[currentRow + 1][currentColumn] != null) {
                                        if (!shiftIsPressed) {
                                            Library.removeAllSelectedThumbs();
                                            mostRecentSelection = MainFrame.thumbsOnDisplayArray[++currentRow][currentColumn];
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                        else {
                                            mostRecentSelection = MainFrame.thumbsOnDisplayArray[++currentRow][currentColumn];
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                    }
                                    else {
                                        if (!shiftIsPressed) {
                                            Library.removeAllSelectedThumbs();
                                            mostRecentSelection.toggleSelection();
                                            Library.addSelectedThumb(mostRecentSelection);
                                        }
                                    }
                                }
                                else {
                                    if (!shiftIsPressed) {
                                        Library.removeAllSelectedThumbs();
                                        mostRecentSelection.toggleSelection();
                                        Library.addSelectedThumb(mostRecentSelection);
                                    }
                                }
                                break;

                        }
                        break;

                }

            }
        }
    }
}
