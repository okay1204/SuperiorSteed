package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import net.md_5.bungee.api.ChatColor;

public class AmountPicker {
    private StaticPane pane;
    private ItemStack item;
    private GuiItem guiItem;
    private String name;
    private String units;
    private double amount;
    private Gui gui;
    private AmountPickerSettings settings;
    private SetAmountCallback amountSetter;

    public AmountPicker(ItemStack item, String name, String units, double amount, int x, int y, Gui gui, AmountPickerSettings settings, SetAmountCallback statSetter) {
        pane = new StaticPane(x, y, 1, 1, Priority.NORMAL);

        this.item = item;
        this.settings = settings;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name + ": " + roundAndFormat(amount) + " " + (units == null ? "" : units));
        
        ArrayList<String> lore = new ArrayList<String>();
        String largeStep = roundAndFormat(settings.getLargeStep());
        lore.add(ChatColor.GRAY + "Left Click to decrease -" + largeStep);
        lore.add(ChatColor.GRAY + "Right Click to increase +" + largeStep);
        lore.add(ChatColor.GRAY + "Hold shift to step by " + roundAndFormat(settings.getSmallStep()) + " instead");
        meta.setLore(lore);
        item.setItemMeta(meta);

        this.amount = amount;
        this.name = name;
        this.units = units;
        this.amountSetter = statSetter;
        this.gui = gui;

        this.guiItem = new GuiItem(item);
        pane.addItem(guiItem, 0, 0);
        pane.setOnClick(event -> {
            onClick(event);
        });
    }

    public AmountPicker(Material material, String name, String units, double amount, int x, int y, Gui gui, AmountPickerSettings settings, SetAmountCallback statSetter) {
        this(new ItemStack(material), name, units, amount, x, y, gui, settings, statSetter);
    }

    private void onClick(InventoryClickEvent event) {
        if (event.isLeftClick()) {
            if (event.isShiftClick()) {
                amount -= settings.getSmallStep();
                playSound(event, 1.2f);
            } else {
                amount -= settings.getLargeStep();
                playSound(event, 1f);
            }
        } else {
            if (event.isShiftClick()) {
                amount += settings.getSmallStep();
                playSound(event, 1.2f);
            } else {
                amount += settings.getLargeStep();
                playSound(event, 1f);
            }
        }

        if (amount < settings.getMin()) {
            amount = settings.getMin();
        } else if (amount > settings.getMax()) {
            amount = settings.getMax();
        }
        
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name + ": " + roundAndFormat(amount) + " " + (units == null ? "" : units));
        item.setItemMeta(meta);
        amountSetter.setAmount(amount);
        gui.update();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private String roundAndFormat(double number)  {
        // if precision < 0, do not round
        // if precision = 0, round to integer
        // if precision > 0, round to precision decimal places
        double rounded;
        int precision = settings.getPrecision();
        if (precision >= 0) {
            rounded = Math.round(number * Math.pow(10.0, precision)) / Math.pow(10.0, precision);
        }
        else {
            rounded = number;
        }

        String numberString = Double.toString(rounded);
        if (numberString.endsWith(".0")) {
            return numberString.substring(0, numberString.length() - 2);
        }
        return numberString;
    }

    public StaticPane getPane() {
        return pane;
    }

    private void playSound(InventoryClickEvent event, float pitch) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            ((Player) human).playSound(human.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, pitch);
        }
    }

    public void setVisible(boolean visible) {
        if (!visible && pane.getItems().size() > 0) {
            pane.removeItem(guiItem);
        }
        else if (visible && pane.getItems().size() == 0) {
            pane.addItem(guiItem, 0, 0);
        }
    }
}