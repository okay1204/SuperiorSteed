package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.component.Label;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.AgeMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.AttributesMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.GroomingMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.LastRiddenMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.LooksMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.OwnerMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.PregnancyMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.StatsMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.TraitsMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.TypeMenu;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.utils.Format;

public class HorseEditor {
    SuperiorHorseInfo horseInfo;
    ChestGui gui;
    StaticPane backPane;
    ArrayList<SubMenu> submenus;

    public static ItemStack customItem(Material material, String name, boolean colorize) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorize ? Format.colorize(name) : name);
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
        StaticPane submitPane = new StaticPane(8, 4, 1, 1);
        GuiItem submitButton = guiItem(Material.LIME_DYE, "&aSubmit", true);
        submitPane.setOnClick(event -> {
            player.closeInventory();

            if (horseInfo.isMale()) {
                horseInfo.setPregnancyTimer(0);
                horseInfo.setPregnantWith(null);
            }

            submitCallback.onSubmit(horseInfo);
        });

        submitPane.addItem(submitButton, 0, 0);
        gui.addPane(submitPane);
        
        // Back Pane (back button)
        backPane = new StaticPane(0, 4, 1, 1);
        GuiItem backButton = guiItem(Material.ARROW, "&cBack", true);
        
        backPane.setVisible(false);
        backPane.setOnClick(event -> {
            paginatedPane.setPage(0);
            backPane.setVisible(false);
            gui.update();
        });

        backPane.addItem(backButton, 0, 0);
        gui.addPane(backPane);

        // Main Menu, to pick between each submenu
        OutlinePane mainMenu = new OutlinePane(0, 0, 7, 3, Priority.HIGH);

        submenus = new ArrayList<SubMenu>();
        submenus.add(new LooksMenu(gui, horseInfo));
        submenus.add(new AttributesMenu(gui, horseInfo));
        submenus.add(new StatsMenu(gui, horseInfo));
        submenus.add(new TypeMenu(gui, horseInfo));
        submenus.add(new TraitsMenu(gui, horseInfo));
        // FIXME inventory manager doesn't support anvil GUIs right now, so this is disabled for now
        // submenus.add(new OwnerMenu(gui, horseInfo));
        submenus.add(new AgeMenu(gui, horseInfo));
        submenus.add(new GroomingMenu(gui, horseInfo));
        submenus.add(new PregnancyMenu(gui, horseInfo));
        submenus.add(new LastRiddenMenu(gui, horseInfo));

        paginatedPane.addPane(0, mainMenu);
        for (int i = 0; i < submenus.size(); i++) {
            int page = i + 1;
            SubMenu submenu = submenus.get(i);
            for (Pane pane : submenu.getPanes()) {
                paginatedPane.addPane(page, pane);
            }
            mainMenu.addItem(
                new GuiItem(submenu.getSubmenuItem(), event -> {
                    paginatedPane.setPage(page);
                    submenu.onShow();

                    backPane.setVisible(true);
                    gui.update();
                })
            );
        }

        paginatedPane.setPage(0);
        gui.addPane(paginatedPane);

        gui.show(player);
    }
}
