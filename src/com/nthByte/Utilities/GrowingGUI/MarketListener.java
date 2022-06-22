package com.nthByte.Utilities.GrowingGUI;

import com.nthByte.API;
import com.nthByte.Config;
import com.nthByte.PlotRentsPlugin;
import com.nthByte.Utilities.ChangeInventory.ChangeIconInventory;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MarketListener implements Listener {
    @EventHandler
    public void onMarketClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        int slot = e.getSlot();
        if (slot >= inv.getSize()) return;
        String title = e.getView().getTitle();
        if (title.startsWith("Market #")) {
            int page = Integer.parseInt(title.replaceAll("Market #", ""));
            e.setCancelled(true);
            int rsize = Listing.listings.size();
            int invSize = rsize/28 + ((rsize%28 > 0? 1 : 0));
            if (slot >= inv.getSize() - 9) {
                e.setCancelled(true);
                int slt = (slot + 1)%9;
                if ((slt == 3 || slt == 5 || slt == 7) && rsize == 0) {
                    Config.sendMessage(p, Config.getListingNotFound());
                    p.closeInventory();
                    return;
                }
                if (slt == 3) { // Previous
                    if (page - 1 >= 1) {
                        (new MarketInventory(p, page - 2)).openInventory(p);
                    } else {
                        (new MarketInventory(p, invSize - 1)).openInventory(p);
                    }
                } else if (slt == 5) { // Refresh
                    if (page <= invSize) {
                        (new MarketInventory(p, page - 1)).openInventory(p);
                    } else {
                        (new MarketInventory(p, invSize - 1)).openInventory(p);
                    }
                } else if (slt == 7) { // Next
                    if ((page + 1) <= invSize) {
                        (new MarketInventory(p, page)).openInventory(p);
                    } else {
                        (new MarketInventory(p, invSize - 1)).openInventory(p);
                    }
                }
            } else if (slot <= 8 || slot%9 == 0 || (slot + 1)%9 == 0) {
                e.setCancelled(true);
            } else {
                ClickType clk = e.getClick();
                PersistentDataContainer pc = item.getItemMeta().getPersistentDataContainer();
                int id = pc.get(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER);
                Listing listing = Listing.getById(id);
                if (listing == null) {
                    Config.sendMessage(p, Config.getItemAlreadyTaken());
                    return;
                }
                if (listing.oName.equalsIgnoreCase(p.getName())) {
                    if (clk == ClickType.LEFT || clk == ClickType.RIGHT) p.closeInventory();
                    if (clk == ClickType.LEFT) {
                        (new ChangeIconInventory(id, page, false)).openInventory(p);
                    } else if (clk == ClickType.RIGHT) {
                        if (listing.home == null) {
                            Config.sendMessage(p, Config.getHomeNotSet(id));
                            return;
                        }
                        p.teleport(listing.home);
                        Config.sendMessage(p, Config.getHomeTeleportSuccess(id));
                    } else if (clk == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                        if (Listing.removeListing(id)) {
                            Config.sendMessage(p, Config.getSuccessfullyRemovedItem());
                        } else {
                            Config.sendMessage(p, Config.getItemAlreadyTaken());
                        }
                    }
                } else {
                    if (clk == ClickType.LEFT) {
                        int price = listing.price;
                        if (API.countRemoveItem(p, Config.getCurrencyItem(), price)) {
                            Rental.createNewRental(id, p.getName(), listing.plot, System.currentTimeMillis()/1000 + listing.duration, listing.region, listing.icon, listing.home);
                            Listing.removeListing(id);
                            Config.sendMessage(p, Config.getRentalSuccess(price));
                            Player op = Bukkit.getPlayer(listing.oName);
                            if (op == null) {
                                Config.addMoney(listing.oName, price);
                            } else {
                                ItemStack citem = Config.getCurrencyItem();
                                citem.setAmount(price);
                                op.getInventory().addItem(citem);
                            }
                        } else {
                            Config.sendMessage(p, Config.getNoMoney());
                        }
                    } else if (clk == ClickType.RIGHT) {
                        if (listing.home == null) {
                            Config.sendMessage(p, Config.getHomeNotSet(id));
                            return;
                        }
                        p.teleport(listing.home);
                        Config.sendMessage(p, Config.getHomeTeleportSuccess(id));
                    }
                    if (clk == ClickType.LEFT || clk == ClickType.RIGHT) p.closeInventory();
                }
            }
        }
    }
}
