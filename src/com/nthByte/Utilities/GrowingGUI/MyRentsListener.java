package com.nthByte.Utilities.GrowingGUI;

import com.nthByte.API;
import com.nthByte.Config;
import com.nthByte.Utilities.ChangeInventory.ChangeIconInventory;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import org.bukkit.Bukkit;
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

public class MyRentsListener implements Listener {
    @EventHandler
    public void onRentsClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        int slot = e.getSlot();
        if (slot >= inv.getSize()) return;
        String title = e.getView().getTitle();
        if (title.startsWith("My Rents #")) {
            int page = Integer.parseInt(title.replaceAll("My Rents #", ""));
            e.setCancelled(true);
            int rsize = Rental.rents.containsKey(p.getName())? Rental.rents.get(p.getName()).size() : 0;
            int invSize = rsize/28 + ((rsize%28 > 0? 1 : 0));
            if (slot >= inv.getSize() - 9) {
                e.setCancelled(true);
                int slt = (slot + 1)%9;
                if ((slt == 3 || slt == 5 || slt == 7) && rsize == 0) {
                    Config.sendMessage(p, Config.getRentalNotFound());
                    p.closeInventory();
                    return;
                }
                if (slt == 3) { // Previous
                    if (page - 1 >= 1) {
                        (new MyRentsInventory(p, page - 2)).openInventory(p);
                    } else {
                        (new MyRentsInventory(p, invSize - 1)).openInventory(p);
                    }
                } else if (slt == 5) { // Refresh
                    if (page <= invSize) {
                        (new MyRentsInventory(p, page - 1)).openInventory(p);
                    } else {
                        (new MyRentsInventory(p, invSize - 1)).openInventory(p);
                    }
                } else if (slt == 7) { // Next
                    if ((page + 1) <= invSize) {
                        (new MyRentsInventory(p, page)).openInventory(p);
                    } else {
                        (new MyRentsInventory(p, invSize - 1)).openInventory(p);
                    }
                }
            } else if (slot <= 8 || slot%9 == 0 || (slot + 1)%9 == 0) {
                e.setCancelled(true);
            } else { // inv items
                ClickType clk = e.getClick();
                if (clk == ClickType.LEFT || clk == ClickType.RIGHT) p.closeInventory();
                PersistentDataContainer pc = item.getItemMeta().getPersistentDataContainer();
                int id = pc.get(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER);
                Rental rental = Rental.getById(id);
                if (rental == null) {
                    Config.sendMessage(p, Config.getRentalExpired(id));
                    return;
                }
                if (rental.removeIfEnded()) {
                    Config.sendMessage(p, Config.getRentalExpired(id));
                    return;
                }
                if (clk == ClickType.LEFT) {
                    (new ChangeIconInventory(id, page, true)).openInventory(p);
                } else if (clk == ClickType.RIGHT) {
                    if (rental.home == null) {
                        Config.sendMessage(p, Config.getHomeNotSet(id));
                        return;
                    }
                    p.teleport(rental.home);
                    Config.sendMessage(p, Config.getHomeTeleportSuccess(id));
                }
            }
        }
    }
}
