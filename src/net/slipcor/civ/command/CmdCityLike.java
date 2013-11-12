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
public class CmdCityLike extends AbstractCommand {

    public CmdCityLike(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //                  0
        // /city like | -> show the cities you like
        // /city like | [city] -> like [city]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            ICity goodCity = plugin.getCity(args[0]);
            
            nulling: if (goodCity == null) {
                for (Player player : Bukkit.matchPlayer(args[0])) {
                    goodCity = plugin.getCity(player);
                    break nulling;
                }
                sender.sendMessage(Language.parse(Language.CITY_UNKNOWN, args[0]));
                return true;
            }
            
            final ICity city = plugin.getCity((Player) sender);
            final CityLikeEvent event = new CityLikeEvent(city, goodCity, true);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.CITY_LIKE_CANCELLED));
            	return true;
            }
            city.like(goodCity);
            sender.sendMessage(Language.parse(Language.CITY_YOU_NOW_LIKE, goodCity.getName()));
            city.save();
        } else {
            final ICity city = plugin.getCity((Player) sender);
            sender.sendMessage(Language.parse(Language.CITY_YOU_LIKE, StringParser.joinSet(city.getLiked(), ", ")));
        }
        
        return true;
    }
}
