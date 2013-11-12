package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import net.slipcor.civ.events.CityLikeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdCityDislike extends AbstractCommand {

    public CmdCityDislike(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //                  0
        // /city dislike | -> show the cities you dislike
        // /city dislike | [city] -> dislike [city]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            ICity badCity = plugin.getCity(args[0]);
            
            nulling: if (badCity == null) {
                for (Player player : Bukkit.matchPlayer(args[0])) {
                    badCity = plugin.getCity(player);
                    break nulling;
                }
                sender.sendMessage(Language.parse(Language.CITY_UNKNOWN, args[0]));
                return true;
            }
            
            final ICity city = plugin.getCity((Player) sender);
            final CityLikeEvent event = new CityLikeEvent(city, badCity, false);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.CITY_LIKE_CANCELLED));
            	return true;
            }
            city.dislike(badCity);
            sender.sendMessage(Language.parse(Language.CITY_YOU_NOW_DISLIKE, badCity.getName()));
            city.save();
        } else {
            final ICity city = plugin.getCity((Player) sender);
            sender.sendMessage(Language.parse(Language.CITY_YOU_DISLIKE, StringParser.joinSet(city.getDisliked(), ", ")));
        }
        
        return true;
    }
}
