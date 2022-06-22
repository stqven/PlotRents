package com.nthByte;

import com.nthByte.Utilities.Rental.Rental;
import com.nthByte.Utilities.Wand.Wand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlotListener implements Listener {

    HashMap<String, Long> blocks = new HashMap<>();
    ArrayList<Material> mats = new ArrayList<>(Arrays.asList(new Material[]{Material.ANVIL,
            Material.BLACK_BED, Material.ORANGE_BED, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED, Material.LEGACY_BED, Material.LEGACY_BED_BLOCK
            , Material.BELL, Material.BLAST_FURNACE, Material.BREWING_STAND
            , Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.OAK_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON, Material.WARPED_BUTTON, Material.LEGACY_STONE_BUTTON, Material.LEGACY_WOOD_BUTTON
            , Material.CAMPFIRE, Material.CARTOGRAPHY_TABLE, Material.CAULDRON, Material.CHEST, Material.COMMAND_BLOCK, Material.COMPARATOR, Material.CRAFTING_TABLE
            , Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.CRIMSON_DOOR, Material.DARK_OAK_DOOR, Material.IRON_DOOR, Material.JUNGLE_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.WARPED_DOOR, Material.LEGACY_BIRCH_DOOR, Material.LEGACY_ACACIA_DOOR, Material.LEGACY_DARK_OAK_DOOR, Material.LEGACY_IRON_DOOR, Material.LEGACY_JUNGLE_DOOR, Material.LEGACY_SPRUCE_DOOR
            , Material.LEGACY_TRAP_DOOR, Material.LEGACY_WOOD_DOOR, Material.LEGACY_WOODEN_DOOR, Material.LEGACY_ACACIA_DOOR_ITEM, Material.LEGACY_BIRCH_DOOR_ITEM, Material.LEGACY_DARK_OAK_DOOR_ITEM, Material.LEGACY_IRON_DOOR_BLOCK, Material.LEGACY_JUNGLE_DOOR_ITEM, Material.LEGACY_SPRUCE_DOOR_ITEM, Material.ACACIA_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.IRON_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.WARPED_TRAPDOOR, Material.LEGACY_IRON_TRAPDOOR, Material.LEGACY_TRAP_DOOR
            , Material.ENCHANTING_TABLE, Material.END_PORTAL, Material.END_PORTAL_FRAME
            , Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.LEGACY_ACACIA_FENCE_GATE, Material.CRIMSON_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.LEGACY_BIRCH_FENCE_GATE, Material.LEGACY_ACACIA_FENCE_GATE, Material.LEGACY_DARK_OAK_FENCE_GATE, Material.LEGACY_FENCE_GATE, Material.LEGACY_JUNGLE_FENCE_GATE, Material.LEGACY_SPRUCE_FENCE_GATE
            , Material.FURNACE, Material.FURNACE_MINECART, Material.BLAST_FURNACE, Material.LEGACY_BURNING_FURNACE, Material.LEGACY_FURNACE, Material.GRINDSTONE, Material.ITEM_FRAME, Material.GLOW_ITEM_FRAME, Material.LEGACY_ITEM_FRAME
            , Material.JUKEBOX, Material.LECTERN, Material.LEVER, Material.LODESTONE, Material.LOOM, Material.NOTE_BLOCK, Material.LEGACY_NOTE_BLOCK, Material.PISTON
            , Material.ACACIA_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE
            , Material.PUMPKIN, Material.REDSTONE_ORE, Material.RESPAWN_ANCHOR, Material.SMITHING_TABLE, Material.SMOKER, Material.SPAWNER
            , Material.LEGACY_WOOD_STAIRS, Material.LEGACY_SPRUCE_WOOD_STAIRS, Material.LEGACY_SMOOTH_STAIRS, Material.LEGACY_SANDSTONE_STAIRS, Material.LEGACY_RED_SANDSTONE_STAIRS, Material.LEGACY_QUARTZ_STAIRS, Material.LEGACY_PURPUR_STAIRS, Material.LEGACY_NETHER_BRICK_STAIRS, Material.LEGACY_JUNGLE_WOOD_STAIRS, Material.LEGACY_DARK_OAK_STAIRS, Material.LEGACY_COBBLESTONE_STAIRS, Material.LEGACY_BRICK_STAIRS, Material.LEGACY_BIRCH_WOOD_STAIRS, Material.LEGACY_ACACIA_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS, Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, Material.WAXED_CUT_COPPER_STAIRS, Material.WARPED_STAIRS, Material.STONE_STAIRS, Material.STONE_BRICK_STAIRS, Material.SPRUCE_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS, Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.RED_NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.PURPUR_STAIRS, Material.PRISMARINE_STAIRS, Material.PRISMARINE_BRICK_STAIRS, Material.POLISHED_GRANITE_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS, Material.POLISHED_BLACKSTONE_STAIRS, Material.POLISHED_BLACKSTONE_BRICK_STAIRS, Material.POLISHED_ANDESITE_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS, Material.OAK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS, Material.JUNGLE_STAIRS, Material.GRANITE_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS, Material.END_STONE_BRICK_STAIRS, Material.DIORITE_STAIRS, Material.DEEPSLATE_TILE_STAIRS, Material.DEEPSLATE_BRICK_STAIRS, Material.DARK_PRISMARINE_STAIRS, Material.DARK_OAK_STAIRS, Material.CUT_COPPER_STAIRS, Material.CRIMSON_STAIRS, Material.COBBLESTONE_STAIRS, Material.COBBLED_DEEPSLATE_STAIRS, Material.BRICK_STAIRS, Material.BLACKSTONE_STAIRS, Material.BIRCH_STAIRS, Material.ANDESITE_STAIRS, Material.ACACIA_STAIRS
            , Material.STICKY_PISTON, Material.STONECUTTER, Material.TRAPPED_CHEST}));

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (Rental.rents.containsKey(p.getName()))
            Rental.rents.remove(p.getName());
        Bukkit.getScheduler().runTaskLater(PlotRentsPlugin.instance, new Runnable() {
            @Override
            public void run() {
                Rental.loadRentals(p);
                int price = Config.getMoney(p.getName());
                ItemStack citem = Config.getCurrencyItem();
                citem.setAmount(price);
                p.getInventory().addItem(citem);
            }
        }, 1L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (Rental.rents.containsKey(p.getName()))
            Rental.rents.remove(p.getName());
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (block == null) return;
        Location loc = block.getLocation();
        if (loc == null) return;
        if (!Config.hasAccess(p, loc)) {
            e.setCancelled(true);
            if (Wand.isCooled(p)) return;
            p.sendMessage(Config.getNoAccess());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (block == null) return;
        Location loc = e.getBlock().getLocation();
        if (loc == null) return;
        if (!Config.hasAccess(p, loc)) {
            e.setCancelled(true);
            if (Wand.isCooled(p)) return;
            p.sendMessage(Config.getNoAccess());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block == null) return;
        Location loc = block.getLocation();
        if (loc == null) return;
        Material mat = block.getType();
        if (!Config.hasAccess(p, loc) && (mats.contains(mat)) && !p.isSneaking()) {
            e.setCancelled(true);
            if (Wand.isCooled(p)) return;
            p.sendMessage(Config.getNoAccess());
        }
    }
}
