package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.statsmenu;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;

import net.md_5.bungee.api.ChatColor;

class AmountSelector {
    private static final int MIN = 0;
    private static final int MAX = 100;

    private int amount;
    private ItemStack item;
    private String name;
    private int x, y;
    private Gui gui;
    private SetStatCallback statSetter;

    public AmountSelector(ItemStack item, String name, int x, int y, double rawAmount, Gui gui, SetStatCallback statSetter) {
        this.item = item;
        ItemMeta meta = item.getItemMeta();
        amount = (int) (rawAmount * 100);
        meta.setDisplayName(name + ": " + amount);
        
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Left Click to decrease -10");
        lore.add(ChatColor.GRAY + "Right Click to increase +10");
        lore.add(ChatColor.GRAY + "Hold shift to step by 1 instead");
        meta.setLore(lore);
        item.setItemMeta(meta);

        this.name = name;
        this.statSetter = statSetter;
        this.x = x;
        this.y = y;
        this.gui = gui;
    }

    public AmountSelector(Material material, String name, int x, int y, double rawAmount, Gui gui, SetStatCallback statSetter) {
        this(new ItemStack(material), name, x, y, rawAmount, gui, statSetter);
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

            if (amount < MIN) {
                amount = MIN;
            } else if (amount > MAX) {
                amount = MAX;
            }
            
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name + ": " + amount);
            item.setItemMeta(meta);
            statSetter.setStat(amount / 100.0);
            gui.update();
        });
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}