package com.nthByte.Utilities.GrowingGUI;

import com.nthByte.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GrowingGUI {
    private Inventory inv;

    protected String name;
    protected int page;
    protected ArrayList<ItemStack> items = new ArrayList<>();

    public GrowingGUI(String name, int page) {
        this.name = name;
        this.page = page;
    }

    public void openInventory(Player p) {
        if (items.size() == 0) {
            p.closeInventory();
            return;
        }
        int invSize = Math.min((18 + (((items.size() - page*28)/7) + (((items.size() - page*28)%7 >= 1)? 1 : 0))*9), 54);
        inv = Bukkit.createInventory(null, invSize, name);
        {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(" ");
            item.setItemMeta(mitem);

            for (int i = 0; i < 9; i++) {
                inv.setItem(i, item);
            }
            for (int i = invSize - 1; i >= invSize - 9; i--) {
                inv.setItem(i, item);
            }
            for (int i = 0; i < invSize; i++) {
                if (i%9 == 0 || ((i + 1)%9 == 0)) {
                    inv.setItem(i, item);
                }
            }
        }
        for (int i = 0; i < 28; i++) {
            int index = (page*28) + i;
            if (index >= items.size()) break;
            inv.addItem(items.get(index));
        }

        {
            ItemStack item = new ItemStack(Material.EMERALD);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName("Refresh");
            item.setItemMeta(mitem);
            inv.setItem((invSize - 5), Config.getRefreshItem());
        }

        {
            ItemStack item = new ItemStack(Material.BOOK);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName("Next Page");
            item.setItemMeta(mitem);
            inv.setItem((invSize - 3), Config.getNextPageItem());
        }

        {
            ItemStack item = new ItemStack(Material.BOOK);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName("Previous Page");
            item.setItemMeta(mitem);
            inv.setItem((invSize - 7), Config.getPreviousPageItem());
        }

        p.openInventory(inv);
    }
}
