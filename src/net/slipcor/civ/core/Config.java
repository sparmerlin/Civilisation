package net.slipcor.civ.core;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.slipcor.civ.api.IBlockLocation;
import net.slipcor.civ.api.ILocation;
import net.slipcor.civ.impl.CBlockLocation;
import net.slipcor.civ.impl.CLocation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Config {

    private YamlConfiguration cfg;
    private File configFile;
    private Map<String, Boolean> booleans;
    private Map<String, Integer> ints;
    private Map<String, Double> doubles;
    private Map<String, String> strings;

    public static enum CFG {

        Z("configversion", "v0.0.0.1"),
        CALLHOME("callHome", true),
        ONLY_SECURE_OFFLINE("onlySecureOffline", true),
        LANGUAGEFILE("language", "lang-en.yml"),
        PERMS_HOUSE("permissions.house", "bdeilp"),
        PERMS_CITY("permissions.house", "beilp"),
        PERMS_NATION("permissions.house", "bcdeilmp"),
        PLAYERMOVE_LISTEN("playermove.listen", false),
        PLAYERMOVE_OFFSET("playermove.offset", 5) //CHAT_COLORNICK("chat.colorNick", true),
        //DAMAGE_SPAWNCAMP("damage.spawncamp", 1),
        //GENERAL_OWNER("general.owner", "server"),
        //ITEMS_EXCLUDEFROMDROPS("items.excludeFromDrops", "none", true),
        //ITEMS_TAKEOUTOFGAME("items.takeOutOfGame", "none", true),
        //LISTS_BLACKLIST("block.blacklist", new ArrayList<String>()),
        //PLAYER_EXHAUSTION("player.exhaustion", 0.0),
        //GOAL_BLOCKDESTROY_BLOCKTYPE("goal.blockdestroy.blocktype", "IRON_BLOCK", false),
        ;

        private String node;
        private final Object value;
        private final String type;

        public static CFG getByNode(final String node) {
            for (CFG m : CFG.values()) {
                if (m.getNode().equals(node)) {
                    return m;
                }
            }
            return null;
        }

        private CFG(final String node, final String value) {
            this.node = node;
            this.value = value;
            this.type = "string";
        }

        private CFG(final String node, final Boolean value) {
            this.node = node;
            this.value = value;
            this.type = "boolean";
        }

        private CFG(final String node, final Integer value) {
            this.node = node;
            this.value = value;
            this.type = "int";
        }

        private CFG(final String node, final Double value) {
            this.node = node;
            this.value = value;
            this.type = "double";
        }

        private CFG(final String node, final String value, final boolean multiple) {
            this.node = node;
            this.value = value;
            this.type = multiple ? "items" : "material";
        }

        private CFG(final String node, final List<String> value) {
            this.node = node;
            this.value = value;
            this.type = "list";
        }

        public String getNode() {
            return node;
        }

        public void setNode(final String value) {
            node = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public Object getValue() {
            return value;
        }

        public static CFG[] getValues() {
            return values();
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Create a new Config instance that uses the specified file for loading and
     * saving.
     *
     * @param configFile a YAML file
     */
    public Config(final File configFile) {
        this.cfg = new YamlConfiguration();
        this.configFile = configFile;
        this.booleans = new HashMap<String, Boolean>();
        this.ints = new HashMap<String, Integer>();
        this.doubles = new HashMap<String, Double>();
        this.strings = new HashMap<String, String>();
        
        load();
        
        
        for (CFG cfg : CFG.values()) {
            if (this.cfg.get(cfg.node) == null) {
                createDefaults();
                break;
            }
        }
    }

    public final void createDefaults() {
        this.cfg.options().indent(4);
        this.cfg.options().copyDefaults(true);

        for (CFG cfg : CFG.values()) {
            this.cfg.addDefault(cfg.getNode(), cfg.getValue());
        }
        save();
    }

    /**
     * Load the config-file into the YamlConfiguration, and then populate the
     * value maps.
     *
     * @return true, if the load succeeded, false otherwise.
     */
    public boolean load() {
        try {
            cfg.load(configFile);
            reloadMaps();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Iterates through all keys in the config-file, and populates the value
     * maps. Boolean values are stored in the booleans-map, Strings in the
     * strings-map, etc.
     */
    public void reloadMaps() {
        for (String s : cfg.getKeys(true)) {
            final Object object = cfg.get(s);

            if (object instanceof Boolean) {
                booleans.put(s, (Boolean) object);
            } else if (object instanceof Integer) {
                ints.put(s, (Integer) object);
            } else if (object instanceof Double) {
                doubles.put(s, (Double) object);
            } else if (object instanceof String) {
                strings.put(s, (String) object);
            }
        }
    }

    /**
     * Save the YamlConfiguration to the config-file.
     *
     * @return true, if the save succeeded, false otherwise.
     */
    public boolean save() {
        try {
            cfg.save(configFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete the config-file.
     *
     * @return true, if the delete succeeded, false otherwise.
     */
    public boolean delete() {
        return configFile.delete();
    }

    /**
     * Set the header of the config-file.
     *
     * @param header the header
     */
    public void setHeader(final String header) {
        cfg.options().header(header);
    }

	// /////////////////////////////////////////////////////////////////////////
    // //
    // GETTERS //
    // //
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Get the YamlConfiguration associated with this Config instance. Note that
     * changes made directly to the YamlConfiguration will cause an
     * inconsistency with the value maps unless reloadMaps() is called.
     *
     * @return the YamlConfiguration of this Config instance
     */
    public YamlConfiguration getYamlConfiguration() {
        return cfg;
    }

    /**
     * Retrieve a value from the YamlConfiguration.
     *
     * @param string the path of the value
     * @return the value of the path
     */
    public Object getUnsafe(final String string) {
        return cfg.get(string);
    }

    public boolean getBoolean(final CFG cfg) {
        return getBoolean(cfg, (Boolean) cfg.getValue());
    }

    /**
     * Retrieve a boolean from the value maps.
     *
     * @param path the path of the value
     * @param def a default value to return if the value was not in the map
     * @return the boolean value of the path if it exists, def otherwise
     */
    private boolean getBoolean(final CFG cfg, final boolean def) {
        final String path = cfg.getNode();
        final Boolean result = booleans.get(path);
        return (result == null ? def : result);
    }

    /**
     * Retrieve an int from the value maps.
     *
     * @param path the path of the value
     * @return the int value of the path if the path exists, 0 otherwise
     */
    public int getInt(final CFG cfg) {
        return getInt(cfg, (Integer) cfg.getValue());
    }

    /**
     * Retrieve an int from the value maps.
     *
     * @return the int value of the path if it exists, def otherwise
     */
    public int getInt(final CFG cfg, final int def) {
        final String path = cfg.getNode();
        final Integer result = ints.get(path);
        return (result == null ? def : result);
    }

    /**
     * Retrieve a double from the value maps.
     *
     * @return the double value of the path if the path exists, 0D otherwise
     */
    public double getDouble(CFG cfg) {
        return getDouble(cfg, (Double) cfg.getValue());
    }

    /**
     * Retrieve a double from the value maps.
     *
     * @return the double value of the path if it exists, def otherwise
     */
    public double getDouble(CFG cfg, double def) {
        final String path = cfg.getNode();
        final Double result = doubles.get(path);
        return (result == null ? def : result);
    }

    public String getString(CFG cfg) {
        return getString(cfg, (String) cfg.getValue());
    }

    public String getString(CFG cfg, String def) {
        final String path = cfg.getNode();
        final String result = strings.get(path);
        return (result == null ? def : result);
    }

    public Material getMaterial(CFG cfg) {
        return getMaterial(cfg, Material.valueOf((String) cfg.getValue()));
    }

    public Material getMaterial(CFG cfg, Material def) {
        final String path = cfg.getNode();
        final String result = strings.get(path);
        if (result == null || result.equals("none")) {
            return def;
        }
        return Material.valueOf(result);
    }

    public ItemStack[] getItems(final CFG cfg) {
        return getItems(cfg, StringParser.getItemStacksFromString((String) cfg.getValue()));
    }

    public ItemStack[] getItems(final CFG cfg, final ItemStack[] def) {
        final String path = cfg.getNode();
        final String result = strings.get(path);
        if (result == null || result.equals("none")) {
            return def;
        }
        return StringParser.getItemStacksFromString(result);
    }

    public Set<String> getKeys(final String path) {
        if (cfg.get(path) == null) {
            return null;
        }

        final ConfigurationSection section = cfg.getConfigurationSection(path);
        return section.getKeys(false);
    }

    public List<String> getStringList(final String path, final List<String> def) {
        if (cfg.get(path) == null) {
            return def == null ? new LinkedList<String>() : def;
        }

        return cfg.getStringList(path);
    }

	// /////////////////////////////////////////////////////////////////////////
    // //
    // MUTATORS //
    // //
    // /////////////////////////////////////////////////////////////////////////
    public void set(final CFG cfg, final Object value) {
        this.cfg.set(cfg.getNode(), value);
    }

	// /////////////////////////////////////////////////////////////////////////
    // //
    // UTILITY METHODS //
    // //
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Parse an input string of the form "world,x,y,z[,yaw,pitch]" to create a
     * Block Location. This method will only accept strings of the specified
     * form.
     *
     * @param coords a string of the form "world,x,y,z[,yaw,pitch]"
     * @return a CBlockLocation in the given world with the given coordinates
     */
    public static IBlockLocation parseBlockLocation(String coords) {
        String[] parts = coords.split(",");
        if (parts.length != 4 && parts.length != 6) {
            throw new IllegalArgumentException(
                    "Input string must contain world, x, y, and z: " + coords);
        }

        final Integer x = parseInteger(parts[1]);
        final Integer y = parseInteger(parts[2]);
        final Integer z = parseInteger(parts[3]);

        if (Bukkit.getWorld(parts[0]) == null || x == null || y == null
                || z == null) {
            throw new IllegalArgumentException(
                    "Some of the parsed values are null: " + coords);
        }

        return new CBlockLocation(parts[0], x, y, z);
    }

    /**
     * Parse an input string of the form "world,x,y,z,yaw,pitch" to create a
     * Block Location. This method will only accept strings of the specified
     * form.
     *
     * @param coords a string of the form "world,x,y,z,yaw,pitch"
     * @return a CLocation in the given world with the given coordinates
     */
    public static ILocation parseLocation(final String coords) {
        String[] parts = coords.split(",");

        if (parts.length == 4) {
            parts = (coords + ",0.0,0.0").split(",");
        }

        if (parts.length != 6) {
            throw new IllegalArgumentException(
                    "Input string must contain world, x, y, z, yaw and pitch: "
                    + coords);
        }

        final Integer x = parseInteger(parts[1]);
        final Integer y = parseInteger(parts[2]);
        final Integer z = parseInteger(parts[3]);
        final Float yaw = parseFloat(parts[4]);
        final Float pitch = parseFloat(parts[5]);

        if (Bukkit.getWorld(parts[0]) == null || x == null || y == null
                || z == null || yaw == null || pitch == null) {
            throw new IllegalArgumentException(
                    "Some of the parsed values are null: " + coords);
        }
        return new CLocation(parts[0], x, y, z, pitch, yaw);
    }

    public static Integer parseInteger(final String string) {
        try {
            return Integer.parseInt(string.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static Float parseFloat(final String string) {
        try {
            return Float.parseFloat(string.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseToString(final ILocation loc) {
        final String[] result = new String[6];
        result[0] = String.valueOf(loc.getWorldName());
        result[1] = String.valueOf(loc.getBlockX());
        result[2] = String.valueOf(loc.getBlockY());
        result[3] = String.valueOf(loc.getBlockZ());
        result[4] = String.valueOf(loc.getYaw());
        result[5] = String.valueOf(loc.getPitch());
        // "world,x,y,z,yaw,pitch"
        return StringParser.joinArray(result, ",");
    }

    public static String parseToString(final IBlockLocation loc) {
        final String[] result = new String[4];
        result[0] = String.valueOf(loc.getWorldName());
        result[1] = String.valueOf(loc.getX());
        result[2] = String.valueOf(loc.getY());
        result[3] = String.valueOf(loc.getZ());
        // "world,x,y,z"
        return StringParser.joinArray(result, ",");
    }

}
