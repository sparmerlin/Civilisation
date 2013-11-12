package net.slipcor.civ.events;

import net.slipcor.civ.api.ICity;

import org.bukkit.event.Cancellable;

public class CityLikeEvent extends CityEvent implements Cancellable {
	private ICity house;
	private boolean like;
	public CityLikeEvent(ICity me, ICity other, boolean like) {
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
	
	public ICity getOther() {
		return house;
	}
	
	public boolean isLike() {
		return like;
	}
}
