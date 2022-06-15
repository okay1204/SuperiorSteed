package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;

public abstract class SubMenu {
    private Gui gui;
    private SuperiorHorseInfo horseInfo;
    private Pane pane;
    private ItemStack submenuItem;

    public SubMenu(Gui gui, SuperiorHorseInfo horseInfo) {
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

    protected SuperiorHorseInfo getHorseInfo() {
        return horseInfo;
    }
}
