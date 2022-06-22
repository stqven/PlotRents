package com.nthByte.Commands;

import com.nthByte.API;
import com.nthByte.Config;
import com.nthByte.Exception.RegionCrossException;
import com.nthByte.Exception.WandNotSetException;
import com.nthByte.Utilities.GrowingGUI.MarketInventory;
import com.nthByte.Utilities.GrowingGUI.MyRentsInventory;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.TimeConverter;
import com.nthByte.Utilities.Wand.Region;
import com.nthByte.Utilities.Wand.Wand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RentsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rents")) {
            Player p = (Player) sender;
            ArrayList<String> messages = Config.getCommands();
            if (args.length == 0) {
                Config.sendMessage(p, "");
                for (int i = 0; i < messages.size(); i++) {
                    String cmd_bind;
                    if (i == 0) {
                        cmd_bind = "/Rents wand";
                    } else if (i == 1) {
                        cmd_bind = "/Rents myRents";
                    } else if (i == 2) {
                        cmd_bind = "/Rents market";
                    } else if (i == 3) {
                        cmd_bind = "/Rents list [duration] [price]";
                    } else if (i == 4) {
                        cmd_bind = "/Rents setHome [id]";
                    } else {
                        cmd_bind = "/Rents setCurrency";
                    }
                    Config.sendMessage(p, (i != 5)? messages.get(i) : ((p.isOp() || p.hasPermission("plotrents.admin"))? messages.get(i) : ""), Config.getCommandsHover(), cmd_bind, true);
                }
            } else {
                 if (args[0].equalsIgnoreCase("wand")) {
                     ItemStack hand = p.getInventory().getItemInMainHand();
                     if (hand == null || hand.getType() == Material.AIR) {
                         p.getInventory().setItemInMainHand(Config.getWandItem());
                     } else {
                         p.getInventory().addItem(Config.getWandItem());
                     }
                } else if (args[0].equalsIgnoreCase("setCurrency")) {
                     if (!p.isOp() && !p.hasPermission("plotrents.admin")) {
                         Config.sendMessage(p, Config.getNoPerms());
                         return false;
                     }
                     ItemStack hand = p.getInventory().getItemInMainHand();
                     if (hand == null || hand.getType() == Material.AIR) {
                         Config.sendMessage(p, Config.getNoHand());
                         return false;
                     }
                     Config.setCurrencyItem(hand);
                     Config.sendMessage(p, Config.getCurrencyModified());
                 } else if (args[0].equalsIgnoreCase("myRents")) {
                     if (Rental.rents.containsKey(p.getName()) && Rental.rents.get(p.getName()).size() != 0) (new MyRentsInventory(p, 0)).openInventory(p);
                     else Config.sendMessage(p, Config.getRentalNotFound());
                 } else if (args[0].equalsIgnoreCase("market")) {
                    if (Listing.listings.size() != 0) (new MarketInventory(p, 0)).openInventory(p);
                    else Config.sendMessage(p, Config.getListingNotFound());
                } else if (args[0].equalsIgnoreCase("list")) {
                     if (args.length > 2) {
                         long duration = TimeConverter.reverse(args[1]);
                         if (duration < Config.getMinRentDuration()) {
                             Config.sendMessage(p, Config.getListingDurationError());
                             return false;
                         }
                         int price = Integer.parseInt(args[2]);
                         try {
                             Location loc1 = Wand.getLoc1(p);
                             Location loc2 = Wand.getLoc2(p);
                             if ((Math.abs(loc1.getBlockX() - loc2.getBlockX()) + 1) < Config.getMinAreaX() || (Math.abs(loc1.getBlockY() - loc2.getBlockY()) + 1) < Config.getMinAreaY() || (Math.abs(loc1.getBlockZ() - loc2.getBlockZ()) + 1) < Config.getMinAreaZ()) {
                                 Config.sendMessage(p, Config.getNoMinArea());
                                 return false;
                             }
                             int id = Listing.createNewListing(p.getName(), API.existPlot(loc1), duration, new Region(loc1, loc2), price).id;
                             Config.sendMessage(p,Config.getListingSuccess(TimeConverter.convert(duration), price, id));
                         } catch (RegionCrossException e) {
                             Config.sendMessage(p, Config.getListingCross());
                         } catch (WandNotSetException e) {
                             Config.sendMessage(p, Config.getWandNotSet(e.getNum()));
                         }
                     } else if (args.length == 1) {
                         Config.sendMessage(p, "");
                         Config.sendMessage(p, messages.get(3), Config.getCommandsHover(), "/Rents list [duration] [price]", true);
                     } else {
                         Config.sendMessage(p, "");
                         Config.sendMessage(p, messages.get(3), Config.getCommandsHover(), "/Rents list " + args[1] + " [price]", true);
                     }
                 } else if (args[0].equalsIgnoreCase("setHome")) {
                     if (args.length >= 2) {
                         int id = Integer.parseInt(args[1]);
                         Location loc = p.getLocation();
                         Listing listing = Listing.getById(id);
                         Rental rental = Rental.getById(id);
                         if (listing != null) {
                             if (!listing.oName.equals(p.getName())) {
                                 Config.sendMessage(p, Config.getSetHomeNotOwner(id));
                                 return false;
                             }
                             if (!listing.region.contains(loc)) {
                                 Config.sendMessage(p, Config.getSetHomeNotInside(id));
                                 return false;
                             }
                             listing.home = loc;
                             Config.sendMessage(p, Config.getSetHomeSuccess(id));
                         } else if (rental != null) {
                             if (!rental.tName.equals(p.getName())) {
                                 Config.sendMessage(p, Config.getSetHomeNotOwner(id));
                                 return false;
                             }
                             if (!rental.region.contains(loc)) {
                                 Config.sendMessage(p, Config.getSetHomeNotInside(id));
                                 return false;
                             }
                             rental.home = loc;
                             Config.sendMessage(p, Config.getSetHomeSuccess(id));
                         } else {
                             Config.sendMessage(p, Config.getSetHomeNotExist(id));
                         }
                     } else {
                         Config.sendMessage(p, "");
                         Config.sendMessage(p, messages.get(4), Config.getCommandsHover(), "/Rents setHome [id]", true);
                     }
                 } else {
                     Config.sendMessage(p, "");
                     for (int i = 0; i < messages.size(); i++) {
                         String cmd_bind;
                         if (i == 0) {
                             cmd_bind = "/Rents wand";
                         } else if (i == 1) {
                             cmd_bind = "/Rents myRents";
                         } else if (i == 2) {
                             cmd_bind = "/Rents market";
                         } else if (i == 3) {
                             cmd_bind = "/Rents list [duration] [price]";
                         } else if (i == 4) {
                             cmd_bind = "/Rents setHome [id]";
                         } else {
                             cmd_bind = "/Rents setCurrency";
                         }
                         Config.sendMessage(p, (i != 5)? messages.get(i) : ((p.isOp() || p.hasPermission("plotrents.admin"))? messages.get(i) : ""), Config.getCommandsHover(), cmd_bind, true);
                     }
                 }
            }
        }
        return false;
    }
    public static ArrayList<String> getPlayerIDs(Player p) {
        ArrayList<String> ids = new ArrayList<>();
        if (Rental.rents.containsKey(p.getName())) {
            ArrayList<Rental> rents = Rental.rents.get(p.getName());
            for (Rental rent : rents) {
                ids.add(Integer.toString(rent.id));
            }
        }
        for (Listing listing : Listing.listings) {
            if (listing.oName.equalsIgnoreCase(p.getName())) {
                ids.add(Integer.toString(listing.id));
            }
        }
        return ids;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rents")) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (p.isOp() || p.hasPermission("plotrents.admin")) return Arrays.asList(new String[] {"wand", "myRents", "market", "list", "setHome", "setCurrency"});
                return Arrays.asList(new String[] {"wand", "myRents", "market", "list", "setHome"});
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("list")) {
                    return new ArrayList<>();
                } else if (args[0].equalsIgnoreCase("setHome")) {
                    return getPlayerIDs(p);
                }
            } else {
                return new ArrayList<>();
            }
        }
        return null;
    }
}
