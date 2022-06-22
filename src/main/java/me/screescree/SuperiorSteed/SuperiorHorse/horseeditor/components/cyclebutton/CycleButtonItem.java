package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.cyclebutton;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;


public class CycleButtonItem<T> {
    private T definedValue;
    private GuiItem guiItem;

    public CycleButtonItem(T definedValue, Material material, String name) {
        this.definedValue = definedValue;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        guiItem = new GuiItem(item);
    }

    public T getDefinedValue() {
        return definedValue;
    }

    public GuiItem getGuiItem() {
        return guiItem;
    }
}
