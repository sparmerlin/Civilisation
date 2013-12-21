package net.slipcor.civ.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.api.IPermMap;
import net.slipcor.civ.api.Permission;
import net.slipcor.civ.core.Config.CFG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author slipcor
 */
public class CHouse implements IHouse {
    private final String name;
    private final Map<IHouse, Boolean> likeStatus = new HashMap<IHouse, Boolean>();
    private final Map<String, IPermMap> permissions = new HashMap<String, IPermMap>();
    private final Set<IChunk> claimed = new HashSet<IChunk>();
    private int bonus;

    public CHouse(final String name) {
        this.name = name;
    }

    @Override
    public int getBonus() {
        return bonus;
    }

    @Override
    public Set<IChunk> getClaimed() {
        return claimed;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<IHouse> getDisliked() {
        final Set<IHouse> result = new HashSet<IHouse>();
        for (IHouse house : likeStatus.keySet()) {
            if (!likeStatus.get(house)) {
                result.add(house);
            }
        }
        return result;
    }

    @Override
    public Set<IHouse> getLiked() {
        final Set<IHouse> result = new HashSet<IHouse>();
        for (IHouse house : likeStatus.keySet()) {
            if (likeStatus.get(house)) {
                result.add(house);
            }
        }
        return result;
    }

    @Override
    public void dislike(final IHouse house) {
        likeStatus.put(house, false);
    }

    @Override
    public void like(final IHouse house) {
        likeStatus.put(house, true);
    }

    @Override
    public Map<String, IPermMap> getPerms() {
        return permissions;
    }

    @Override
    public void addPerm(final String name, final Permission perm) {
        permissions.get(name).add(perm);
    }

    @Override
    public void removePerm(final String name, final Permission perm) {
        permissions.get(name).remove(perm);
    }

    @Override
    public void setPerms(final String name, final IPermMap permMap) {
        permissions.put(name, permMap);
    }

    @Override
    public void save() {
        Civilisation.getPlugin().getDataManager().save(this);
    }

    @Override
    public void setBonus(final int value) {
        bonus = value;
    }

    @Override
    public boolean prevents(final CLocation location, final Player player,
        final Permission perm) {
        
        if (player != null && this.getName().equals(player.getName())) {
            System.out.print("1 false");
            return false;
        }
        
        final IChunk chunk = new CChunk(location.getWorldName(), location.getBlockX()>>4, location.getBlockZ()>>4);
        
        if (this.getClaimed().contains(chunk)) {
            
            if (Civilisation.getPlugin().config().getBoolean(CFG.ONLY_SECURE_OFFLINE)
                    && Bukkit.getPlayer(this.getName()) != null) {
                System.out.print("2 false");
                return false;
            }
            
            if (this.permissions.containsKey("everyone")
                    && this.permissions.get("everyone").has(perm)) {
            System.out.print("3 false");
                return false;
            }
            
            if (player == null) {
            System.out.print("4 true");
                return true; // only "everyone" would allow this
            }
            
            if (this.permissions.containsKey(player.getName())
                    && this.permissions.get(player.getName()).has(perm)) {
            System.out.print("5 false");
                return false;
            }
            
            final IHouse otherHouse = Civilisation.getPlugin().getHouse(player.getName(), true);
            
            if (this.permissions.containsKey("liked")
                    && this.permissions.get("liked").has(perm)) {
                if (this.getLiked().contains(otherHouse)) {
                    System.out.print("6 false");
                    return false;
                }
            }
            
            for (ICity city : Civilisation.getPlugin().getCities()) {
                if (city.getHouses().contains(this)) {
                    if (city.getHouses().contains(otherHouse)) {
                        if (this.permissions.containsKey("city")) {
                            return !this.permissions.get("city").has(perm);
                        }
                    } else {
            System.out.print("7 true");
                        return true;
                    }
                }
            }
            
            System.out.print("8 true");
            return true;
        }
            System.out.print("9 false");
        return false;
    }

	@Override
	public String[] debug() {
		String[] output = new String[6];
		output[0] = "House of " + name;
		output[1] = "--------------";
		output[2] = "liking: ";
		for (IHouse house : likeStatus.keySet()) {
			output[2] += (likeStatus.get(house)?ChatColor.GREEN:ChatColor.RED);
			output[2] += ChatColor.RESET+", ";
		}
		output[3] = "perms: ";
		for (String key : permissions.keySet()) {
			output[3] += permissions.get(key).printColorized();
			output[3] += ChatColor.RESET+", ";
		}
		output[4] = "claimed: " + claimed.size();
		output[5] = "bonus: " + bonus;
		return output;
	}
}
