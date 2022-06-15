package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.StatsMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.looksmenu.LooksMenu;

public class HorseEditor {
    HorseEditorInfo horseInfo;
    ChestGui gui;
    StaticPane backPane;
    ArrayList<SubMenu> submenus;

    public static ItemStack customItem(Material material, String name, boolean colorize, boolean enchanted) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorize ? Utils.colorize(name) : name);
        if (enchanted) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack customItem(Material material, String name, boolean colorize) {
        return customItem(material, name, colorize, false);
    }

    public static GuiItem guiItem(Material material, String name, boolean colorize, boolean enchanted) {
        return new GuiItem(customItem(material, name, colorize, enchanted));
    }

    public static GuiItem guiItem(Material material, String name, boolean colorize) {
        return new GuiItem(customItem(material, name, colorize));
    }
    
    public HorseEditor(String title) {
        horseInfo = HorseEditorInfo.startingTemplate();
        
        // GUI
        gui = new ChestGui(5, title);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        
        // Paginated pane, to cycle through different submenus
        PaginatedPane paginatedPane = new PaginatedPane(1, 1, 7, 3, Priority.NORMAL);

        // Background (black stain glass background)
        OutlinePane background = new OutlinePane(0, 0, 9, 5, Priority.LOWEST);
        GuiItem backgroundItem = guiItem(Material.BLACK_STAINED_GLASS_PANE, " ", false);
        background.addItem(backgroundItem);
        background.setRepeat(true);
        
        gui.addPane(background);
        
        // Submit Pane (submit button)
        StaticPane submitPane = new StaticPane(8, 4, 1, 1, Priority.NORMAL);
        GuiItem submitItem = guiItem(Material.LIME_CONCRETE, "&aSubmit", true);
        submitPane.addItem(submitItem, 0, 0);
        
        gui.addPane(submitPane);
        
        // Back Pane (back button)
        backPane = new StaticPane(0, 4, 1, 1, Priority.NORMAL);
        backPane.addItem(guiItem(Material.ARROW, "&aBack", true), 0, 0);
        backPane.setVisible(false);
        backPane.setOnClick(event -> {
            paginatedPane.setPage(0);
            backPane.setVisible(false);
            gui.update();
        });

        gui.addPane(backPane);

        // Main Menu, to pick between each submenu
        OutlinePane mainMenu = new OutlinePane(0, 0, 7, 3, Priority.HIGH);

        submenus = new ArrayList<SubMenu>();
        submenus.add(new LooksMenu(gui, horseInfo));
        submenus.add(new StatsMenu(gui, horseInfo));

        paginatedPane.addPane(0, mainMenu);
        for (int i = 0; i < submenus.size(); i++) {
            int page = i + 1;
            paginatedPane.addPane(page, submenus.get(i).getPane());
            mainMenu.addItem(
                new GuiItem(submenus.get(i).getSubmenuItem(), event -> {
                    paginatedPane.setPage(page);
                    backPane.setVisible(true);
                    gui.update();
                })
            );
        }

        paginatedPane.setPage(0);
        gui.addPane(paginatedPane);
    }

    public void show(Player player) {
        gui.show(player);
    }
}
