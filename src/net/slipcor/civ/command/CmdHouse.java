package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.impl.CChunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house command
 * 
 * @author slipcor
 */
public class CmdHouse extends AbstractCommand {

    public CmdHouse(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        
        
        // /house perm add [player] [perm]
        this.addSubCommand("perm", new CmdHousePerm(plugin));
        // /house dislike {[house]}
        this.addSubCommand("dislike", new CmdHouseDislike(plugin));
        // /house like {[house]}
        this.addSubCommand("like", new CmdHouseLike(plugin));
        
        // /house claim | claim the chunk you stand on
        this.addArgument("claim");
        // /house unclaim | unclaim the chunk you stand on
        this.addArgument("unclaim");
        
        // /house | show info about your house
        
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            //TODO print house information
            
            return true;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.parse(Language.COMMAND_ONLYPLAYER));
            return true;
        }
        
        final Player player = (Player) sender;
        final IChunk chunk = new CChunk(player.getLocation().getChunk());
        final IHouse house = plugin.getHouse(player.getName(), true);
        
        if (args[0].equals("claim")) {
            for (IHouse serverHouse : plugin.getHouses()) {
                if (serverHouse.getClaimed().contains(chunk)) {
                    if (serverHouse.equals(house)) {
                        sender.sendMessage(Language.parse(Language.HOUSE_CLAIM_ALREADY));
                        return true;
                    }
                    
                    sender.sendMessage(Language.parse(Language.HOUSE_CLAIM_ALREADY_OTHER, serverHouse.toString()));
                    return true;
                }
            }
            
            if (house.getClaimed().size() > 4) {
                sender.sendMessage(Language.parse(Language.HOUSE_CLAIM_MAX));
            } else {
                house.getClaimed().add(chunk);
                house.save();
                sender.sendMessage(Language.parse(Language.HOUSE_CLAIM_DONE)); 
            }
            
        } else if (args[0].equals("unclaim")) {
            if (house.getClaimed().contains(chunk)) {
                house.getClaimed().remove(chunk);
                house.save();
                sender.sendMessage(Language.parse(Language.HOUSE_UNCLAIM_DONE));
            } else {
                sender.sendMessage(Language.parse(Language.HOUSE_UNCLAIM_FAIL));
            }
        }
        return true;
    }
}
