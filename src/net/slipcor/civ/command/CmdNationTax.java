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
public class CmdNationTax extends AbstractCommand {

    public CmdNationTax(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(1, 1);
        //               0
        // /city tax [amount] | update the tax amount
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
    	double amount = Double.parseDouble(args[0]);
    	ICity city = plugin.getCity((Player) sender);
    	INation nation = plugin.getNation(city);
    	nation.setTax(amount);
    	nation.save();
    	String sAmount = String.valueOf(amount);
    	sender.sendMessage(Language.parse(Language.NATION_TAX_DONE, sAmount));
        
        return true;
    }
}
