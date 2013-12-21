package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.events.CityClaimEvent;
import net.slipcor.civ.events.CityLeaveEvent;
import net.slipcor.civ.impl.CChunk;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house command
 * 
 * @author slipcor
 */
public class CmdCity extends AbstractCommand {

    public CmdCity(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        
        
        // /city perm add [player] [perm]
        this.addSubCommand("perm", new CmdCityPerm(plugin));
        // /city dislike [house]
        this.addSubCommand("dislike", new CmdCityDislike(plugin));
        // /city like [house]
        this.addSubCommand("like", new CmdCityLike(plugin));
        // /city owner [player]
        this.addSubCommand("owner", new CmdCityOwner(plugin));
        // /city money [action] [amount]
        this.addSubCommand("money", new CmdCityMoney(plugin));
        // /city tax [amount]
        this.addSubCommand("tax", new CmdCityTax(plugin));
        // /city nation [name]
        this.addSubCommand("nation", new CmdCityNation(plugin));
        
        // /city claim | claim the chunk you stand on
        this.addArgument("claim");
        // /city unclaim | unclaim the chunk you stand on
        this.addArgument("unclaim");
        // /city leave | leave the city
        this.addArgument("leave");
        
        // /city | show info about your city
        
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.parse(Language.COMMAND_ONLYPLAYER));
            return true;
        }
        final Player player = (Player) sender;
        final ICity city = plugin.getCity(player);
        
        if (city == null) {
        	sender.sendMessage(Language.parse(Language.CITY_YOU_NO_CITY));
        	return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage(city.debug());
            return true;
        }
        
        final IChunk chunk = new CChunk(player.getLocation().getChunk());
        final IHouse house = plugin.getHouse(player.getName(), true);

        if (args[0].equals("leave")) {
        	final CityLeaveEvent event = new CityLeaveEvent(city, sender);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.CITY_LEAVE_CANCELLED));
            	return true;
            }
            // TODO check the owner!
        	city.getHouses().remove(house);
        	sender.sendMessage(Language.parse(Language.CITY_YOU_LEFT, city.getName()));
        	return true;
        }
        
        if (!(city.getOwner().equals(house) ||
        		(args.length == 3 && args[0].equals("money") && args[1].equals("add")))) {
        	sender.sendMessage(Language.parse(Language.CITY_NO_OWNER));
        	return true;
        }
        
        if (args[0].equals("claim")) {
            for (IHouse otherHouse : plugin.getHouses()) {
                if (otherHouse.getClaimed().contains(chunk)) {
                    if (otherHouse.equals(house)) {
                        sender.sendMessage(Language.parse(Language.HOUSE_CLAIM_ALREADY));
                        return true;
                    }
                    final CityClaimEvent event = new CityClaimEvent(city, sender, chunk, true);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                    	sender.sendMessage(Language.parse(Language.CITY_CLAIM_CANCELLED));
                    	return true;
                    }
                    sender.sendMessage(Language.parse(Language.HOUSE_CLAIM_ALREADY_OTHER, otherHouse.toString()));
                    return true;
                }
            }
            for (ICity otherCity : plugin.getCities()) {
                if (otherCity.getOutposts().contains(chunk)) {
                    if (otherCity.equals(city)) {
                        sender.sendMessage(Language.parse(Language.CITY_CLAIM_ALREADY));
                        return true;
                    }
                    
                    sender.sendMessage(Language.parse(Language.CITY_CLAIM_ALREADY_OTHER, otherCity.toString()));
                    return true;
                }
            }
            
            if (city.getOutposts().size() > city.getMaxOutposts()) {
                sender.sendMessage(Language.parse(Language.CITY_CLAIM_MAX));
            } else {
            	city.getOutposts().add(chunk);
            	city.save();
                sender.sendMessage(Language.parse(Language.CITY_CLAIM_DONE)); 
            }
            
        } else if (args[0].equals("unclaim")) {

            final CityClaimEvent event = new CityClaimEvent(city, sender, chunk, false);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.CITY_CLAIM_CANCELLED));
            	return true;
            }
            if (city.getOutposts().contains(chunk)) {
            	city.getOutposts().remove(chunk);
                house.save();
                sender.sendMessage(Language.parse(Language.CITY_UNCLAIM_DONE));
            } else {
                sender.sendMessage(Language.parse(Language.CITY_UNCLAIM_FAIL));
            }
        }
        return true;
    }
}
