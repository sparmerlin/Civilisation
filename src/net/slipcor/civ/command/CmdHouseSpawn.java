package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.impl.CChunk;
import net.slipcor.civ.impl.CLocation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The house->perm command
 * 
 * @author slipcor
 */
public class CmdHouseSpawn extends AbstractCommand {

    public CmdHouseSpawn(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 1);
        //                  0
        // /house spawn | -> go to the house spawn
        // /house spawn | set -> set the house spawn
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.parse(Language.COMMAND_ONLYPLAYER));
            return true;
        }

        final Player player = (Player) sender;
        final IHouse house = plugin.getHouse(player.getName(), true);
        
        if (args.length > 0) {
            IChunk chunk = new CChunk(player.getLocation().getChunk());
            
            if (house.getClaimed().contains(chunk)) {
                house.setSpawn(new CLocation(player.getLocation()));
                house.save();
                sender.sendMessage(Language.parse(Language.HOUSE_SPAWN_SET));
            } else {
                sender.sendMessage(Language.parse(Language.HOUSE_SPAWN_NOTINHOUSE));
            }
            
        } else {
            if (house.getSpawn() == null) {
                sender.sendMessage(Language.parse(Language.HOUSE_SPAWN_NOTSET));
                return true;
            }
            player.teleport(house.getSpawn().toLocation());
            sender.sendMessage(Language.parse(Language.HOUSE_SPAWN_TP));
        }
        
        return true;
    }
}
