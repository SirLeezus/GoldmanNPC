package lee.code.npc.nms;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.Collections;

public class VillagerNPC extends EntityVillager {

    public VillagerNPC(Location loc, VillagerType villagertype, VillagerProfession villagerProfession, String name) {
        super(EntityTypes.VILLAGER, ((CraftWorld)loc.getWorld()).getHandle(), villagertype);
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
        this.removeAI();
        this.setInvulnerable(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', name)));
        this.setCustomNameVisible(true);
        this.setVillagerData(this.getVillagerData().withProfession(villagerProfession));
        this.setVillagerData(this.getVillagerData().withType(villagertype));
        this.setAge(1);
        this.setSilent(true);
        this.collides = false;
        this.goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 5, 100));
        this.goalSelector.a(1, new PathfinderGoalRandomLookaround(this));
    }

    private void removeAI() {

        try {
            Field availableGoalsField = PathfinderGoalSelector.class.getDeclaredField("d");
            Field priorityBehaviorsField = BehaviorController.class.getDeclaredField("e");
            Field coreActivityField = BehaviorController.class.getDeclaredField("i");

            availableGoalsField.setAccessible(true);
            priorityBehaviorsField.setAccessible(true);
            coreActivityField.setAccessible(true);

            availableGoalsField.set(this.goalSelector, Sets.newLinkedHashSet());
            availableGoalsField.set(this.targetSelector, Sets.newLinkedHashSet());
            priorityBehaviorsField.set(this.getBehaviorController(), Collections.emptyMap());
            coreActivityField.set(this.getBehaviorController(), Sets.newHashSet());

        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }
}
