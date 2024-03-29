package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.timepicker.TimePicker;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import net.md_5.bungee.api.ChatColor;

public class AgeMenu extends SubMenu {
    private ArrayList<Pane> panes = new ArrayList<>();

    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.EXPOSED_COPPER, ChatColor.of("#a67661") + "Age", false);
    }

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }

    public AgeMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        TimePicker timePicker = new TimePicker(gui, horseInfo.getAge(), (ticks) -> {
            horseInfo.setAge(ticks);
        });

        for (Pane pane : timePicker.getPanes()) {
            panes.add(pane);
        }
    }
}
