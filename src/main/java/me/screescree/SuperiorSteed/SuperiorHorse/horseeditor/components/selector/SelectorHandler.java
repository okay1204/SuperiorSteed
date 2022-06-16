package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class SelectorHandler<T> {
    private ArrayList<Selector<T>> selectors;
    private Selector<T> selected;

    public SelectorHandler() {
        selectors = new ArrayList<Selector<T>>();
    }

    public void add(Selector<T> selector) {
        selectors.add(selector);
    }

    public ArrayList<Selector<T>> getSelectors() {
        return selectors;
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

    public void setSelected(Selector<T> selector) {
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = selector;
        selected.setSelected(true);
    }
}
