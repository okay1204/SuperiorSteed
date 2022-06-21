package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector.TraitSelector;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector.TraitSelectorHandler;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class TraitsMenu extends SubMenu {
    private ArrayList<Pane> panes = new ArrayList<>();

    @Override
    public ArrayList<Pane> getPanes() {
        return panes;
    }
    
    @Override
    public ItemStack getSubmenuItem() {
        return HorseEditor.customItem(Material.TARGET, "&cTraits", true);
    }
    
    public TraitsMenu(Gui gui, SuperiorHorseInfo horseInfo) {
        OutlinePane traitsPane = new OutlinePane(0, 0, 7, 1, Priority.NORMAL);
        traitsPane.align(OutlinePane.Alignment.CENTER);

        TraitSelectorHandler traitSelector = new TraitSelectorHandler(gui, traitsPane, horseInfo.getTraits(), (traits) -> {
            horseInfo.setTraits(traits);
        });

        traitSelector.add(new TraitSelector(Trait.LONER, Material.BAMBOO, Trait.LONER.getFormalName()));
        traitSelector.add(new TraitSelector(Trait.PICKY_EATER, Material.BEETROOT_SEEDS, Trait.PICKY_EATER.getFormalName()));
        traitSelector.add(new TraitSelector(Trait.HARD_KEEPER, Material.DIAMOND_HOE, Trait.HARD_KEEPER.getFormalName()));
        traitSelector.add(new TraitSelector(Trait.BUSY_BEE, Material.BEE_NEST, Trait.BUSY_BEE.getFormalName()));
        traitSelector.add(new TraitSelector(Trait.ANGEL, Material.FEATHER, Trait.ANGEL.getFormalName()));
        traitSelector.add(new TraitSelector(Trait.FRIENDLY, Material.APPLE, Trait.FRIENDLY.getFormalName()));
        traitSelector.add(new TraitSelector(Trait.EXTROVERT, Material.HAY_BLOCK, Trait.EXTROVERT.getFormalName()));

        panes.add(traitsPane);
    }
}
