package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.Permission;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import net.slipcor.civ.impl.CPermMap;
import org.bukkit.command.CommandSender;

/**
 * The house->perm command
 * 
 * @author slipcor
 */
public class CmdCityPerm extends AbstractCommand {

    public CmdCityPerm(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 3);
        this.addArgument("add");
        this.addArgument("remove");
        //               0      1        2
        // /house perm | -> list perms for your house
        // /house perm | add    [perm] -> add perms for everyone
        // /house perm | add    [player] [perm] -> add perms for [player]
        // /house perm | remove [perm] -> add perms for everyone
        // /house perm | remove [player] [perm] -> add perms for [player]
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        
        /*
         * perms
         */
        
        final IHouse house = plugin.getHouse(sender.getName(), true);
        if (args.length == 0) {
            
            sender.sendMessage(Language.parse(Language.HOUSE_YOURPERMS));
            
            for (String name : house.getPerms().keySet()) {
                if (name.equals("city")) {
                    sender.sendMessage(
                            Language.parse(Language.HOUSE_PERM_LIST_CITY, 
                            house.getPerms().get(name).printColorized()));
                } else if (name.equals("everyone")) {
                    sender.sendMessage(
                            Language.parse(Language.HOUSE_PERM_LIST_EVERYONE, 
                            house.getPerms().get(name).printColorized()));
                } else if (name.equals("nation")) {
                    sender.sendMessage(
                            Language.parse(Language.HOUSE_PERM_LIST_NATION, 
                            house.getPerms().get(name).printColorized()));
                } else {
                    sender.sendMessage(name + ": " + house.getPerms().get(name).printColorized());
                }
            }
        } else if (args[0].equals("add")) {
            handleSet(sender, StringParser.shiftArrayBy(args, 1), true);
        } else if (args[0].equals("remove")) {
            handleSet(sender, StringParser.shiftArrayBy(args, 1), false);
        }
        return true;
    }

    private void handleSet(final CommandSender sender, final String[] args, final boolean add) {
        if (args.length < 1) {
            sender.sendMessage(Language.parse(
                    Language.COMMAND_ARG_MIN,
                    String.valueOf(args.length + 2),
                    String.valueOf(3)));
            return;
        }
        
        // /house perm | add    | [perm] -> add perms for everyone
        // /house perm | add    | [player] [perm] -> add perms for [player]
        // /house perm | remove | [perm] -> add perms for everyone
        // /house perm | remove | [player] [perm] -> add perms for [player]
        
        final IHouse house = plugin.getHouse(args[0]);
        String toSet = null;
        Permission setPerm = null;
        
        
        if (house == null) {
            // first argument was no house, maybe it is a special node?
            if (args[0].equals("everyone") || args[0].equals("city") || args[0].equals("nation")) {
                toSet = args[0];
                if (args.length > 1) {
                    for (Permission perm : Permission.values()) {
                        if (perm.toString().equals(args[0]) || perm.name().equalsIgnoreCase(args[0])) {
                            // perm found
                            setPerm = perm;
                            break;
                        }
                    }
                }
            } else {
                // first argument is no special node, so it should better be a perm!
                for (Permission perm : Permission.values()) {
                    if (perm.toString().equals(args[0]) || perm.name().equalsIgnoreCase(args[0])) {
                        // perm found, assume we set everyone!
                        toSet = "everyone";
                        setPerm = perm;
                        break;
                    }
                }
                
                if (toSet == null) {
                    // first is no house, no special node, no perm
                    
                    sender.sendMessage(Language.parse(Language.COMMAND_ARG_UNKNOWN, args[0]));
                    sender.sendMessage(Language.parse(
                            Language.COMMAND_ARGS, StringParser.joinArray(new String[]{"%playername%","everyone","city","nation"}, ", ")));
                    return;
                }
                
                
            }
        } else {
            toSet = house.getName();
            if (args.length > 1) {
                for (Permission perm : Permission.values()) {
                    if (perm.toString().equals(args[0]) || perm.name().equalsIgnoreCase(args[0])) {
                        // perm found
                        setPerm = perm;
                        break;
                    }
                }
            }
        }
        
        if (setPerm == null) {
            if (add) {
                house.setPerms(toSet, new CPermMap(65535));
                sender.sendMessage(Language.parse(Language.HOUSE_PERM_ADDED, toSet,
                        Language.parse(Language.HOUSE_PERM_ALL)));
            } else {
                house.setPerms(toSet, new CPermMap(0));
                sender.sendMessage(Language.parse(Language.HOUSE_PERM_REMOVED, toSet,
                        Language.parse(Language.HOUSE_PERM_ALL)));
            }
        } else if (add) {
            house.addPerm(toSet, setPerm);
            sender.sendMessage(Language.parse(Language.HOUSE_PERM_ADDED, toSet, setPerm.name()));
        } else {
            house.removePerm(toSet, setPerm);
            sender.sendMessage(Language.parse(Language.HOUSE_PERM_REMOVED, toSet, setPerm.name()));
        }
        house.save();
    }
}
