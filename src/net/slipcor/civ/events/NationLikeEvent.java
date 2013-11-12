package net.slipcor.civ.events;

import net.slipcor.civ.api.INation;

import org.bukkit.event.Cancellable;

public class NationLikeEvent extends NationEvent implements Cancellable {
	private INation nation;
	private boolean like;
	public NationLikeEvent(INation me, INation other, boolean like) {
		super(me);
		nation = other;
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
	
	public INation getOther() {
		return nation;
	}
	
	public boolean isLike() {
		return like;
	}
}
