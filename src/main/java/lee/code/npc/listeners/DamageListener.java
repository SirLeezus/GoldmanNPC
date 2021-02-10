package lee.code.npc.listeners;

import lee.code.npc.GoldmanNPC;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onPetDamage(EntityDamageByEntityEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();

        if (e.getEntity() instanceof Villager) {
            String customName = e.getEntity().getCustomName();
            if (customName != null) {
                String name = plugin.getPU().unFormat(customName);
                if (plugin.getData().getActiveNPCs().contains(name)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
