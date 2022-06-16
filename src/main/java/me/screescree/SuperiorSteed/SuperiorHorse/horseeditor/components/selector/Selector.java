package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Selector<T> {
    private T definedValue;
    private String name;
    private ItemStack item;

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
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Selected: " + ChatColor.RESET + name);
        } else {
            meta.removeEnchant(Enchantment.DURABILITY);
            meta.setDisplayName(name);
        }
        item.setItemMeta(meta);
    }

    public boolean equals(ItemStack item) {
        return getItem().getType() == item.getType() && getName().toLowerCase().equals(item.getItemMeta().getDisplayName().toLowerCase());
    }
}
