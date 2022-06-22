package com.nthByte.Utilities.GrowingGUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GrowingGUIListener implements Listener {

    @EventHandler
    public void onRefresh(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        if (inv == null) return;
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        int slot = e.getSlot();
        if (e.getView().getTitle().startsWith("Market #") || e.getView().getTitle().startsWith("My Rents #")) {
            e.setCancelled(true);
            if (slot <= 8 || slot%9 == 0 || (slot + 1)%9 == 0 || (slot >= inv.getSize() - 9 && slot < inv.getSize())) {
                e.setCancelled(true);
                if (slot >= inv.getSize() - 9 && slot < inv.getSize()) {
                    if ((slot + 1)%9 == 3) { // Previous

                    } else if ((slot + 1)%9 == 5) { // Emerald

                    } else if ((slot + 1)%9 == 7) { // Next

                    }
                }
            }
        }
    }
}
