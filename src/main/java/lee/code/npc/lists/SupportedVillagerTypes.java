package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.npc.VillagerType;

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