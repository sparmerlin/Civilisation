package net.slipcor.civ.events;

import net.slipcor.civ.api.INation;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

public class NationLeaveEvent extends NationEvent implements Cancellable {
	private final CommandSender player;
	public NationLeaveEvent(INation me, CommandSender player) {
		super(me);
		this.player = player;
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
	
	public CommandSender getPlayer() {
		return player;
	}
}
