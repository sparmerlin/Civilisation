package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.core.Language;
import org.bukkit.command.CommandSender;

/**
 * The reload command
 * 
 * @author dumptruckman,slipcor
 */
public class CmdReload extends AbstractCommand {

    public CmdReload(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 0);
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(Language.parse(Language.PLUGINRELOADED));
        return true;
    }
}
