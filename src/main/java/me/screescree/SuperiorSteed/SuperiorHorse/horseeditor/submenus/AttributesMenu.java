package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import org.bukkit.Material;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;

public class AttributesMenu extends SubMenu {
    public AttributesMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        super(gui, horseInfo);

        setSubmenuItem(HorseEditor.customItem(Material.CHAIN, "&cAttributes", true));

        StaticPane pane = new StaticPane(0, 0, 7, 3, Priority.NORMAL);
        setPane(pane);

        pane.addItem(
            new AmountPicker(
                Material.SUGAR,
                Utils.colorize("&b&lSpeed"),
                "b/s",
                horseInfo.getSpeed() * 43.17,
                gui,
                new AmountPickerSettings(4.86, 14.57, 0.01, 1.0, 2),
                speed -> {
                    horseInfo.setSpeed(speed / 43.17);
            }).getGuiItem(),
            0,
            1
        );

        pane.addItem(
            new AmountPicker(
                Material.RABBIT_FOOT,
                Utils.colorize("&a&lJump Strength"),
                "blocks",
                ((horseInfo.getJumpStrength() * 59.0) - 17.0) / 8.0,
                gui,
                new AmountPickerSettings(0.825, 5.25, 0.01, 0.5, 2),
                jumpStrength -> {
                    horseInfo.setJumpStrength(((jumpStrength * 8.0) + 17.0) / 59.0);
            }).getGuiItem(),
            3,
            1
        );

        pane.addItem(
            new AmountPicker(
                Material.GLISTERING_MELON_SLICE,
                Utils.colorize("&c&lMax Health"),
                "hp",
                horseInfo.getMaxHealth(),
                gui,
                new AmountPickerSettings(15, 30, 0.5, 5, 1),
                maxHealth -> {
                    horseInfo.setMaxHealth(maxHealth);
            }).getGuiItem(),
            6,
            1
        );
    }
}
