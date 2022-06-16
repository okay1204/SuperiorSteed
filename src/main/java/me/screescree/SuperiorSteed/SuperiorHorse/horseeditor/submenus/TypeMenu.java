package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import org.bukkit.Material;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.togglebutton.ToggleButton;

public class TypeMenu extends SubMenu {
    public TypeMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        super(gui, horseInfo);
        setSubmenuItem(HorseEditor.customItem(Material.SOUL_TORCH, "&bType", true));

        StaticPane pane = new StaticPane(0, 0, 7, 3, Priority.NORMAL);
        setPane(pane);

        ToggleButton stallionToggle = new ToggleButton(
            horseInfo.isStallion(),
            Material.GRASS_BLOCK,
            Utils.colorize("&2&lStallion"),
            Material.OAK_SAPLING,
            Utils.colorize("&a&lGelding"),
            gui,
            enabled -> {
                horseInfo.setStallion(enabled);
            }
        );
        
        if (horseInfo.isMale()) {
            pane.addItem(stallionToggle.getGuiItem(), 5, 1);
        }
            

        pane.addItem(
            new ToggleButton(
                horseInfo.isMale(),
                Material.BLUE_CONCRETE,
                Utils.colorize("&9&lMale"),
                Material.PINK_CONCRETE,
                Utils.colorize("&d&lFemale"),
                gui,
                enabled -> {
                    horseInfo.setMale(enabled);

                    if (enabled) {
                        pane.addItem(stallionToggle.getGuiItem(), 5, 1);
                    }
                    else {
                        pane.removeItem(stallionToggle.getGuiItem());
                    }
                }
            ).getGuiItem(),
            1,
            1
        );

    }
}
