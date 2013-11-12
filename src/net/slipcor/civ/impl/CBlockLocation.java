package net.slipcor.civ.impl;

import net.slipcor.civ.api.IBlockLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CBlockLocation implements IBlockLocation {
	private final String world;
	private int x;
	private int y;
	private int z;

	public CBlockLocation(final String world, final int x, final int y, final int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public CBlockLocation(final Location bukkitLocation) {
		this.world = bukkitLocation.getWorld().getName();
		this.x = bukkitLocation.getBlockX();
		this.y = bukkitLocation.getBlockY();
		this.z = bukkitLocation.getBlockZ();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + y;
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
		final CBlockLocation other = (CBlockLocation) obj;
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
		if (y != other.y) {
			return false;
                }
		if (z != other.z) {
			return false;
                }
		return true;
	}

        @Override
	public double getDistance(final IBlockLocation otherLocation) {
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
				+ Math.pow(this.y - otherLocation.getY(), 2.0D) + Math.pow(this.z - otherLocation.getZ(), 2.0D));
	}

        @Override
	public double getDistanceSquared(final IBlockLocation otherLocation) {
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
				+ Math.pow(this.y - otherLocation.getY(), 2.0D) + Math.pow(this.z - otherLocation.getZ(), 2.0D);
	}

        @Override
	public IBlockLocation getMidpoint(final IBlockLocation location) {
		return new CBlockLocation(world, (x + location.getX()) / 2, (y + location.getY()) / 2,
				(z + location.getZ()) / 2);
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
	public int getY() {
		return y;
	}

        @Override
	public int getZ() {
		return z;
	}

        @Override
	public boolean isInAABB(final IBlockLocation min, final IBlockLocation max) {
		if (this.getX() < min.getX() || this.getX() > max.getX()) {
			return false;
		}
		if (this.getY() < min.getY() || this.getY() > max.getY()) {
			return false;
		}
		if (this.getZ() < min.getZ() || this.getZ() > max.getZ()) {
			return false;
		}
		return true;
	}

        @Override
	public void setX(final int value) {
		x = value;
	}

        @Override
	public void setY(final int value) {
		y = value;
	}

        @Override
	public void setZ(final int value) {
		z = value;
	}

        @Override
	public Location toLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z);
	}

	@Override
	public String toString() {
		return world + ":" + x + "," + y + "," + z;
	}
}
