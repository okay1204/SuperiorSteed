package me.screescree.SuperiorSteed.superiorhorse;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Stat {
    double value;
    PersistentDataContainer container;
    NamespacedKey key;

    public Stat(double value, PersistentDataContainer container, NamespacedKey key) {
        this.value = value;
        this.container = container;
        this.key = key;

        container.set(key, PersistentDataType.DOUBLE, value);
    }

    private void limitBounds() {
        if (value < 0) {
            value = 0;
        }
        else if (value > 1) {
            value = 1;
        }
    }

    public double get() {
        return value;
    }

    public void set(double value) {
        this.value = value;
        limitBounds();
        container.set(key, PersistentDataType.DOUBLE, value);
    }

    public void add(double value) {
        this.value += value;
        limitBounds();
        container.set(key, PersistentDataType.DOUBLE, value);
        System.out.println("Added " + value + " to " + key.toString());
        System.out.println(this.value);
    }
}
