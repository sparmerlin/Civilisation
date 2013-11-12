package net.slipcor.civ.impl;

import net.slipcor.civ.api.IChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class CChunk implements IChunk {
	private final String world;
	private int x;
	private int z;
        
        public CChunk(final String def) {
            final String[] split = def.split(":");
            final String[] coords = split[1].split("/");
            
            this.world = split[0];
            this.x = Integer.parseInt(coords[0]);
            this.z = Integer.parseInt(coords[1]);
        }

	public CChunk(final String world, final int x, final int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}

	public CChunk(final Chunk bukkitChunk) {
		this.world = bukkitChunk.getWorld().getName();
		this.x = bukkitChunk.getX();
		this.z = bukkitChunk.getZ();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
                }
		if (obj == null) {
			return false;
                }
		if (getClass() != obj.getClass()) {
			return false;
                }
		final CChunk other = (CChunk) obj;
		if (world == null) {
			if (other.world != null) {
				return false;
                        }
		} else if (!world.equals(other.world)) {
			return false;
                }
		if (x != other.x) {
			return false;
                }
		if (z != other.z) {
			return false;
                }
		return true;
	}

        @Override
	public double getDistance(final IChunk otherLocation) {
		if (otherLocation == null) {
			throw new IllegalArgumentException(
					"Cannot measure distance to a null location");
		}
		if (!otherLocation.getWorldName().equals(world)) {
			throw new IllegalArgumentException(
					"Cannot measure distance between " + world + " and "
							+ otherLocation.getWorldName());
		}

		return Math.sqrt(Math.pow(this.x - otherLocation.getX(), 2.0D)
				+ Math.pow(this.z - otherLocation.getZ(), 2.0D));
	}

        @Override
	public double getDistanceSquared(final IChunk otherLocation) {
		if (otherLocation == null) {
			throw new IllegalArgumentException(
					"Cannot measure distance to a null location");
		}
		if (!otherLocation.getWorldName().equals(world)) {
			throw new IllegalArgumentException(
					"Cannot measure distance between " + world + " and "
							+ otherLocation.getWorldName());
		}

		return Math.pow(this.x - otherLocation.getX(), 2.0D)
				+ Math.pow(this.z - otherLocation.getZ(), 2.0D);
	}

        @Override
	public String getWorldName() {
		return world;
	}

        @Override
	public int getX() {
		return x;
	}

        @Override
	public int getZ() {
		return z;
	}

        @Override
	public Chunk toChunk() {
		return Bukkit.getWorld(world).getChunkAt(x, z);
	}

	@Override
	public String toString() {
		return world + ":" + x + "/" + z;
	}
}
