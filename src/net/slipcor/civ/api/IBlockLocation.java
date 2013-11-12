package net.slipcor.civ.api;

import org.bukkit.Location;

/**
 *
 * @author slipcor
 */
public interface IBlockLocation {
    double getDistance(final IBlockLocation otherLocation);
    double getDistanceSquared(final IBlockLocation otherLocation);
    IBlockLocation getMidpoint(final IBlockLocation location);
    String getWorldName();
    int getX();
    int getY();
    int getZ();
    boolean isInAABB(final IBlockLocation min, final IBlockLocation max);
    void setX(final int value);
    void setY(final int value);
    void setZ(final int value);
    Location toLocation();
}
