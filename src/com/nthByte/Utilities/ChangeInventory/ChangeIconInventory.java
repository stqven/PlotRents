package com.nthByte.Utilities.ChangeInventory;

import com.nthByte.Config;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChangeIconInventory {
    private Inventory inv = Bukkit.createInventory(null, 27, "Change Icon");
    private int id;
    private boolean isRental;
    private int page;

    public ChangeIconInventory(int id, int page, boolean isRental) {
        this.id = id;
        this.isRental = isRental;
        this.page = page;
        {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(" ");
            item.setItemMeta(mitem);
            for (int i = 0; i < 27; i++) {
                if (i == 13) continue;
                inv.setItem(i, item);
                if (i == 0) {
                    ItemMeta mf = inv.getItem(0).getItemMeta();
                    PersistentDataContainer pc = mf.getPersistentDataContainer();
                    pc.set(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER, id);
                    pc.set(NamespacedKey.minecraft("is_rental"), PersistentDataType.INTEGER, isRental? 1 : 0);
                    pc.set(NamespacedKey.minecraft("page"), PersistentDataType.INTEGER, page);
                    inv.getItem(0).setItemMeta(mf);
                }
            }
        }
        Material mat;
        if (isRental) {
            Rental rental = Rental.getById(id);
            if (rental == null) mat = Config.getRentalIcon();
            mat = rental.icon;
        } else {
            Listing listing = Listing.getById(id);
            if (listing == null) mat = Config.getMarketIcon();
            mat = listing.icon;
        }
        ItemStack item = new ItemStack(mat);
        inv.setItem(13, item);
    }

    public void openInventory(Player p) {
        p.openInventory(inv);
    }
}
