package com.nthByte.Utilities.Listing;

import com.nthByte.API;
import com.nthByte.Config;
import com.nthByte.Exception.RegionCrossException;
import com.nthByte.Exception.WandNotSetException;
import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.Wand.Region;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class Listing {
    public static ArrayList<Listing> listings = new ArrayList<>();
    public int id;
    public String oName;
    public Plot plot;
    public long duration;
    public Region region;
    public int price;
    public Material icon;
    public Location home = null;

    public Listing(int id, String oName, Plot plot, long duration, Region region, int price, Material icon, Location home) {
        this.id = id;
        this.oName = oName;
        this.plot = plot;
        this.duration = duration;
        this.region = region;
        this.price = price;
        this.icon = icon;
        this.home = home;
    }

    public static Listing createNewListing(String oName, Plot plot, long duration, Region region, int price) throws RegionCrossException {
        if (checkCross(region)) throw new RegionCrossException();

        int id = API.generateRandom();
        Listing listing  = new Listing(id, oName, plot, duration, region, price, Config.getMarketIcon(), null);
        Config.addListing(id, oName, plot.getId().getX(), plot.getId().getY(), duration, region.getLoc1(), region.getLoc2(), price);

        listings.add(listing);
        return listing;
    }

    public static boolean checkCross(Region region) {
        ArrayList<Region> regions = Config.getAllRegions();
        ArrayList<Location> nLocs = API.getAllCorners(region); // Locations of the new region
        for (Region rg : regions) {
            ArrayList<Location> oLocs = API.getAllCorners(rg); // Locations of the old (already set) region
            for (Location loc : nLocs) {
                if (rg.contains(loc)) return true;
            }
            for (Location loc : oLocs) {
                if (region.contains(loc)) return true;
            }
        }
        return false;
    }

    public static Listing getById(int id) {
        for (Listing listing : listings) {
            if (listing.id == id) return listing;
        }
        return null;
    }

    public static boolean removeListing(int id) {
        for (Listing listing : listings) {
            if (listing.id == id) {
                listings.remove(listing);
                Config.removeListing(id);
                return true;
            }
        }
        return false;
    }

    public void updateIcon(Material icon) {
        this.icon = icon;
        Config.updateListsIcon(id, icon);
    }

    public static void loadListings() {
        listings = Config.getFileListings();
    }

}
