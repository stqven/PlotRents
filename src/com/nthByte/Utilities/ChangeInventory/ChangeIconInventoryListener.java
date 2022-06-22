package com.nthByte.Utilities.ChangeInventory;

import com.nthByte.Config;
import com.nthByte.PlotRentsPlugin;
import com.nthByte.Utilities.GrowingGUI.MarketInventory;
import com.nthByte.Utilities.GrowingGUI.MyRentsInventory;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChangeIconInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();
        int slot = e.getSlot();
        if (title.equals("Change Icon")) {
            if ((slot >= 0 && slot < 27)  && slot != 13 && e.getClickedInventory().getType() == InventoryType.CHEST) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        String title = e.getView().getTitle();
        if (title.equals("Change Icon")) {
            ItemStack item = inv.getItem(13);
            PersistentDataContainer pc = inv.getItem(0).getItemMeta().getPersistentDataContainer();
            int id = pc.get(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER);
            boolean isRental = pc.get(NamespacedKey.minecraft("is_rental"), PersistentDataType.INTEGER) == 1? true : false;
            int page = pc.get(NamespacedKey.minecraft("page"), PersistentDataType.INTEGER);
            if (isRental) {
                Rental rental = Rental.getById(id);
                if (item == null) {
                    if (rental.icon != Config.getRentalIcon()) Config.sendMessage(p, Config.getUpdateIconSuccess());
                    rental.updateIcon(Config.getRentalIcon());
                } else {
                    if (rental.icon != item.getType()) Config.sendMessage(p, Config.getUpdateIconSuccess());
                    rental.updateIcon(item.getType());
                }
                Bukkit.getScheduler().runTaskLater(PlotRentsPlugin.instance, new Runnable() {
                    @Override
                    public void run() {
                        (new MyRentsInventory(p, Math.min((Rental.getRentsSize()/28 + (Rental.getRentsSize()%28 > 0? 1 : 0)), page) - 1)).openInventory(p);
                    }
                }, 2L);
            } else {
                Listing listing = Listing.getById(id);
                if (item == null) {
                    if (listing.icon != Config.getMarketIcon()) Config.sendMessage(p, Config.getUpdateIconSuccess());
                    listing.updateIcon(Config.getMarketIcon());
                } else {
                    if (listing.icon != item.getType()) Config.sendMessage(p, Config.getUpdateIconSuccess());
                    listing.updateIcon(item.getType());
                }
                Bukkit.getScheduler().runTaskLater(PlotRentsPlugin.instance, new Runnable() {
                    @Override
                    public void run() {
                        (new MarketInventory(p, Math.min((Listing.listings.size()/28 + (Listing.listings.size()%28 > 0? 1 : 0)), page) - 1)).openInventory(p);
                    }
                }, 2L);
            }
        }
    }
}
