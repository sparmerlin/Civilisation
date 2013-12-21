package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import net.slipcor.civ.events.NationLikeEvent;
import net.slipcor.civ.impl.CNation;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdCityNation extends AbstractCommand {

    public CmdCityNation(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(1, 1);
        //                       0
        // /city nation | [name] -> create nation [name]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
    	if (!sender.hasPermission("civ.admin")) {
    		sender.sendMessage(Language.parse(Language.NOPERM, "civ.admin"));
    		return true;
    	}
    	INation iNation = plugin.getNation(args[0]);
        
        if (iNation != null) {
            sender.sendMessage(Language.parse(Language.NATION_ALREADY_EXISTS, args[0]));
            return true;
        }
        
        final ICity city = plugin.getCity((Player) sender);
        
        final INation nation = new CNation(args[0], city);
        Civilisation.getPlugin().addNation(nation);
        Civilisation.getPlugin().getDataManager().save(nation);
        
        sender.sendMessage(Language.parse(Language.NATION_CREATED, args[0]));
        return true;
    }
}
