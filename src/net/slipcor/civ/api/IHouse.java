package net.slipcor.civ.api;

import java.util.Map;
import java.util.Set;
import net.slipcor.civ.impl.CLocation;
import org.bukkit.entity.Player;

/**
 * A house is the lowest form of civilisation
 * it is strictly bound to a player
 * 
 * @author Chris
 */
public interface IHouse {
    int getBonus();
    Set<IChunk> getClaimed();
    Set<IHouse> getDisliked();
    Set<IHouse> getLiked();
    String getName();
    ILocation getSpawn();

    void dislike(final IHouse house);
    void like(final IHouse house);

    Map<String, IPermMap> getPerms();
    void addPerm(final String name, final Permission perm);
    void removePerm(final String name, final Permission perm);
    void setPerms(final String name, final IPermMap permMap);
    void setSpawn(ILocation location);
    
    void save();

    void setBonus(final int aInt);

    boolean prevents(final CLocation location, final Player player,
        final Permission perm);
	String[] debug();
}
