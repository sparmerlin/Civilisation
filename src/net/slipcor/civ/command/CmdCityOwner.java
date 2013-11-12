package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdCityOwner extends AbstractCommand {

    public CmdCityOwner(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(1, 1);
        //               0
        // /city owner [player] | -> set a new owner
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
    	
    	ICity city = plugin.getCity((Player) sender);
    	
    	IHouse owner = city.getOwner();
    	
    	IHouse newOwner = plugin.getHouse(args[0], true);
    	
    	if (owner.equals(newOwner)) {
    		sender.sendMessage(Language.parse(Language.CITY_OWNER_ALREADY));
    		return true;
    	}
    	
    	if (!city.getHouses().contains(newOwner)) {
    		sender.sendMessage(Language.parse(Language.CITY_OWNER_NOTINCITY, newOwner.getName()));
    		return true;
    	}
    	
    	city.getHouses().add(owner);
    	city.getHouses().remove(newOwner);
    	city.setOwner(newOwner);
    	sender.sendMessage(Language.parse(Language.CITY_OWNER_DONE, city.getName()));
    	city.save();
        return true;
    }
}
