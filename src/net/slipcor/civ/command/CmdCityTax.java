package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.core.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author slipcor
 */
public class CmdCityTax extends AbstractCommand {

    public CmdCityTax(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(1, 1);
        //               0
        // /city tax [amount] | update the tax amount
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
    	double amount = Double.parseDouble(args[0]);
    	ICity city = plugin.getCity((Player) sender);
    	city.setTax(amount);
    	city.save();
    	String sAmount = String.valueOf(amount);
    	sender.sendMessage(Language.parse(Language.CITY_TAX_DONE, sAmount));
        
        return true;
    }
}
