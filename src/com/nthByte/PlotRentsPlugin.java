/*
Change plot world ("plots") need to be added into config file (used in listings config)
check if values leave empty is good
player on join and leave loads
is opped build in any plot
plotsquared permissions
*/
package com.nthByte;
import com.nthByte.Commands.RentsCommand;
import com.nthByte.Exception.WandNotSetException;
import com.nthByte.Utilities.ChangeInventory.ChangeIconInventoryListener;
import com.nthByte.Utilities.GrowingGUI.*;
import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.Wand.Wand;
import com.plotsquared.core.PlotAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlotRentsPlugin extends JavaPlugin implements Listener {

    public static PlotRentsPlugin instance;
    public static PlotAPI ps;

    public void onEnable() {
        saveResource("config.yml", false);
        saveResource("items.yml", false);
        saveResource("lang.yml", false);
        ps = new PlotAPI();
        (new Console("The plugin has been enabled")).sendInfo();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new Wand(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MyRentsListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MarketListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GrowingGUIListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChangeIconInventoryListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlotListener(), this);
        getCommand("rents").setExecutor(new RentsCommand());
        getCommand("rents").setTabCompleter(new RentsCommand());
        Config.createMainFolder();
        Config.createConfigFile();
        Config.createRentalsFile();
        Config.createListsFile();
        Config.createLangFile();
        Config.createItemsFile();
        Config.loadDefaultValues();
        Listing.loadListings();
        MyRentsInventory.runScheduler();
        for (Player all : Bukkit.getOnlinePlayers()) {
            Rental.loadRentals(all);
        }
    }

    public void onLoad() {
        instance = this;
    }

    public void onDisable() {
        (new Console("The plugin has been disabled")).sendInfo();
    }

    @EventHandler
    public void onChatClear(PlayerCommandPreprocessEvent e) throws WandNotSetException {
        Player p = e.getPlayer();
        if (e.getMessage().equalsIgnoreCase("/cc")) {
            e.setCancelled(true);
            for (int i = 0; i < 100; i++) {
                e.getPlayer().sendMessage(" ");
            }
        } else if (e.getMessage().equalsIgnoreCase("/test")) {
           p.sendMessage("" + API.countRemoveItem(p, Config.getCurrencyItem(), 3));
        }
    }
}