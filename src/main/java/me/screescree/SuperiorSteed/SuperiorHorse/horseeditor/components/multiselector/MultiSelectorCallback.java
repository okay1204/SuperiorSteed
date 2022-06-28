package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.multiselector;

import java.util.Set;

public interface MultiSelectorCallback<T> {
    void setSelected(Set<T> selected);
}
