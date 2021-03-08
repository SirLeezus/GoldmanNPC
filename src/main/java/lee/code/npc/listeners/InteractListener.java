package lee.code.npc.listeners;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.Cache;
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
        Cache cache = plugin.getCache();

        if (e.getRightClicked() instanceof Villager) {
            Player player = e.getPlayer();
            String customName = e.getRightClicked().getCustomName();
            if (customName != null) {
                String name = plugin.getPU().unFormat(customName);
                if (cache.isNPC(name)) {
                    e.setCancelled(true);
                    if (plugin.getData().hasPlayerClickDelay(player.getUniqueId())) return;
                    else plugin.getPU().addPlayerClickDelay(player.getUniqueId());
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    if (cache.hasNPCCommand(name)) {
                        String command = cache.getNPCCommand(name);
                        String commandType = cache.getNPCCommandType(name);
                        plugin.getPU().runCommand(player, command, commandType);
                    }
                }
            }
        }
    }
}
