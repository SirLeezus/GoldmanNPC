package lee.code.npc.listeners;

import lee.code.core.util.bukkit.BukkitUtils;
import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.CommandType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteractEvent(PlayerInteractEntityEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();

        if (e.getRightClicked() instanceof Villager) {
            Player player = e.getPlayer();
            int id = plugin.getPU().getID(e.getRightClicked());
            if (cacheManager.isNPC(id)) {
                e.setCancelled(true);
                if (BukkitUtils.hasClickDelay(player)) return;
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                if (cacheManager.hasNPCCommand(id)) {
                    String command = cacheManager.getNPCCommand(id);
                    CommandType commandType = cacheManager.getNPCCommandType(id);
                    plugin.getPU().runCommand(player, command, commandType);
                }
            }
        }
    }
}
