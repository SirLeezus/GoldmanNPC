package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.VillagerType;

@AllArgsConstructor
public enum SupportedVillagerTypes {

    JUNGLE(VillagerType.JUNGLE),
    DESERT(VillagerType.DESERT),
    PLAINS(VillagerType.PLAINS),
    SAVANNA(VillagerType.SAVANNA),
    SNOW(VillagerType.SNOW),
    SWAMP(VillagerType.SWAMP),
    TAIGA(VillagerType.TAIGA),
    ;

    @Getter private final VillagerType type;
}