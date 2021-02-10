package lee.code.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Data {

    private final HashMap<UUID, String> selectedNPC = new HashMap<>();
    private final List<String> savedNPCs = new ArrayList<>();

    public void addActiveNPC(String name) {
        savedNPCs.add(name);
    }
    public void removeActiveNPC(String name) {
        savedNPCs.remove(name);
    }
    public List<String> getActiveNPCs() {
        return savedNPCs;
    }
    public String getSelectedNPC(UUID player) {
        return selectedNPC.get(player);
    }
    public void setSelectedNPC(UUID player, String name) {
        selectedNPC.put(player, name);
    }
    public boolean hasSelectedNPC(UUID player) {
        return selectedNPC.containsKey(player);
    }
}
