package net.slipcor.civ.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.ICivilisation;
import net.slipcor.civ.api.IDataManager;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.api.INation;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author slipcor
 */
public class CDataManager implements IDataManager {
    private final ICivilisation plugin;
    private File root = null;
    private File houseRoot = null;
    private File cityRoot = null;
    private File nationRoot = null;

    public CDataManager(final ICivilisation plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        root = new File(plugin.getDataFolder(), "Data");
        if (!root.exists()) {
            root.mkdir();
        }
        houseRoot = new File(root, "Houses");
        if (houseRoot.exists()) {
            for (File f : houseRoot.listFiles(new FileExtensionFilter(".yml"))) {
                final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                final IHouse house = plugin.getHouse(f.getName().replace(".yml", ""), true);
                plugin.addHouse(house);
                
                for (String like : cfg.getStringList("likes")) {
                    house.like(plugin.getHouse(like, true));
                }
                
                for (String dislike : cfg.getStringList("dislikes")) {
                    house.dislike(plugin.getHouse(dislike, true));
                }
                
                if (cfg.get("perms") != null) {
                    for (String key : cfg.getConfigurationSection("perms").getKeys(false)) {
                        house.setPerms(key, new CPermMap(cfg.getString("perms."+key, "")));
                    }
                }
                
                if (cfg.get("spawn") != null) {
                    house.setSpawn(new CLocation(cfg.getString("spawn")));
                }
                
                house.setBonus(cfg.getInt("bonus"));
                
                for (String chunk : cfg.getStringList("claimed")) {
                    house.getClaimed().add(new CChunk(chunk));
                }
                plugin.getLogger().info("House loaded: " + house.getName() + " (" + house.getClaimed().size() + " Chunks)");
            }
            plugin.getLogger().info("Houses loaded: " + plugin.getHouses().size());
        } else {
            houseRoot.mkdir();
            plugin.getLogger().info("House root created!");
        }
        
        cityRoot = new File(root, "Cities");
        if (cityRoot.exists()) {
        	for (File f : cityRoot.listFiles(new FileExtensionFilter(".yml"))) {
                final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                
                final String name = f.getName().replace(".yml", "");
                
                final ICity city = new CCity(name, plugin.getHouse(cfg.getString("owner"), true));
                plugin.addCity(city);
                new CCityBuffer(city, cfg);
                
                if (cfg.get("perms") != null) {
                    for (String key : cfg.getConfigurationSection("perms").getKeys(false)) {
                    	city.setPerms(key, new CPermMap(cfg.getString("perms."+key, "")));
                    }
                }
                
                for (String chunk : cfg.getStringList("outposts")) {
                    city.getOutposts().add(new CChunk(chunk));
                }
                
                for (String house : cfg.getStringList("houses")) {
                	city.getHouses().add(plugin.getHouse(house, true));
                }
                
                if (cfg.get("spawn") != null) {
                    city.setSpawn(new CLocation(cfg.getString("spawn")));
                }

                city.addMoney(cfg.getInt("money")/100);
                city.setTax(cfg.getInt("tax")/100);
                plugin.getLogger().info("City loaded: " + city.getName());
            }
            plugin.getLogger().info("Cities loaded: " + plugin.getCities().size());
        	CCityBuffer.commitAll(plugin);
        } else {
            cityRoot.mkdir();
        }
        
        
        
        nationRoot = new File(root, "Nations");
        if (nationRoot.exists()) {
        	for (File f : nationRoot.listFiles(new FileExtensionFilter(".yml"))) {
                final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                
                final String name = f.getName().replace(".yml", "");
                
                final INation nation = new CNation(name, plugin.getCity(cfg.getString("capital")));
                plugin.addNation(nation);
                new CNationBuffer(nation, cfg);
                
                if (cfg.get("perms") != null) {
                    for (String key : cfg.getConfigurationSection("perms").getKeys(false)) {
                    	nation.setPerms(key, new CPermMap(cfg.getString("perms."+key, "")));
                    }
                }
                
                for (String city : cfg.getStringList("cities")) {
                	nation.getCities().add(plugin.getCity(city));
                }

                nation.addMoney(cfg.getInt("money")/100);
                nation.setTax(cfg.getInt("tax")/100);
                plugin.getLogger().info("Nation loaded: " + nation.getName());
            }
        	CNationBuffer.commitAll(plugin);
            plugin.getLogger().info("Nations loaded: " + plugin.getNations().size());
        } else {
            nationRoot.mkdir();
        }
    }

    @Override
    public void save(final IHouse house) {
        final YamlConfiguration cfg = new YamlConfiguration();
        
        final List<String> likelist = new ArrayList<String>();
        
        for (IHouse entry : house.getLiked()) {
            likelist.add(entry.getName());
        }
        
        cfg.set("likes", likelist);
        final List<String> dislikelist = new ArrayList<String>();
        
        for (IHouse entry : house.getDisliked()) {
            dislikelist.add(entry.getName());
        }
        
        cfg.set("dislikes", dislikelist);
        final List<String> claimedlist = new ArrayList<String>();
        
        for (IChunk entry : house.getClaimed()) {
            plugin.getLogger().info("chunk " + entry.toString());
            claimedlist.add(entry.toString());
        }
        
        cfg.set("claimed", claimedlist);
        cfg.set("bonus", house.getBonus());
        
        if (house.getSpawn() != null) {
            cfg.set("spawn", house.getSpawn().toString());
        }
        
        for (String entry : house.getPerms().keySet()) {
            cfg.set(("perms."+entry), house.getPerms().get(entry).toString());
        }
        
        
        try {
            final File destination = new File(houseRoot, house.getName() + ".yml");
            if (!destination.exists()) {
                destination.createNewFile();
            }
            cfg.save(destination);
        } catch (IOException ex) {
            Logger.getLogger(CDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save(ICity city) {
    	final YamlConfiguration cfg = new YamlConfiguration();
        
        final List<String> likelist = new ArrayList<String>();
        
        for (ICity entry : city.getLiked()) {
            likelist.add(entry.getName());
        }
        
        cfg.set("likes", likelist);
        final List<String> dislikelist = new ArrayList<String>();
        
        for (ICity entry : city.getDisliked()) {
            dislikelist.add(entry.getName());
        }
        
        cfg.set("dislikes", dislikelist);
        final List<String> claimedlist = new ArrayList<String>();
        
        for (IChunk entry : city.getOutposts()) {
            claimedlist.add(entry.toString());
        }
        
        cfg.set("outposts", claimedlist);
        
        final List<String> houses = new ArrayList<String>();
        
        for (IHouse entry : city.getHouses()) {
        	houses.add(entry.getName());
        }
        
        cfg.set("houses", houses);
        
        for (String entry : city.getPerms().keySet()) {
            cfg.set(("perms."+entry), city.getPerms().get(entry).toString());
        }
        
        cfg.set("owner", city.getOwner().getName());
        cfg.set("money", (int)(city.getMoney()*100));
        cfg.set("tax", (int)(city.getTax()*100));
        
        if (city.getSpawn() != null) {
            cfg.set("spawn", city.getSpawn().toString());
        }
        
        try {
            final File destination = new File(cityRoot, city.getName() + ".yml");
            if (!destination.exists()) {
                destination.createNewFile();
            }
            cfg.save(destination);
        } catch (IOException ex) {
            Logger.getLogger(CDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save(INation nation) {
    	final YamlConfiguration cfg = new YamlConfiguration();
        
        final List<String> likelist = new ArrayList<String>();
        
        for (INation entry : nation.getLiked()) {
            likelist.add(entry.getName());
        }
        
        cfg.set("likes", likelist);
        final List<String> dislikelist = new ArrayList<String>();
        
        for (INation entry : nation.getDisliked()) {
            dislikelist.add(entry.getName());
        }
        
        cfg.set("dislikes", dislikelist);
        
        final List<String> cities = new ArrayList<String>();
        
        for (ICity entry : nation.getCities()) {
        	cities.add(entry.getName());
        }
        
        cfg.set("cities", cities);
        
        for (String entry : nation.getPerms().keySet()) {
            cfg.set(("perms."+entry), nation.getPerms().get(entry).toString());
        }
        
        cfg.set("capital", nation.getCapital().getName());
        cfg.set("money", (int)(nation.getMoney()*100));
        cfg.set("tax", (int)(nation.getTax()*100));
        
        
        try {
            final File destination = new File(nationRoot, nation.getName() + ".yml");
            if (!destination.exists()) {
                destination.createNewFile();
            }
            cfg.save(destination);
        } catch (IOException ex) {
            Logger.getLogger(CDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save() {
        for (IHouse house : plugin.getHouses()) {
            save(house);
        }
        
        for (ICity city : plugin.getCities()) {
            save(city);
        }
        
        for (INation nation : plugin.getNations()) {
            save(nation);
        }
    }
    
    final class FileExtensionFilter implements FileFilter {
	
	private final String extension;
	
	public FileExtensionFilter(final String extension) {
            this.extension = extension;
	}
	
	@Override
	public boolean accept(final File file) {
            return file.getName().endsWith(extension);
	}
    }
}
