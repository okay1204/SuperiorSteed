package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditorInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import net.md_5.bungee.api.ChatColor;

public class LooksMenu extends SubMenu {

    private static Mask MASK = new Mask(
        "1111111",
        "0000000",
        "0111110"
    );

    private static enum ColorSelector {
        WHITE(Horse.Color.WHITE, Material.WHITE_CONCRETE, ChatColor.of("#e1d2cb") + "White"),
        CREAMY(Horse.Color.CREAMY, Material.WHITE_TERRACOTTA, ChatColor.of("#c59b7c") + "Creamy"),
        CHESTNUT(Horse.Color.CHESTNUT, Material.ORANGE_TERRACOTTA, ChatColor.of("#5d3224") + "Chestnut"),
        BROWN(Horse.Color.BROWN, Material.BROWN_TERRACOTTA, ChatColor.of("#3e2620") + "Brown"),
        BLACK(Horse.Color.BLACK, Material.BLACK_CONCRETE, ChatColor.of("#222228") + "Black"),
        GRAY(Horse.Color.GRAY, Material.GRAY_CONCRETE, ChatColor.of("#3a3a3a") + "Gray"),
        DARK_BROWN(Horse.Color.DARK_BROWN, Material.BLACK_TERRACOTTA, ChatColor.of("#351e17") + "Dark Brown");

        private Horse.Color horseColor;
        private Material material;
        private String name;

        private ColorSelector(Horse.Color horseColor, Material material, String name) {
            this.horseColor = horseColor;
            this.material = material;
            this.name = name;
        }

        public Horse.Color getHorseColor() {
            return horseColor;
        }

        public Material getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public int getItemIndex() {
            return ordinal();
        }

        public static ColorSelector getByHorseColor(Horse.Color horseColor) {
            for (ColorSelector colorSelector : values()) {
                if (colorSelector.getHorseColor() == horseColor) {
                    return colorSelector;
                }
            }

            return null;
        }

        public static ColorSelector getByName(String name) {
            for (ColorSelector colorSelector : values()) {
                if (colorSelector.getName().toLowerCase().equals(name.toLowerCase())) {
                    return colorSelector;
                }
            }

            return null;
        }
    }

    private static enum StyleSelector {
        NONE(Horse.Style.NONE, Material.BARRIER, ChatColor.getByChar('c') + "None"),
        WHITE(Horse.Style.WHITE, Material.WHITE_CONCRETE, ChatColor.WHITE + "White"),
        WHITEFIELD(Horse.Style.WHITEFIELD, Material.WHITE_CANDLE, ChatColor.WHITE + "White Field"),
        WHITE_DOTS(Horse.Style.WHITE_DOTS, Material.BONE_MEAL, ChatColor.WHITE + "White Dots"),
        BLACK_DOTS(Horse.Style.BLACK_DOTS, Material.INK_SAC, ChatColor.of("#454545") + "Black Dots");

        private Horse.Style horseStyle;
        private Material material;
        private String name;

        private StyleSelector(Horse.Style horseStyle, Material material, String name) {
            this.horseStyle = horseStyle;
            this.material = material;
            this.name = name;
        }

        public Horse.Style getHorseStyle() {
            return horseStyle;
        }

        public Material getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public int getItemIndex() {
            return ordinal() + ColorSelector.values().length;
        }

        public static StyleSelector getByHorseStyle(Horse.Style horseStyle) {
            for (StyleSelector styleSelector : values()) {
                if (styleSelector.getHorseStyle() == horseStyle) {
                    return styleSelector;
                }
            }

            return null;
        }

        public static StyleSelector getByName(String name) {
            for (StyleSelector styleSelector : values()) {
                if (styleSelector.getName().toLowerCase().equals(name.toLowerCase())) {
                    return styleSelector;
                }
            }

            return null;
        }
    }

    private ColorSelector activeColor;
    private StyleSelector activeStyle;

    public LooksMenu(Gui gui, HorseEditorInfo horseInfo) {
        super(gui, horseInfo);
        setSubmenuItem(HorseEditor.customItem(Material.BROWN_DYE, ChatColor.of("#c59b7c") + "Looks", false));

        activeColor = ColorSelector.getByHorseColor(horseInfo.getColor());
        activeStyle = StyleSelector.getByHorseStyle(horseInfo.getStyle());

        OutlinePane pane = new OutlinePane(0, 0, 7, 3, Priority.HIGH);
        pane.align(OutlinePane.Alignment.CENTER);
        pane.applyMask(MASK);
        
        // Color Selectors
        for (ColorSelector colorSelector : ColorSelector.values()) {
            pane.addItem(HorseEditor.guiItem(colorSelector.getMaterial(), colorSelector.getName(), false, colorSelector.getHorseColor() == horseInfo.getColor()));
        }
        
        // Style Selectors
        for (StyleSelector styleSelector : StyleSelector.values()) {
            pane.addItem(HorseEditor.guiItem(styleSelector.getMaterial(), styleSelector.getName(), false, styleSelector.getHorseStyle() == horseInfo.getStyle()));
        }

        pane.setOnClick(event -> {
            checkColor(event);
            checkStyle(event);
            gui.update();
        });

        setPane(pane);
    }

    private void checkColor(InventoryClickEvent event) {
        ColorSelector newColor = ColorSelector.getByName(event.getCurrentItem().getItemMeta().getDisplayName());
        // If the item selected wasn't even a color (e.g. the background), do nothing
        if (newColor == null) {
            return;
        }

        OutlinePane pane = (OutlinePane) getPane();

        // remove enchant from previously selected color
        pane.removeItem(pane.getItems().get(activeColor.getItemIndex()));
        pane.insertItem(HorseEditor.guiItem(activeColor.getMaterial(), activeColor.getName(), false, false), activeColor.getItemIndex());
        // add enchant to newly selected color
        pane.removeItem(pane.getItems().get(newColor.getItemIndex()));
        pane.insertItem(HorseEditor.guiItem(newColor.getMaterial(), newColor.getName(), false, true), newColor.getItemIndex());
        // save new color
        activeColor = newColor;
        getHorseInfo().setColor(activeColor.getHorseColor());
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }

    private void checkStyle(InventoryClickEvent event) {
        StyleSelector newStyle = StyleSelector.getByName(event.getCurrentItem().getItemMeta().getDisplayName());
        // If the item selected wasn't even a style (e.g. the background), do nothing
        if (newStyle == null) {
            return;
        }

        OutlinePane pane = (OutlinePane) getPane();


        // remove enchant from previously selected color
        pane.removeItem(pane.getItems().get(activeStyle.getItemIndex()));
        pane.insertItem(HorseEditor.guiItem(activeStyle.getMaterial(), activeStyle.getName(), false, false), activeStyle.getItemIndex());
        // add enchant to newly selected color
        pane.removeItem(pane.getItems().get(newStyle.getItemIndex()));
        pane.insertItem(HorseEditor.guiItem(newStyle.getMaterial(), newStyle.getName(), false, true), newStyle.getItemIndex());
        // save new color
        activeStyle = newStyle;
        getHorseInfo().setStyle(activeStyle.getHorseStyle());
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }
}
