package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector.Selector;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class TraitSelector extends Selector<Trait> {
    private Material originalMaterial;
    private boolean disabled;
    private boolean selected;

    public TraitSelector(Trait definedValue, Material material, String name) {
        super(definedValue, material, name);
        originalMaterial = material;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;

        if (disabled) {
            getItem().setType(Material.BARRIER);
        }
        else {
            getItem().setType(originalMaterial);
        }
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);

        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean equals(ItemStack item) {
        String itemName = item.getItemMeta().getDisplayName().toLowerCase();
        // remove selected prefix if it exists
        if (itemName.startsWith(SELECTED_PREFIX.toLowerCase())) {
            itemName = itemName.substring(SELECTED_PREFIX.length());
        }

        return (item.getType() == Material.BARRIER || getItem().getType() == item.getType()) && getName().toLowerCase().equals(itemName);
    }
}
