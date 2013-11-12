package net.slipcor.civ.events;

import net.slipcor.civ.api.IHouse;

import org.bukkit.event.Cancellable;

public class HouseLikeEvent extends HouseEvent implements Cancellable {
	private IHouse house;
	private boolean like;
	public HouseLikeEvent(IHouse me, IHouse other, boolean like) {
		super(me);
		house = other;
		this.like = like;
	}

	private boolean cancelled;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean value) {
		cancelled = value;
	}
	
	public IHouse getOther() {
		return house;
	}
	
	public boolean isLike() {
		return like;
	}
}
