package net.slipcor.civ.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.slipcor.civ.Civilisation;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.StringParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Abstract command superclass
 * 
 * A class that all subcommands have to inherit
 * 
 * @author slipcor
 */
public abstract class AbstractCommand implements CommandExecutor {
    protected Civilisation plugin;
    
    protected final Map<String, AbstractCommand> subCommands = new HashMap<String, AbstractCommand>();
    
    private int min;
    private int max;
    protected List<String> arguments = new ArrayList<String>();
    
    public AbstractCommand(final Civilisation plugin) {
        this.plugin = plugin;
    }
    
    public void addArgument(final String arg) {
        arguments.add(arg);
    }
    
    public void addSubCommand(final String sub, final AbstractCommand command) {
        if (this.equals(command)) {
            throw new IllegalArgumentException("Command may not be SubCommand of itself!");
        }
        this.subCommands.put(sub, command);
    }
    
    /**
     * Command handling, check all vars and finally run the command
     * @param sender the CommandSender running the command
     * @param cmnd the Command being called
     * @param cmdLabel the Command string
     * @param strings the command arguments
     * @return false if help should be displayed
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command cmnd, final String cmdLabel, final String[] strings) {
        if (strings.length > 0 && subCommands.containsKey(strings[0])) {
            return subCommands.get(strings[0]).onCommand(sender, cmnd, cmdLabel, StringParser.shiftArrayBy(strings, 1));
        }
        
        if (strings.length > 0 && (
        		arguments.size() > 0 || subCommands.size() > 0) &&
                (!arguments.contains(strings[0]) &&
                !subCommands.containsKey(strings[0]))) {
        	new Exception().printStackTrace();
            sender.sendMessage(Language.parse(Language.COMMAND_ARG_UNKNOWN, strings[0]));
            sender.sendMessage(Language.parse(Language.COMMAND_SUBCMDS, StringParser.joinSet(subCommands.keySet(), ", ")));
            sender.sendMessage(Language.parse(Language.COMMAND_ARGS, StringParser.joinArray(arguments.toArray(), ", ")));
            return true;
        }
        
        if (strings.length < min) {
            sender.sendMessage(Language.parse(Language.COMMAND_ARG_MIN, String.valueOf(strings.length), String.valueOf(min)));
            return false;
        }
        
        if (strings.length > max) {
            sender.sendMessage(Language.parse(Language.COMMAND_ARG_MAX, String.valueOf(strings.length), String.valueOf(max)));
            return false;
        }
        
        return this.runCommand(sender, strings);
    }
    
    /**
     * Run the actual command
     * 
     * @param sender the CommandSender running the command
     * @param args the command arguments
     * @return false if help should be displayed
     */
    public abstract boolean runCommand(final CommandSender sender, final String[] args);
    
    /**
     * Set the valid argument range
     * @param min the minimal needed arguments
     * @param max the maximal allowed arguments
     */
    protected void setArgRange(final int min, final int max) {
        this.min = min;
        this.max = max;
    }
}
