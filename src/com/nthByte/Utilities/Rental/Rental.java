package com.nthByte.Utilities.Rental;

import com.nthByte.Config;
import com.nthByte.Utilities.Wand.Region;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Rental {
    public static HashMap<String, ArrayList<Rental>> rents = new HashMap<>();
    public int id;
    public String tName;
    public Plot plot;
    public long expireTime;
    public Region region;
    public Material icon;
    public Location home;

    public Rental(int id, String tName, Plot plot, long expireTime, Region region, Material icon, Location home) {
        this.id = id;
        this.tName = tName;
        this.plot = plot;
        this.expireTime = expireTime;
        this.region = region;
        this.icon = icon;
        this.home = home;
    }

    public String getTenantName() {
        return tName;
    }

    public Plot getPlot() {
        return plot;
    }

    public long getExpireTime()  {
        return expireTime;
    }

    public void updateIcon(Material icon) {
        this.icon = icon;
        Config.updateRentalIcon(id, icon);
    }

    public Region getRegion() {
        return region;
    }

    public static Rental createNewRental(int id, String tName, Plot plot, long expireTime, Region region, Material icon, Location home) {
        Rental rental = new Rental(id, tName, plot, expireTime, region, icon, home);
        Config.addRental(id, tName, plot.getId().getX(), plot.getId().getY(), expireTime, region.getLoc1(), region.getLoc2(), icon, home);
        if (rents.keySet().contains(tName)) rents.remove(tName);
        loadRentals(Bukkit.getPlayer(tName));
        return rental;
    }

    public static ArrayList<Rental> loadRentals(Player p) {
        ArrayList<Rental> rentals = Config.getFileRentals(p);
        rents.put(p.getName(), rentals);
        return rentals;
    }

    public boolean removeIfEnded() {
        if (System.currentTimeMillis()/1000 > expireTime) {
            Config.removeRental(id);
            rents.get(tName).remove(this);
            return true;
        }
        return false;
    }

    public static Rental getById(int id) {
        for (String name : rents.keySet()) {
            for (Rental rent : rents.get(name)) {
                if (rent.id == id) return rent;
            }
        }
        return null;
    }

    public static int getRentsSize() {
        int counter = 0;
        for (String str : rents.keySet()) {
            for (Rental rent : rents.get(str)) {
                counter++;
            }
        }
        return counter;
    }
}
