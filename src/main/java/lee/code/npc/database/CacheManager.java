package lee.code.npc.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.tables.NPCTable;
import lee.code.npc.lists.CommandType;
import lee.code.npc.lists.NPCProfession;
import lee.code.npc.lists.NPCType;
import lombok.Getter;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class CacheManager {

    @Getter
    private final Cache<Integer, NPCTable> npcCache = CacheBuilder
            .newBuilder()
            .initialCapacity(5000)
            .recordStats()
            .build();

    private NPCTable getNPCTable(int id) {
        return getNpcCache().getIfPresent(id);
    }

    public void spawnNPC(NPCTable npcTable) {
        GoldmanNPC.getPlugin().getPU().spawnNPC(
                BukkitUtils.serializeColorComponentJson(npcTable.getDisplayName()),
                BukkitUtils.parseLocation(npcTable.getLocation()),
                NPCType.valueOf(npcTable.getType()),
                NPCProfession.valueOf(npcTable.getProfession()),
                npcTable.getId());
    }

    public void removeNPC(NPCTable npcTable) {
        GoldmanNPC.getPlugin().getPU().removeNPC(npcTable.getId(), BukkitUtils.parseLocation(npcTable.getLocation()));
    }

    public void reloadNPC(NPCTable npcTable) {
        removeNPC(npcTable);
        spawnNPC(npcTable);
    }

    public void setNPCData(NPCTable npcTable) {
        getNpcCache().put(npcTable.getId(), npcTable);
        spawnNPC(npcTable);
    }

    public void updateNPCData(NPCTable npcTable) {
        getNpcCache().put(npcTable.getId(), npcTable);
        GoldmanNPC.getPlugin().getDatabaseManager().updateNPCTable(npcTable);
    }

    public void createNPCData(NPCTable npcTable) {
        getNpcCache().put(npcTable.getId(), npcTable);
        GoldmanNPC.getPlugin().getDatabaseManager().createNPCTable(npcTable);
        spawnNPC(npcTable);
    }

    public void deleteNPCData(NPCTable npcTable) {
        getNpcCache().invalidate(npcTable.getId());
        GoldmanNPC.getPlugin().getDatabaseManager().deleteNPCTable(npcTable);
        removeNPC(npcTable);
    }

    public void deleteNPC(int id) {
        deleteNPCData(getNPCTable(id));
    }

    public String getNPCDisplayName(int id) {
        return getNPCTable(id).getDisplayName();
    }

    public void setNPCDisplayName(int id, String newName) {
        NPCTable npcTable = getNPCTable(id);
        npcTable.setDisplayName(newName);
        updateNPCData(npcTable);
        reloadNPC(npcTable);
    }

    public void setNPCProfession(int id, NPCProfession profession) {
        NPCTable npcTable = getNPCTable(id);
        npcTable.setProfession(profession.name());
        updateNPCData(npcTable);
        reloadNPC(npcTable);
    }

    public void setNPCLocation(int id, Location location) {
        NPCTable npcTable = getNPCTable(id);
        removeNPC(npcTable);
        npcTable.setLocation(BukkitUtils.serializeLocation(location));
        updateNPCData(npcTable);
        spawnNPC(npcTable);
    }

    public void setNPCType(int id, NPCType type) {
        NPCTable npcTable = getNPCTable(id);
        npcTable.setType(type.name());
        updateNPCData(npcTable);
        reloadNPC(npcTable);
    }

    public void setNPCCommand(int id, String command, CommandType commandType) {
        NPCTable npcTable = getNPCTable(id);
        npcTable.setCommand(command);
        npcTable.setCommandType(commandType.name());
        updateNPCData(npcTable);
    }

    public boolean isNPC(int id) {
        return getNpcCache().asMap().containsKey(id);
    }

    public int getIDFromName(String name) {
        Optional<Integer> hasName = getNpcCache().asMap().entrySet().stream()
                .filter(e -> e.getValue().getDisplayName().equals(name))
                .map(Map.Entry::getKey)
                .findFirst();
        return hasName.orElse(0);
    }

    public Map<Integer, Location> getNPCLocations() {
        Map<Integer, Location> locationMap = new HashMap<>();
        for (NPCTable npcTable : getNpcCache().asMap().values()) {
            locationMap.put(npcTable.getId(), BukkitUtils.parseLocation(npcTable.getLocation()));
        }
        return locationMap;
    }

    public Location getNPCLocation(int id) {
        return BukkitUtils.parseLocation(getNPCTable(id).getLocation());
    }

    public Map<Integer, String> getNPCNames() {
        return getNpcCache().asMap().values().stream().collect(Collectors.toMap(NPCTable::getId, NPCTable::getDisplayName));
    }

    public List<String> getAllIDString() {
        return new ArrayList<>(getNpcCache().asMap().keySet()).stream().map(Object::toString).collect(Collectors.toList());
    }

    public boolean hasNPCCommand(int id) {
        return !getNPCTable(id).getCommand().equals("0");
    }

    public String getNPCCommand(int id) {
        return getNPCTable(id).getCommand();
    }

    public CommandType getNPCCommandType(int id) {
        return CommandType.valueOf(getNPCTable(id).getCommandType());
    }

    public int getNextID() {
        return getNpcCache().asMap().keySet().size() + 1;
    }

    public boolean hasPlaceholders(int id) {
        return getNPCTable(id).isPlaceholders();
    }

    public void setPlaceholders(int id, boolean result) {
        NPCTable npcTable = getNPCTable(id);
        npcTable.setPlaceholders(result);
        updateNPCData(npcTable);
    }
}
