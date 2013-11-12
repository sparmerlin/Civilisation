package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import net.slipcor.civ.events.NationLikeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdNationDislike extends AbstractCommand {

    public CmdNationDislike(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //                  0
        // /city dislike | -> show the cities you dislike
        // /city dislike | [city] -> dislike [city]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
        	INation badCity = plugin.getNation(args[0]);
            
            if (badCity == null) {
                sender.sendMessage(Language.parse(Language.NATION_UNKNOWN, args[0]));
                return true;
            }
            
            final ICity city = plugin.getCity((Player) sender);
            final INation nation = plugin.getNation(city);
            
            final NationLikeEvent event = new NationLikeEvent(nation, badCity, false);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.NATION_LIKE_CANCELLED));
            	return true;
            }
            nation.dislike(badCity);
            sender.sendMessage(Language.parse(Language.NATION_YOU_NOW_DISLIKE, badCity.getName()));
            nation.save();
        } else {
            final ICity city = plugin.getCity((Player) sender);
            final INation nation = plugin.getNation(city);
            sender.sendMessage(Language.parse(Language.NATION_YOU_DISLIKE, StringParser.joinSet(nation.getDisliked(), ", ")));
        }
        
        return true;
    }
}
