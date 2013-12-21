package net.slipcor.civ;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.ICivilisation;
import net.slipcor.civ.api.IDataManager;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.command.CmdCity;
import net.slipcor.civ.command.CmdHouse;
import net.slipcor.civ.command.CmdNation;
import net.slipcor.civ.command.CmdReload;
import net.slipcor.civ.core.CivListener;
import net.slipcor.civ.core.Config;
import net.slipcor.civ.core.Config.CFG;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.core.Tracker;
import net.slipcor.civ.impl.CDataManager;
import net.slipcor.civ.impl.CHouse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Plugin implementation class
 * 
 * Contains the actual functionality
 * 
 * @author slipcor
 */
public class Civilisation extends JavaPlugin implements ICivilisation {
    
    protected Config cfg = null;
    private static final int CFGVERSION = 3;
    private final Set<IHouse> houses = new HashSet<IHouse>();
    private final Set<ICity> cities = new HashSet<ICity>();
    private final Set<INation> nations = new HashSet<INation>();
    
    private static ICivilisation plugin;
    private IDataManager dataManager;
    
    @Override
    public void addHouse(final IHouse house) {
        houses.add(house);
    }
    
    @Override
    public void addCity(final ICity city) {
        cities.add(city);
    }
    
    /**
     * Return the Config instance
     * 
     * If necessary, create one
     * 
     * @return the instance
     */
    @Override
    public Config config() {
        if (this.cfg == null) {
        try
        {
          this.cfg = newConfigInstance();
          getLogger().info("Loaded config file!");
        } catch (IOException e) {
          getLogger().severe("Error loading config file!");
          getLogger().severe(e.getMessage());
          Bukkit.getPluginManager().disablePlugin(this);
          return null;
        }
      }
      return this.cfg;
    }
    
    public static ICivilisation getPlugin() {
        return plugin;
    }
    
    /**
     * Create and return a new Config instance
     * @return the instnace
     * @throws IOException 
     */
    private Config newConfigInstance() throws IOException {
        
        if (getConfig().getInt("ver", 0) < CFGVERSION) {
            /*
            getConfig().options().copyDefaults(true);
            getConfig().set("ver", CFGVERSION);
            saveConfig();*/
        }

        this.reloadConfig();
        return new Config(new File(this.getDataFolder(), "config.yml"));
    }
    
    /**
     * When disabling the plugin, stop the Tracker thread
     */
    @Override
    public void onDisable() {
        Tracker.stop();
    }
    
    /**
     * When enabling the plugin, initiate the Language, register
     * the events, commands and start the Tracker
     */
    @Override
    public void onEnable() {
        plugin = this;

        final String fileName = config().getString(CFG.LANGUAGEFILE);
        try {
            Language.init(this, fileName);
            getLogger().info(Language.parse(Language.LANGUAGEFILELOADED, fileName));
        } catch (IOException e) {
            getLogger().severe(Language.parse(Language.LANGUAGEFILELOADERROR, fileName));
            this.onDisable();
            return;
        }
        
        final CivListener listener = new CivListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
        registerCommands();
        
        final Tracker trackMe = new Tracker(this);
        trackMe.start();
        
        dataManager = new CDataManager(this);
        dataManager.load();
    }
    
    /**
     * Register the ICivilisation commands
     */
    private void registerCommands() {
        getCommand("creload").setExecutor(new CmdReload(this));
        getCommand("house").setExecutor(new CmdHouse(this));
        getCommand("city").setExecutor(new CmdCity(this));
        getCommand("nation").setExecutor(new CmdNation(this));
    }
    
    /**
     * Reload the configuration
     */
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        if (cfg != null) {
            cfg.reloadMaps();
        }
    }

    @Override
    public IHouse getHouse(final String name, final boolean create) {
        for (IHouse house : houses) {
            if (house.getName().equals(name)) {
                return house;
            }
        }
        if (create) {
            final IHouse house = new CHouse(name);
            addHouse(house);
            this.getDataManager().save(house);
            return house;
        }
        return null;
    }
    
    @Override
    public IHouse getHouse(final String name) {
        return getHouse(name, false);
    }

    @Override
    public Set<INation> getNations() {
        return nations;
    }

    @Override
    public Set<ICity> getCities() {
        return cities;
    }

    @Override
    public Set<IHouse> getHouses() {
        return houses;
    }

    @Override
    public IDataManager getDataManager() {
        return dataManager;
    }

    @Override
    public ICity getCity(final String name) {
        for (ICity city : cities) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null;
    }

    @Override
    public ICity getCity(final Player member) {
        for (ICity city : cities) {
        	if (city.getOwner().getName().equals(member.getName())) {
        		return city;
        	}
            for (IHouse house : city.getHouses()) {
                if (house.getName().equals(member.getName())) {
                    return city;
                }
            }
        }
        return null;
    }

    @Override
	public INation getNation(final String name) {
		for (INation nation : nations) {
			if (nation.getName().equals(name)) {
				return nation;
			}
		}
		return null;
	}

	public INation getNation(final ICity city) {
		for (INation nation : nations) {
			if (nation.getCapital().equals(city) || nation.getCities().contains(city)) {
				return nation;
			}
		}
		return null;
	}

	@Override
	public void addNation(INation nation) {
		this.nations.add(nation);
	}
}
