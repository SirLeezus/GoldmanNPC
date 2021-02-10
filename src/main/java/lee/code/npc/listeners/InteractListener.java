package lee.code.npc.listeners;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.database.SQLite;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteractEvent(PlayerInteractEntityEvent e) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        SQLite SQL = plugin.getSqLite();
        Player player = e.getPlayer();

        if (e.getRightClicked() instanceof Villager) {
            String customName = e.getRightClicked().getCustomName();
            if (customName != null) {
                String name = plugin.getPU().unFormat(customName);
                if (plugin.getData().getActiveNPCs().contains(name)) {
                    e.setCancelled(true);
                    String command = SQL.getNPCCommand(name);
                    String commandType = SQL.getNPCCommandType(name);
                    plugin.getPU().runCommand(player, command, commandType);
                }
            }
        }
    }
}
