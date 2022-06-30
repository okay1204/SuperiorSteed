package me.screescree.SuperiorSteed.superiorhorse;

import java.util.HashSet;
import java.util.Set;

public class Births {
    private static Set<BirthInfo> births = new HashSet<BirthInfo>();

    public static Set<BirthInfo> get() {
        return births;
    }

    public static void add(BirthInfo horse) {
        births.add(horse);
    }
    
    public static void clear() {
        births.clear();
    }
}
