package lee.code.npc;

import java.util.HashMap;
import java.util.UUID;

public class Data {

    private final HashMap<UUID, String> selectedNPC = new HashMap<>();

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
