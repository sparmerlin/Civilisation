package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.core.Language;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdNationCapital extends AbstractCommand {

    public CmdNationCapital(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(1, 1);
        //               0
        // /city owner [player] | -> set a new owner
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
    	
    	ICity city = plugin.getCity((Player) sender);
    	INation nation = plugin.getNation(city);
    	
    	ICity capital = nation.getCapital();
    	
    	ICity newCapital = plugin.getCity(args[0]);
    	
    	if (capital.equals(newCapital)) {
    		sender.sendMessage(Language.parse(Language.NATION_CAPITAL_ALREADY));
    		return true;
    	}
    	
    	if (!nation.getCities().contains(newCapital)) {
    		sender.sendMessage(Language.parse(Language.NATION_CAPITAL_NOTINNATION, newCapital.getName()));
    		return true;
    	}
    	
    	nation.getCities().add(capital);
    	nation.getCities().remove(newCapital);
    	nation.setCapital(newCapital);
    	sender.sendMessage(Language.parse(Language.NATION_CAPITAL_DONE, newCapital.getName()));
    	nation.save();
        return true;
    }
}
