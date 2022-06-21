package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.traitselector;

import java.util.HashSet;

import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public interface TraitCallback {
    void setSelected(HashSet<Trait> selected);
}
