package lee.code.npc;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.CommandType;
import lee.code.npc.lists.NPCProfession;
import lee.code.npc.lists.NPCType;
import lee.code.npc.nms.VillagerNPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class PU {

    public void runCommand(Player player, String command, CommandType commandType) {
        switch (commandType) {
            case CONSOLE -> {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                String run = command.replaceAll("%player%", player.getName());
                Bukkit.dispatchCommand(console, run);
            }
            case PLAYER -> player.chat(command);
            case NETWORK -> BukkitUtils.sendPlayerServer(player, command);
        }
    }

    public void removeNPCs() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        Map<Integer, Location> npcs = cacheManager.getNPCLocations();
        if (npcs.isEmpty()) return;

        for (Map.Entry<Integer, Location> npc : npcs.entrySet()) {
            for (Entity entity : npc.getValue().getChunk().getEntities()) {
                if (entity instanceof Villager) {
                    int id = getID(entity);
                    if (npc.getKey().equals(id)) entity.remove();
                }
            }
        }
    }

    public void removeNPC(int id, Location location) {
        for (Entity entity : location.getChunk().getEntities()) {
            if (entity instanceof Villager) {
                if (getID(entity) == id) {
                    entity.remove();
                    return;
                }
            }
        }
    }

    public int selectNPC(Player player) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();
        UUID uuid = player.getUniqueId();

        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity) {
                if (getLookingAt(player, (LivingEntity) entity)) {
                    int id = getID(entity);
                    if (cacheManager.isNPC(id)) {
                        plugin.getData().setSelectedNPC(uuid, id);
                        return id;
                    }
                }
            }
        }
        return 0;
    }

    private boolean getLookingAt(Player player, LivingEntity entity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = entity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.99D;
    }

    public void spawnNPC(String name, Location location, NPCType npcType, NPCProfession npcProfession, int id) {
        location.getWorld().getChunkAtAsync(location, false).thenAccept(result -> {
            location.getChunk().setForceLoaded(true);
            VillagerNPC villager = new VillagerNPC(location, npcType.getType(), npcProfession.getProfession(), name);
            Bukkit.getScheduler().runTaskLater(GoldmanNPC.getPlugin(), () -> {
                CraftEntity entity = villager.getBukkitEntity();
                NamespacedKey key = new NamespacedKey(GoldmanNPC.getPlugin(), "npc");
                PersistentDataContainer container = entity.getPersistentDataContainer();
                container.set(key, PersistentDataType.INTEGER, id);
                entity.spawnAt(location, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }, 10);
        });
    }

    public int getID(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(GoldmanNPC.getPlugin(), "npc");
        return container.getOrDefault(key, PersistentDataType.INTEGER, 0);
    }
}
