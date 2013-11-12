package net.slipcor.civ.api;

import org.bukkit.Location;

/**
 *
 * @author slipcor
 */
public interface ILocation {
    double getDistance(final ILocation otherLocation);
    double getDistanceSquared(final ILocation otherLocation);
    String getWorldName();
    double getX();
    double getY();
    double getZ();
    int getBlockX();
    int getBlockY();
    int getBlockZ();
    float getPitch();
    float getYaw();
    void setPitch(final float value);
    void setX(final double value);
    void setY(final double value);
    void setYaw(final float value);
    void setZ(final double value);
    Location toLocation();
}
