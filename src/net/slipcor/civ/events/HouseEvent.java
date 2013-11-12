package net.slipcor.civ.events;

import net.slipcor.civ.api.IHouse;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HouseEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	private final IHouse me;

	/**
	 * create a start event instance
	 * 
	 * @param a
	 *            the starting arena
	 */
	public HouseEvent(final IHouse house) {
		super(); 
		me = house;
	}

	public IHouse getHouse() {
		return me;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
