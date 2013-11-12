package net.slipcor.civ.api;

/**
 *
 * @author slipcor
 */
public interface IDataManager {
    void load();
    
    void save(IHouse house);
    void save(ICity city);
    void save(INation nation);
    
    void save();
}
