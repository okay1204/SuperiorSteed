package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;

public class StatsMenu extends SubMenu {

    public StatsMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        super(gui, horseInfo);

        setSubmenuItem(HorseEditor.customItem(Material.LADDER, "&bStats", true));

        StaticPane pane = new StaticPane(0, 0, 7, 3, Priority.NORMAL);
        setPane(pane);

        ArrayList<AmountPicker> amountSelectors = new ArrayList<AmountPicker>();

        AmountPickerSettings amountPickerSettings = new AmountPickerSettings(0, 100, 1, 10, 0);
        // hunger
        amountSelectors.add(new AmountPicker(Material.WHEAT, Utils.colorize("&6&lHunger"), null, (int) (horseInfo.getHunger() * 100), gui, amountPickerSettings, hunger -> {
            horseInfo.setHunger(hunger / 100);
        }));
        // hydration
        ItemStack waterBottle = new ItemStack(Material.POTION);
        PotionMeta waterBottleMeta = (PotionMeta) waterBottle.getItemMeta();
        waterBottleMeta.setBasePotionData(new PotionData(PotionType.WATER));
        waterBottleMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        waterBottle.setItemMeta(waterBottleMeta);
        amountSelectors.add(new AmountPicker(waterBottle, Utils.colorize("&c&lHydration"), null, (int) (horseInfo.getHydration() * 100), gui, amountPickerSettings, hydration -> {
            horseInfo.setHydration(hydration / 100);
        }));
        // trust
        amountSelectors.add(new AmountPicker(Material.DIAMOND, Utils.colorize("&b&lTrust"), null, (int) (horseInfo.getTrust() * 100), gui, amountPickerSettings, trust -> {
            horseInfo.setTrust(trust / 100);
        }));
        // friendliness
        amountSelectors.add(new AmountPicker(Material.EMERALD, Utils.colorize("&e&lFriendliness"), null,  (int) (horseInfo.getFriendliness() * 100), gui, amountPickerSettings, friendliness -> {
            horseInfo.setFriendliness(friendliness / 100);
        }));
        // comfortability
        amountSelectors.add(new AmountPicker(Material.AMETHYST_SHARD, Utils.colorize("&a&lComfortability"), null, (int) (horseInfo.getComfortability() * 100), gui, amountPickerSettings, comfortability -> {
            horseInfo.setComfortability(comfortability / 100);
        }));
        // water bravery
        amountSelectors.add(new AmountPicker(Material.WATER_BUCKET, Utils.colorize("&9&lWater Bravery"), null, (int) (horseInfo.getWaterBravery() * 100), gui, amountPickerSettings, waterBravery -> {
            horseInfo.setWaterBravery(waterBravery / 100);
        }));

        for (int i = 0; i < amountSelectors.size(); i++) {            
            pane.addItem(amountSelectors.get(i).getGuiItem(), (i % 3) * 3, (i / 3) * 2);
        }

    }
}
