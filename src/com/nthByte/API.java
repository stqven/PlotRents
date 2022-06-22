package com.nthByte;

import com.nthByte.Utilities.Wand.Region;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class API {

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt(upper - lower + 1) + lower;
    }

    public static Plot existPlot(Location loc) {
        com.plotsquared.core.location.Location ploc = com.plotsquared.core.location.Location.at(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        for (PlotArea pa : PlotRentsPlugin.ps.getPlotAreas(loc.getWorld().getName())) {
            Plot plot = pa.getPlot(ploc);
            if (plot != null) {
                return plot;
            }
        }
        return null;
    }

    public static ArrayList<Location> getAllCorners(Region region) {
        Location realLoc1 = region.getLoc1();
        Location realLoc2 = region.getLoc2();
        World world = realLoc1.getWorld();
        Location loc1 = new Location(world, realLoc1.getX(), realLoc1.getY(), realLoc1.getZ());
        Location loc2 = new Location(world, realLoc2.getX(), realLoc1.getY(), realLoc1.getZ());
        Location loc3 = new Location(world, realLoc1.getX(), realLoc1.getY(), realLoc2.getZ());
        Location loc4 = new Location(world, realLoc2.getX(), realLoc1.getY(), realLoc2.getZ());

        Location loc5 = new Location(world, realLoc1.getX(), realLoc2.getY(), realLoc1.getZ());
        Location loc6 = new Location(world, realLoc2.getX(), realLoc2.getY(), realLoc1.getZ());
        Location loc7 = new Location(world, realLoc1.getX(), realLoc2.getY(), realLoc2.getZ());
        Location loc8 = new Location(world, realLoc2.getX(), realLoc2.getY(), realLoc2.getZ());
        return new ArrayList<Location>(Arrays.asList(loc1, loc2, loc3, loc4, loc5, loc6, loc7, loc8));
    }

    public static int generateRandom() {
        int random = getRandom(10000, 99999);
        while (Config.cfgListings.getConfigurationSection("").getKeys(false).contains(random) || Config.cfgRents.getConfigurationSection("").getKeys(false).contains(random)) {
            random = getRandom(10000, 99999);
        }
        return random;
    }

    private static int countItem(Player p, ItemStack citem) {
        int total = 0;
        Inventory inv = p.getInventory();
        for (int i = 0; i < 41; i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
            ItemMeta mitem = item.getItemMeta();
            ItemMeta mcitem = citem.getItemMeta();
            if (mitem == null) continue;
            if (mcitem == null) continue;
            if (item.getType() == citem.getType() && mitem.getDisplayName().equals(mcitem.getDisplayName())) {
                if (mitem.hasLore() && mcitem.hasLore()) {
                    if (mitem.getLore().equals(mcitem.getLore())) {
                        total += item.getAmount();
                    }
                } else {
                    total += item.getAmount();
                }
            }
        }
        return total;
    }

    public static boolean countRemoveItem(Player p, ItemStack citem, int price) {
        if (countItem(p, citem) < price) return false;
        Inventory inv = p.getInventory();
        for (int i = 0; i < 41; i++) {
            if (price == 0) return true;
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
            ItemMeta mitem = item.getItemMeta();
            ItemMeta mcitem = citem.getItemMeta();
            if (mitem == null) continue;
            if (mcitem == null) continue;
            if (item.getType() == citem.getType() && mitem.getDisplayName().equals(mcitem.getDisplayName())) {
                if (mitem.hasLore() && mcitem.hasLore()) {
                    if (mitem.getLore().equals(mcitem.getLore())) {
                        int amount = item.getAmount();
                        if (amount >= price) {
                            p.getInventory().getItem(i).setAmount(amount - price);
                            p.updateInventory();
                        } else {
                            p.getInventory().getItem(i).setAmount(0);
                        }
                        price -= Math.min(price, amount);
                    }
                } else {
                    int amount = item.getAmount();
                    if (amount >= price) {
                        p.getInventory().getItem(i).setAmount(amount - price);
                        p.updateInventory();
                    } else {
                        p.getInventory().getItem(i).setAmount(0);
                    }
                    price -= Math.min(price, amount);
                }
            }
        }
        return true;
    }

}
