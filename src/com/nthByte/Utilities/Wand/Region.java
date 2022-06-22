package com.nthByte.Utilities.Wand;

import org.bukkit.Location;

public class Region {
    private Location loc1, loc2;

    public Region(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public boolean contains(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        return (x >= Math.min(loc1.getBlockX(), loc2.getBlockX()) && x <= Math.max(loc1.getBlockX(), loc2.getBlockX())) &&
                (y >= Math.min(loc1.getBlockY(), loc2.getBlockY()) && y <= Math.max(loc1.getBlockY(), loc2.getBlockY())) &&
                (z >= Math.min(loc1.getBlockZ(), loc2.getBlockZ()) && z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
    }
}
