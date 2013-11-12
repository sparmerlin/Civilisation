package net.slipcor.civ.events;

import net.slipcor.civ.api.INation;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NationEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	private final INation me;

	/**
	 * create a start event instance
	 * 
	 * @param a
	 *            the starting arena
	 */
	public NationEvent(final INation house) {
		super(); 
		me = house;
	}

	public INation getNation() {
		return me;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
