package lee.code.npc.database;

import lee.code.cache.jedis.Jedis;
import lee.code.cache.jedis.JedisPool;
import lee.code.cache.jedis.Pipeline;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.lists.SupportedVillagerProfessions;
import lee.code.npc.lists.SupportedVillagerTypes;
import lee.code.npc.nms.VillagerNPC;
import net.minecraft.server.v1_16_R3.VillagerProfession;
import net.minecraft.server.v1_16_R3.VillagerType;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.*;

public class Cache {

    public void setNPC(String name, String location, String profession, String type, String command, String commandType) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            Pipeline pipe = jedis.pipelined();
            pipe.hset("npcLocation", name, location);
            pipe.hset("npcProfession", name, profession);
            pipe.hset("npcType", name, type);
            pipe.hset("npcCommand", name, command);
            pipe.hset("npcCommandType", name, commandType);
            pipe.sync();
            addToNPCNameList(name);
        }
    }

    public void createNPC(UUID uuid, String name, Location location, String profession, String type, String command, String commandType) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        String sName = name.replaceAll("%", "");
        String sLocation = plugin.getPU().formatEntityLocation(location);

        try (Jedis jedis = pool.getResource()) {
            Pipeline pipe = jedis.pipelined();
            pipe.hset("npcLocation", sName, sLocation);
            pipe.hset("npcProfession", sName, profession);
            pipe.hset("npcType", sName, type);
            pipe.hset("npcCommand", sName, command);
            pipe.hset("npcCommandType", sName, commandType);
            pipe.sync();

            addToNPCNameList(sName);
            setNPCSelected(uuid, sName);

            WorldServer npcWorld = ((CraftWorld) location.getWorld()).getHandle();
            VillagerProfession npcProfession = SupportedVillagerProfessions.valueOf(profession).getProfession();
            VillagerType npcType = SupportedVillagerTypes.valueOf(type).getType();
            VillagerNPC villager = new VillagerNPC(location, npcType, npcProfession, sName);

            Bukkit.getScheduler().runTaskLater(plugin, () -> npcWorld.addEntity(villager), 10);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQL.createNPC(sName, sLocation, profession, type, command, commandType));
        }
    }

    private void reloadNPC(String name, String oldName) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            Location location = plugin.getPU().unFormatEntityLocation(jedis.hget("npcLocation", name));
            String sProfession = jedis.hget("npcProfession", name);
            String sType = jedis.hget("npcType", name);

            for (Entity entity : location.getChunk().getEntities()) {
                String customName = entity.getCustomName();
                if (customName != null) {
                    String entityName = plugin.getPU().unFormat(entity.getCustomName());
                    if (entityName.equals(name) || entityName.equals(oldName)) {
                        entity.remove();

                        WorldServer npcWorld = ((CraftWorld) location.getWorld()).getHandle();
                        VillagerProfession npcProfession = SupportedVillagerProfessions.valueOf(sProfession).getProfession();
                        VillagerType npcType = SupportedVillagerTypes.valueOf(sType).getType();
                        VillagerNPC villager = new VillagerNPC(location, npcType, npcProfession, name);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> npcWorld.addEntity(villager), 10);
                        return;
                    }
                }
            }
        }
    }

    public void setNPCName(UUID uuid, String oldName, String newName) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();
        SQLite SQL = plugin.getSqLite();

        String sName = newName.replaceAll("%", "");

        try (Jedis jedis = pool.getResource()) {

            String sLocation = jedis.hget("npcLocation", oldName);
            String sProfession = jedis.hget("npcProfession", oldName);
            String sType = jedis.hget("npcType", oldName);
            String sCommand = jedis.hget("npcCommand", oldName);
            String sCommandType = jedis.hget("npcCommandType", oldName);

            Pipeline pipe = jedis.pipelined();
            pipe.hset("npcLocation", sName, sLocation);
            pipe.hset("npcProfession", sName, sProfession);
            pipe.hset("npcType", sName, sType);
            pipe.hset("npcCommand", sName, sCommand);
            pipe.hset("npcCommandType", sName, sCommandType);
            pipe.hdel("npcLocation", oldName);
            pipe.hdel("npcProfession", oldName);
            pipe.hdel("npcType", oldName);
            pipe.hdel("npcCommand", oldName);
            pipe.hdel("npcCommandType", oldName);
            pipe.sync();

            removeFromNPCNames(oldName);
            addToNPCNameList(sName);
            reloadNPC(sName, oldName);
            setNPCSelected(uuid, sName);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQL.setName(oldName, sName));
        }
    }

    public void setNPCProfession(String name, String profession) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();
        SQLite SQL = plugin.getSqLite();

        try (Jedis jedis = pool.getResource()) {
            jedis.hset("npcProfession", name, profession);
            reloadNPC(name, "null");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQL.setProfession(name, profession));
        }
    }

    public void setNPCType(String name, String type) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();
        SQLite SQL = plugin.getSqLite();

        try (Jedis jedis = pool.getResource()) {
            jedis.hset("npcType", name, type);
            reloadNPC(name, "null");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQL.setType(name, type));
        }
    }

    public void setNPCCommand(String name, String command, String commandType) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();
        SQLite SQL = plugin.getSqLite();

        try (Jedis jedis = pool.getResource()) {
            jedis.hset("npcCommand", name, command);
            jedis.hset("npcCommandType", name, commandType);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQL.setCommand(name, command));
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQL.setCommandType(name, commandType));
        }
    }

    public void setNPCSelected(UUID uuid, String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        String sUUID = String.valueOf(uuid);

        try (Jedis jedis = pool.getResource()) {
            jedis.hset("npcSelected", sUUID, name);
        }
    }

    public String getNPCSelected(UUID uuid) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        String sUUID = String.valueOf(uuid);

        try (Jedis jedis = pool.getResource()) {
            return jedis.hget("npcSelected", sUUID);
        }
    }

    public boolean hasNPCSelected(UUID uuid) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        String sUUID = String.valueOf(uuid);

        try (Jedis jedis = pool.getResource()) {
            return jedis.hexists("npcSelected", sUUID);
        }
    }

    public boolean isNPC(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            return jedis.hexists("npcLocation", name);
        }
    }

    private void addToNPCNameList(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            if (jedis.exists("npcNames")) {
                String newNameList = jedis.get("npcNames") + "%" + name;
                jedis.set("npcNames", newNameList);
            } else jedis.set("npcNames", name);
        }
    }

    public List<String> getNPCNames() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            if (jedis.exists("npcNames")) {
                String[] split = StringUtils.split(jedis.get("npcNames"), '%');
                return new ArrayList<>(Arrays.asList(split));
            } else return Collections.singletonList("");
        }
    }

    private void removeFromNPCNames(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            List<String> newList = new ArrayList<>();
            String[] split = StringUtils.split(jedis.get("npcNames"), '%');
            for (String npcName : split) if (!npcName.equals(name)) newList.add(npcName);
            String newNameList = StringUtils.join(newList, "%");
            jedis.set("npcNames", newNameList);
        }
    }

    public boolean hasNPCCommand(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            if (jedis.hexists("npcCommand", name)) {
                return !jedis.hget("npcCommand", name).equals("n");
            } else return false;
        }
    }

    public String getNPCCommand(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            return jedis.hget("npcCommand", name);
        }
    }

    public String getNPCCommandType(String name) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        JedisPool pool = plugin.getCacheAPI().getNPCPool();

        try (Jedis jedis = pool.getResource()) {
            return jedis.hget("npcCommandType", name);
        }
    }
}
