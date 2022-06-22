package com.nthByte.Utilities.Wand;

import com.nthByte.API;
import com.nthByte.Config;
import com.nthByte.Exception.NotPlotOwnerException;
import com.nthByte.Exception.NotSamePlotException;
import com.nthByte.Exception.WandNotSetException;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Wand implements Listener {
    private static HashMap<String, Region> selections = new HashMap<String, Region>();
    private static HashMap<String, Long> clicked = new HashMap<>();

    public static void setLoc1(Player p, Location loc) throws NotPlotOwnerException, NotSamePlotException {
        String pName = p.getName();
        Plot plot = API.existPlot(loc);
        if (plot == null || !plot.getOwner().equals(p.getUniqueId()) || !plot.getOwners().contains(p.getUniqueId()) || !Config.hasAccess(p, loc)) throw new NotPlotOwnerException();
        if (selections.containsKey(pName)) {
            selections.get(pName).setLoc1(loc);
            Region region = selections.get(pName);
            if (region.getLoc2() == null) return;
            Plot plot2 = API.existPlot(region.getLoc2());
            if (plot2 != null && !plot.getId().equals(plot2.getId())) {
                selections.get(pName).setLoc2(null);
                throw new NotSamePlotException();
            }
        } else {
            selections.put(pName, new Region(loc, null));
        }
    }

    public static void setLoc2(Player p, Location loc) throws NotPlotOwnerException, NotSamePlotException {
        String pName = p.getName();
        Plot plot = API.existPlot(loc);
        if (plot == null || !plot.getOwner().equals(p.getUniqueId()) || !plot.getOwners().contains(p.getUniqueId()) || !Config.hasAccess(p, loc)) throw new NotPlotOwnerException();
        if (selections.containsKey(pName)) {
            selections.get(pName).setLoc2(loc);

            Region region = selections.get(pName);
            if (region.getLoc1() == null) return;
            Plot plot1 = API.existPlot(region.getLoc1());
            if (plot1 != null && !plot.getId().equals(plot1.getId())) {
                selections.get(pName).setLoc1(null);
                throw new NotSamePlotException();
            }
        } else {
            selections.put(pName, new Region(null, loc));
        }
    }

    public static Location getLoc1(Player p) throws WandNotSetException {
        String pName = p.getName();
        if (selections.containsKey(pName)) {
            Location loc = selections.get(pName).getLoc1();
            if (loc == null) throw new WandNotSetException(1);
            return loc;
        }
        throw new WandNotSetException(1);
    }

    public static Location getLoc2(Player p) throws WandNotSetException {
        String pName = p.getName();
        if (selections.containsKey(pName)) {
            Location loc = selections.get(pName).getLoc2();
            if (loc == null) throw new WandNotSetException(2);
            return loc;
        }
        throw new WandNotSetException(2);
    }

    public static Region getRegion(Player p) throws WandNotSetException {
        Location loc2 = getLoc2(p);
        Location loc1 = getLoc1(p);
        return new Region(loc1, loc2);
    }

    public static boolean isCooled(Player p) {
        String name = p.getName();
        if (clicked.containsKey(p.getName())) {
            if (System.currentTimeMillis() >= clicked.get(p.getName())) {
                clicked.put(name, System.currentTimeMillis() + 100);
                return false;
            } else return true;
        } else {
            clicked.put(name, System.currentTimeMillis() + 100);
            return false;
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (b == null) return;
        Location loc = e.getClickedBlock().getLocation();
        if (loc == null) return;
        ItemStack hand = p.getInventory().getItemInMainHand(),
        wand = Config.getWandItem();
        if (hand.hasItemMeta() && hand.getItemMeta().getDisplayName().equals(hand.getItemMeta().getDisplayName()) && hand.getType().equals(wand.getType())) {
            if (hand.getItemMeta().hasLore() && !hand.getItemMeta().getLore().equals(hand.getItemMeta().getLore())) return;
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                try {
                    setLoc1(p, loc);
                    if (isCooled(p)) return;
                    Config.sendMessage(p, Config.getWandSetLoc(1, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), Config.getWandSetLocHover(1), "/tp " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ(), false);
                } catch (NotPlotOwnerException ex) {
                    if (isCooled(p)) return;
                    Config.sendMessage(p, Config.getWandNotOwner());
                } catch (NotSamePlotException ex) {
                    if (isCooled(p)) return;
                    Bukkit.broadcastMessage("h2");
                    Config.sendMessage(p, Config.getWandSetLoc(1, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                    Config.sendMessage(p, Config.getWandNotSame(2));
                }
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
                try {
                    setLoc2(p, e.getClickedBlock().getLocation());
                    if (isCooled(p)) return;
                    Config.sendMessage(p, Config.getWandSetLoc(2, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                } catch (NotPlotOwnerException ex) {
                    if (isCooled(p)) return;
                    Config.sendMessage(p, Config.getWandNotOwner());
                } catch (NotSamePlotException ex) {
                    if (isCooled(p)) return;
                    Config.sendMessage(p, Config.getWandSetLoc(2, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                    Config.sendMessage(p, Config.getWandNotSame(1));
                }
            }
        }
    }
}
