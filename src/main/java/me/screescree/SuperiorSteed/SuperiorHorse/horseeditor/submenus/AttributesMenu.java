package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.utils.Format;

public class AttributesMenu extends SubMenu {
    private ArrayList<Pane> panes = new ArrayList<>();

    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.CHAIN, "&7Attributes", true);
    }

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }

    public AttributesMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        panes.add(
            new AmountPicker(
                Material.SUGAR,
                Format.colorize("&b&lSpeed"),
                "b/s",
                horseInfo.getSpeed() * 43.17,
                0,
                1,
                gui,
                new AmountPickerSettings(4.86, 14.57, 0.01, 1.0, 2),
                speed -> {
                    horseInfo.setSpeed(speed / 43.17);
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.RABBIT_FOOT,
                Format.colorize("&a&lJump Strength"),
                "blocks",
                ((horseInfo.getJumpStrength() * 59.0) - 17.0) / 8.0,
                3,
                1,
                gui,
                new AmountPickerSettings(0.825, 5.25, 0.01, 0.5, 2),
                jumpStrength -> {
                    horseInfo.setJumpStrength(((jumpStrength * 8.0) + 17.0) / 59.0);
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.GLISTERING_MELON_SLICE,
                Format.colorize("&c&lMax Health"),
                "hp",
                horseInfo.getMaxHealth(),
                6,
                1,
                gui,
                new AmountPickerSettings(15, 30, 0.5, 5, 1),
                maxHealth -> {
                    horseInfo.setMaxHealth(maxHealth);
                }
            ).getPane()
        );
    }
}
