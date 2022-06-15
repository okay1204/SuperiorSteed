package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.looksmenu;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class SelectorHandler<T> {
    private ArrayList<Selector<T>> selectors;
    private Selector<T> selected;
    private int slotOffset;

    public SelectorHandler() {
        this(0);
    }

    public SelectorHandler(int slotOffset) {
        selectors = new ArrayList<Selector<T>>();
        this.slotOffset = slotOffset;
    }

    public void add(Selector<T> selector) {
        selectors.add(selector);
    }

    public ArrayList<Selector<T>> getSelectors() {
        return selectors;
    }

    public Selector<T> getSelected() {
        return selected;
    }

    public int getSize() {
        return selectors.size();
    }

    public Selector<T> getByItem(ItemStack item) {
        for (Selector<T> selector : selectors) {
            if (selector.equals(item)) {
                return selector;
            }
        }
        return null;
    }

    public int getSlot(Selector<T> selector) {
        return slotOffset + selectors.indexOf(selector);
    }

    public void setSelected(Selector<T> selector) {
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = selector;
        selected.setSelected(true);
    }
}
