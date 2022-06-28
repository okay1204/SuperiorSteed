package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.multiselector.MultiSelectorHandler;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector.Selector;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.utils.Format;

public class GroomingMenu extends SubMenu {
    private static Set<Integer> ALL_ITEMS = new HashSet<>();
    static {
        for (int i = 0; i <= SuperiorHorse.GROOMING_ITEM_AMOUNT; i++) {
            ALL_ITEMS.add(i);
        }
    }

    private ArrayList<Pane> panes = new ArrayList<>();

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }
    
    @Override
    public ItemStack getSubmenuItem() {
        ItemStack item = new ItemStack(Material.BRICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Format.colorize("&9Grooming"));
        meta.setCustomModelData(3);
        item.setItemMeta(meta);
        return item;
    }

    
    public GroomingMenu(Gui gui, SuperiorHorseInfo horseInfo) {


        double expireTimeInHours;
        if (horseInfo.getGroomExpireTimer() > 0) {
            expireTimeInHours = Math.max(Math.round((horseInfo.getGroomExpireTimer() / 72000.0) * 10.0) / 10.0, 0.1);
        }
        else {
            expireTimeInHours = 24;
        }

        AmountPicker groomExpireTimerPicker = new AmountPicker(
            Material.TURTLE_HELMET,
            Format.colorize("&a&lTime until next groom"),
            "hours",
            expireTimeInHours,
            6,
            1,
            gui,
            new AmountPickerSettings(0.1, 24, 0.1, 1, 1),
            groomExpireTimer -> {
                horseInfo.setGroomExpireTimer((int) (groomExpireTimer * 72000));
            }
        );

        groomExpireTimerPicker.setVisible(horseInfo.getGroomExpireTimer() > 0);

        panes.add(groomExpireTimerPicker.getPane());

        OutlinePane multiSelectorPane = new OutlinePane(0, 1, 5, 1);
        
        MultiSelectorHandler<Integer> multiSelector = new MultiSelectorHandler<>(
            gui,
            multiSelectorPane,
            horseInfo.getGroomExpireTimer() > 0 ? ALL_ITEMS : horseInfo.getGroomedBy(),
            groomedBy -> {
                
                if (groomedBy.size() < SuperiorHorse.GROOMING_ITEM_AMOUNT) {
                    groomExpireTimerPicker.setVisible(false);
                    horseInfo.setGroomedBy(groomedBy);
                    horseInfo.setGroomExpireTimer(0);
                    
                }
                else {
                    horseInfo.setGroomedBy(new HashSet<>());
                    groomExpireTimerPicker.setVisible(true);
                    horseInfo.setGroomExpireTimer((int) (groomExpireTimerPicker.getAmount() * 72000));
                }
            }
        );

        for (int i = 1; i <= 5; i++) {
            multiSelector.add(new Selector<Integer>(i, getGroomingItem(i)));
        }

        panes.add(multiSelectorPane);
    }

    private ItemStack getGroomingItem(int type) {
        ItemStack item = new ItemStack(Material.BRICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Format.colorize("&f&lItem " + type));
        meta.setCustomModelData(type);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
}
