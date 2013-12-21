package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.events.NationLeaveEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house command
 * 
 * @author slipcor
 */
public class CmdNation extends AbstractCommand {

    public CmdNation(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        
        
        // /nation dislike [house]
        this.addSubCommand("dislike", new CmdNationDislike(plugin));
        // /nation like [house]
        this.addSubCommand("like", new CmdNationLike(plugin));
        // /nation capital [city]
        this.addSubCommand("owner", new CmdNationCapital(plugin));
        // /nation money [action] [amount]
        this.addSubCommand("money", new CmdNationMoney(plugin));
        // /nation tax [amount]
        this.addSubCommand("tax", new CmdNationTax(plugin));
        
        // /nation leave | leave the nation
        this.addArgument("leave");
        
        // /nation | show info about your nation
        
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
        
        final INation nation = plugin.getNation(city);
        
        if (nation == null) {
        	sender.sendMessage(Language.parse(Language.NATION_YOU_NO_NATION));
        	return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage(nation.debug());
            return true;
        }
        

        if (args[0].equals("leave")) {
        	final NationLeaveEvent event = new NationLeaveEvent(nation, sender);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
            	sender.sendMessage(Language.parse(Language.NATION_LEAVE_CANCELLED));
            	return true;
            }
            //TODO check the capital !!
        	nation.getCities().remove(city);
        	sender.sendMessage(Language.parse(Language.NATION_YOU_LEFT, nation.getName()));
        	return true;
        }
        
        if (!(nation.getCapital().equals(city) ||
        		(args.length == 3 && args[0].equals("money") && args[1].equals("add")))) {
        	sender.sendMessage(Language.parse(Language.NATION_NO_CAPITAL));
        	return true;
        }
        
        return true;
    }
}
