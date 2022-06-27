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
import me.screescree.SuperiorSteed.utils.AgeTimeSplitter;
import me.screescree.SuperiorSteed.utils.Format;
import net.md_5.bungee.api.ChatColor;

public class AgeMenu extends SubMenu {
    private ArrayList<Pane> panes = new ArrayList<>();

    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.EXPOSED_COPPER, ChatColor.of("#a67661") + "Age", false);
    }

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }

    public AgeMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        AgeTimeSplitter ageTimeSplitter = new AgeTimeSplitter(horseInfo.getAge());

        panes.add(
            new AmountPicker(
                Material.WHITE_GLAZED_TERRACOTTA,
                Format.colorize("&f&lYears"),
                null,
                ageTimeSplitter.getYears(),
                0,
                1,
                gui,
                new AmountPickerSettings(0, 1000000000, 1, 10, 0),
                years -> {
                    ageTimeSplitter.setYears((int) Math.round(years));
                    horseInfo.setAge(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.ORANGE_GLAZED_TERRACOTTA,
                Format.colorize("&f&lMonths"),
                null,
                ageTimeSplitter.getMonths(),
                1,
                1,
                gui,
                new AmountPickerSettings(0, 11, 1, 3, 0),
                months -> {
                    ageTimeSplitter.setMonths((int) Math.round(months));
                    horseInfo.setAge(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Format.colorize("&f&lDays"),
                null,
                ageTimeSplitter.getDays(),
                2,
                1,
                gui,
                new AmountPickerSettings(0, 29, 1, 5, 0),
                days -> {
                    ageTimeSplitter.setDays((int) Math.round(days));
                    horseInfo.setAge(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.LIME_GLAZED_TERRACOTTA,
                Format.colorize("&f&lHours"),
                null,
                ageTimeSplitter.getHours(),
                3,
                1,
                gui,
                new AmountPickerSettings(0, 23, 1, 4, 0),
                hours -> {
                    ageTimeSplitter.setHours((int) Math.round(hours));
                    horseInfo.setAge(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Format.colorize("&f&lMinutes"),
                null,
                ageTimeSplitter.getMinutes(),
                4,
                1,
                gui,
                new AmountPickerSettings(0, 59, 1, 10, 0),
                minutes -> {
                    ageTimeSplitter.setMinutes((int) Math.round(minutes));
                    horseInfo.setAge(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );

        panes.add(
            new AmountPicker(
                Material.BLACK_GLAZED_TERRACOTTA,
                Format.colorize("&f&lSeconds"),
                null,
                ageTimeSplitter.getSeconds(),
                5,
                1,
                gui,
                new AmountPickerSettings(0, 59, 1, 10, 0),
                seconds -> {
                    ageTimeSplitter.setSeconds((int) Math.round(seconds));
                    horseInfo.setAge(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );
    }
}
