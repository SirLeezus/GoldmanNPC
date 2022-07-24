package lee.code.npc.listeners;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.CacheManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onNPCDamage(EntityDamageByEntityEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (e.getEntity() instanceof Villager) {
            int id = plugin.getPU().getID(e.getEntity());
            if (cacheManager.isNPC(id)) {
                e.setCancelled(true);
                if (e.getDamager() instanceof Player player) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1, 1);
                }
            }
        }
    }

    @EventHandler
    public void onNPCDamage(EntityDamageEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (e.getEntity() instanceof Villager) {
            int id = plugin.getPU().getID(e.getEntity());
            if (cacheManager.isNPC(id)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onNPCZap(EntityZapEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (e.getEntity() instanceof Villager) {
            int id = plugin.getPU().getID(e.getEntity());
            if (cacheManager.isNPC(id)) {
                e.setCancelled(true);
            }
        }
    }
}
