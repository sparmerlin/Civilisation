package net.slipcor.civ.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.ICivilisation;

public class CCityBuffer {
	private final ICity city;
	private final YamlConfiguration cfg;
	
	private final static List<CCityBuffer> buffers = new ArrayList<CCityBuffer>();
	
	public CCityBuffer(final ICity city, final YamlConfiguration cfg) {
		this.city = city;
		this.cfg = cfg;
		
		buffers.add(this);
	}
	
	public static void commitAll(final ICivilisation plugin) {
		for (CCityBuffer buffer : buffers) {
			buffer.commit(plugin);
		}
		buffers.clear();
	}

	private void commit(final ICivilisation plugin) {

        for (String like : cfg.getStringList("likes")) {
        	city.like(plugin.getCity(like));
        }
        
        for (String dislike : cfg.getStringList("dislikes")) {
        	city.dislike(plugin.getCity(dislike));
        }
	}
}
