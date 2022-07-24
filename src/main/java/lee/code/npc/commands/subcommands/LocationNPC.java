package lee.code.npc.commands.subcommands;

import lee.code.npc.GoldmanNPC;
import lee.code.npc.commands.SubCommand;
import lee.code.npc.database.CacheManager;
import lee.code.npc.lists.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LocationNPC extends SubCommand {

    @Override
    public String getName() {
        return "location";
    }

    @Override
    public String getDescription() {
        return "Set the location of the NPC you have selected.";
    }

    @Override
    public String getSyntax() {
        return "/npc location";
    }

    @Override
    public String getPermission() {
        return "npc.command.location";
    }

    @Override
    public void perform(Player player, String[] args) {
        GoldmanNPC plugin = GoldmanNPC.getPlugin();
        CacheManager cacheManager = plugin.getCacheManager();
        UUID uuid = player.getUniqueId();

        if (plugin.getData().hasSelectedNPC(uuid)) {
            int id = plugin.getData().getSelectedNPC(uuid);
            cacheManager.setNPCLocation(id, player.getLocation());
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_NPC.getComponent(null)));
    }

    @Override
    public void performConsole(CommandSender console, String[] args) {
        console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_A_CONSOLE_COMMAND.getComponent(null)));
    }
}
