package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;

public class StatsMenu extends SubMenu {

    private ArrayList<Pane> panes = new ArrayList<Pane>();

    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.LADDER, "&eStats", true);
    }

    public ArrayList<Pane> getPanes() {
        return panes;
    }

    public StatsMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        AmountPickerSettings amountPickerSettings = new AmountPickerSettings(0, 100, 1, 10, 0);

        // hunger
        panes.add(new AmountPicker(Material.WHEAT, Utils.colorize("&6&lHunger"), null, (int) (horseInfo.getHunger() * 100), 0, 0, gui, amountPickerSettings, hunger -> {
            horseInfo.setHunger(hunger / 100);
        }).getPane());

        // hydration
        ItemStack waterBottle = new ItemStack(Material.POTION);
        PotionMeta waterBottleMeta = (PotionMeta) waterBottle.getItemMeta();
        waterBottleMeta.setBasePotionData(new PotionData(PotionType.WATER));
        waterBottleMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        waterBottle.setItemMeta(waterBottleMeta);
        panes.add(new AmountPicker(waterBottle, Utils.colorize("&c&lHydration"), null, (int) (horseInfo.getHydration() * 100), 3, 0, gui, amountPickerSettings, hydration -> {
            horseInfo.setHydration(hydration / 100);
        }).getPane());
        
        // trust
        panes.add(new AmountPicker(Material.DIAMOND, Utils.colorize("&b&lTrust"), null, (int) (horseInfo.getTrust() * 100), 6, 0, gui, amountPickerSettings, trust -> {
            horseInfo.setTrust(trust / 100);
        }).getPane());

        // friendliness
        panes.add(new AmountPicker(Material.EMERALD, Utils.colorize("&e&lFriendliness"), null,  (int) (horseInfo.getFriendliness() * 100), 0, 2, gui, amountPickerSettings, friendliness -> {
            horseInfo.setFriendliness(friendliness / 100);
        }).getPane());

        // comfortability
        panes.add(new AmountPicker(Material.AMETHYST_SHARD, Utils.colorize("&a&lComfortability"), null, (int) (horseInfo.getComfortability() * 100), 3, 2, gui, amountPickerSettings, comfortability -> {
            horseInfo.setComfortability(comfortability / 100);
        }).getPane());

        // water bravery
        panes.add(new AmountPicker(Material.WATER_BUCKET, Utils.colorize("&9&lWater Bravery"), null, (int) (horseInfo.getWaterBravery() * 100), 6, 2, gui, amountPickerSettings, waterBravery -> {
            horseInfo.setWaterBravery(waterBravery / 100);
        }).getPane());
    }
}
