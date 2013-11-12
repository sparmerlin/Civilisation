package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import net.slipcor.civ.events.HouseLikeEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house->perm command
 * 
 * @author slipcor
 */
public class CmdHouseLike extends AbstractCommand {

    public CmdHouseLike(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //               0
        // /house like | -> show the houses you like
        // /house like | [house] -> like [house]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            IHouse goodHouse = plugin.getHouse(args[0]);
            
            nulling: if (goodHouse == null) {
                for (Player player : Bukkit.matchPlayer(args[0])) {
                    goodHouse = plugin.getHouse(player.getName(), true);
                    break nulling;
                }
                sender.sendMessage(Language.parse(Language.HOUSE_UNKNOWN, args[0]));
                return true;
            }
            
            final IHouse house = plugin.getHouse(sender.getName(), true);
            final HouseLikeEvent event = new HouseLikeEvent(house, goodHouse, true);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.HOUSE_LIKE_CANCELLED));
            	return true;
            }
            house.like(goodHouse);
            sender.sendMessage(Language.parse(Language.HOUSE_YOU_NOW_LIKE, goodHouse.getName()));
            house.save();
        } else {
            final IHouse house = plugin.getHouse(sender.getName(), true);
            sender.sendMessage(Language.parse(Language.HOUSE_YOU_LIKE, StringParser.joinSet(house.getLiked(), ", ")));
        }
        
        return true;
    }
}
