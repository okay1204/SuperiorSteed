package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.pane.Pane;


public abstract class SubMenu {
    public abstract ArrayList<Pane> getPanes();
    public abstract ItemStack getSubmenuItem();
}
