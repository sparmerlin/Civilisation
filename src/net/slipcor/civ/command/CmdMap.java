package net.slipcor.civ.command;

import net.slipcor.civ.Civilisation;
import net.slipcor.civ.api.IChunk;
import net.slipcor.civ.api.ICity;
import net.slipcor.civ.api.IHouse;
import net.slipcor.civ.api.INation;
import net.slipcor.civ.core.Language;
import net.slipcor.civ.impl.CChunk;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The reload command
 *
 * @author dumptruckman,slipcor
 */
public class CmdMap extends AbstractCommand {

    public CmdMap(final Civilisation plugin) {
        super(plugin);
        this.setArgRange(0, 0);
    }

    private enum MAPENTRYTYPE {

        EMPTY("empty", ChatColor.GRAY, "-"),
        YOURHOUSE("your house", ChatColor.BLUE, "H"),
        YOURCITY("your city", ChatColor.BLUE, "C"),
        YOURNATION("your nation", ChatColor.BLUE, "N"),
        OTHERHOUSE_N("other house (neutral)", ChatColor.GRAY, "H"),
        OTHERHOUSE_L("other house (liked)", ChatColor.GREEN, "H"),
        OTHERHOUSE_D("other house (disliked)", ChatColor.RED, "H"),
        OTHERCITY_N("other city (neutral)", ChatColor.GRAY, "C"),
        OTHERCITY_L("other city (liked)", ChatColor.GREEN, "C"),
        OTHERCITY_D("other city (disliked)", ChatColor.RED, "C"),
        OTHERNATION_N("other nation (neutral)", ChatColor.GRAY, "N"),
        OTHERNATION_L("other nation (liked)", ChatColor.GREEN, "N"),
        OTHERNATION_D("other nation (disliked)", ChatColor.RED, "N");

        final String description;
        final ChatColor color;
        final String value;

        MAPENTRYTYPE(String desc, ChatColor cc, String val) {
            description = desc;
            value = val;
            color = cc;
        }

        @Override
        public String toString() {
            return color + value;
        }

        public String toString(final boolean local) {
            return local ? (ChatColor.GOLD + value) : this.toString();
        }

        public String getDesc() {
            return description;
        }
    }

    @Override
    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.parse(Language.COMMAND_ONLYPLAYER));
            return true;
        }

        final Player player = (Player) sender;

        final IHouse myHouse = plugin.getHouse(sender.getName(), true);

        final ICity myCity = plugin.getCity(player);

        final INation myNation = plugin.getNation(myCity); // null check unnecessesary
        
        player.sendMessage(ChatColor.WHITE + "------------");
        player.sendMessage(ChatColor.WHITE + "Civilisations");
        player.sendMessage(ChatColor.WHITE + "------------");
        
        
        float direction = (player.getLocation().getYaw() + 22.5f + 45f + 360f)%360f;
        
        // 0 == SOUTHEAST
        /* 00-45 : SE
         * 45-90 : S
         * 90-135 : SW
        
         * 135-180 : W
        
         * 180-225 : NW
         * 225-270 : N
         * 270-315 : NE
        
         * 315-360 : E
         * 
         */
        
        final String nString;
        final String mString;
        final String sString;
        
        if (direction < 135) {
            // SOUTH
            
            if (direction < 45) {
                // SE
                sString = ChatColor.DARK_GRAY+"-S"+ChatColor.GOLD+"\\";
            } else if (direction > 90) {
                // SW
                sString = ChatColor.GOLD+"/"+ChatColor.DARK_GRAY+"S-";
            } else {
                // S
                sString = ChatColor.DARK_GRAY+"-"+ChatColor.GOLD+"S"+ChatColor.DARK_GRAY+"-";
            }
            mString = ChatColor.DARK_GRAY+"W+E";
            nString = ChatColor.DARK_GRAY+"-N-";
            
        } else if  (direction > 315) {
            sString = ChatColor.DARK_GRAY+"-S-";
            // EAST
            mString = ChatColor.DARK_GRAY+"W+"+ChatColor.GOLD+"E";
            nString = ChatColor.DARK_GRAY+"-N-";
        } else if (direction > 180) {
            sString = ChatColor.DARK_GRAY+"-S-";
            // NORTH
            mString = ChatColor.DARK_GRAY+"W"+ChatColor.DARK_GRAY+"+E";
            
            if (direction < 225) {
                // NW
                nString = ChatColor.GOLD+"\\"+ChatColor.DARK_GRAY+"N-";
            } else if (direction > 270) {
                // NE
                nString = ChatColor.DARK_GRAY+"-N"+ChatColor.GOLD+"/";
            } else {
                // N
                nString = ChatColor.DARK_GRAY+"-"+ChatColor.GOLD+"N"+ChatColor.DARK_GRAY+"-";
            }
            
        } else {
            sString = ChatColor.DARK_GRAY+"-S-";
            // WEST
            mString = ChatColor.GOLD+"W"+ChatColor.DARK_GRAY+"+E";
            nString = ChatColor.DARK_GRAY+"-N-";
        }
        
        String[] appendix = new String[9];
        
        appendix[0] = appendix[4] = ChatColor.WHITE + " ---";
        appendix[1] = " " + nString;
        appendix[2] = " " + mString;
        appendix[3] = " " + sString;
        
        int pos = 0;

        for (int z = player.getLocation().getChunk().getZ() - 4; z <= player.getLocation().getChunk().getZ() + 4; z++) {

            final StringBuffer buffer = new StringBuffer();

            check:
            for (int x = player.getLocation().getChunk().getX() - 4; x <= player.getLocation().getChunk().getX() + 4; x++) {

                IChunk chunk = new CChunk(player.getLocation().getWorld().getChunkAt(x, z));
                
                boolean local = (chunk.equals(new CChunk(player.getLocation().getChunk())));

                for (IHouse house : plugin.getHouses()) {
                    if (house.getClaimed().contains(chunk)) {
                        if (house.equals(myHouse)) {
                            buffer.append(MAPENTRYTYPE.YOURHOUSE.toString(local));
                            continue check;
                        }
                        if (myCity != null && myCity.getHouses().contains(house)) {
                            buffer.append(MAPENTRYTYPE.YOURCITY.toString(local));
                            continue check;
                        }
                        if (myNation != null) {
                            if (myNation.getCapital().getHouses().contains(house)) {
                                buffer.append(MAPENTRYTYPE.YOURNATION.toString(local));
                                continue check;
                            }
                            for (ICity city : myNation.getCities()) {
                                if (city.getHouses().contains(house)) {
                                    buffer.append(MAPENTRYTYPE.YOURNATION.toString(local));
                                    continue check;
                                }
                            }
                        }

                        // the plot does not belong to any of my direct connections!
                        for (ICity city : plugin.getCities()) {
                            if (city.getHouses().contains(house)) {
                                INation nation = plugin.getNation(city);

                                if (nation == null) {
                                    // house belongs to other city
                                    if (myCity != null && myCity.getDisliked().contains(city)) {
                                        buffer.append(MAPENTRYTYPE.OTHERCITY_D.toString(local));
                                    } else if (myCity != null && myCity.getLiked().contains(city)) {
                                        buffer.append(MAPENTRYTYPE.OTHERCITY_L.toString(local));
                                    } else {
                                        buffer.append(MAPENTRYTYPE.OTHERCITY_N.toString(local));
                                    }
                                    continue check;
                                } else {
                                    // house belongs to other nation

                                    if (myNation != null) {
                                        if (myNation.getDisliked().contains(nation)) {
                                            buffer.append(MAPENTRYTYPE.OTHERNATION_D.toString(local));
                                        } else if (myNation.getLiked().contains(nation)) {
                                            buffer.append(MAPENTRYTYPE.OTHERNATION_L.toString(local));
                                        } else {
                                            buffer.append(MAPENTRYTYPE.OTHERNATION_N.toString(local));
                                        }
                                        continue check;
                                    } else if (myCity != null) {
                                        // we have no nation, but there is another nation. Check if we dislike any cities
                                        for (ICity otherCity : nation.getCities()) {
                                            if (myCity.getDisliked().contains(otherCity)) {
                                                buffer.append(MAPENTRYTYPE.OTHERNATION_D.toString(local));
                                                break check;
                                            } else if (myCity.getLiked().contains(otherCity)) {
                                                buffer.append(MAPENTRYTYPE.OTHERNATION_L.toString(local));

                                            }
                                        }
                                        // this nation has no cities we care about
                                        buffer.append(MAPENTRYTYPE.OTHERNATION_N.toString(local));
                                    } else {
                                        // we have no nation and no city, this nation should be nothing we care about
                                        buffer.append(MAPENTRYTYPE.OTHERNATION_N.toString(local));
                                    }
                                    continue check;
                                }
                            }
                        }

                        if (myHouse.getDisliked().contains(house)) {
                            buffer.append(MAPENTRYTYPE.OTHERHOUSE_D.toString(local));
                        } else if (myHouse.getLiked().contains(house)) {
                            buffer.append(MAPENTRYTYPE.OTHERHOUSE_L.toString(local));
                        } else {
                            buffer.append(MAPENTRYTYPE.OTHERHOUSE_N.toString(local));
                        }
                        continue check;
                    }
                }
                
                // chunk is not part of a house, maybe of an outpost?
                
                for (ICity city : plugin.getCities()) {
                    if (city.getOutposts().contains(chunk)) {
                        
                        INation nation = plugin.getNation(city);
                        
                        if (nation == null) {
                            // chunk belongs to other city

                            if (myCity != null && myCity.getDisliked().contains(city)) {
                                buffer.append(MAPENTRYTYPE.OTHERCITY_D.toString(local));
                            } else if (myCity != null && myCity.getLiked().contains(city)) {
                                buffer.append(MAPENTRYTYPE.OTHERCITY_L.toString(local));
                            } else {
                                buffer.append(MAPENTRYTYPE.OTHERCITY_N.toString(local));
                            }
                            continue check;
                        } else {
                            // chunk belongs to other nation

                            if (myNation != null) {
                                if (myNation.getDisliked().contains(nation)) {
                                    buffer.append(MAPENTRYTYPE.OTHERNATION_D.toString(local));
                                } else if (myNation.getLiked().contains(nation)) {
                                    buffer.append(MAPENTRYTYPE.OTHERNATION_L.toString(local));
                                } else {
                                    buffer.append(MAPENTRYTYPE.OTHERNATION_N.toString(local));
                                }
                                continue check;
                            } else if (myCity != null) {
                                // we have no nation, but there is another nation. Check if we dislike any cities
                                for (ICity otherCity : nation.getCities()) {
                                    if (myCity.getDisliked().contains(otherCity)) {
                                        buffer.append(MAPENTRYTYPE.OTHERNATION_D.toString(local));
                                        break check;
                                    } else if (myCity.getLiked().contains(otherCity)) {
                                        buffer.append(MAPENTRYTYPE.OTHERNATION_L.toString(local));

                                    }
                                }
                                // this nation has no cities we care about
                                buffer.append(MAPENTRYTYPE.OTHERNATION_N.toString(local));
                            } else {
                                // we have no nation and no city, this nation should be nothing we care about
                                buffer.append(MAPENTRYTYPE.OTHERNATION_N.toString(local));
                            }
                            continue check;
                        }
                    }
                }
                buffer.append(MAPENTRYTYPE.EMPTY.toString(local));
            }
            if (pos < 5) {
                buffer.append(appendix[pos++]);
            }
            player.sendMessage(buffer.toString());
        }
        
        player.sendMessage(ChatColor.WHITE + "------------");
        

        return true;
    }
}
