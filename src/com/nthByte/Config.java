package com.nthByte;

import com.nthByte.Utilities.Listing.Listing;
import com.nthByte.Utilities.Pair;
import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.TimeConverter;
import com.nthByte.Utilities.Wand.Region;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.PlotId;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Config {

    public static YamlConfiguration cfgRents;
    public static YamlConfiguration cfgListings;
    public static YamlConfiguration cfgConfig;
    private static YamlConfiguration cfgLang;
    private static YamlConfiguration cfgItems;

    private static HashMap<String, String> langDefaultValues = new HashMap<>();
    private static HashMap<String, ArrayList<String>> langDefaultValuesLists = new HashMap<>();
    private static HashMap<String, String> itemsDefaultValues = new HashMap<>();
    private static HashMap<String, ArrayList<String>> itemsDefaultValuesLists = new HashMap<>();
    private static HashMap<String, String> configDefaultValues = new HashMap<>();

    public static void sendMessage(Player p, String msg) {
        if (msg == null || msg.equalsIgnoreCase("")) return;
        p.sendMessage(msg);
    }

    public static void sendMessage(Player p, String msg, String hover, String cmd, boolean suggest) {
        if (msg == null || msg.equalsIgnoreCase("")) return;
        TextComponent comp = new TextComponent(msg);
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        comp.setClickEvent(new ClickEvent(suggest? ClickEvent.Action.SUGGEST_COMMAND : ClickEvent.Action.RUN_COMMAND, cmd));
        p.spigot().sendMessage(comp);
    }

    public static String replaceText(String str, Pair<String, String>... replaces) {
        str = str.replaceAll("&", "§");
        for (Pair<String, String> pair : replaces) {
            str = str.replaceAll(pair.getKey(), pair.getValue());
        }
        return str;
    }

    private static boolean getBoolean(String path, FileConfiguration cfg) {
        return (cfg.contains(path)? cfg.getBoolean(path) : false);
    }

    private static String getText(String path, FileConfiguration cfg) {
        return (cfg.contains(path)? cfg.getString(path).replaceAll("&", "§").replaceAll("%prefix", getPrefix()) : null);
    }

    private static String getText(String path, FileConfiguration cfg, Pair<String, String>... replaces) {
        if (cfg.contains(path)) return replaceText(getText(path, cfg), replaces);
        else return null;
    }

    public static ArrayList<String> getList(String path, FileConfiguration cfg) {
        ArrayList<String> arr = new ArrayList<String>();
        for (String str : cfg.getStringList(path)) {
            arr.add(replaceText(str, new Pair<String, String>("&", "§")));
        }
        return arr;
    }

    public static ArrayList<String> getList(String path, FileConfiguration cfg, Pair<String, String>... replaces) {
        ArrayList<String> arr = new ArrayList<String>();
        for (String str : cfg.getStringList(path)) {
            arr.add(replaceText(str, replaces));
        }
        return arr;
    }


    // File Props

    public static void createMainFolder() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder().getPath());
        if (!file.exists()) file.mkdir();
    }

    public static void createConfigFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                (new Console("config.yml has been created")).sendInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            (new Console("config.yml has been loaded")).sendInfo();
        }
        cfgConfig = YamlConfiguration.loadConfiguration(file);
    }

    public static void createRentalsFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "rentals.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                (new Console("rentals.yml has been created")).sendInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            (new Console("rentals.yml has been loaded")).sendInfo();
        }
        cfgRents = YamlConfiguration.loadConfiguration(file);
    }

    public static void createListsFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "lists.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                (new Console("lists.yml has been created")).sendInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            (new Console("lists.yml has been loaded")).sendInfo();
        }
        cfgListings = YamlConfiguration.loadConfiguration(file);
    }

    public static void createLangFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "lang.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                (new Console("lang.yml has been created")).sendInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            (new Console("lang.yml has been loaded")).sendInfo();
        }
        cfgLang = YamlConfiguration.loadConfiguration(file);
    }

    public static void createItemsFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "items.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                (new Console("items.yml has been created")).sendInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            (new Console("items.yml has been loaded")).sendInfo();
        }
        cfgItems = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveConfigFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "config.yml");
        try {
            cfgConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRentalsFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "rentals.yml");
        try {
            cfgRents.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveListingsFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "lists.yml");
        try {
            cfgListings.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLangFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "lang.yml");
        try {
            cfgLang.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveItemsFile() {
        File file = new File(PlotRentsPlugin.instance.getDataFolder(), "items.yml");
        try {
            cfgItems.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDefaultValues() {
        // Lang File
        langDefaultValues.put("Messages.Prefix", "&8| &ePlotRents &8|&7");

        langDefaultValues.put("Messages.wand.loc1.msg", "%prefix First Location is set to (&e%x&7, &e%y&7 &e%z&7)");
        langDefaultValues.put("Messages.wand.loc1.hover", "&eCLICK TO TELEPORT");
        langDefaultValues.put("Messages.wand.loc2.msg", "%prefix Second Location is set to (&e%x&7, &e%y&7 &e%z&7)");
        langDefaultValues.put("Messages.wand.loc2.hover", "&eCLICK TO TELEPORT");
        langDefaultValues.put("Messages.wand.not_set.1", "%prefix &cThe first point is not set yet!");
        langDefaultValues.put("Messages.wand.not_set.2", "%prefix &cThe second point is not set yet!");
        langDefaultValues.put("Messages.wand.not_same.1", "%prefix &cThe first point has been removed because it's located in another plot");
        langDefaultValues.put("Messages.wand.not_same.2", "%prefix &cThe second point has been removed because it's located in another plot");
        langDefaultValues.put("Messages.wand.not_owner", "%prefix &cThe selection should be inside your own area!");
        langDefaultValues.put("Messages.listing.success", "%prefix &aThe selected region is successfully listed for &e%duration &aand &e$%price (id: %id)");
        langDefaultValues.put("Messages.listing.cross", "%prefix &cThis region intersects with other regions");
        langDefaultValues.put("Messages.listing.not_found", "%prefix &cThere are not areas available for sale at the moment");
        langDefaultValues.put("Messages.listing.duration_error", "%prefix &cRenting duration must be at least %duration");
        langDefaultValues.put("Messages.rental.not_found", "%prefix &cYou don't have any areas yet, rent some with &e/rents market");
        langDefaultValues.put("Messages.rental.expired", "%prefix &cA rent with id &e%id &cis expired");
        langDefaultValues.put("Messages.update_icon_success", "%prefix &aIcon has been updated successfully");
        langDefaultValues.put("Messages.item_already_taken", "%prefix &cThis item is already taken");
        langDefaultValues.put("Messages.successfully_removed_item", "%prefix &aThis item has been successfully removed from the listing");
        langDefaultValues.put("Messages.no_money", "%prefix &cYou don't have enough money to buy this");
        langDefaultValues.put("Messages.home.set_success", "%prefix &aA new home has been set for &e%id");
        langDefaultValues.put("Messages.home.set_not_exist", "%prefix &eID: %id &cdoes not exist!");
        langDefaultValues.put("Messages.home.set_not_owner", "%prefix &cYou don't own the region with id &e%id");
        langDefaultValues.put("Messages.home.set_not_inside", "%prefix &cHome location should be located inside your region");
        langDefaultValues.put("Messages.home.not_set", "%prefix &cHome location is not set yet, you have been randomly teleported inside the region");
        langDefaultValues.put("Messages.home.teleport_success", "%prefix &aYou have been teleported to &e%id&a's home");
        langDefaultValues.put("Messages.no_access", "%prefix &cYou don't have access to this area!");
        langDefaultValues.put("Messages.rent_success", "%prefix &aYou have rented a region for &e%price");
        langDefaultValues.put("Messages.min_area", "%prefix &cYour area should have a minimum space of &e%xx%yx%z");
        langDefaultValues.put("Messages.no_perms", "%prefix &cYou don't have enough permissions to execute this command");
        langDefaultValues.put("Messages.currency_modified", "%prefix &aCurrency has been successfully modified");
        langDefaultValues.put("Messages.no_hand", "%prefix &cYou don't have an item in your main hand");
        langDefaultValues.put("Messages.commands.hover", "&eCLICK TO BIND");

        langDefaultValuesLists.put("Messages.commands.msg", new ArrayList<>(Arrays.asList(new String[]{"%prefix &b/Rents wand &8* &7Gives you a selection stick",
                "%prefix &b/Rents myRents &8* &7Your current rents",
                "%prefix &b/Rents market &8* &7The available regions for sale",
                "%prefix &b/Rents list [duration] [price] &8* &7Rent your region",
                "%prefix &b/Rents setHome [id] &8* &7Set a teleport point for id",
                "%prefix &b/Rents setCurrency &8* &7Change the currency item"})));


        langDefaultValuesLists.put("Inventory.market.others_lore", new ArrayList<>(Arrays.asList(new String[]{"&8» &fOwner: &e%owner",
                "&8» &fPlot: &e%plot",
                "&8» &fDuration: &e%duration",
                "&8» &fPrice: &e%price",
                "",
                "&eLEFT-CLICK &7to buy",
                "&eRIGHT-CLICK &7to teleport"})));


        langDefaultValues.put("Inventory.market.display_name", "&8* &e%id &8*");
        langDefaultValuesLists.put("Inventory.market.owner_lore", new ArrayList<>(Arrays.asList(new String[]{"&8» &fOwner: &eYOU",
                "&8» &fPlot: &e%plot",
                "&8» &fDuration: &e%duration",
                "&8» &fPrice: &e%price",
                "",
                "&eLEFT-CLICK &7to edit",
                "&eRIGHT-CLICK &7to teleport",
                "&eSHIFT-CLICK &7to remove"})));
        langDefaultValuesLists.put("Inventory.market.others_lore", new ArrayList<>(Arrays.asList(new String[]{"&8» &fOwner: &e%owner",
                "&8» &fPlot: &e%plot",
                "&8» &fDuration: &e%duration",
                "&8» &fPrice: &e%price",
                "",
                "&eLEFT-CLICK &7to buy",
                "&eRIGHT-CLICK &7to teleport"})));


        langDefaultValues.put("Inventory.my_rents.display_name", "&8* &e%id &8*");
        langDefaultValuesLists.put("Inventory.my_rents.lore", new ArrayList<>(Arrays.asList(new String[]{"&8» &fPlot: &e%plot",
                "&8» &fRemaining Time: &e%remainingTime",
                "",
                "&eLEFT-CLICK &7to edit icon",
                "&eRIGHT-CLICK &7to teleport"})));



        // Items File
        itemsDefaultValues.put("Refresh.display_name", "&aRefresh");
        itemsDefaultValues.put("Refresh.item_type", "EMERALD");
        itemsDefaultValuesLists.put("Refresh.lore", new ArrayList<>(Arrays.asList(new String[]{"&7Refresh the current page"})));
        itemsDefaultValues.put("NextPage.display_name", "&eNext Page »");
        itemsDefaultValues.put("NextPage.item_type", "BOOK");
        itemsDefaultValuesLists.put("NextPage.lore", new ArrayList<>(Arrays.asList(new String[]{"&7Go to the next page"})));
        itemsDefaultValues.put("PreviousPage.display_name", "&e« Previous Page");
        itemsDefaultValuesLists.put("PreviousPage.lore", new ArrayList<>(Arrays.asList(new String[]{"&7Go to the previous page"})));
        itemsDefaultValues.put("PreviousPage.item_type", "BOOK");

        itemsDefaultValues.put("Wand.display_name", "&eSelection Wand");
        itemsDefaultValuesLists.put("Wand.lore", new ArrayList<>());
        itemsDefaultValues.put("Wand.item_type", "STICK");



        // Config File
        configDefaultValues.put("default_rental_icon", "WARPED_NYLIUM");
        configDefaultValues.put("default_market_icon", "CRIMSON_NYLIUM");
        configDefaultValues.put("min_list_duration", "1h");
        Bukkit.broadcastMessage("hey");



        for (String path : langDefaultValues.keySet()) {
            if (!cfgLang.contains(path)) {
                cfgLang.set(path, langDefaultValues.get(path));
            }
        }

        for (String path : langDefaultValuesLists.keySet()) {
            if (!cfgLang.contains(path)) {
                cfgLang.set(path, langDefaultValuesLists.get(path));
            }
        }
        saveLangFile();


        for (String path : itemsDefaultValues.keySet()) {
            if (!cfgItems.contains(path)) {
                cfgItems.set(path, itemsDefaultValues.get(path));
            }
        }

        for (String path : itemsDefaultValuesLists.keySet()) {
            if (!cfgItems.contains(path)) {
                cfgItems.set(path, itemsDefaultValuesLists.get(path));
            }
        }
        saveItemsFile();


        for (String path : configDefaultValues.keySet()) {
            Bukkit.broadcastMessage("hey3: " + cfgConfig.contains(path) + ", path: "  + path);
            if (!cfgConfig.contains(path)) {
                Bukkit.broadcastMessage("hey2");
                cfgConfig.set(path, configDefaultValues.get(path));
            }
        }
        cfgConfig.set("min_area_blocks_x", 2);
        cfgConfig.set("min_area_blocks_y", 2);
        cfgConfig.set("min_area_blocks_z", 2);
        saveConfigFile();
    }

    // Config file

    public static Material getRentalIcon() {
        Material mat = Material.getMaterial(cfgConfig.contains("default_rental_icon")? cfgConfig.getString("default_rental_icon") : "GRASS_BLOCK");
        return mat != null? mat : Material.GRASS_BLOCK;
    }

    public static Material getMarketIcon() {
        Material mat = Material.getMaterial(cfgConfig.contains("default_market_icon")? cfgConfig.getString("default_market_icon") : "GRASS_BLOCK");
        return mat != null? mat : Material.GRASS_BLOCK;
    }

    public static long getMinRentDuration() {
        return TimeConverter.reverse(getText("min_list_duration", cfgConfig));
    }

    // Items file

    public static ItemStack getRefreshItem() {
        Material mat = Material.getMaterial(getText("Refresh.item_type", cfgItems));
        ItemStack item = new ItemStack(mat != null? mat : Material.EMERALD);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(getText("Refresh.display_name", cfgItems));
        mitem.setLore(getList("Refresh.lore", cfgItems));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getNextPageItem() {
        Material mat = Material.getMaterial(getText("NextPage.item_type", cfgItems));
        ItemStack item = new ItemStack(mat != null? mat : Material.EMERALD);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(getText("NextPage.display_name", cfgItems));
        mitem.setLore(getList("NextPage.lore", cfgItems));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getPreviousPageItem() {
        Material mat = Material.getMaterial(getText("PreviousPage.item_type", cfgItems));
        ItemStack item = new ItemStack(mat != null? mat : Material.EMERALD);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(getText("PreviousPage.display_name", cfgItems));
        mitem.setLore(getList("PreviousPage.lore", cfgItems));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getWandItem() {
        Material mat = Material.getMaterial(getText("Wand.item_type", cfgItems));
        ItemStack item = new ItemStack(mat != null? mat : Material.DIAMOND);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(getText("Wand.display_name", cfgItems));
        mitem.setLore(getList("Wand.lore", cfgItems));
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("key"), PersistentDataType.INTEGER, API.getRandom(1, 9999));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getCurrencyItem() {
        if (cfgItems.contains("Currency")) {
            return cfgItems.getItemStack("Currency");
        }
        return null;
    }

    public static void setCurrencyItem(ItemStack item) {
        cfgItems.set("Currency", item);
        saveItemsFile();
    }

    // Rental Config

    public static void addRental(int id, String tName, int plotX, int plotY, long expireTime, Location loc1, Location loc2, Material icon, Location home) {
        String header = Integer.toString(id);
        cfgRents.set(header + ".tenant_name", tName);
        cfgRents.set(header + ".expire_time", expireTime);
        cfgRents.set(header + ".plot.x", plotX);
        cfgRents.set(header + ".plot.y", plotY);
        cfgRents.set(header + ".loc1", loc1);
        cfgRents.set(header + ".loc2", loc2);
        cfgRents.set(header + ".home", home);
        if (icon.equals(Config.getMarketIcon())) {
            cfgRents.set(header + ".icon", Config.getRentalIcon());
        } else {
            cfgRents.set(header + ".icon", icon);
        }
        saveRentalsFile();
    }

    public static void updateRentalIcon(int id, Material icon) {
        cfgRents.set(id + ".icon", icon.toString());
        saveRentalsFile();
    }

    public static void updateListsIcon(int id, Material icon) {
        cfgListings.set(id + ".icon", icon.toString());
        saveListingsFile();
    }

    public static void removeRental(int id) {
        cfgRents.set(Integer.toString(id), null);
        saveRentalsFile();
    }

    public static ArrayList<Rental> getFileRentals(Player p) {
        ArrayList<Rental> rents = new ArrayList<>();
        String tName = p.getName();
        for (String id : cfgRents.getConfigurationSection("").getKeys(false)) {
            if (tName.equalsIgnoreCase(getText(id + ".tenant_name", cfgRents))) {

                int x = Integer.parseInt(getText(id + ".plot.x", cfgRents));
                int y = Integer.parseInt(getText(id + ".plot.y", cfgRents));

                Plot tempPlot = null;
                for (PlotArea pa : PlotRentsPlugin.ps.getPlotAreas(p.getWorld().getName())) {
                    tempPlot = pa.getPlot(PlotId.of(x, y));
                    if (tempPlot != null) {
                        break;
                    }
                }

                if (tempPlot != null) {
                    Location loc1 = cfgRents.getLocation(id + ".loc1");
                    Location loc2 = cfgRents.getLocation(id + ".loc2");
                    Material icon = Material.getMaterial(getText(id + ".icon", cfgRents));
                    Location home = cfgRents.getLocation(id + ".home");
                    if (icon == null) icon = Config.getRentalIcon();
                    rents.add(new Rental(Integer.parseInt(id), tName, tempPlot, cfgRents.getLong(id + ".expire_time"), new Region(loc1, loc2), icon, home));
                }
            }
        }
        return rents;
    }

    // Listing Config

    public static void addListing(int id, String oName, int plotX, int plotY, long duration, Location loc1, Location loc2, int price) {
        String header = Integer.toString(id);
        cfgListings.set(header + ".owner_name", oName);
        cfgListings.set(header + ".duration", duration);
        cfgListings.set(header + ".icon", getMarketIcon());
        cfgListings.set(header + ".price", price);
        cfgListings.set(header + ".plot.x", plotX);
        cfgListings.set(header + ".plot.y", plotY);
        cfgListings.set(header + ".loc1", loc1);
        cfgListings.set(header + ".loc2", loc2);
        saveListingsFile();
    }

    public static void removeListing(int id) {
        cfgListings.set(Integer.toString(id), null);
        saveListingsFile();
    }

    public static ArrayList<Listing> getFileListings() {
        ArrayList<Listing> listings = new ArrayList<>();
        for (String id : cfgListings.getConfigurationSection("").getKeys(false)) {
            String oName = getText(id + ".owner_name", cfgListings);
            int x = Integer.parseInt(getText(id + ".plot.x", cfgListings));
            int y = Integer.parseInt(getText(id + ".plot.y", cfgListings));
            Location loc1 = (Location) cfgListings.get(id + ".loc1");
            Location loc2 = (Location) cfgListings.get(id + ".loc2");
            int price = Integer.parseInt(getText(id + ".price", cfgListings));
            long duration = cfgListings.getLong(id + ".duration");
            Region region = new Region(loc1, loc2);

            Plot tempPlot = null;
            for (PlotArea pa : PlotRentsPlugin.ps.getPlotAreas("plots")) {
                tempPlot = pa.getPlot(PlotId.of(x, y));
                if (tempPlot != null) {
                    break;
                }
            }

            if (tempPlot != null) {
                Material icon = Material.getMaterial(getText(id + ".icon", cfgListings));
                Location home = cfgListings.getLocation(id + ".home");
                if (icon == null) icon = Config.getMarketIcon();
                listings.add(new Listing(Integer.parseInt(id), oName, tempPlot, duration, region, price, icon, home));
            }
        }
        return listings;
    }

    public static ArrayList<Region> getAllRegions() {
        ArrayList<Region> regions = new ArrayList<>();
        for (String key : cfgListings.getConfigurationSection("").getKeys(false)) {
            Location loc1 = (Location) cfgListings.get(key + ".loc1");
            Location loc2 = (Location) cfgListings.get(key + ".loc2");
            Region region = new Region(loc1, loc2);
            regions.add(region);
        }
        for (String key : cfgRents.getConfigurationSection("").getKeys(false)) {
            Location loc1 = (Location) cfgRents.get(key + ".loc1");
            Location loc2 = (Location) cfgRents.get(key + ".loc2");
            Region region = new Region(loc1, loc2);
            regions.add(region);
        }
        return regions;
    }

    // Lang Config

    public static String getPrefix() {
        return cfgLang.contains("Messages.Prefix")? cfgLang.getString("Messages.Prefix").replaceAll("&", "§") : "";
    }

    public static String getWandSetLoc(int num, int x, int y, int z) {
        return getText(("Messages.wand.loc" + num + ".msg"), cfgLang, new Pair<>("%x", Integer.toString(x)), new Pair<>("%y", Integer.toString(y)), new Pair<>("%z", Integer.toString(z)));
    }

    public static String getWandSetLocHover(int num) {
        return getText(("Messages.wand.loc" + num + ".hover"), cfgLang);
    }

    public static String getWandNotSet(int num) {
        return getText(("Messages.wand.not_set." + num), cfgLang);
    }

    public static String getWandNotSame(int num) {
        return getText(("Messages.wand.Not_same." + num), cfgLang);
    }

    public static String getWandNotOwner() {
        return getText(("Messages.wand.not_owner"), cfgLang);
    }

    public static String getListingSuccess(String duration, int price, int id) {
        return getText(("Messages.listing.success"), cfgLang, new Pair<>("%duration", duration), new Pair<>("%price", Integer.toString(price)), new Pair<>("%id", Integer.toString(id)));
    }

    public static String getListingCross() {
        return getText(("Messages.listing.cross"), cfgLang);
    }

    public static String getListingDurationError() {
        return getText(("Messages.listing.duration_error"), cfgLang, new Pair<>("%duration", TimeConverter.convert(Config.getMinRentDuration())));
    }

    public static String getMyListsName(int id) {
        return getText(("Inventory.market.display_name"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static ArrayList<String> getMyListsOwnerLore(Plot plot, String duration, int price) {
        return getList("Inventory.market.owner_lore", cfgLang, new Pair<>("%plot", Integer.toString(plot.getId().getX()) + ";" + plot.getId().getY()), new Pair<>("%duration", duration), new Pair<>("%price", Integer.toString(price)));
    }

    public static ArrayList<String> getMyListsOthersLore(String oName, Plot plot, String duration, int price) {
        return getList("Inventory.market.others_lore", cfgLang, new Pair<>("%owner", oName), new Pair<>("%plot", Integer.toString(plot.getId().getX()) + ";" + plot.getId().getY()), new Pair<>("%duration", duration), new Pair<>("%price", Integer.toString(price)));
    }

    public static String getMyRentsName(int id) {
        return getText(("Inventory.my_rents.display_name"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }


    public static String getSetHomeSuccess(int id) {
        return getText(("Messages.home.set_success"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getSetHomeNotExist(int id) {
        return getText(("Messages.home.set_not_exist"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getSetHomeNotOwner(int id) {
        return getText(("Messages.home.set_not_owner"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getSetHomeNotInside(int id) {
        return getText(("Messages.home.set_not_inside"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getHomeNotSet(int id) {
        return getText(("Messages.home.not_set"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getHomeTeleportSuccess(int id) {
        return getText(("Messages.home.teleport_success"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getRentalExpired(int id) {
        return getText(("Messages.rental.expired"), cfgLang, new Pair<>("%id", Integer.toString(id)));
    }

    public static String getRentalSuccess(int price) {
        return getText(("Messages.rent_success"), cfgLang, new Pair<>("%price", Integer.toString(price)));
    }

    public static ArrayList<String> getMyRentsLore(Plot plot, String remainingTime) {
        return getList("Inventory.my_rents.lore", cfgLang, new Pair<>("%plot", Integer.toString(plot.getId().getX()) + ";" + plot.getId().getY()), new Pair<>("%remainingTime", remainingTime));
    }

    public static ArrayList<String> getCommands() {
        return getList("Messages.commands.msg", cfgLang, new Pair<>("%prefix", getPrefix()));
    }

    public static String getCommandsHover() {
        return getText("Messages.commands.hover", cfgLang);
    }

    public static String getListingNotFound() {
        return getText("Messages.listing.not_found", cfgLang);
    }

    public static String getRentalNotFound() {
        return getText("Messages.rental.not_found", cfgLang);
    }

    public static String getUpdateIconSuccess() {
        return getText("Messages.update_icon_success", cfgLang);
    }

    public static String getItemAlreadyTaken() {
        return getText("Messages.item_already_taken", cfgLang);
    }

    public static String getSuccessfullyRemovedItem() {
        return getText("Messages.successfully_removed_item", cfgLang);
    }

    public static String getNoMoney() {
        return getText("Messages.no_money", cfgLang);
    }

    public static String getNoAccess() {
        return getText("Messages.no_access", cfgLang);
    }

    public static String getNoPerms() {
        return getText("Messages.no_perms", cfgLang);
    }

    public static String getCurrencyModified() {
        return getText("Messages.currency_modified", cfgLang);
    }

    public static String getNoHand() {
        return getText("Messages.no_hand", cfgLang);
    }

    public static String getNoMinArea() {
        return getText("Messages.min_area", cfgLang, new Pair<>("%x", Integer.toString(getMinAreaX())), new Pair<>("%y", Integer.toString(getMinAreaY())), new Pair<>("%z", Integer.toString(getMinAreaZ())));
    }

    public static int getMinAreaX() {
        return Integer.parseInt(getText("min_area_blocks_x", cfgConfig));
    }

    public static int getMinAreaY() {
        return Integer.parseInt(getText("min_area_blocks_y", cfgConfig));
    }

    public static int getMinAreaZ() {
        return Integer.parseInt(getText("min_area_blocks_z", cfgConfig));
    }

    // Additional

    public static boolean hasAccess(Player p, Location loc) {
        for (String id : Config.cfgRents.getConfigurationSection("").getKeys(false)) {
            String tName = Config.getText(id + ".tenant_name", cfgRents);
            Location loc1 = cfgRents.getLocation(id + ".loc1");
            Location loc2 = cfgRents.getLocation(id + ".loc2");
            Region region = new Region(loc1, loc2);
            if (region.contains(loc)) {
                if (tName.equalsIgnoreCase(p.getName())) return true;
                else return false;
            }
        }
        Plot ct = API.existPlot(loc);
        if (ct == null) return false;
        if (ct.getOwner() == p.getUniqueId() || ct.getOwners().contains(p.getUniqueId()) || (ct.getMembers().contains(p.getUniqueId()) && (Bukkit.getPlayer(ct.getOwner()) != null)) || ct.getTrusted().contains(p.getUniqueId())) return true;
        return false;
    }

    public static void addMoney(String pName, int amount) {
        List<String> data = cfgConfig.contains("money." + pName)? getList("money." + pName, cfgConfig) : new ArrayList<>();
        data.add(Integer.toString(amount));
        cfgConfig.set("money." + pName, data);
        saveConfigFile();
    }

    public static int getMoney(String pName) {
        int amount = 0;
        ArrayList<String> data = cfgConfig.contains("money." + pName)? (ArrayList<String>) cfgConfig.getStringList("money." + pName) : new ArrayList<>();
        for (String str : data) {
            amount += Integer.parseInt(str);
        }
        cfgConfig.set("money." + pName, null);
        saveConfigFile();
        return amount;
    }
}
