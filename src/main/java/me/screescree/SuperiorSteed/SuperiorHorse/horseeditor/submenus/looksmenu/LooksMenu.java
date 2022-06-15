package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus.looksmenu;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditorInfo;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import net.md_5.bungee.api.ChatColor;

public class LooksMenu extends SubMenu {

    private static final Mask MASK = new Mask(
        "1111111",
        "0000000",
        "0111110"
    );

    private SelectorHandler<Color> colorSelector;
    private SelectorHandler<Style> styleSelector;
    
    public LooksMenu(Gui gui, HorseEditorInfo horseInfo) {
        super(gui, horseInfo);
        setSubmenuItem(HorseEditor.customItem(Material.BROWN_DYE, ChatColor.of("#c59b7c") + "Looks", false));
        OutlinePane pane = new OutlinePane(0, 0, 7, 3, Priority.HIGH);
        pane.align(OutlinePane.Alignment.CENTER);
        pane.applyMask(MASK);

        colorSelector = new SelectorHandler<Color>();
        colorSelector.add(new Selector<Color>(Color.WHITE, Material.WHITE_CONCRETE, ChatColor.of("#e1d2cb") + "White"));
        colorSelector.add(new Selector<Color>(Color.CREAMY, Material.WHITE_TERRACOTTA, ChatColor.of("#c59b7c") + "Creamy"));
        colorSelector.add(new Selector<Color>(Color.CHESTNUT, Material.ORANGE_TERRACOTTA, ChatColor.of("#5d3224") + "Chestnut"));
        colorSelector.add(new Selector<Color>(Color.BROWN, Material.BROWN_TERRACOTTA, ChatColor.of("#3e2620") + "Brown"));
        colorSelector.add(new Selector<Color>(Color.BLACK, Material.BLACK_CONCRETE, ChatColor.of("#222228") + "Black"));
        colorSelector.add(new Selector<Color>(Color.GRAY, Material.GRAY_CONCRETE, ChatColor.of("#3a3a3a") + "Gray"));
        colorSelector.add(new Selector<Color>(Color.DARK_BROWN, Material.BLACK_TERRACOTTA, ChatColor.of("#351e17") + "Dark Brown"));

        styleSelector = new SelectorHandler<Style>(colorSelector.getSize());
        styleSelector.add(new Selector<Style>(Style.NONE, Material.BARRIER, ChatColor.getByChar('c') + "None"));
        styleSelector.add(new Selector<Style>(Style.WHITE, Material.WHITE_CONCRETE, ChatColor.WHITE + "White"));
        styleSelector.add(new Selector<Style>(Style.WHITEFIELD, Material.WHITE_CANDLE, ChatColor.WHITE + "White Field"));
        styleSelector.add(new Selector<Style>(Style.WHITE_DOTS, Material.BONE_MEAL, ChatColor.WHITE + "White Dots"));
        styleSelector.add(new Selector<Style>(Style.BLACK_DOTS, Material.INK_SAC, ChatColor.of("#454545") + "Black Dots"));

        // activeColor = ColorSelector.getByHorseColor(horseInfo.getColor());
        for (Selector<Color> color : colorSelector.getSelectors()) {
            pane.addItem(new GuiItem(color.getItem()));
            if (color.getDefinedValue() == horseInfo.getColor()) {
                colorSelector.setSelected(color);
            }
        }

        for (Selector<Style> style : styleSelector.getSelectors()) {
            pane.addItem(new GuiItem(style.getItem()));
            if (style.getDefinedValue() == horseInfo.getStyle()) {
                styleSelector.setSelected(style);
            }
        }

        pane.setOnClick(event -> {
            checkColor(event);
            checkStyle(event);
            gui.update();
        });

        setPane(pane);
    }

    private void playSelectSound(HumanEntity human) {
        if (human instanceof Player) {
            Player player = (Player) human;
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }

    private void checkColor(InventoryClickEvent event) {
        Selector<Color> newColor = colorSelector.getByItem(event.getCurrentItem());
        // If the item selected wasn't even a color (e.g. the background), do nothing
        if (newColor == null) {
            return;
        }

        colorSelector.setSelected(newColor);
        getHorseInfo().setColor(newColor.getDefinedValue());
        playSelectSound(event.getWhoClicked());
    }

    private void checkStyle(InventoryClickEvent event) {
        Selector<Style> newStyle = styleSelector.getByItem(event.getCurrentItem());
        // If the item selected wasn't even a color (e.g. the background), do nothing
        if (newStyle == null) {
            return;
        }

        styleSelector.setSelected(newStyle);
        getHorseInfo().setStyle(newStyle.getDefinedValue());
        playSelectSound(event.getWhoClicked());
    }
}
