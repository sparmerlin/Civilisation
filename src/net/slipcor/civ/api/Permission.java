package net.slipcor.civ.api;

/**
 *
 * @author slipcor
 */
public enum Permission {
    BUILD('b'),
    CHEST('c'),
    DOOR('d'),
    PVE('e'),
    INTERACT('i'), // anvil, workbench
    LEVER('l'),
    MOBS('m'), 
    PVP('p');
    
    private final char letter;
    
    private Permission(final char letter) {
        this.letter = letter;
    }
    
    @Override
    public String toString() {
        return String.valueOf(letter);
    }
}
