package me.screescree.SuperiorSteed.superiorhorse;

import org.bukkit.Location;

import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;

public class BirthInfo {
    private Location location;
    private SuperiorHorseInfo horseInfo;

    public BirthInfo(Location location, SuperiorHorseInfo horseInfo) {
        this.location = location;
        this.horseInfo = horseInfo;
    }

    public Location getLocation() {
        return location;
    }

    public SuperiorHorseInfo getHorseInfo() {
        return horseInfo;
    }
}
