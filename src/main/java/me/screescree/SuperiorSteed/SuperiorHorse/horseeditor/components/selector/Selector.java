package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Selector<T> {
    public final String SELECTED_PREFIX = ChatColor.AQUA + "" + ChatColor.BOLD + "Selected: ";

    private T definedValue;
    private String name;
    private ItemStack item;

    public Selector(T definedValue, ItemStack item) {
        this.definedValue = definedValue;
        this.item = item;
        this.name = item.getItemMeta().getDisplayName();
    }

    public Selector(T definedValue, Material material, String name) {
        this.definedValue = definedValue;

        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        this.name = name;
    }

    public T getDefinedValue() {
        return definedValue;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public void setSelected(boolean selected) {
        ItemMeta meta = item.getItemMeta();
        
        if (selected) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.setDisplayName(SELECTED_PREFIX + name);
        } else {
            meta.removeEnchant(Enchantment.DURABILITY);
            meta.setDisplayName(name);
        }
        item.setItemMeta(meta);
    }

    public boolean equals(ItemStack item) {
        String itemName = item.getItemMeta().getDisplayName().toLowerCase();
        // remove selected prefix if it exists
        if (itemName.startsWith(SELECTED_PREFIX.toLowerCase())) {
            itemName = itemName.substring(SELECTED_PREFIX.length());
        }

        return getItem().getType() == item.getType() && getName().toLowerCase().equals(itemName);
    }
}
