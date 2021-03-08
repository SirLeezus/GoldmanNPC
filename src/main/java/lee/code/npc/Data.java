package lee.code.npc;

import lee.code.npc.database.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Data {

    private final List<UUID> playerClickDelay = new ArrayList<>();

    public boolean hasPlayerClickDelay(UUID uuid) {
        return playerClickDelay.contains(uuid);
    }
    public void addPlayerClickDelay(UUID uuid) {
        playerClickDelay.add(uuid);
    }
    public void removePlayerClickDelay(UUID uuid) {
        playerClickDelay.remove(uuid);
    }

    public void cacheDatabase() {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        SQL.loadNPCs();
    }
}
