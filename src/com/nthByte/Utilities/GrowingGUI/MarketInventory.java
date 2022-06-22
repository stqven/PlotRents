package com.nthByte.Utilities.GrowingGUI;

import com.nthByte.Config;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.TimeConverter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MarketInventory extends GrowingGUI {

    private Player p;
    private boolean own;

    public MarketInventory(Player p, int page) {
        super(("Market #" + (page + 1)), page);
        this.p = p;
        setupItems();
    }

    void setupItems() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Listing list : Listing.listings) {
            ItemStack item = new ItemStack(list.icon);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(Config.getMyListsName(list.id));
            if (list.oName.equals(p.getName())) {
                mitem.setLore(Config.getMyListsOwnerLore(list.plot, TimeConverter.convert(list.duration), list.price));
            } else {
                mitem.setLore(Config.getMyListsOthersLore(list.oName, list.plot, TimeConverter.convert(list.duration), list.price));
            }
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER, list.id);
            item.setItemMeta(mitem);
            items.add(item);
        }
        if (Listing.listings.size() == 0) {
            Config.sendMessage(p, Config.getListingNotFound());
            return;
        }
        super.items = items;
    }
}
