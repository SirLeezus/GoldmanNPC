package lee.code.npc.lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.VillagerProfession;

@AllArgsConstructor
public enum SupportedVillagerProfessions {

    ARMORER(VillagerProfession.ARMORER),
    BUTCHER(VillagerProfession.BUTCHER),
    CARTOGRAPHER(VillagerProfession.CARTOGRAPHER),
    CLERIC(VillagerProfession.CLERIC),
    FARMER(VillagerProfession.FARMER),
    FISHERMAN(VillagerProfession.FISHERMAN),
    FLETCHER(VillagerProfession.FLETCHER),
    LEATHERWORKER(VillagerProfession.LEATHERWORKER),
    LIBRARIAN(VillagerProfession.LIBRARIAN),
    MASON(VillagerProfession.MASON),
    SHEPHERD(VillagerProfession.SHEPHERD),
    TOOLSMITH(VillagerProfession.TOOLSMITH),
    WEAPONSMITH(VillagerProfession.WEAPONSMITH),
    NONE(VillagerProfession.NONE),
    ;

    @Getter private final VillagerProfession profession;
}
