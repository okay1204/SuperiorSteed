package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.togglebutton;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;

import me.screescree.SuperiorSteed.Utils;

public class ToggleButton {
    private boolean enabled;

    private Material enabledMaterial;
    private Material disabledMaterial;

    private String enabledName;
    private String disabledName;

    private Gui gui;
    private ToggleCallback callback;

    private ItemStack item;
    private GuiItem guiItem;

    public ToggleButton(boolean enabled, Material enabledMaterial, String enabledName, Material disabledMaterial, String disabledName, Gui gui, ToggleCallback callback) {
        this.enabled = enabled;

        this.enabledMaterial = enabledMaterial;
        this.disabledMaterial = disabledMaterial;

        this.enabledName = enabledName;
        this.disabledName = disabledName;

        this.gui = gui;
        this.callback = callback;

        // placeholder material to initialize the item
        item = new ItemStack(Material.STRUCTURE_VOID);

        setItemStack(enabled);
        guiItem = new GuiItem(item, event -> {
            this.enabled = !this.enabled;
            setItemStack(this.enabled);
            callback.onToggle(this.enabled);
            gui.update();

            HumanEntity human = event.getWhoClicked();
            if (human instanceof Player) {
                Player player = (Player) human;
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        });
    }

    private void setItemStack(boolean enabled) {
        Material material = enabled ? enabledMaterial : disabledMaterial;
        String name = enabled ? enabledName : disabledName;
        String lore = Utils.colorize("&7&oClick to switch to " + (enabled ? disabledName : enabledName));

        item.setType(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        item.setItemMeta(meta); 
    }

    public GuiItem getGuiItem() {
        return guiItem;
    }
}
