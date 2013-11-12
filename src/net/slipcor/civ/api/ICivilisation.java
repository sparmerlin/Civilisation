package net.slipcor.civ.api;

import java.util.Set;
import net.slipcor.civ.core.Config;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * The Plugin class
 * 
 * @author slipcor
 */
public interface ICivilisation extends Plugin {
    
    /**
     * Get the Config instance
     * 
     * @return the instance
     */
    Config config();
    void addHouse(IHouse house);
	void addCity(ICity city);
    IDataManager getDataManager();
    IHouse getHouse(String name);
    IHouse getHouse(String name, boolean create);
    ICity getCity(String name);
    ICity getCity(Player member);
    Set<IHouse> getHouses();
    Set<ICity> getCities();
    Set<INation> getNations();
	INation getNation(String nation);
}
