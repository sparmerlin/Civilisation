package net.slipcor.civ.core;

import java.io.File;
import java.io.IOException;
import net.slipcor.civ.api.ICivilisation;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The language enum
 * 
 * Contains all language nodes and their values
 * 
 * @author slipcor
 */
public enum Language {
    
    VERSION("version", "v0.0.0.1"),
    
    COMMAND_ARG_MIN("args_min","Not enough arguments. Found %1%, need %2%!"),
    COMMAND_ARG_MAX("args_max","Too many arguments. Found %1%, max is %2%!"),
    COMMAND_ARG_UNKNOWN("arg_unknown", "Unknown argument %1%!"),
    COMMAND_ARGS("args_valid", "Valid arguments: %1%"),
    
    COMMAND_ONLYPLAYER("only_player", "Command can only be used by players!"),
    
    COMMAND_SUBCMDS("arg_subcmds", "Valid subcommands: %1%"),

    CITY_ALREADY_EXISTS("city_already_exists", "City already exists: %1%"),
    
    CITY_HOUSE_ALREADY("city_house_already", "%1% is already part of your city!"),
    CITY_HOUSE_ALREADY_OTHER("city_house_already_other", "%1% is already part of the city %2%!"),
    CITY_HOUSE_INVITED("city_house_invited", "The house %1% has been invited to your city!"),
    CITY_HOUSE_INVITED_YOU("city_house_invited_you", "The city %1% has invited you!"),
    CITY_HOUSE_KICKED("city_house_kicked", "The house %1% has been kicked from your city!"),
    CITY_HOUSE_KICKED_YOU("city_house_kicked_you", "You have been kicked from your city!"),
    CITY_HOUSE_NOTKICK_NOTPART("city_house_notkick_notpart", "You cannot kick a house that is not in your city!"),
    CITY_HOUSE_NOTKICK_YOURSELF("city_house_notkick_yourself", "You cannot kick yourself out of your city!"),
    
    CITY_CLAIM_ALREADY("city_claim_already", "This chunk already belongs to your city!"),
    CITY_CLAIM_ALREADY_OTHER("city_claim_already_other", "This chunk already belongs to %1%!"),
    CITY_CLAIM_DONE("city_claim_done","Outpost claimed successfully!"),
    CITY_CLAIM_MAX("city_claim_max", "Your City already claimed the maximum outposts!"),

    CITY_CREATED("city_created", "City '%1%' created!"),
    
    CITY_NO_OWNER("city_no_owner", "You may not use this command as you don't own your city!"),
    
    CITY_SPAWN_NOTINCITY("city_spawn_notincity", "You need to be in your city!"),
    CITY_SPAWN_NOTSET("city_spawn_notset", "There is no city spawn set yet!"),
    CITY_SPAWN_TP("city_spawn_tp", "You were teleported to the city spawn!"),
    CITY_SPAWN_SET("city_spawn_set", "City spawn updated!"),
    
    CITY_YOU_LEFT("city_you_left", "You left %1%!"),
    CITY_YOU_NO_CITY("city_you_no_city", "You are not part of a city!"),
    CITY_UNCLAIM_DONE("city_unclaim_done", "Outpost unclaimed successfully!"),
    CITY_UNCLAIM_FAIL("city_unclaim_fail", "Outpost could not be unclaimed, does not belong to your City!"),
    
    CITY_UNKNOWN("city_unknown", "City does not exist: %1%"),
    
    CITY_CLAIM_CANCELLED("city_claim_cancelled", "Sorry, but this command has been cancelled."),
    CITY_LEAVE_CANCELLED("city_leave_cancelled", "Sorry, but this command has been cancelled."),
    CITY_LIKE_CANCELLED("city_like_cancelled", "Sorry, but this command has been cancelled."),
    
    CITY_YOU_LIKE("city_you_like", "Your City likes %1%"),
    CITY_YOU_NOW_LIKE("city_you_now_like", "Your City now likes the City %1%"),
    CITY_YOU_DISLIKE("city_you_dislike", "Your City dislikes %1%"),
    CITY_YOU_NOW_DISLIKE("city_you_now_dislike", "Your City now dislikes the City %1%"),

    CITY_MONEY_HAS("city_money_has", "Your City currently has %1%."),
    CITY_MONEY_ADDED("city_money_added", "You added %1% to the City Bank."),
    CITY_MONEY_TOOK("city_money_took", "You took %1% from the City Bank."),

    CITY_MONEY_NOT_ENOUGH_CITY("city_not_enough_city", "The City does not have %1%!"),
    CITY_MONEY_NOT_ENOUGH_YOU("city_not_enough_you", "You do not have %1%!"),

    CITY_OWNER_ALREADY("city_owner_already", "You want to set yourself to be the new owner?"),
    CITY_OWNER_DONE("city_owner_done", "The new owner is %1%!"),
    CITY_OWNER_NOTINCITY("city_owner_notincity", "%1% is not in the City!"),
    
    CITY_TAX_DONE("city_tax_done", "The new tax is %1%!"),
    
    HOUSE_CLAIM_ALREADY("house_claim_already", "This chunk already belongs to your house!"),
    HOUSE_CLAIM_ALREADY_OTHER("house_claim_already_other", "This chunk already belongs to %1%!"),
    HOUSE_CLAIM_DONE("house_claim_done","Chunk claimed successfully!"),
    HOUSE_CLAIM_MAX("house_claim_max", "Your House already claimed the maximum chunks!"),
    
    HOUSE_DISALLOW_BREAK("house_disallow_break", "You may not break blocks here!"),
    HOUSE_DISALLOW_CHEST("house_disallow_chest", "You may not access chests here!"),
    HOUSE_DISALLOW_DOOR("house_disallow_door", "You may not open doors here!"),
    HOUSE_DISALLOW_INTERACT("house_disallow_interact", "You may not interact here!"),
    HOUSE_DISALLOW_LEVER("house_disallow_lever", "You may not switch levers here!"),
    HOUSE_DISALLOW_PLACE("house_disallow_place", "You may not place blocks here!"),
    HOUSE_DISALLOW_PVE("house_disallow_pve", "You may not fight entities here!"),
    HOUSE_DISALLOW_PVP("house_disallow_pvp", "You may not fight players here!"),
    
    HOUSE_SPAWN_NOTINHOUSE("house_spawn_notinhouse", "You need to be in your house's chunks!"),
    HOUSE_SPAWN_NOTSET("house_spawn_notset", "There is no house spawn set yet!"),
    HOUSE_SPAWN_TP("house_spawn_tp", "You were teleported to the house spawn!"),
    HOUSE_SPAWN_SET("house_spawn_set", "House spawn updated!"),
    
    HOUSE_UNCLAIM_DONE("house_unclaim_done", "Chunk unclaimed successfully!"),
    HOUSE_UNCLAIM_FAIL("house_unclaim_fail", "Chunk could not be unclaimed, does not belong to your House!"),
    
    HOUSE_UNKNOWN("house_unknown", "House does not exist: %1%"),
    
    HOUSE_LIKE_CANCELLED("house_like_cancelled", "Sorry, but this command has been cancelled."),
    
    HOUSE_YOU_LIKE("house_you_like", "Your House likes %1%"),
    HOUSE_YOU_NOW_LIKE("house_you_now_like", "Your House now likes the House %1%"),
    HOUSE_YOU_DISLIKE("house_you_dislike", "Your House dislikes %1%"),
    HOUSE_YOU_NOW_DISLIKE("house_you_now_dislike", "Your House now dislikes the House %1%"),
    HOUSE_YOURPERMS("house_your_perms", "This is the list of your House's permissions:"),
    
    HOUSE_PERM_ADDED("house_perm_added", "Added perm %2% for %1%"),
    HOUSE_PERM_ALL("house_perm_all", "ALL"),
    HOUSE_PERM_LIST_EVERYONE("house_perm_list_everyone", "everyone: %1%"),
    HOUSE_PERM_LIST_CITY("house_perm_list_city", "city: %1%"),
    HOUSE_PERM_LIST_NATION("house_perm_list_nation", "nation: %1%"),
    HOUSE_PERM_REMOVED("house_perm_removed", "Removed perm %2% for %1%"),
    
    NATION_LEAVE_CANCELLED("nation_leave_cancelled", "Sorry, but this command has been cancelled."),
    NATION_LIKE_CANCELLED("nation_like_cancelled", "Sorry, but this command has been cancelled."),
    
    NATION_YOU_LEFT("nation_you_left", "You left %1%!"),
    NATION_YOU_NO_NATION("nation_you_no_nation", "You are not part of a nation!"),

    NATION_ALREADY_EXISTS("nation_already_exists", "Nation already exists: %1%"),
    NATION_UNKNOWN("nation_unknown", "Nation does not exist: %1%"),
    

    NATION_YOU_LIKE("nation_you_like", "Your Nation likes %1%"),
    NATION_YOU_NOW_LIKE("nation_you_now_like", "Your Nation now likes the Nation %1%"),
    NATION_YOU_DISLIKE("nation_you_dislike", "Your Nation dislikes %1%"),
    NATION_YOU_NOW_DISLIKE("nation_you_now_dislike", "Your Nation now dislikes the Nation %1%"),

    NATION_MONEY_HAS("nation_money_has", "Your Nation currently has %1%."),
    NATION_MONEY_ADDED("nation_money_added", "You added %1% to the Nation Bank."),
    NATION_MONEY_TOOK("nation_money_took", "You took %1% from the Nation Bank."),

    NATION_MONEY_NOT_ENOUGH_NATION("nation_not_enough_nation", "The Nation does not have %1%!"),
    NATION_MONEY_NOT_ENOUGH_YOU("nation_not_enough_you", "You do not have %1%!"),

    NATION_CAPITAL_ALREADY("nation_capital_already", "You want to set yourself to be the new capital?"),
    NATION_CAPITAL_DONE("nation_capital_done", "The new capital is %1%!"),
    NATION_CAPITAL_NOTINNATION("nation_capital_notinnation", "%1% is not in the Nation!"),

    NATION_CREATED("nation_created", "Nation created: %1%"),
    
    NATION_NO_CAPITAL("nation_no_capital", "You may not use this command as you are not the capital!"),
    
    NATION_TAX_DONE("nation_tax_done", "The new tax is %1%!"),
    
    NOPERM("noperm", "&cYou don't have the permission &r%1%&c!"),
    
    PLUGINRELOADED("pluginreloaded", "Plugin reloaded successfully!"),
    
    LANGUAGEFILELOADED("fileloaded", "Language file loaded: %1%"),
    LANGUAGEFILELOADERROR("fileloaderror", "Error while loading lang file: %1%");
    
    String node;
    String msg;
    
    static YamlConfiguration cfgFile = null;
    
    Language(final String node, final String msg) {
        this.node = node;
        this.msg = msg;
    }
    
    /**
     * Initiate the language class
     * 
     * @param plugin the plugin
     * @param file the configuration file
     * @throws IOException 
     */
    public static void init(final ICivilisation plugin, final String file) throws IOException {
        final File cfg = new File(plugin.getDataFolder(), file);
        if (!cfg.exists()) {
            cfg.createNewFile();
        }
        cfgFile = YamlConfiguration.loadConfiguration(cfg);
        
        cfgFile.options().copyDefaults(true);
        for (Language l : Language.values()) {
            cfgFile.addDefault(l.node, l.msg);
        }
        cfgFile.save(cfg);
    }
    
    public static String parse(final Language message) {
		return StringParser.colorize(message.toString());
	}
    
    public static String parse(final Language message, final String... args) {
		
		String result = message.toString();
		int i = 0;
		for (String word : args) {
			result = result.replace("%" + ++i + "%", word);
		}
		return StringParser.colorize(result);
	}
    
    /**
     * Override the toString() method in order to get the actual values easier
     * 
     * @return the config value
     */
    @Override
    public String toString() {
        return cfgFile.getString(node);
    }
}
