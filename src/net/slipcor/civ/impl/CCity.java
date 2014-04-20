package net.slipcor.civ.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.api.ILocation;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.api.IPermMap;
import net.slipcor.civ.api.Permission;
import net.slipcor.civ.core.Config;
import net.slipcor.civ.core.StringParser;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author slipcor
 */
public class CCity implements ICity {

    private final Map<ICity, Boolean> likeStatus = new HashMap<ICity, Boolean>();
    private final Map<String, IPermMap> permissions = new HashMap<String, IPermMap>();
    private final List<IHouse> houses = new ArrayList<IHouse>();
    private final List<IChunk> outposts = new ArrayList<IChunk>();
    private String name;
    private IHouse owner;
    private double money = 0;
    private double tax = 0;
    private ILocation spawn;

    public CCity(final String name, final IHouse owner) {
        this.name = name;
        this.owner = owner;
    }

    @Override
    public void dislike(final ICity city) {
        likeStatus.put(city, false);
    }

    @Override
    public void like(final ICity city) {
        likeStatus.put(city, true);
    }

    @Override
    public List<IHouse> getHouses() {
        return houses;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<IChunk> getOutposts() {
        return outposts;
    }

    @Override
    public IHouse getOwner() {
        return owner;
    }

    @Override
    public Map<String, IPermMap> getPerms() {
        return permissions;
    }

    @Override
    public Set<ICity> getDisliked() {
        final Set<ICity> result = new HashSet<ICity>();
        for (ICity city : likeStatus.keySet()) {
            if (!likeStatus.get(city)) {
                result.add(city);
            }
        }
        return result;
    }

    @Override
    public Set<ICity> getLiked() {
        final Set<ICity> result = new HashSet<ICity>();
        for (ICity city : likeStatus.keySet()) {
            if (likeStatus.get(city)) {
                result.add(city);
            }
        }
        return result;
    }

    @Override
    public boolean prevents(final CLocation location, final Player player,
            final Permission perm) {

        if (player != null && this.getOwner().getName().equals(player.getName())) {
            System.out.print("1 false");
            return false;
        }

        final IChunk chunk = new CChunk(location.getWorldName(), location.getBlockX() >> 4, location.getBlockZ() >> 4);

        if (this.getOutposts().contains(chunk)) {

            if (Civilisation.getPlugin().config().getBoolean(Config.CFG.ONLY_SECURE_OFFLINE)
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

            final ICity otherHouse = Civilisation.getPlugin().getCity(player);

            if (otherHouse == null) {
                // not in a city, same as "everyone", deny!
                return true;
            }

            if (this.permissions.containsKey("liked")
                    && this.permissions.get("liked").has(perm)) {
                if (this.getLiked().contains(otherHouse)) {
                    System.out.print("6 false");
                    return false;
                }
            }

            for (INation city : Civilisation.getPlugin().getNations()) {
                if (city.getCities().contains(this)) {
                    if (city.getCities().contains(otherHouse)) {
                        if (this.permissions.containsKey("nation")) {
                            return !this.permissions.get("nation").has(perm);
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
    public void setPerms(final String name, final IPermMap permMap) {
        permissions.put(name, permMap);
    }

    @Override
    public int getMaxOutposts() {
        return getHouses().size();
    }

    @Override
    public void save() {
        Civilisation.getPlugin().getDataManager().save(this);
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void addMoney(double amount) {
        money += amount;
    }

    @Override
    public void removeMoney(double amount) {
        money -= amount;
    }

    @Override
    public void setOwner(IHouse newOwner) {
        owner = newOwner;
    }

    @Override
    public void setTax(double amount) {
        tax = amount;
    }

    @Override
    public double getTax() {
        return tax;
    }

    @Override
    public String[] debug() {
        String[] output = new String[9];
        output[0] = "City " + name;
        output[1] = "--------------";
        output[2] = "liking: ";
        for (ICity city : likeStatus.keySet()) {
            output[2] += (likeStatus.get(city) ? ChatColor.GREEN : ChatColor.RED);
            output[2] += ChatColor.RESET + ", ";
        }
        output[3] = "perms: ";
        for (String key : permissions.keySet()) {
            output[3] += permissions.get(key).printColorized();
            output[3] += ChatColor.RESET + ", ";
        }
        output[4] = "houses: " + StringParser.joinArray(houses.toArray(), ", ");
        output[5] = "outposts: " + outposts.size();
        output[6] = "owner: " + owner.getName();
        output[7] = "money: " + money;
        output[8] = "tax: " + tax;
        return output;
    }
    
    @Override
    public ILocation getSpawn() {
        return spawn;
    }
    
    @Override
    public void setSpawn(ILocation location) {
        spawn = location;
    }
}
