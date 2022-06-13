package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

public abstract class SubMenu {
    private Gui gui;
    private HorseEditorInfo horseInfo;
    private Pane pane;
    private ItemStack submenuItem;

    public SubMenu(Gui gui, HorseEditorInfo horseInfo) {
        this.gui = gui;
        this.horseInfo = horseInfo;
    }

    protected void setPane(Pane pane) {
        this.pane = pane;
    }

    protected void setSubmenuItem(ItemStack submenuGuiItem) {
        this.submenuItem = submenuGuiItem;
    }

    public Pane getPane() {
        return pane;
    }

    public ItemStack getSubmenuItem() {
        return submenuItem;
    }

    protected Gui getGui() {
        return gui;
    }

    protected HorseEditorInfo getHorseInfo() {
        return horseInfo;
    }
}
