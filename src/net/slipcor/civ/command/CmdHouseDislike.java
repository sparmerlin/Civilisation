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
public class CmdHouseDislike extends AbstractCommand {

    public CmdHouseDislike(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //                  0
        // /house dislike | -> show the houses you dislike
        // /house dislike | [house] -> dislike [house]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            IHouse badHouse = plugin.getHouse(args[0]);
            
            nulling: if (badHouse == null) {
                for (Player player : Bukkit.matchPlayer(args[0])) {
                    badHouse = plugin.getHouse(player.getName(), true);
                    break nulling;
                }
                sender.sendMessage(Language.parse(Language.HOUSE_UNKNOWN, args[0]));
                return true;
            }
            
            final IHouse house = plugin.getHouse(sender.getName(), true);
            final HouseLikeEvent event = new HouseLikeEvent(house, badHouse, false);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.HOUSE_LIKE_CANCELLED));
            	return true;
            }
            house.dislike(badHouse);
            sender.sendMessage(Language.parse(Language.HOUSE_YOU_NOW_DISLIKE, badHouse.getName()));
            house.save();
        } else {
            final IHouse house = plugin.getHouse(sender.getName(), true);
            sender.sendMessage(Language.parse(Language.HOUSE_YOU_DISLIKE, StringParser.joinSet(house.getDisliked(), ", ")));
        }
        
        return true;
    }
}
