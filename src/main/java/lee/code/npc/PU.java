package lee.code.npc;

import lee.code.npc.database.SQLite;
import lee.code.npc.lists.SupportedVillagerProfessions;
import lee.code.npc.lists.SupportedVillagerTypes;
import lee.code.npc.nms.VillagerNPC;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PU {

    public String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public String formatEntityLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public Location unFormatEntityLocation(String location) {
        String[] split = location.split(",", 6);
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), (float) Double.parseDouble(split[4]), (float) Double.parseDouble(split[5]));
    }

    public String buildStringFromArgs(String[] args, int start) {
        StringBuilder w = new StringBuilder();
        for(int i = start; i < args.length; i++) {
            w.append(args[i]).append(" ");
        }
        w = new StringBuilder(w.substring(0, w.length() - 1));
        return w.toString();
    }

    public void removeNPCs() {

        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();

        for (Location location : SQL.getNPCLocations()) {
            location.getChunk().load();
            location.getChunk().setForceLoaded(true);

            for (Entity entity : location.getChunk().getEntities()) {

                String customName = entity.getCustomName();

                if (customName != null) {
                    String name = entity.getCustomName().replaceAll("§", "&");
                    if (SQL.getNPCNames().contains(name)) {
                        entity.remove();
                        System.out.println(entity.getCustomName());
                    }
                }
            }
        }
    }

    public void removeNPC(String npc) {

        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();

        for (Entity entity : SQL.getNPCLocation(npc).getChunk().getEntities()) {
            String customName = entity.getCustomName();
            if (customName != null) {
                String name = entity.getCustomName().replaceAll("§", "&");
                if (name.equals(npc)) {
                    entity.remove();
                    return;
                }
            }
        }
    }

    public void spawnNPC(String name, Location location, String profession, String type) {
        VillagerType villagerType = SupportedVillagerTypes.valueOf(type).getType();
        VillagerProfession villagerProfession = SupportedVillagerProfessions.valueOf(profession).getProfession();
        VillagerNPC villager = new VillagerNPC(location, villagerType, villagerProfession, name);

        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        world.addEntity(villager);
        location.getChunk().setForceLoaded(true);
    }

    public String selectNPC(Player player) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        UUID uuid = player.getUniqueId();

        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity) {
                if (getLookingAt(player, (LivingEntity) entity)) {
                    String customName = entity.getCustomName();
                    if (customName != null) {
                        String name = customName.replaceAll("§", "&");
                        if (SQL.getNPCNames().contains(name)) {
                            plugin.getData().setSelectedNPC(uuid, name);
                            return name;
                        }
                    }
                }
            }
        }
        return "n";
    }

    private boolean getLookingAt(Player player, LivingEntity entity){
        Location eye = player.getEyeLocation();
        Vector toEntity = entity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.99D;
    }

    public List<String> getVillagerTypes() {
        return EnumSet.allOf(SupportedVillagerTypes.class).stream().map(SupportedVillagerTypes::name).collect(Collectors.toList());
    }

    public List<String> getVillagerProfessions() {
        return EnumSet.allOf(SupportedVillagerProfessions.class).stream().map(SupportedVillagerProfessions::name).collect(Collectors.toList());
    }
}