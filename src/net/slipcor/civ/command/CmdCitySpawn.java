package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.impl.CChunk;
import net.slipcor.civ.impl.CLocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdCitySpawn extends AbstractCommand {

    public CmdCitySpawn(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //                  0
        // /city spawn | -> go to the city spawn
        // /city spawn | set -> set the city spawn
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.parse(Language.COMMAND_ONLYPLAYER));
            return true;
        }

        final Player player = (Player) sender;
        final IHouse house = plugin.getHouse(player.getName());
        final ICity city = plugin.getCity(player);
        
        if (city.getOwner().equals(house) && args.length > 0) {
            
            IChunk chunk = new CChunk(player.getLocation().getChunk());
            
            if (city.getOutposts().contains(chunk) || house.getClaimed().contains(chunk)) {
                city.setSpawn(new CLocation(player.getLocation()));
                city.save();
                sender.sendMessage(Language.parse(Language.CITY_SPAWN_SET));
            } else {
                for (IHouse iHouse : city.getHouses()) {
                    if (iHouse.getClaimed().contains(chunk)) {
                        city.setSpawn(new CLocation(player.getLocation()));
                        city.save();
                        sender.sendMessage(Language.parse(Language.CITY_SPAWN_SET));
                        return true;
                    }
                }
                
                sender.sendMessage(Language.parse(Language.CITY_SPAWN_NOTINCITY));
            }
            
        } else {
            if (city.getSpawn() == null) {
                sender.sendMessage(Language.parse(Language.CITY_SPAWN_NOTSET));
                return true;
            }
            player.teleport(city.getSpawn().toLocation());
            sender.sendMessage(Language.parse(Language.CITY_SPAWN_TP));
        }
        
        return true;
    }
}
