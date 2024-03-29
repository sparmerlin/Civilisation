package net.slipcor.civ.core;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import net.slipcor.civ.Civilisation;
import net.slipcor.civ.core.Config.CFG;
import org.bukkit.Bukkit;

/**
 * The plugin tracker
 * 
 * Phones home to www.slipcor.net to announce the plugin version
 * 
 * @author slipcor
 */
public class Tracker implements Runnable {
    private static int taskID = -1;
        
    private static Civilisation plugin;

    public Tracker(final Civilisation plugin) {
        Tracker.plugin = plugin;
    }

    /**
     * Call home to announce the plugin version
     */
    private void callHome() {
        if (!plugin.config().getBoolean(CFG.CALLHOME)) {
            stop();
            return;
        }

        String url = null;
        try {
            url = String.format("http://www.slipcor.net/stats/call.php?port=%s&name=%s&version=%s",
                plugin.getServer().getPort(),
                URLEncoder.encode(plugin.getDescription().getName(), "UTF-8"),
                URLEncoder.encode(plugin.getDescription().getVersion(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            new URL(url).openConnection().getInputStream();
        } catch (Exception e) {
            plugin.getLogger().warning("Error while connecting to www.slipcor.net");
            return;
        }
    }

    @Override
    public void run() {
        callHome();
    }

    /**
     * Start tracking
     */
    public void start() {
        plugin.getLogger().info("Preparing to send stats to www.slipcor.net...");
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this,
            0L, 72000L);
    }

    /**
     * Stop tracking
     */
    public static void stop() {
        plugin.getLogger().info("Disabled sending stats!");
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
