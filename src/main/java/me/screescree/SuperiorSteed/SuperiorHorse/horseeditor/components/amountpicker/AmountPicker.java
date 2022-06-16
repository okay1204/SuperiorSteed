package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;

import net.md_5.bungee.api.ChatColor;

public class AmountPicker {
    private double amount;
    private ItemStack item;
    private String name;
    private Gui gui;
    private AmountPickerSettings settings;
    private SetAmountCallback amountSetter;

    public AmountPicker(ItemStack item, String name, double amount, Gui gui, AmountPickerSettings settings, SetAmountCallback statSetter) {
        this.item = item;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name + ": " + removeTrailingZero(amount));
        
        ArrayList<String> lore = new ArrayList<String>();
        String largeStep = removeTrailingZero(settings.getLargeStep());
        lore.add(ChatColor.GRAY + "Left Click to decrease -" + largeStep);
        lore.add(ChatColor.GRAY + "Right Click to increase +" + largeStep);
        lore.add(ChatColor.GRAY + "Hold shift to step by " + removeTrailingZero(settings.getSmallStep()) + " instead");
        meta.setLore(lore);
        item.setItemMeta(meta);

        this.amount = amount;
        this.name = name;
        this.amountSetter = statSetter;
        this.gui = gui;
        this.settings = settings;
    }

    public AmountPicker(Material material, String name, double rawAmount, Gui gui, AmountPickerSettings settings, SetAmountCallback statSetter) {
        this(new ItemStack(material), name, rawAmount, gui, settings, statSetter);
    }

    private static String removeTrailingZero(double number)  {
        String numberString = Double.toString(number);
        if (numberString.endsWith(".0")) {
            return numberString.substring(0, numberString.length() - 2);
        }
        return numberString;
    }

    public GuiItem getGuiItem() {
        return new GuiItem(item, (event) -> {
            if (event.isLeftClick()) {
                if (event.isShiftClick()) {
                    amount -= 1;
                } else {
                    amount -= 10;
                }
            } else {
                if (event.isShiftClick()) {
                    amount += 1;
                } else {
                    amount += 10;
                }
            }

            if (amount < settings.getMin()) {
                amount = settings.getMin();
            } else if (amount > settings.getMax()) {
                amount = settings.getMax();
            }
            
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name + ": " + removeTrailingZero(amount));
            item.setItemMeta(meta);
            amountSetter.setAmount(amount);
            gui.update();
        });
    }
}