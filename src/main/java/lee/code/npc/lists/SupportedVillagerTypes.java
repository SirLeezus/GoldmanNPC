package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.npc.VillagerType;

@AllArgsConstructor
public enum SupportedVillagerTypes {

    JUNGLE(VillagerType.b),
    DESERT(VillagerType.a),
    PLAINS(VillagerType.c),
    SAVANNA(VillagerType.d),
    SNOW(VillagerType.e),
    SWAMP(VillagerType.f),
    TAIGA(VillagerType.g),
    ;

    @Getter private final VillagerType type;
}