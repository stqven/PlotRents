package com.nthByte.Utilities.GrowingGUI;

import com.nthByte.Config;
import com.nthByte.PlotRentsPlugin;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MyRentsInventory extends GrowingGUI {


    private Player p;
    private boolean own;

    public MyRentsInventory(Player p, int page) {
        super(("My Rents #" + (page + 1)), page);
        super.items = items;
        this.p = p;
        this.own = own;
        setupItems();
    }

    void setupItems() {
        ArrayList<ItemStack> items = new ArrayList<>();
        ArrayList<Rental> rents = Rental.rents.get(p.getName());
        if (rents == null || rents.size() == 0) return;
        for (Rental rent : Rental.rents.get(p.getName())) {
            rent.removeIfEnded();
            ItemStack item = new ItemStack(rent.icon);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName(Config.getMyListsName(rent.id));
            mitem.setLore(Config.getMyRentsLore(rent.plot, TimeConverter.convert(rent.getExpireTime() - System.currentTimeMillis()/1000)));
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER, rent.id);
            item.setItemMeta(mitem);
            items.add(item);
        }
        if (Rental.rents.get(p.getName()).size() == 0) {
            Config.sendMessage(p, Config.getRentalNotFound());
            return;
        }
        super.items = items;
    }

    public static void runScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PlotRentsPlugin.instance, new Runnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Inventory inv = all.getOpenInventory().getTopInventory();
                    String title = all.getOpenInventory().getTitle();
                    int pages = Rental.rents.containsKey(all.getName())? (Rental.rents.get(all.getName()).size() - 1)/28 + (((Rental.rents.get(all.getName()).size() - 1)%28 > 0)? 1 : 0) : 0;
                    if (title.startsWith("My Rents #")) {
                        int page = Integer.parseInt(title.replaceAll("My Rents #", ""));
                        for (int i = 0; i < inv.getSize(); i++) {
                            if (i%9 == 0 || ((i + 1)%9 == 0) || i < 9 || i > inv.getSize() - 9) continue;
                            if (inv.getItem(i) == null) continue;
                            ItemMeta mitem = inv.getItem(i).getItemMeta();
                            if (mitem == null) continue;
                            PersistentDataContainer container = inv.getItem(i).getItemMeta().getPersistentDataContainer();
                            int id = (container.has(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER)? container.get(NamespacedKey.minecraft("id"), PersistentDataType.INTEGER) : -1);
                            if (id == -1) continue;
                            Rental rent = Rental.getById(id);
                            if (rent == null || rent.removeIfEnded()) {
                                (new MyRentsInventory(all, Math.min(page - 1, pages - 1))).openInventory(all);
                                Config.sendMessage(all, Config.getRentalExpired(id));
                                return;
                            }
                            mitem.setLore(Config.getMyRentsLore(rent.plot, TimeConverter.convert(rent.getExpireTime() - System.currentTimeMillis()/1000)));
                            inv.getItem(i).setItemMeta(mitem);
                        }
                    }
                }
                for (String name : Rental.rents.keySet()) {
                    for (Rental rental : Rental.rents.get(name)) {
                        int id = rental.id;
                        if (rental.removeIfEnded()) {
                            Player p = Bukkit.getPlayer(name);
                            int pages = Rental.rents.containsKey(p.getName())? (Rental.rents.get(p.getName()).size() - 1)/28 + (((Rental.rents.get(p.getName()).size() - 1) > 0)? 1 : 0) : 0;
                            if (p == null) continue;
                            Inventory inv = p.getOpenInventory().getTopInventory();
                            if (inv == null) continue;
                            String title = p.getOpenInventory().getTitle();
                            if (title.startsWith("My Rents #")) {
                                int page = Integer.parseInt(title.replaceAll("My Rents #", ""));
                                (new MyRentsInventory(p, Math.min(page - 1, pages - 1))).openInventory(p);
                                Config.sendMessage(p, Config.getRentalExpired(id));
                            }
                        }
                    }
                }
            }
        }, 0L, 20L);
    }
}
