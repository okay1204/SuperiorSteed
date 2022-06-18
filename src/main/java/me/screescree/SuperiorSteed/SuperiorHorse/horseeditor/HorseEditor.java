package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.component.Label;

import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.AttributesMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.LooksMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.StatsMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.TypeMenu;

public class HorseEditor {
    SuperiorHorseInfo horseInfo;
    ChestGui gui;
    Label backLabel;
    ArrayList<SubMenu> submenus;

    public static ItemStack customItem(Material material, String name, boolean colorize) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorize ? Utils.colorize(name) : name);
        item.setItemMeta(meta);
        return item;
    }
    
    public static GuiItem guiItem(Material material, String name, boolean colorize) {
        return new GuiItem(customItem(material, name, colorize));
    }
    
    public HorseEditor(Player player, String title, SubmitCallback submitCallback) {
        this(SuperiorHorseInfo.startingTemplate(), player, title, submitCallback);
    }    
    
    public HorseEditor(SuperiorHorseInfo horseInfo, Player player, String title, SubmitCallback submitCallback) {
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
        Label submitLabel = new Label(8, 4, 1, 1, Font.LIME);
        submitLabel.setText("✓", (character, item) -> {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.colorize("&aSubmit"));
            item.setItemMeta(meta);
            return new GuiItem(item);
        });
        submitLabel.setOnClick(event -> {
            player.closeInventory();
            submitCallback.onSubmit(horseInfo);
        });
        
        gui.addPane(submitLabel);
        
        // Back Pane (back button)
        backLabel = new Label(0, 4, 1, 1, Font.OAK_PLANKS);
        backLabel.setText("←", (character, item) -> {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.colorize("&eBack"));
            item.setItemMeta(meta);
            return new GuiItem(item);
        });
        backLabel.setVisible(false);
        backLabel.setOnClick(event -> {
            paginatedPane.setPage(0);
            backLabel.setVisible(false);
            gui.update();
        });

        gui.addPane(backLabel);

        // Main Menu, to pick between each submenu
        OutlinePane mainMenu = new OutlinePane(0, 0, 7, 3, Priority.HIGH);

        submenus = new ArrayList<SubMenu>();
        submenus.add(new LooksMenu(gui, horseInfo));
        submenus.add(new AttributesMenu(gui, horseInfo));
        submenus.add(new StatsMenu(gui, horseInfo));
        submenus.add(new TypeMenu(gui, horseInfo));

        paginatedPane.addPane(0, mainMenu);
        for (int i = 0; i < submenus.size(); i++) {
            int page = i + 1;
            for (Pane pane : submenus.get(i).getPanes()) {
                paginatedPane.addPane(page, pane);
            }
            mainMenu.addItem(
                new GuiItem(submenus.get(i).getSubmenuItem(), event -> {
                    paginatedPane.setPage(page);
                    backLabel.setVisible(true);
                    gui.update();
                })
            );
        }

        paginatedPane.setPage(0);
        gui.addPane(paginatedPane);

        gui.show(player);
    }
}
