package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house->perm command
 * 
 * @author slipcor
 */
public class CmdCityHouse extends AbstractCommand {

    public CmdCityHouse(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(2, 2);
        this.addArgument("invite");
        this.addArgument("kick");
        //               0      1        2
        // /city house | invite  [house] -> invite House into City
        // /city house | kick    [house] -> kick House from City
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        
        /*
         * perms
         */
        
        final ICity city = plugin.getCity((Player) sender);
        
        final IHouse house = plugin.getHouse(sender.getName(), true);
        
        if (!city.getOwner().equals(house)) {
            sender.sendMessage(Language.parse(Language.CITY_NO_OWNER));
            return true;
        }
        IHouse thatHouse = plugin.getHouse(args[1]);
        
        if (args[0].equals("invite")) {
            if (thatHouse == null) {
                sender.sendMessage(Language.parse(Language.HOUSE_UNKNOWN, args[1]));
                return true;
            }
            if (thatHouse.equals(house) || city.getHouses().contains(thatHouse)) {
                sender.sendMessage(Language.parse(Language.CITY_HOUSE_ALREADY, args[1]));
                return true;
            }
            
            for (ICity otherCity : plugin.getCities()) {
                if (otherCity.getOwner().equals(thatHouse) || otherCity.getHouses().contains(thatHouse)) {
                    sender.sendMessage(Language.parse(Language.CITY_HOUSE_ALREADY_OTHER, args[1], otherCity.getName()));
                    return true;
                } 
            }
            
            city.getHouses().add(thatHouse);
            city.save();
            
            sender.sendMessage(Language.parse(Language.CITY_HOUSE_INVITED, args[1]));
            
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null) {
                sender.sendMessage(Language.parse(Language.CITY_HOUSE_INVITED_YOU, city.getName()));
            }
        } else if (args[0].equals("kick")) {
            if (thatHouse.equals(house)) {
                sender.sendMessage(Language.parse(Language.CITY_HOUSE_NOTKICK_YOURSELF, city.getName()));
            } else if (city.getHouses().contains(thatHouse)) {
                city.getHouses().remove(thatHouse);
                city.save();
                
                sender.sendMessage(Language.parse(Language.CITY_HOUSE_KICKED, args[1]));
                
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    sender.sendMessage(Language.parse(Language.CITY_HOUSE_KICKED_YOU));
                }
            } else {
                sender.sendMessage(Language.parse(Language.CITY_HOUSE_NOTKICK_NOTPART, city.getName()));
            }
        }
        return true;
    }
}
