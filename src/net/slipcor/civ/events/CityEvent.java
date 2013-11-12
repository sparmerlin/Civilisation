package net.slipcor.civ.events;

import net.slipcor.civ.api.ICity;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CityEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	private final ICity me;

	/**
	 * create a start event instance
	 * 
	 * @param a
	 *            the starting arena
	 */
	public CityEvent(final ICity house) {
		super(); 
		me = house;
	}

	public ICity getHouse() {
		return me;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
