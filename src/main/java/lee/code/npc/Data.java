package lee.code.npc;

import lee.code.npc.lists.NPCProfession;
import lee.code.npc.lists.NPCType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

    private final ConcurrentHashMap<UUID, Integer> selectedNPC = new ConcurrentHashMap<>();

    public void setSelectedNPC(UUID uuid, int id) { selectedNPC.put(uuid, id); }
    public int getSelectedNPC(UUID uuid) { return selectedNPC.get(uuid); }
    public boolean hasSelectedNPC(UUID uuid) { return selectedNPC.containsKey(uuid); }
    public void removeSelectedNPC(UUID uuid) { selectedNPC.remove(uuid); }

    @Getter private final List<String> villagerTypes = new ArrayList<>();
    @Getter private final List<String> villagerProfessions = new ArrayList<>();

    public void loadData() {

        //villager types
        villagerTypes.addAll(EnumSet.allOf(NPCType.class).stream().map(NPCType::name).toList());

        //villager professions
        villagerProfessions.addAll(EnumSet.allOf(NPCProfession.class).stream().map(NPCProfession::name).toList());
    }
}
