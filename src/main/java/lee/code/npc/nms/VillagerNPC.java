package lee.code.npc.nms;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import java.lang.reflect.Field;
import java.util.Collections;

public class VillagerNPC extends EntityVillager {

    public VillagerNPC(Location loc, VillagerType villagertype, VillagerProfession villagerProfession, String name) {
        super(EntityTypes.aV, ((CraftWorld)loc.getWorld()).getHandle(), villagertype);
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
        this.bP.a(0, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 5, 100));
        this.bP.a(1, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound){
        return nbttagcompound;
    }

    private void removeAI() {

        try {
            Field availableGoalsField = PathfinderGoalSelector.class.getDeclaredField("d");
            Field priorityBehaviorsField = BehaviorController.class.getDeclaredField("f");
            Field coreActivityField = BehaviorController.class.getDeclaredField("j");

            availableGoalsField.setAccessible(true);
            priorityBehaviorsField.setAccessible(true);
            coreActivityField.setAccessible(true);

            availableGoalsField.set(this.bP, Sets.newLinkedHashSet());
            availableGoalsField.set(this.bQ, Sets.newLinkedHashSet());
            priorityBehaviorsField.set(this.getBehaviorController(), Collections.emptyMap());
            coreActivityField.set(this.getBehaviorController(), Sets.newHashSet());

        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }
}
