package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditorInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;

public class StatsMenu extends SubMenu {

    public StatsMenu(Gui gui, HorseEditorInfo horseInfo) {
        super(gui, horseInfo);

        // Inventory inventory;
        // try {
        //     Field inventoryField = Gui.class.getDeclaredField("inventory");
        //     inventoryField.setAccessible(true);
        //     inventory = (Inventory) inventoryField.get(gui);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return;
        // }
        // System.out.println(inventory);
        // inventory.setMaxStackSize(100);

        setSubmenuItem(HorseEditor.customItem(Material.LADDER, "&bStats", true));

        StaticPane pane = new StaticPane(0, 0, 7, 3, Priority.NORMAL);
        ItemStack item = new ItemStack(Material.WHEAT);
        item.setAmount(100);
        pane.addItem(new GuiItem(item), 0, 0);

        setPane(pane);
    }
}
