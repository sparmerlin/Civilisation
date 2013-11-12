package net.slipcor.civ.core;

import net.slipcor.civ.api.Permission;
import net.slipcor.civ.api.ICivilisation;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.impl.CLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The listener class
 * 
 * @author slipcor
 */
public class CivListener implements Listener {

    private final ICivilisation plugin;

    public CivListener(final ICivilisation plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onBreak(final BlockBreakEvent event) {
        for (IHouse house : plugin.getHouses()) {
            if (house.prevents(new CLocation(event.getBlock().getLocation()),
                    event.getPlayer(), Permission.BUILD)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Language.parse(Language.HOUSE_DISALLOW_BREAK));
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onBreak(final HangingBreakEvent event) {
        if (event instanceof HangingBreakByEntityEvent) {
            return;
        }

        final Block block = event.getEntity().getLocation().getBlock();
        for (IHouse house : plugin.getHouses()) {
            if (house.prevents(new CLocation(block.getLocation()),
                    null, Permission.BUILD)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onBreak(final HangingBreakByEntityEvent event) {
        final Block block = event.getEntity().getLocation().getBlock();
        if (!(event.getRemover() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getRemover();
        for (IHouse house : plugin.getHouses()) {
            if (house.prevents(new CLocation(block.getLocation()),
                    player, Permission.BUILD)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onPlace(final BlockPlaceEvent event) {
        for (IHouse house : plugin.getHouses()) {
            if (house.prevents(new CLocation(event.getBlock().getLocation()),
                    event.getPlayer(), Permission.BUILD)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Language.parse(Language.HOUSE_DISALLOW_PLACE));
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onBlockClick(final PlayerInteractEvent event) {
        if (event.hasBlock()) {
            return;
        }
        
        final Block block = event.getClickedBlock();
        
        for (IHouse house : plugin.getHouses()) {
            if (isChest(block.getType()) && house.prevents(new CLocation(block.getLocation()),
                    event.getPlayer(), Permission.CHEST)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Language.parse(Language.HOUSE_DISALLOW_CHEST));
            } else if (isDoor(block.getType()) && house.prevents(new CLocation(block.getLocation()),
                    event.getPlayer(), Permission.DOOR)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Language.parse(Language.HOUSE_DISALLOW_DOOR));
            } else if (isLever(block.getType()) && house.prevents(new CLocation(block.getLocation()),
                    event.getPlayer(), Permission.LEVER)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Language.parse(Language.HOUSE_DISALLOW_LEVER));
            } else if (house.prevents(new CLocation(block.getLocation()),
                    event.getPlayer(), Permission.INTERACT)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Language.parse(Language.HOUSE_DISALLOW_INTERACT));
            }
        }
    }
    
    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Player player;
        if (event.getDamager() instanceof Player) {
            player = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            final Projectile pro = (Projectile) event.getDamager();
            if (pro.getShooter() instanceof Player) {
                player = (Player) pro.getShooter();
            } else {
                return;
            }
        } else if (event.getDamager() instanceof Tameable) {
            final Tameable tamed = (Tameable) event.getDamager();
            if (tamed.getOwner() instanceof Player) {
                player = (Player) tamed.getOwner();
            } else {
                return;
            }
        } else {
            return;
        }
        
        if (event.getEntity() instanceof Player) {
            for (IHouse house : plugin.getHouses()) {
                if (house.prevents(new CLocation(event.getEntity().getLocation()),
                        player, Permission.PVP)) {
                    event.setCancelled(true);
                    player.sendMessage(Language.parse(Language.HOUSE_DISALLOW_PVP));
                }
            }
        } else {
            for (IHouse house : plugin.getHouses()) {
                if (house.prevents(new CLocation(event.getEntity().getLocation()),
                        player, Permission.PVE)) {
                    event.setCancelled(true);
                    player.sendMessage(Language.parse(Language.HOUSE_DISALLOW_PVE));
                }
            }
        }
    }

    private boolean isChest(final Material type) {
        return (type == Material.CHEST ||type == Material.ENDER_CHEST ||type == Material.DROPPER
                || type == Material.HOPPER);
    }

    private boolean isDoor(final Material type) {
        return (type == Material.WOODEN_DOOR ||type == Material.WOOD_DOOR ||type == Material.TRAP_DOOR);
    }

    private boolean isLever(final Material type) {
        return (type == Material.STONE_BUTTON ||type == Material.WOOD_BUTTON ||type == Material.LEVER);
    }
}
