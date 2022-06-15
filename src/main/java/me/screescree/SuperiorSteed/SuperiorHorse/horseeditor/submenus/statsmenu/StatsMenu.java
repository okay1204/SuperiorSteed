package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.statsmenu;

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
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditorInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;

public class StatsMenu extends SubMenu {

    public StatsMenu(Gui gui, HorseEditorInfo horseInfo) {
        super(gui, horseInfo);

        setSubmenuItem(HorseEditor.customItem(Material.LADDER, "&bStats", true));

        StaticPane pane = new StaticPane(0, 0, 7, 3, Priority.NORMAL);
        setPane(pane);

        ArrayList<AmountSelector> amountSelectors = new ArrayList<AmountSelector>();

        // hunger
        amountSelectors.add(new AmountSelector(Material.WHEAT, Utils.colorize("&6&lHunger"), 0, 0, horseInfo.getHunger(), gui, hunger -> {
            horseInfo.setHunger(hunger);
        }));
        // hydration
        ItemStack waterBottle = new ItemStack(Material.POTION);
        PotionMeta waterBottleMeta = (PotionMeta) waterBottle.getItemMeta();
        waterBottleMeta.setBasePotionData(new PotionData(PotionType.WATER));
        waterBottleMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        waterBottle.setItemMeta(waterBottleMeta);
        amountSelectors.add(new AmountSelector(waterBottle, Utils.colorize("&c&lHydration"), 3, 0, horseInfo.getHydration(), gui, hydration -> {
            horseInfo.setHydration(hydration);
        }));
        // trust
        amountSelectors.add(new AmountSelector(Material.DIAMOND, Utils.colorize("&b&lTrust"), 6, 0, horseInfo.getTrust(), gui, trust -> {
            horseInfo.setTrust(trust);
        }));
        // friendliness
        amountSelectors.add(new AmountSelector(Material.EMERALD, Utils.colorize("&e&lFriendliness"), 0, 2, horseInfo.getFriendliness(), gui, friendliness -> {
            horseInfo.setFriendliness(friendliness);
        }));
        // comfortability
        amountSelectors.add(new AmountSelector(Material.AMETHYST_SHARD, Utils.colorize("&a&lComfortability"), 3, 2, horseInfo.getComfortability(), gui, comfortability -> {
            horseInfo.setComfortability(comfortability);
        }));
        // water bravery
        amountSelectors.add(new AmountSelector(Material.WATER_BUCKET, Utils.colorize("&9&lWater Bravery"), 6, 2, horseInfo.getWaterBravery(), gui, waterBravery -> {
            horseInfo.setWaterBravery(waterBravery);
        }));

        for (AmountSelector selector : amountSelectors) {
            pane.addItem(selector.getGuiItem(), selector.getX(), selector.getY());
        }

    }
}
