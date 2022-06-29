package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector;

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

import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class TraitSelectorHandler {
    private ArrayList<TraitSelector> selectors;
    private ArrayList<TraitSelector> selected = new ArrayList<>();
    private OutlinePane pane;
    private Set<Trait> initialValues;

    public TraitSelectorHandler(Gui gui, OutlinePane pane, Set<Trait> initialValues, TraitCallback callback) {
        selectors = new ArrayList<TraitSelector>();
        this.pane = pane;
        this.initialValues = initialValues;

        pane.setOnClick(event -> {
            TraitSelector newSelector = getByItem(event.getCurrentItem());

            if (newSelector.isDisabled()) {
                return;
            }
    
            toggleSelected(newSelector);

            HashSet<Trait> selectedTraits = new HashSet<Trait>();
            for (TraitSelector selector : selected) {
                selectedTraits.add(selector.getDefinedValue());
            }

            callback.setSelected(selectedTraits);
            gui.update();

            HumanEntity human = event.getWhoClicked();
            if (human instanceof Player) {
                Player player = (Player) human;
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        });
    }

    public void add(TraitSelector selector) {
        selectors.add(selector);

        pane.addItem(new GuiItem(selector.getItem()));
        if (initialValues.contains(selector.getDefinedValue())) {
            toggleSelected(selector);
        }

        // if a selector which is mutually exclusive with this one is already added and selected, disable this one
        if (Trait.INCOMPATIBLE_TRAITS.contains(selector.getDefinedValue())) {
            for (TraitSelector otherSelector : selectors) {
                if (Trait.INCOMPATIBLE_TRAITS.contains(otherSelector.getDefinedValue()) && otherSelector.isSelected() && otherSelector != selector) {
                    selector.setDisabled(true);
                    break;
                }
            }
        }
    }

    public ArrayList<TraitSelector> getSelectors() {
        return selectors;
    }

    public TraitSelector getByItem(ItemStack item) {
        for (TraitSelector selector : selectors) {
            if (selector.equals(item)) {
                return selector;
            }
        }
        return null;
    }

    public void toggleSelected(TraitSelector selector) {
        if (selected.contains(selector)) {
            selected.remove(selector);
            selector.setSelected(false);

            // Make all traits that are mutually exclusive with this one un-disabled
            if (Trait.INCOMPATIBLE_TRAITS.contains(selector.getDefinedValue())) {
                for (TraitSelector otherSelector : selectors) {
                    if (Trait.INCOMPATIBLE_TRAITS.contains(otherSelector.getDefinedValue()) && otherSelector != selector) {
                        otherSelector.setDisabled(false);
                    }
                }
            }
        }
        else {
            selected.add(selector);
            selector.setSelected(true);

            // Make all traits that are mutually exclusive with this one disabled
            if (Trait.INCOMPATIBLE_TRAITS.contains(selector.getDefinedValue())) {
                for (TraitSelector otherSelector : selectors) {
                    if (Trait.INCOMPATIBLE_TRAITS.contains(otherSelector.getDefinedValue()) && otherSelector != selector) {
                        otherSelector.setDisabled(true);
                    }
                }
            }
        }
    }
}
