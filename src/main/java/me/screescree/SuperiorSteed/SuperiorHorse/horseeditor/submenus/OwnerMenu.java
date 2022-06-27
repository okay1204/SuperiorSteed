package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.utils.Format;

public class OwnerMenu extends SubMenu {
    private ArrayList<Pane> panes = new ArrayList<>();
    private StaticPane mainPane;
    private Gui gui;
    private AnvilGui typeNameGui;
    // bamboo is just a placeholder material
    private ItemStack ownerItem = new ItemStack(Material.BAMBOO);
    private GuiItem removeOwner;

    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.PLAYER_HEAD, "&6Owner", true);
    }

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }

    public OwnerMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        this.gui = gui;
        // SECTION anvil gui
        typeNameGui = new AnvilGui("Enter Player Name");
        typeNameGui.setOnGlobalClick(event -> event.setCancelled(true));

        // First pane to allow player to type
        StaticPane renameAffectPane = new StaticPane(0, 0, 1, 1, Priority.LOWEST);
        renameAffectPane.addItem(HorseEditor.guiItem(Material.NAME_TAG, "&r", true), 0, 0);
        typeNameGui.getFirstItemComponent().addPane(renameAffectPane);

        // Submit Pane
        StaticPane submitSearchPane = new StaticPane(0, 0, 1, 1);
        submitSearchPane.addItem(HorseEditor.guiItem(Material.SPYGLASS, "&e&lSearch", true), 0, 0);
        submitSearchPane.setOnClick(event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();

            Player target = Bukkit.getPlayer(typeNameGui.getRenameText());
            if (target != null) {
                horseInfo.setOwnerUuid(target.getUniqueId());
                updateOwnerItem(target.getUniqueId());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
            else {
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
            }

            gui.show(player);
        });

        typeNameGui.getResultComponent().addPane(submitSearchPane);
        // !SECTION

        mainPane = new StaticPane(0, 1, 7, 1, Priority.NORMAL);
        
        removeOwner = new GuiItem(HorseEditor.customItem(Material.BARRIER, "&cRemove Owner", true), event -> {
            horseInfo.setOwnerUuid(null);
            updateOwnerItem(null);

            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
        });

        updateOwnerItem(horseInfo.getOwnerUuid());
        mainPane.addItem(new GuiItem(ownerItem), 3, 0);
        

        GuiItem showPromptItem = new GuiItem(HorseEditor.customItem(Material.NAME_TAG, "&6New Player", true), event -> {
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            player.closeInventory();
            typeNameGui.show(player);
        });
        mainPane.addItem(showPromptItem, 6, 0);
        
        panes.add(mainPane);
    }

    public void updateOwnerItem(UUID ownerUuid) {
        if (ownerUuid != null) {
            ownerItem.setType(Material.PLAYER_HEAD);

            SkullMeta ownerSkullMeta = (SkullMeta) ownerItem.getItemMeta();
            OfflinePlayer ownerPlayer = Bukkit.getOfflinePlayer(ownerUuid);
            ownerSkullMeta.setDisplayName(Format.colorize("&6Owned by: &r" + ownerPlayer.getName()));
            ownerSkullMeta.setOwningPlayer(ownerPlayer);
            
            ownerItem.setItemMeta(ownerSkullMeta);

            if (!mainPane.getItems().contains(removeOwner)) {
                mainPane.addItem(removeOwner, 0, 0);
            }
        }
        else {
            ownerItem.setType(Material.BARRIER);
            
            ItemMeta noOwnerMeta = ownerItem.getItemMeta();
            noOwnerMeta.setDisplayName(Format.colorize("&cNo Owner"));
            ownerItem.setItemMeta(noOwnerMeta);
            
            if (mainPane.getItems().contains(removeOwner)) {
                mainPane.removeItem(removeOwner);
            }
        }
        
        gui.update();
    }
}
