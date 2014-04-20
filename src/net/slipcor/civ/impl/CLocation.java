package net.slipcor.civ.impl;

import net.slipcor.civ.api.ILocation;
import net.slipcor.civ.core.StringParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CLocation implements ILocation {
	private final String world;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	public CLocation(final String world, final double x, final double y, final double z, final float pitch,
			final float yaw) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public CLocation(final Location bukkitLocation) {
		world = bukkitLocation.getWorld().getName();
		x = bukkitLocation.getX();
		y = bukkitLocation.getY();
		z = bukkitLocation.getZ();
		pitch = bukkitLocation.getPitch();
		yaw = bukkitLocation.getYaw();
	}
        
        public CLocation(final String definition) {
            String[] values = definition.split("\\|");
            
            world = values[0].substring(2);
            
            x = Double.parseDouble(values[1].substring(2));
            y = Double.parseDouble(values[2].substring(2));
            z = Double.parseDouble(values[3].substring(2));
            
            pitch = Float.parseFloat(values[4].substring(2));
            yaw = Float.parseFloat(values[5].substring(2));
        }

	public ILocation add(final double x, final double y, final double z) {
		return new CLocation(world, x + this.x, y + this.y, z + this.z, pitch,
				yaw);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(pitch);
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(yaw);
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		final ILocation other = (CLocation) obj;
		if (Float.floatToIntBits(pitch) != Float.floatToIntBits(other.getPitch())) {
			return false;
                }
		if (world == null) {
			if (other.getWorldName() != null) {
				return false;
                        }
		} else if (!world.equals(other.getWorldName())) {
			return false;
                }
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.getX())) {
			return false;
                }
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.getY())) {
			return false;
                }
		if (Float.floatToIntBits(yaw) != Float.floatToIntBits(other.getYaw())) {
			return false;
                }
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.getZ())) {
			return false;
                }
		return true;
	}

        @Override
	public int getBlockX() {
		return (int) Math.floor(x);
	}

        @Override
	public int getBlockY() {
		return (int) Math.floor(y);
	}

        @Override
	public int getBlockZ() {
		return (int) Math.floor(z);
	}

        @Override
	public double getDistance(final ILocation otherLocation) {
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
				+ Math.pow(this.y - otherLocation.getY(), 2.0D)
                                + Math.pow(this.z - otherLocation.getZ(), 2.0D));
	}

        @Override
	public double getDistanceSquared(final ILocation otherLocation) {
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
				+ Math.pow(this.y - otherLocation.getY(), 2.0D)
                                + Math.pow(this.z - otherLocation.getZ(), 2.0D);
	}

        @Override
	public float getPitch() {
		return pitch;
	}

        @Override
	public String getWorldName() {
		return world;
	}

        @Override
	public double getX() {
		return x;
	}

        @Override
	public double getY() {
		return y;
	}

        @Override
	public float getYaw() {
		return yaw;
	}

        @Override
	public double getZ() {
		return z;
	}

        @Override
	public void setPitch(final float value) {
		pitch = value;
	}

        @Override
	public void setX(final double value) {
		x = value;
	}

        @Override
	public void setY(final double value) {
		y = value;
	}

        @Override
	public void setYaw(final float value) {
		yaw = value;
	}

        @Override
	public void setZ(final double value) {
		z = value;
	}

        @Override
	public Location toLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}

	@Override
	public String toString() {
		String[] aLoc = new String[6];
		aLoc[0] = "w:" + getWorldName();
		aLoc[1] = "x:" + getX();
		aLoc[2] = "y:" + getY();
		aLoc[3] = "z:" + getZ();
		aLoc[4] = "P:" + getPitch();
		aLoc[5] = "Y:" + getYaw();
		return StringParser.joinArray(aLoc, "|");
	}
}
