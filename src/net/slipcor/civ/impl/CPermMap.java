package net.slipcor.civ.impl;

import java.util.ArrayList;
import java.util.List;
import net.slipcor.civ.api.Permission;
import net.slipcor.civ.api.IPermMap;
import org.bukkit.ChatColor;

/**
 *
 * @author slipcor
 */
public class CPermMap implements IPermMap {
    private final List<Permission> allowed = new ArrayList<Permission>();
    
    public CPermMap (final Permission perm) {
        allowed.add(perm);
    }
    
    public CPermMap (final int bits) {
        for (Permission perm : Permission.values()) {
            if ((bits & (int) Math.pow(2, perm.ordinal())) != 0) {
                allowed.add(perm);
            }
        }
    }
    
    public CPermMap (final String string) {
        if (string != null) {
            for (Permission perm : Permission.values()) {
                if (string.contains(perm.toString())) {
                    allowed.add(perm);
                }
            }
        }
    }

    @Override
    public void add(final Permission perm) {
        allowed.add(perm);
    }

    @Override
    public String printColorized() {
        final StringBuffer buff = new StringBuffer("");
        for (Permission perm : Permission.values()) {
            if (allowed.contains(perm)) {
                buff.append(ChatColor.GREEN);
            } else {
                buff.append(ChatColor.RED);
            }
            buff.append(perm.toString());
            buff.append(' ');
        }
        return buff.toString();
    }

    @Override
    public void remove(final Permission perm) {
        allowed.remove(perm);
    }
    
    @Override
    public String toString() {
        final StringBuffer buff = new StringBuffer("");
        for (Permission perm : allowed) {
            buff.append(perm.toString());
        }
        return buff.toString();
    }

    @Override
    public boolean has(final Permission perm) {
        return allowed.contains(perm);
    }
}
