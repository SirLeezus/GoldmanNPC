package lee.code.npc.listeners;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.SQLite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onPetDamage(EntityDamageByEntityEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();

        String customName = e.getEntity().getCustomName();
        if (customName != null) {
            String name = customName.replaceAll("§", "&");
            if (SQL.getNPCNames().contains(name)) {
                e.setCancelled(true);
            }
        }
    }
}
