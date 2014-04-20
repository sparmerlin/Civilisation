package net.slipcor.civ.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.slipcor.civ.impl.CLocation;
import org.bukkit.entity.Player;

/**
 * A city is the second layer of civilisation,
 * several houses can form a city
 * 
 * @author slipcor
 */
public interface ICity {
    String getName();
    IHouse getOwner();
    List<IHouse> getHouses();
    List<IChunk> getOutposts();
    
    Set<ICity> getDisliked();
    Set<ICity> getLiked();

    boolean prevents(final CLocation location, final Player player,
        final Permission perm);
    void dislike(ICity city);
    void like(ICity city);
    Map<String, IPermMap> getPerms();
    void setPerms(String name, IPermMap permMap);
    int getMaxOutposts();
    ILocation getSpawn();
    void save();

    double getMoney();
    void addMoney(double amount);
    void removeMoney(double amount);
    void setOwner(IHouse newOwneWr);
    void setTax(double amount);
    void setSpawn(ILocation location);
    double getTax();
    String[] debug();
	
}
