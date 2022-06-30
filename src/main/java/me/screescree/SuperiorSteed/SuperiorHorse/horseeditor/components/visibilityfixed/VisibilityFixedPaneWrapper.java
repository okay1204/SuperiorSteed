package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.visibilityfixed;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;

public class VisibilityFixedPaneWrapper {
    StaticPane pane;
    GuiItem guiItem;

    public VisibilityFixedPaneWrapper(int x, int y, GuiItem guiItem) {
        this.pane = new StaticPane(x, y, 1, 1);
        this.guiItem = guiItem;
    }

    public StaticPane getPane() {
        return pane;
    }

    public GuiItem getGuiItem() {
        return guiItem;
    }

    public void setVisible(boolean visible) {
        if (!visible && pane.getItems().size() > 0) {
            pane.removeItem(guiItem);
        }
        else if (visible && pane.getItems().size() == 0) {
            pane.addItem(guiItem, 0, 0);
        }
    }
}
