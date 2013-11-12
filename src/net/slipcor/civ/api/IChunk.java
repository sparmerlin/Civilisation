package net.slipcor.civ.api;

import org.bukkit.Chunk;

/**
 *
 * @author slipcor
 */
public interface IChunk {
    double getDistance(final IChunk otherLocation);
    double getDistanceSquared(final IChunk otherLocation);
    String getWorldName();
    int getX();
    int getZ();
    Chunk toChunk();
}
