package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.timepicker;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;
import me.screescree.SuperiorSteed.utils.AgeTimeSplitter;
import me.screescree.SuperiorSteed.utils.Format;

public class TimePicker {
    private Set<Pane> panes = new HashSet<>();

    public Set<Pane> getPanes() {
        return panes;
    }

    public TimePicker(Gui gui, long ticks, TimePickerCallback callback) {
        AgeTimeSplitter ageTimeSplitter = new AgeTimeSplitter(ticks);

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
                    callback.setTicks(ageTimeSplitter.getTicks());
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
                    callback.setTicks(ageTimeSplitter.getTicks());
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
                    callback.setTicks(ageTimeSplitter.getTicks());
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
                    callback.setTicks(ageTimeSplitter.getTicks());
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
                    callback.setTicks(ageTimeSplitter.getTicks());
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
                    callback.setTicks(ageTimeSplitter.getTicks());
                }
            ).getPane()
        );
    }
}
