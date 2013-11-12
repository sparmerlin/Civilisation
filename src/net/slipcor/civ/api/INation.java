package net.slipcor.civ.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The highest level of civilisation
 * several cities form a nation
 * 
 * @author slipcor
 */
public interface INation {
    String getName();
    ICity getCapital();
    List<ICity> getCities();
	void dislike(INation nation);
	void like(INation nation);
	Map<String, IPermMap> getPerms();
	Set<INation> getDisliked();
	Set<INation> getLiked();
	void setPerms(String name, IPermMap permMap);
	void save();
	double getMoney();
	void addMoney(double amount);
	void removeMoney(double amount);
	void setCapital(ICity newCapital);
	void setTax(double amount);
	double getTax();
}
