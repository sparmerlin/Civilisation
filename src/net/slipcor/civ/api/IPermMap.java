package net.slipcor.civ.api;

/**
 * The Permission Map
 * 
 * it contains the permissions of a special entity
 * 
 * @author slipcor
 */
public interface IPermMap {
    
    String printColorized();

    void add(final Permission perm);

    void remove(final Permission perm);

    boolean has(Permission perm);
    
}
