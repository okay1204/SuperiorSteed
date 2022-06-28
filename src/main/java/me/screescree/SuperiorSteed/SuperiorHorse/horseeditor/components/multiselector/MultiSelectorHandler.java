package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.multiselector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector.Selector;

public class MultiSelectorHandler<T> {
    private ArrayList<Selector<T>> selectors = new ArrayList<>();
    private ArrayList<Selector<T>> selected = new ArrayList<>();
    private OutlinePane pane;
    private Set<T> initialValues;

    public MultiSelectorHandler(Gui gui, OutlinePane pane, Set<T> initialValues, MultiSelectorCallback<T> callback) {
        this.pane = pane;
        this.initialValues = initialValues;

        pane.setOnClick(event -> {
            Selector<T> newSelector = getByItem(event.getCurrentItem());
    
            toggleSelected(newSelector);

            Set<T> selectedValues = new HashSet<T>();
            for (Selector<T> selector : selected) {
                selectedValues.add(selector.getDefinedValue());
            }

            callback.setSelected(selectedValues);
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
        if (initialValues.contains(selector.getDefinedValue())) {
            toggleSelected(selector);
        }
    }

    public ArrayList<Selector<T>> getSelectors() {
        return selectors;
    }

    public Selector<T> getByItem(ItemStack item) {
        for (Selector<T> selector : selectors) {
            if (selector.equals(item)) {
                return selector;
            }
        }
        return null;
    }

    public void toggleSelected(Selector<T> selector) {
        if (selected.contains(selector)) {
            selected.remove(selector);
            selector.setSelected(false);
        }
        else {
            selected.add(selector);
            selector.setSelected(true);
        }
    }
}
