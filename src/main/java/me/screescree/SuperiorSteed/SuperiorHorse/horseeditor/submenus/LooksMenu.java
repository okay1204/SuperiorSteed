package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector.Selector;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.selector.SelectorHandler;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import net.md_5.bungee.api.ChatColor;

public class LooksMenu extends SubMenu {

    private ArrayList<Pane> panes = new ArrayList<>();

    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.BROWN_DYE, ChatColor.of("#c59b7c") + "Looks", false);
    }

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }
    
    public LooksMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        OutlinePane colorPane = new OutlinePane(0, 0, 7, 1, Priority.NORMAL);
        colorPane.align(OutlinePane.Alignment.CENTER);

        SelectorHandler<Color> colorSelector = new SelectorHandler<Color>(gui, colorPane, horseInfo.getColor(), (color) -> {
            horseInfo.setColor(color);
        });
        colorSelector.add(new Selector<Color>(Color.WHITE, Material.WHITE_CONCRETE, ChatColor.of("#e1d2cb") + "White"));
        colorSelector.add(new Selector<Color>(Color.CREAMY, Material.WHITE_TERRACOTTA, ChatColor.of("#c59b7c") + "Creamy"));
        colorSelector.add(new Selector<Color>(Color.CHESTNUT, Material.ORANGE_TERRACOTTA, ChatColor.of("#5d3224") + "Chestnut"));
        colorSelector.add(new Selector<Color>(Color.BROWN, Material.BROWN_TERRACOTTA, ChatColor.of("#3e2620") + "Brown"));
        colorSelector.add(new Selector<Color>(Color.BLACK, Material.BLACK_CONCRETE, ChatColor.of("#222228") + "Black"));
        colorSelector.add(new Selector<Color>(Color.GRAY, Material.GRAY_CONCRETE, ChatColor.of("#3a3a3a") + "Gray"));
        colorSelector.add(new Selector<Color>(Color.DARK_BROWN, Material.BLACK_TERRACOTTA, ChatColor.of("#351e17") + "Dark Brown"));

        OutlinePane stylePane = new OutlinePane(0, 2, 7, 1, Priority.NORMAL);
        stylePane.align(OutlinePane.Alignment.CENTER);

        SelectorHandler<Style> styleSelector = new SelectorHandler<Style>(gui, stylePane, horseInfo.getStyle(), (style) -> {
            horseInfo.setStyle(style);
        });
        styleSelector.add(new Selector<Style>(Style.NONE, Material.BARRIER, ChatColor.getByChar('c') + "None"));
        styleSelector.add(new Selector<Style>(Style.WHITE, Material.WHITE_CONCRETE, ChatColor.WHITE + "White Stockings/Blaze"));
        styleSelector.add(new Selector<Style>(Style.WHITEFIELD, Material.WHITE_CANDLE, ChatColor.WHITE + "White Patches"));
        styleSelector.add(new Selector<Style>(Style.WHITE_DOTS, Material.BONE_MEAL, ChatColor.WHITE + "White Dots"));
        styleSelector.add(new Selector<Style>(Style.BLACK_DOTS, Material.INK_SAC, ChatColor.of("#454545") + "Black Sooty"));

        panes.add(colorPane);
        panes.add(stylePane);
    }
}
