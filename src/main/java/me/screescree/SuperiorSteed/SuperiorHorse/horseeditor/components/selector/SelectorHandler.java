package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;

public class SelectorHandler<T> {
    private ArrayList<Selector<T>> selectors;
    private Selector<T> selected;
    private OutlinePane pane;
    private T intialValue;

    public SelectorHandler(Gui gui, OutlinePane pane, T initialValue, SelectorCallback<T> callback) {
        selectors = new ArrayList<Selector<T>>();
        this.pane = pane;
        this.intialValue = initialValue;

        pane.setOnClick(event -> {
            Selector<T> newSelector = getByItem(event.getCurrentItem());
    
            setSelected(newSelector);
            callback.setSelected(newSelector.getDefinedValue());
            gui.update();

            HumanEntity human = event.getWhoClicked();
            if (human instanceof Player) {
                Player player = (Player) human;
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        });
    }

    public void add(Selector<T> selector) {
        selectors.add(selector);

        pane.addItem(new GuiItem(selector.getItem()));
        if (selector.getDefinedValue() == intialValue) {
            setSelected(selector);
        }
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
