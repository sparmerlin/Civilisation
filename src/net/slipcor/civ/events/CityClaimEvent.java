package net.slipcor.civ.events;

import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

public class CityClaimEvent extends CityEvent implements Cancellable {
	private final CommandSender player;
	private final IChunk chunk;
	private final boolean claim;
	public CityClaimEvent(ICity me, CommandSender player, IChunk chunk, boolean claim) {
		super(me);
		this.player = player;
		this.chunk = chunk;
		this.claim = claim;
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
	
	public IChunk getChunk() {
		return chunk;
	}
	
	public CommandSender getPlayer() {
		return player;
	}
	
	public boolean isClaim() {
		return claim;
	}
}
