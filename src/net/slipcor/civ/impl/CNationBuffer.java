package net.slipcor.civ.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.slipcor.civ.api.ICivilisation;
import net.slipcor.civ.api.INation;

public class CNationBuffer {
	private final INation city;
	private final YamlConfiguration cfg;
	
	private final static List<CNationBuffer> buffers = new ArrayList<CNationBuffer>();
	
	public CNationBuffer(final INation city, final YamlConfiguration cfg) {
		this.city = city;
		this.cfg = cfg;
		
		buffers.add(this);
	}
	
	public static void commitAll(final ICivilisation plugin) {
		for (CNationBuffer buffer : buffers) {
			buffer.commit(plugin);
		}
		buffers.clear();
	}

	private void commit(final ICivilisation plugin) {

        for (String like : cfg.getStringList("likes")) {
        	city.like(plugin.getNation(like));
        }
        
        for (String dislike : cfg.getStringList("dislikes")) {
        	city.dislike(plugin.getNation(dislike));
        }
	}
}
