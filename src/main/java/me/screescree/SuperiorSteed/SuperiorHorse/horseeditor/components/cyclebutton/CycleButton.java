package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.cyclebutton;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;

public class CycleButton<T> {
    private Gui gui;
    private boolean isVisible;
    private ArrayList<CycleButtonItem<T>> items = new ArrayList<>();
    private int selectedIndex = 0;
    private StaticPane pane;
    private T initialValue;

    public CycleButton(Gui gui, int x, int y, T initialValue, SetOptionCallback<T> callback) {
        this.gui = gui;
        this.initialValue = initialValue;
        pane = new StaticPane(x, y, 1, 1);

        pane.setOnClick(event -> {
            // check if item clicked is in the list
            boolean found = false;
            for (CycleButtonItem<T> item : items) {
                if (item.getGuiItem().getItem().equals(event.getCurrentItem())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return;
            }

            cycle();
            callback.setOption(items.get(selectedIndex).getDefinedValue());
            gui.update();

            HumanEntity human = event.getWhoClicked();
            if (human instanceof Player) {
                Player player = (Player) human;
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        });

        isVisible = true;
    }

    public void add(CycleButtonItem<T> cycleItem) {
        items.add(cycleItem);
        
        if (cycleItem.getDefinedValue() == initialValue) {
            selectedIndex = items.size() - 1;

            if (isVisible) {
                pane.addItem(cycleItem.getGuiItem(), 0, 0);
            }
        }
    }

    public StaticPane getPane() {
        return pane;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;

        if (!visible && pane.getItems().size() > 0) {
            pane.removeItem(0, 0);
        }
        else if (visible && pane.getItems().size() == 0 && items.size() > 0) {
            pane.addItem(items.get(selectedIndex).getGuiItem(), 0, 0);
        }

        gui.update();
    }

    public void cycle() {
        pane.removeItem(0, 0);
        selectedIndex = (selectedIndex + 1) % items.size();
        pane.addItem(items.get(selectedIndex).getGuiItem(), 0, 0);
    }
}
