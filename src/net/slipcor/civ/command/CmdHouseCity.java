package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import net.slipcor.civ.events.HouseLikeEvent;
import net.slipcor.civ.impl.CCity;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house->perm command
 * 
 * @author slipcor
 */
public class CmdHouseCity extends AbstractCommand {

    public CmdHouseCity(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(1, 1);
        //                       0
        // /house city create | [name] -> create city [name]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
    	if (!sender.hasPermission("civ.admin")) {
    		sender.sendMessage(Language.parse(Language.NOPERM, "civ.admin"));
    		return true;
    	}
    	ICity c = Civilisation.getPlugin().getCity(args[0]);
        if (c == null) {
        	c = new CCity(args[0], Civilisation.getPlugin().getHouse(sender.getName(), true));
        	Civilisation.getPlugin().addCity(c);
        	Civilisation.getPlugin().getDataManager().save(c);

        	sender.sendMessage(Language.parse(Language.CITY_CREATED, args[0]));
        } else {
        	sender.sendMessage(Language.parse(Language.CITY_ALREADY_EXISTS, args[0]));
        }
        
        return true;
    }
}
