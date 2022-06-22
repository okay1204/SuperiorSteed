package me.screescree.SuperiorSteed.superiorhorse.horseeditor.submenus;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;

import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.SubMenu;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.cyclebutton.CycleButton;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.cyclebutton.CycleButtonItem;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector.TraitSelector;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector.TraitSelectorHandler;
import me.screescree.SuperiorSteed.superiorhorse.info.Seed;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class TraitsMenu extends SubMenu {
    private static final String SEED_STRING_PREFIX = ChatColor.RED + "Favorite Seed: " + ChatColor.WHITE + ChatColor.BOLD;
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
        CycleButton<Seed> seedCycle = new CycleButton<>(gui, 1, 1, horseInfo.getFavoriteSeed() != null ? horseInfo.getFavoriteSeed() : Seed.ALWAYS_LIKED, seed -> {
            horseInfo.setFavoriteSeed(seed);
        });
        seedCycle.setVisible(horseInfo.getTraits().contains(Trait.PICKY_EATER));

        seedCycle.add(new CycleButtonItem<Seed>(Seed.WHEAT_SEEDS, Seed.WHEAT_SEEDS.getMaterial(), SEED_STRING_PREFIX + "Wheat Seeds"));
        seedCycle.add(new CycleButtonItem<Seed>(Seed.PUMPKIN_SEEDS, Seed.PUMPKIN_SEEDS.getMaterial(), SEED_STRING_PREFIX + "Pumpkin Seeds"));
        seedCycle.add(new CycleButtonItem<Seed>(Seed.MELON_SEEDS, Seed.MELON_SEEDS.getMaterial(), SEED_STRING_PREFIX + "Melon Seeds"));
        seedCycle.add(new CycleButtonItem<Seed>(Seed.BEETROOT_SEEDS, Seed.BEETROOT_SEEDS.getMaterial(), SEED_STRING_PREFIX + "Beetroot Seeds"));
        seedCycle.add(new CycleButtonItem<Seed>(Seed.NETHER_WART, Seed.NETHER_WART.getMaterial(), SEED_STRING_PREFIX + "Nether Wart"));

        panes.add(seedCycle.getPane());


        OutlinePane traitsPane = new OutlinePane(0, 0, 7, 1, Priority.NORMAL);
        traitsPane.align(OutlinePane.Alignment.CENTER);

        TraitSelectorHandler traitSelector = new TraitSelectorHandler(gui, traitsPane, horseInfo.getTraits(), traits -> {
            horseInfo.setTraits(traits);

            if (traits.contains(Trait.PICKY_EATER)) {
                seedCycle.setVisible(true);
            }
            else {
                seedCycle.setVisible(false);
            }
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
