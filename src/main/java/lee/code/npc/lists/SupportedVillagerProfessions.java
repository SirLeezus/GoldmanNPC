package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.npc.VillagerProfession;

@AllArgsConstructor
public enum SupportedVillagerProfessions {

    NONE(VillagerProfession.a),
    ARMORER(VillagerProfession.b),
    BUTCHER(VillagerProfession.c),
    CARTOGRAPHER(VillagerProfession.d),
    CLERIC(VillagerProfession.e),
    FARMER(VillagerProfession.f),
    FISHERMAN(VillagerProfession.g),
    FLETCHER(VillagerProfession.h),
    LEATHERWORKER(VillagerProfession.i),
    LIBRARIAN(VillagerProfession.j),
    MASON(VillagerProfession.k),
    NITWIT(VillagerProfession.l),
    SHEPHERD(VillagerProfession.m),
    TOOLSMITH(VillagerProfession.n),
    WEAPONSMITH(VillagerProfession.o),
    ;

    @Getter private final VillagerProfession profession;
}
