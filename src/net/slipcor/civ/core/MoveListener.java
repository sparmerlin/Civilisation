package net.slipcor.civ.core;

import java.util.HashMap;
import java.util.Map;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.ICivilisation;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.core.Config.CFG;
import net.slipcor.civ.impl.CChunk;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * The listener class
 * 
 * @author slipcor
 */
public class MoveListener implements Listener {

    private final ICivilisation plugin;
    private final int offset;
    int pos = 0;

    public MoveListener(final ICivilisation plugin) {
        this.plugin = plugin;
        offset = plugin.config().getInt(CFG.PLAYERMOVE_OFFSET);
    }
    
    private final Map<String, Object> lastCiv = new HashMap<String, Object>();

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onMove(final PlayerMoveEvent event) {
        pos = ++pos % offset;
        
        if (pos > 0) {
            return;
        }
        
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            return;
        }
        
        IChunk chunk = new CChunk(event.getTo().getChunk());
        
        // new chunk. yay
        for (IHouse house : plugin.getHouses()) {
            if (house.getClaimed().contains(chunk)) {
                if (house.equals(lastCiv.get(event.getPlayer().getName()))) {
                    // same
                    return;
                } else {
                    lastCiv.put(event.getPlayer().getName(), house);
                    final StringBuffer buffer = new StringBuffer(house.getName());
                    for (ICity city : plugin.getCities()) {
                        if (city.getHouses().contains(house)) {
                            buffer.append(" ~ ");
                            buffer.append(city.getName());
                            
                            for (INation nation : plugin.getNations()) {
                                if (nation.getCapital().equals(city) || nation.getCities().contains(city)) {
                                    buffer.append(" ~ ");
                                    buffer.append(nation.getName());
                                    break;
                                }
                            }
                            
                            break;
                        }
                    }
                    
                    event.getPlayer().sendMessage("["+ChatColor.AQUA+"CIV"+ChatColor.RESET+"] "+buffer.toString());
                    return;
                }
            }
        }
        
        for (ICity city : plugin.getCities()) {
            if (city.getOutposts().contains(chunk)) {
                if (city.equals(lastCiv.get(event.getPlayer().getName()))) {
                    return;
                } else {
                    lastCiv.put(event.getPlayer().getName(), city);
                    
                    final StringBuffer buffer = new StringBuffer(city.getName());
                    
                    for (INation nation : plugin.getNations()) {
                        if (nation.getCapital().equals(city) || nation.getCities().contains(city)) {
                            buffer.append(" ~ ");
                            buffer.append(nation.getName());
                            break;
                        }
                    }
                    
                    buffer.append(" (Outpost)");
                    
                    event.getPlayer().sendMessage("["+ChatColor.AQUA+"CIV"+ChatColor.RESET+"] "+buffer.toString());
                    return;
                }
            }
        }
        if (lastCiv.containsKey(event.getPlayer().getName())) {
            lastCiv.remove(event.getPlayer().getName());
            event.getPlayer().sendMessage("["+ChatColor.AQUA+"CIV"+ChatColor.RESET+"] " + ChatColor.GRAY + "unclaimed");
        }
    }
}
