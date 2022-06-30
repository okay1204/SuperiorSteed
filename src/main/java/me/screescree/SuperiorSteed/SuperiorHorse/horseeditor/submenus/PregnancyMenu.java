package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPicker;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker.AmountPickerSettings;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.togglebutton.ToggleButton;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.visibilityfixed.VisibilityFixedPaneWrapper;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.utils.Format;

public class PregnancyMenu extends SubMenu {
    private ArrayList<Pane> panes = new ArrayList<>();
    private SuperiorHorseInfo horseInfo;
    private VisibilityFixedPaneWrapper femaleOnly;
    private AmountPicker pregnancyComplicationStat;
    private AmountPicker pregnancyTimerPicker;
    private VisibilityFixedPaneWrapper editHorseInfoButton;
    private SuperiorHorseInfo foalInfo;

    private ToggleButton pregnantToggle;

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }
    
    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.HORSE_SPAWN_EGG, "&ePregnancy", true);
    }

    public PregnancyMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        this.horseInfo = horseInfo;
        ItemStack femaleOnlyItem = new ItemStack(Material.BARRIER);
        ItemMeta femaleOnlyMeta = femaleOnlyItem.getItemMeta();
        femaleOnlyMeta.setDisplayName(Format.colorize("&cFemale only"));
        femaleOnlyMeta.setLore(List.of(
            Format.colorize("&7This menu is for female horses only."),
            Format.colorize("&7Change this horse to female in"),
            Format.colorize("&7the &bType &7submenu to get access to"),
            Format.colorize("&7this submenu.")
        ));
        femaleOnlyItem.setItemMeta(femaleOnlyMeta);

        femaleOnly = new VisibilityFixedPaneWrapper(3, 1, new GuiItem(femaleOnlyItem));
        panes.add(femaleOnly.getPane());

        boolean isPregnant = horseInfo.getPregnancyTimer() > 0;

        pregnancyComplicationStat = new AmountPicker(
            Material.BEETROOT,
            Format.colorize("&c&lPregnancy Complication"),
            null,
            isPregnant ? (int) (horseInfo.getPregnancyComplication() * 100) : 100,
            2,
            1,
            gui,
            new AmountPickerSettings(0, 100, 1, 10, 0), 
            pregnancyComplication -> {
                horseInfo.setPregnancyComplication(pregnancyComplication / 100);
            }
        );
        pregnancyComplicationStat.setVisible(isPregnant);
        panes.add(pregnancyComplicationStat.getPane());

        double pregnancyTimerInHours;
        if (horseInfo.getPregnancyTimer() > 0) {
            pregnancyTimerInHours = Math.ceil(horseInfo.getPregnancyTimer() / 72000.0);
        }
        else {
            pregnancyTimerInHours = 72;
        }

        pregnancyTimerPicker = new AmountPicker(
            Material.CHORUS_FLOWER,
            Format.colorize("&d&lPregnancy Time Left"),
            "hours",
            pregnancyTimerInHours,
            4,
            1,
            gui,
            new AmountPickerSettings(0, 72, 1, 10, 0),
            pregnancyTimer -> {
                horseInfo.setPregnancyTimer((int) Math.max(pregnancyTimer * 72000, 1));
            }
        );
        pregnancyTimerPicker.setVisible(isPregnant);
        panes.add(pregnancyTimerPicker.getPane());

        ItemStack editHorseInfoItem = new ItemStack(Material.PAPER);
        ItemMeta editHorseInfoMeta = femaleOnlyItem.getItemMeta();
        editHorseInfoMeta.setDisplayName(Format.colorize("&f&lEdit Foal"));
        editHorseInfoMeta.setLore(List.of(
            Format.colorize("&7Edit the foal that this horse"),
            Format.colorize("&7will give birth to.")
        ));
        editHorseInfoItem.setItemMeta(editHorseInfoMeta);

        if (horseInfo.getPregnantWith() != null) {
            foalInfo = horseInfo.getPregnantWith();
        }
        else {
            foalInfo = new SuperiorHorseInfo();
            foalInfo.setAge(0);
            foalInfo.setOwnerUuid(horseInfo.getOwnerUuid());
        }

        editHorseInfoButton = new VisibilityFixedPaneWrapper(6, 1, new GuiItem(editHorseInfoItem, event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();

            new HorseEditor(foalInfo, player, "Edit Foal", newFoalInfo -> {
                foalInfo = newFoalInfo;
                gui.show(player);
            });
        }));
        editHorseInfoButton.setVisible(isPregnant);
        panes.add(editHorseInfoButton.getPane());

        pregnantToggle = new ToggleButton(
            horseInfo.getPregnancyTimer() > 0,
            Material.LIME_CONCRETE,
            Format.colorize("&aPregnant"),
            Material.RED_CONCRETE,
            Format.colorize("&cNot pregnant"),
            gui,
            0,
            1,
            enabled -> {
                pregnancyComplicationStat.setVisible(enabled);
                pregnancyTimerPicker.setVisible(enabled);
                editHorseInfoButton.setVisible(enabled);

                if (!enabled) {
                    horseInfo.setPregnancyComplication(1.0);
                    horseInfo.setPregnancyTimer(0);
                    horseInfo.setPregnantWith(null);
                }
                else {
                    horseInfo.setPregnancyComplication(pregnancyComplicationStat.getAmount() / 100);
                    horseInfo.setPregnancyTimer((int) Math.max(pregnancyTimerPicker.getAmount() * 72000, 1));
                    horseInfo.setPregnantWith(foalInfo);
                }
            }
        );
        pregnantToggle.setVisible(!horseInfo.isMale());
        panes.add(pregnantToggle.getPane());

        verifyIsFemale();
    }

    @Override
    public void onShow() {
        verifyIsFemale();
    }

    public void verifyIsFemale() {
        if (horseInfo.isMale()) {
            femaleOnly.setVisible(true);
            pregnantToggle.setVisible(false);
            pregnancyComplicationStat.setVisible(false);
            pregnancyTimerPicker.setVisible(false);
            editHorseInfoButton.setVisible(false);
        }
        else {
            boolean isPregnant = horseInfo.getPregnancyTimer() > 0;
            
            femaleOnly.setVisible(false);
            pregnantToggle.setVisible(true);
            pregnancyComplicationStat.setVisible(isPregnant);
            pregnancyTimerPicker.setVisible(isPregnant);
            editHorseInfoButton.setVisible(isPregnant);
        }
    }
}
