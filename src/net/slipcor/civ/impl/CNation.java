package net.slipcor.civ.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.api.IPermMap;
import net.slipcor.civ.core.StringParser;

/**
 *
 * @author slipcor
 */
public class CNation implements INation {
    private final Map<INation, Boolean> likeStatus = new HashMap<INation, Boolean>();
    private final Map<String, IPermMap> permissions = new HashMap<String, IPermMap>();
    private final List<ICity> cities = new ArrayList<ICity>();
    private String name;
    private ICity capital;
    private double money = 0;
    private double tax = 0;


    public CNation(final String name, final ICity capital) {
		this.name = name;
		this.capital = capital;
	}
    
	@Override
    public void dislike(final INation nation) {
        likeStatus.put(nation, false);
    }

    @Override
    public void like(final INation nation) {
        likeStatus.put(nation, true);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public ICity getCapital() {
        return capital;
    }

    @Override
    public List<ICity> getCities() {
        return cities;
    }

    @Override
    public Map<String, IPermMap> getPerms() {
        return permissions;
    }

    @Override
    public Set<INation> getDisliked() {
        final Set<INation> result = new HashSet<INation>();
        for (INation nation : likeStatus.keySet()) {
            if (!likeStatus.get(nation)) {
                result.add(nation);
            }
        }
        return result;
    }

    @Override
    public Set<INation> getLiked() {
        final Set<INation> result = new HashSet<INation>();
        for (INation nation : likeStatus.keySet()) {
            if (likeStatus.get(nation)) {
                result.add(nation);
            }
        }
        return result;
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
	public void setCapital(ICity newCapital) {
		capital = newCapital;
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
		String[] output = new String[8];
		output[0] = "Nation " + name;
		output[1] = "--------------";
		output[2] = "liking: ";
		for (INation city : likeStatus.keySet()) {
			output[2] += (likeStatus.get(city)?ChatColor.GREEN:ChatColor.RED);
			output[2] += ChatColor.RESET+", ";
		}
		output[3] = "perms: ";
		for (String key : permissions.keySet()) {
			output[3] += permissions.get(key).printColorized();
			output[3] += ChatColor.RESET+", ";
		}
		output[4] = "cities: " + StringParser.joinArray(cities.toArray(), ", ");
		output[5] = "capital: " + capital.getName();
		output[6] = "money: " + money;
		output[7] = "tax: " + tax;
		return output;
	}

}
