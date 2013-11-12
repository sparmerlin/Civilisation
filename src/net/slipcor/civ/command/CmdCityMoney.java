package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house->perm command
 * 
 * @author slipcor
 */
public class CmdCityMoney extends AbstractCommand {

    public CmdCityMoney(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 2);
        this.addArgument("add");
        this.addArgument("take");
        //               0      1        2
        // /city money | -> show money
        // /city money | add    [value] -> add money
        // /city money | take   [value] -> take money
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        
        /*
         * perms
         */
        
        final ICity city = plugin.getCity((Player) sender);
        
        
        
        if (args.length == 0) {
            
        	String amount = String.valueOf(city.getMoney()); //TODO Economy
        	
            sender.sendMessage(Language.parse(Language.CITY_MONEY_HAS, amount));
            
        } else if (args[0].equals("add")) {
            handleMoney(sender, StringParser.shiftArrayBy(args, 1), true);
        } else if (args[0].equals("take")) {
            handleMoney(sender, StringParser.shiftArrayBy(args, 1), false);
        }
        return true;
    }

    private void handleMoney(final CommandSender sender, final String[] args, final boolean add) {
        if (args.length < 1) {
            sender.sendMessage(Language.parse(
                    Language.COMMAND_ARG_MIN,
                    String.valueOf(args.length + 2),
                    String.valueOf(3)));
            return;
        }

        // /city money | add    [value] -> add money
        // /city money | take   [value] -> take money
        
        final ICity city = plugin.getCity((Player) sender);
        
        double amount = Double.parseDouble(args[0]);
        String sAmount = String.valueOf(amount); //TODO Economy
        
        if (add) {
        	if (false) {
        		//TODO Economy check
        		sender.sendMessage(Language.parse(Language.CITY_MONEY_NOT_ENOUGH_YOU, sAmount));
        	} else {
        		//TODO Economy take player money
        		city.addMoney(amount);
        		sender.sendMessage(Language.parse(Language.CITY_MONEY_ADDED, sAmount));
        		updateMessage(sender, city);
        	}
        } else {
        	if (false) {
        		//TODO Economy check
        		sender.sendMessage(Language.parse(Language.CITY_MONEY_NOT_ENOUGH_CITY, sAmount));
        	} else {
        		//TODO Economy give player money
        		city.removeMoney(amount);
        		sender.sendMessage(Language.parse(Language.CITY_MONEY_TOOK, sAmount));
        		updateMessage(sender, city);
        	}
        }
    }

	private void updateMessage(CommandSender sender, ICity city) {
        String format = String.valueOf(city.getMoney()); //TODO Economy
		sender.sendMessage(Language.parse(Language.CITY_MONEY_HAS, format));
        city.save();
	}
}
